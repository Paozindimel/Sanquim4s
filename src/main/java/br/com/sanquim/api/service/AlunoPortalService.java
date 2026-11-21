package br.com.sanquim.api.service;

import br.com.sanquim.api.domain.entity.*;
import br.com.sanquim.api.domain.enums.SituacaoMatricula;
import br.com.sanquim.api.dto.response.*;
import br.com.sanquim.api.exception.ResourceNotFoundException;
import br.com.sanquim.api.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlunoPortalService {

    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;
    private final AtribuicaoAulaRepository atribuicaoAulaRepository;
    private final NotaRepository notaRepository;
    private final FrequenciaRepository frequenciaRepository;
    private final PlanoAulaRepository planoAulaRepository;

    public AlunoPortalService(
            AlunoRepository alunoRepository,
            MatriculaRepository matriculaRepository,
            AtribuicaoAulaRepository atribuicaoAulaRepository,
            NotaRepository notaRepository,
            FrequenciaRepository frequenciaRepository,
            PlanoAulaRepository planoAulaRepository) {
        this.alunoRepository = alunoRepository;
        this.matriculaRepository = matriculaRepository;
        this.atribuicaoAulaRepository = atribuicaoAulaRepository;
        this.notaRepository = notaRepository;
        this.frequenciaRepository = frequenciaRepository;
        this.planoAulaRepository = planoAulaRepository;
    }

    @Transactional(readOnly = true)
    public BoletimResponseDTO consultarBoletim() {
        Aluno aluno = obterAlunoAutenticado();
        List<Matricula> matriculas = matriculaRepository.findByAlunoIdAndSituacao(aluno.getId(), SituacaoMatricula.ATIVA);
        List<String> nomesTurmas = matriculas.stream().map(m -> m.getTurma().getNome()).toList();
        Set<Long> turmaIds = matriculas.stream().map(m -> m.getTurma().getId()).collect(Collectors.toSet());

        List<Nota> notasAluno = notaRepository.findByAlunoId(aluno.getId());
        Map<Long, List<Nota>> notasPorDisciplina = notasAluno.stream()
                .collect(Collectors.groupingBy(n -> n.getAvaliacao().getDisciplina().getId()));

        Map<Long, Disciplina> disciplinasMapa = new LinkedHashMap<>();
        Map<Long, String> professorPorDisciplina = new HashMap<>();

        for (AtribuicaoAula atrib : atribuicaoAulaRepository.findAll()) {
            if (turmaIds.contains(atrib.getTurma().getId())) {
                disciplinasMapa.put(atrib.getDisciplina().getId(), atrib.getDisciplina());
                professorPorDisciplina.put(atrib.getDisciplina().getId(), atrib.getProfessor().getNome());
            }
        }
        for (Nota n : notasAluno) {
            Disciplina d = n.getAvaliacao().getDisciplina();
            disciplinasMapa.putIfAbsent(d.getId(), d);
            if (n.getAvaliacao().getProfessor() != null) {
                professorPorDisciplina.putIfAbsent(d.getId(), n.getAvaliacao().getProfessor().getNome());
            }
        }

        List<BoletimDisciplinaDTO> disciplinasBoletim = new ArrayList<>();

        for (Map.Entry<Long, Disciplina> entry : disciplinasMapa.entrySet()) {
            Long discId = entry.getKey();
            Disciplina disc = entry.getValue();
            List<Nota> notasDaDisc = notasPorDisciplina.getOrDefault(discId, List.of());

            BigDecimal somaNotasPonderadas = BigDecimal.ZERO;
            BigDecimal somaPesos = BigDecimal.ZERO;
            List<NotaBoletimItemDTO> itensAvaliacao = new ArrayList<>();

            for (Nota nota : notasDaDisc) {
                Avaliacao av = nota.getAvaliacao();
                BigDecimal notaBase10 = nota.getValor()
                        .multiply(BigDecimal.TEN)
                        .divide(av.getNotaMaxima(), 4, RoundingMode.HALF_UP);

                somaNotasPonderadas = somaNotasPonderadas.add(notaBase10.multiply(av.getPeso()));
                somaPesos = somaPesos.add(av.getPeso());

                itensAvaliacao.add(new NotaBoletimItemDTO(
                        av.getId(),
                        av.getTitulo(),
                        av.getTipo().name(),
                        av.getDataEntrega(),
                        av.getPeso(),
                        av.getNotaMaxima(),
                        nota.getValor(),
                        nota.getObservacao()
                ));
            }

            BigDecimal mediaParcial = BigDecimal.ZERO;
            String situacao = "SEM_NOTAS_LANCADAS";
            if (somaPesos.compareTo(BigDecimal.ZERO) > 0) {
                mediaParcial = somaNotasPonderadas.divide(somaPesos, 2, RoundingMode.HALF_UP);
                situacao = mediaParcial.compareTo(new BigDecimal("6.00")) >= 0
                        ? "APROVADO_PARCIAL"
                        : "ABAIXO_DA_MEDIA";
            }

            disciplinasBoletim.add(new BoletimDisciplinaDTO(
                    disc.getId(),
                    disc.getNome(),
                    professorPorDisciplina.getOrDefault(discId, "A definir"),
                    mediaParcial,
                    situacao,
                    itensAvaliacao
            ));
        }

        return new BoletimResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getRa(),
                nomesTurmas,
                disciplinasBoletim
        );
    }

    @Transactional(readOnly = true)
    public FrequenciaAlunoResponseDTO consultarFrequencia() {
        Aluno aluno = obterAlunoAutenticado();
        List<Frequencia> registros = frequenciaRepository.findByAlunoIdOrderByDataAulaDesc(aluno.getId());

        Map<Long, List<Frequencia>> porDisciplinaMap = registros.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getDisciplina().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<FrequenciaDisciplinaDTO> listaDisciplinas = new ArrayList<>();
        int totalAulasGlobal = registros.size();
        int totalPresencasGlobal = 0;

        for (Map.Entry<Long, List<Frequencia>> entry : porDisciplinaMap.entrySet()) {
            List<Frequencia> freqs = entry.getValue();
            String nomeDisc = freqs.get(0).getDisciplina().getNome();
            int total = freqs.size();
            int presencas = (int) freqs.stream().filter(Frequencia::isPresente).count();
            int faltas = total - presencas;
            totalPresencasGlobal += presencas;

            BigDecimal percentual = calcularPercentual(presencas, total);
            String situacao = percentual.compareTo(new BigDecimal("75.00")) >= 0
                    ? "REGULAR"
                    : "ALERTA_INFREQUENCIA";

            listaDisciplinas.add(new FrequenciaDisciplinaDTO(
                    entry.getKey(),
                    nomeDisc,
                    total,
                    presencas,
                    faltas,
                    percentual,
                    situacao
            ));
        }

        int totalFaltasGlobal = totalAulasGlobal - totalPresencasGlobal;
        BigDecimal percentualGlobal = calcularPercentual(totalPresencasGlobal, totalAulasGlobal);
        String situacaoGlobal = totalAulasGlobal == 0
                ? "SEM_REGISTROS"
                : (percentualGlobal.compareTo(new BigDecimal("75.00")) >= 0 ? "REGULAR" : "ALERTA_INFREQUENCIA");

        return new FrequenciaAlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getRa(),
                totalAulasGlobal,
                totalPresencasGlobal,
                totalFaltasGlobal,
                percentualGlobal,
                situacaoGlobal,
                listaDisciplinas
        );
    }

    @Transactional(readOnly = true)
    public List<PlanoAulaResponseDTO> consultarLinhaDoTempoAulas() {
        Aluno aluno = obterAlunoAutenticado();
        List<Long> turmaIds = matriculaRepository.findByAlunoIdAndSituacao(aluno.getId(), SituacaoMatricula.ATIVA)
                .stream()
                .map(m -> m.getTurma().getId())
                .toList();

        if (turmaIds.isEmpty()) {
            return List.of();
        }

        return planoAulaRepository.findByTurmaIdInOrderByDataAulaDesc(turmaIds)
                .stream()
                .map(p -> new PlanoAulaResponseDTO(
                        p.getId(),
                        p.getTurma().getId(),
                        p.getTurma().getNome(),
                        p.getDisciplina().getId(),
                        p.getDisciplina().getNome(),
                        p.getProfessor() != null ? p.getProfessor().getNome() : "Corpo Docente",
                        p.getDataAula(),
                        p.getTitulo(),
                        p.getConteudo(),
                        p.getObjetivos()
                ))
                .toList();
    }

    private Aluno obterAlunoAutenticado() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return alunoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cadastro de aluno não encontrado para o usuário autenticado."));
    }

    private BigDecimal calcularPercentual(int presencas, int total) {
        if (total == 0) {
            return new BigDecimal("100.00");
        }
        return BigDecimal.valueOf(presencas)
                .multiply(new BigDecimal("100"))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }
}
