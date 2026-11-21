package br.com.sanquim.api.service;

import br.com.sanquim.api.domain.entity.*;
import br.com.sanquim.api.domain.enums.Role;
import br.com.sanquim.api.domain.enums.SituacaoMatricula;
import br.com.sanquim.api.dto.request.*;
import br.com.sanquim.api.dto.response.*;
import br.com.sanquim.api.exception.BusinessException;
import br.com.sanquim.api.exception.DuplicateResourceException;
import br.com.sanquim.api.exception.ResourceNotFoundException;
import br.com.sanquim.api.repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DiarioService {

    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AtribuicaoAulaRepository atribuicaoAulaRepository;
    private final MatriculaRepository matriculaRepository;
    private final FrequenciaRepository frequenciaRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final NotaRepository notaRepository;
    private final PlanoAulaRepository planoAulaRepository;

    public DiarioService(
            ProfessorRepository professorRepository,
            AlunoRepository alunoRepository,
            TurmaRepository turmaRepository,
            DisciplinaRepository disciplinaRepository,
            AtribuicaoAulaRepository atribuicaoAulaRepository,
            MatriculaRepository matriculaRepository,
            FrequenciaRepository frequenciaRepository,
            AvaliacaoRepository avaliacaoRepository,
            NotaRepository notaRepository,
            PlanoAulaRepository planoAulaRepository) {
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.atribuicaoAulaRepository = atribuicaoAulaRepository;
        this.matriculaRepository = matriculaRepository;
        this.frequenciaRepository = frequenciaRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.notaRepository = notaRepository;
        this.planoAulaRepository = planoAulaRepository;
    }

    @Transactional(readOnly = true)
    public List<MinhaTurmaResponseDTO> listarMinhasTurmas() {
        Usuario usuario = obterUsuarioAutenticado();
        List<AtribuicaoAula> atribuicoes;

        if (usuario.getRole() == Role.ROLE_GESTAO) {
            atribuicoes = atribuicaoAulaRepository.findAll();
        } else {
            Professor professor = obterProfessorDoUsuario(usuario);
            atribuicoes = atribuicaoAulaRepository.findByProfessorId(professor.getId());
        }

        return atribuicoes.stream().map(atrib -> {
            List<AlunoResumoDTO> alunos = matriculaRepository
                    .findByTurmaIdAndSituacao(atrib.getTurma().getId(), SituacaoMatricula.ATIVA)
                    .stream()
                    .map(m -> new AlunoResumoDTO(m.getAluno().getId(), m.getAluno().getNome(), m.getAluno().getRa()))
                    .toList();

            return new MinhaTurmaResponseDTO(
                    atrib.getId(),
                    atrib.getTurma().getId(),
                    atrib.getTurma().getNome(),
                    atrib.getTurma().getCurso(),
                    atrib.getTurma().getPeriodo(),
                    atrib.getTurma().getLocal(),
                    atrib.getDisciplina().getId(),
                    atrib.getDisciplina().getNome(),
                    atrib.getDisciplina().getCargaHoraria(),
                    atrib.getProfessor().getNome(),
                    alunos.size(),
                    alunos
            );
        }).toList();
    }

    @Transactional
    public ChamadaResponseDTO registrarChamada(ChamadaRequestDTO dto) {
        Turma turma = buscarTurma(dto.turmaId());
        Disciplina disciplina = buscarDisciplina(dto.disciplinaId());
        Professor professor = validarPermissaoProfessorOuGestor(turma.getId(), disciplina.getId());

        if (frequenciaRepository.existsByTurmaIdAndDisciplinaIdAndDataAula(
                turma.getId(), disciplina.getId(), dto.dataAula())) {
            throw new DuplicateResourceException(
                    "Já existe uma chamada registrada para esta turma e disciplina na data " + dto.dataAula());
        }

        Set<Long> alunosNoLote = new HashSet<>();
        List<Frequencia> listaSalvar = new ArrayList<>();
        int presentes = 0;
        int ausentes = 0;

        for (RegistroPresencaDTO item : dto.registros()) {
            if (!alunosNoLote.add(item.alunoId())) {
                throw new BusinessException("O aluno de ID " + item.alunoId() + " foi enviado em duplicidade no mesmo lote de chamada.");
            }

            Aluno aluno = alunoRepository.findById(item.alunoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + item.alunoId()));

            boolean matriculado = matriculaRepository.existsByAlunoIdAndTurmaIdAndSituacao(
                    aluno.getId(), turma.getId(), SituacaoMatricula.ATIVA);
            if (!matriculado) {
                throw new BusinessException("O aluno '" + aluno.getNome() + "' (ID " + aluno.getId() + ") não possui matrícula ativa na turma '" + turma.getNome() + "'.");
            }

            Frequencia freq = new Frequencia();
            freq.setAluno(aluno);
            freq.setTurma(turma);
            freq.setDisciplina(disciplina);
            freq.setProfessor(professor);
            freq.setDataAula(dto.dataAula());
            freq.setPresente(item.presente());
            listaSalvar.add(freq);

            if (Boolean.TRUE.equals(item.presente())) {
                presentes++;
            } else {
                ausentes++;
            }
        }

        frequenciaRepository.saveAll(listaSalvar);

        return new ChamadaResponseDTO(
                turma.getId(),
                turma.getNome(),
                disciplina.getId(),
                disciplina.getNome(),
                dto.dataAula(),
                listaSalvar.size(),
                presentes,
                ausentes,
                "Chamada registrada com sucesso."
        );
    }

    @Transactional
    public AvaliacaoResponseDTO cadastrarAvaliacao(AvaliacaoRequestDTO dto) {
        Turma turma = buscarTurma(dto.turmaId());
        Disciplina disciplina = buscarDisciplina(dto.disciplinaId());
        Professor professor = validarPermissaoProfessorOuGestor(turma.getId(), disciplina.getId());

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setTitulo(dto.titulo().trim());
        avaliacao.setTipo(dto.tipo());
        avaliacao.setPeso(dto.peso());
        avaliacao.setNotaMaxima(dto.notaMaxima());
        avaliacao.setDataEntrega(dto.dataEntrega());
        avaliacao.setTurma(turma);
        avaliacao.setDisciplina(disciplina);
        avaliacao.setProfessor(professor);

        avaliacao = avaliacaoRepository.save(avaliacao);

        return new AvaliacaoResponseDTO(
                avaliacao.getId(),
                avaliacao.getTitulo(),
                avaliacao.getTipo().name(),
                avaliacao.getPeso(),
                avaliacao.getNotaMaxima(),
                avaliacao.getDataEntrega(),
                turma.getId(),
                turma.getNome(),
                disciplina.getId(),
                disciplina.getNome(),
                professor != null ? professor.getNome() : "Gestão Escolar"
        );
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarAvaliacoes() {
        Usuario usuario = obterUsuarioAutenticado();
        List<Avaliacao> avaliacoes;
        if (usuario.getRole() == Role.ROLE_GESTAO) {
            avaliacoes = avaliacaoRepository.findAll();
        } else {
            Professor professor = obterProfessorDoUsuario(usuario);
            avaliacoes = avaliacaoRepository.findByProfessorIdOrderByDataEntregaDesc(professor.getId());
        }
        return avaliacoes.stream().map(a -> new AvaliacaoResponseDTO(
                a.getId(),
                a.getTitulo(),
                a.getTipo().name(),
                a.getPeso(),
                a.getNotaMaxima(),
                a.getDataEntrega(),
                a.getTurma().getId(),
                a.getTurma().getNome(),
                a.getDisciplina().getId(),
                a.getDisciplina().getNome(),
                a.getProfessor() != null ? a.getProfessor().getNome() : "Gestão Escolar"
        )).toList();
    }

    @Transactional
    public List<NotaResponseDTO> lancarOuAtualizarNotas(Long avaliacaoId, LancamentoNotasRequestDTO dto) {
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com ID: " + avaliacaoId));

        validarPermissaoProfessorOuGestor(avaliacao.getTurma().getId(), avaliacao.getDisciplina().getId());

        List<NotaResponseDTO> respostas = new ArrayList<>();

        for (NotaItemRequestDTO item : dto.notas()) {
            if (item.valor().compareTo(avaliacao.getNotaMaxima()) > 0) {
                throw new BusinessException("A nota do aluno ID " + item.alunoId()
                        + " (" + item.valor() + ") ultrapassa a nota máxima da avaliação (" + avaliacao.getNotaMaxima() + ").");
            }

            Aluno aluno = alunoRepository.findById(item.alunoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + item.alunoId()));

            boolean matriculado = matriculaRepository.existsByAlunoIdAndTurmaIdAndSituacao(
                    aluno.getId(), avaliacao.getTurma().getId(), SituacaoMatricula.ATIVA);
            if (!matriculado) {
                throw new BusinessException("O aluno '" + aluno.getNome() + "' não está matriculado na turma desta avaliação.");
            }

            Nota nota = notaRepository.findByAvaliacaoIdAndAlunoId(avaliacaoId, aluno.getId())
                    .orElseGet(() -> {
                        Nota nova = new Nota();
                        nova.setAvaliacao(avaliacao);
                        nova.setAluno(aluno);
                        return nova;
                    });

            nota.setValor(item.valor());
            nota.setObservacao(item.observacao());
            nota.setAtualizadoEm(LocalDateTime.now());
            nota = notaRepository.save(nota);

            respostas.add(new NotaResponseDTO(
                    nota.getId(),
                    avaliacao.getId(),
                    avaliacao.getTitulo(),
                    aluno.getId(),
                    aluno.getNome(),
                    aluno.getRa(),
                    nota.getValor(),
                    avaliacao.getNotaMaxima(),
                    nota.getObservacao(),
                    nota.getAtualizadoEm()
            ));
        }

        return respostas;
    }

    @Transactional
    public PlanoAulaResponseDTO registrarPlanoAula(PlanoAulaRequestDTO dto) {
        Turma turma = buscarTurma(dto.turmaId());
        Disciplina disciplina = buscarDisciplina(dto.disciplinaId());
        Professor professor = validarPermissaoProfessorOuGestor(turma.getId(), disciplina.getId());

        PlanoAula plano = new PlanoAula();
        plano.setTurma(turma);
        plano.setDisciplina(disciplina);
        plano.setProfessor(professor);
        plano.setDataAula(dto.dataAula());
        plano.setTitulo(dto.titulo().trim());
        plano.setConteudo(dto.conteudo().trim());
        plano.setObjetivos(dto.objetivos());

        plano = planoAulaRepository.save(plano);

        return new PlanoAulaResponseDTO(
                plano.getId(),
                turma.getId(),
                turma.getNome(),
                disciplina.getId(),
                disciplina.getNome(),
                professor != null ? professor.getNome() : "Gestão Escolar",
                plano.getDataAula(),
                plano.getTitulo(),
                plano.getConteudo(),
                plano.getObjetivos()
        );
    }

    private Usuario obterUsuarioAutenticado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Professor obterProfessorDoUsuario(Usuario usuario) {
        return professorRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cadastro de professor não localizado para o usuário autenticado."));
    }

    private Professor validarPermissaoProfessorOuGestor(Long turmaId, Long disciplinaId) {
        Usuario usuario = obterUsuarioAutenticado();
        if (usuario.getRole() == Role.ROLE_GESTAO) {
            return atribuicaoAulaRepository.findByTurmaIdAndDisciplinaId(turmaId, disciplinaId)
                    .map(AtribuicaoAula::getProfessor)
                    .orElse(null);
        }

        Professor professor = obterProfessorDoUsuario(usuario);
        boolean titular = atribuicaoAulaRepository.existsByProfessorIdAndTurmaIdAndDisciplinaId(
                professor.getId(), turmaId, disciplinaId);
        if (!titular) {
            throw new AccessDeniedException("Acesso restrito: você não está vinculado a esta disciplina e turma.");
        }
        return professor;
    }

    private Turma buscarTurma(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com ID: " + id));
    }

    private Disciplina buscarDisciplina(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com ID: " + id));
    }
}
