package br.com.sanquim.api.service;

import br.com.sanquim.api.domain.entity.*;
import br.com.sanquim.api.domain.enums.Role;
import br.com.sanquim.api.dto.request.*;
import br.com.sanquim.api.dto.response.*;
import br.com.sanquim.api.exception.BusinessException;
import br.com.sanquim.api.exception.DuplicateResourceException;
import br.com.sanquim.api.exception.ResourceNotFoundException;
import br.com.sanquim.api.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@Service
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AtribuicaoAulaRepository atribuicaoAulaRepository;
    private final MatriculaRepository matriculaRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(
            UsuarioRepository usuarioRepository,
            ProfessorRepository professorRepository,
            AlunoRepository alunoRepository,
            TurmaRepository turmaRepository,
            DisciplinaRepository disciplinaRepository,
            AtribuicaoAulaRepository atribuicaoAulaRepository,
            MatriculaRepository matriculaRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.atribuicaoAulaRepository = atribuicaoAulaRepository;
        this.matriculaRepository = matriculaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ProfessorResponseDTO cadastrarProfessor(ProfessorRequestDTO dto) {
        String email = dto.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmailIgnoreCase(email) || professorRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Já existe um usuário ou professor cadastrado com o e-mail: " + email);
        }
        if (professorRepository.existsByCpf(dto.cpf().trim())) {
            throw new DuplicateResourceException("Já existe um professor cadastrado com o CPF: " + dto.cpf());
        }

        Usuario usuario = new Usuario(
                dto.nome().trim(),
                email,
                passwordEncoder.encode(dto.senha()),
                Role.ROLE_PROFESSOR
        );
        usuario = usuarioRepository.save(usuario);

        Professor professor = new Professor();
        professor.setUsuario(usuario);
        professor.setNome(dto.nome().trim());
        professor.setCpf(dto.cpf().trim());
        professor.setRg(dto.rg());
        professor.setEndereco(dto.endereco());
        professor.setBairro(dto.bairro());
        professor.setCidade(dto.cidade());
        professor.setFone(dto.fone());
        professor.setEmail(email);
        professor.setAtivo(true);

        professor = professorRepository.save(professor);
        return toProfessorResponse(professor);
    }

    @Transactional(readOnly = true)
    public List<ProfessorResponseDTO> listarProfessores() {
        return professorRepository.findAll()
                .stream()
                .map(this::toProfessorResponse)
                .toList();
    }

    @Transactional
    public AlunoResponseDTO cadastrarAluno(AlunoRequestDTO dto) {
        String email = dto.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmailIgnoreCase(email) || alunoRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Já existe um usuário ou aluno cadastrado com o e-mail: " + email);
        }
        if (alunoRepository.existsByRa(dto.ra().trim())) {
            throw new DuplicateResourceException("Já existe um aluno cadastrado com o RA: " + dto.ra());
        }
        if (alunoRepository.existsByCpf(dto.cpf().trim())) {
            throw new DuplicateResourceException("Já existe um aluno cadastrado com o CPF: " + dto.cpf());
        }

        Usuario usuario = new Usuario(
                dto.nome().trim(),
                email,
                passwordEncoder.encode(dto.senha()),
                Role.ROLE_ALUNO
        );
        usuario = usuarioRepository.save(usuario);

        Aluno aluno = new Aluno();
        aluno.setUsuario(usuario);
        aluno.setNome(dto.nome().trim());
        aluno.setRa(dto.ra().trim());
        aluno.setDataNascimento(dto.dataNascimento());
        aluno.setCpf(dto.cpf().trim());
        aluno.setRg(dto.rg());
        aluno.setEndereco(dto.endereco());
        aluno.setBairro(dto.bairro());
        aluno.setCidade(dto.cidade());
        aluno.setFone(dto.fone());
        aluno.setEmail(email);
        aluno.setAtivo(true);

        aluno = alunoRepository.save(aluno);
        return toAlunoResponse(aluno);
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listarAlunos() {
        return alunoRepository.findAll()
                .stream()
                .map(this::toAlunoResponse)
                .toList();
    }

    @Transactional
    public TurmaResponseDTO criarTurma(TurmaRequestDTO dto) {
        if (turmaRepository.existsByNomeIgnoreCase(dto.nome().trim())) {
            throw new DuplicateResourceException("Já existe uma turma cadastrada com o nome: " + dto.nome());
        }

        Turma turma = new Turma();
        turma.setNome(dto.nome().trim());
        turma.setCurso(dto.curso().trim());
        turma.setLocal(dto.local());
        turma.setPeriodo(dto.periodo().trim());
        turma.setAnoLetivo(dto.anoLetivo() != null ? dto.anoLetivo() : Year.now().getValue());
        turma.setAtiva(true);

        turma = turmaRepository.save(turma);
        return toTurmaResponse(turma);
    }

    @Transactional(readOnly = true)
    public List<TurmaResponseDTO> listarTurmas() {
        return turmaRepository.findAll()
                .stream()
                .map(this::toTurmaResponse)
                .toList();
    }

    @Transactional
    public DisciplinaResponseDTO criarDisciplina(DisciplinaRequestDTO dto) {
        if (disciplinaRepository.existsByNomeIgnoreCase(dto.nome().trim())) {
            throw new DuplicateResourceException("Já existe uma disciplina cadastrada com o nome: " + dto.nome());
        }

        if ((dto.turmaId() != null && dto.professorId() == null) || (dto.turmaId() == null && dto.professorId() != null)) {
            throw new BusinessException("Para atribuir a disciplina na criação, informe ambos turmaId e professorId (ou deixe ambos nulos).");
        }

        Disciplina disciplina = new Disciplina();
        disciplina.setNome(dto.nome().trim());
        disciplina.setCargaHoraria(dto.cargaHoraria());
        disciplina.setAtiva(true);
        disciplina = disciplinaRepository.save(disciplina);

        Long atribuicaoId = null;
        if (dto.turmaId() != null && dto.professorId() != null) {
            Turma turma = turmaRepository.findById(dto.turmaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com ID: " + dto.turmaId()));
            Professor professor = professorRepository.findById(dto.professorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com ID: " + dto.professorId()));

            AtribuicaoAula atribuicao = new AtribuicaoAula(professor, turma, disciplina);
            atribuicao = atribuicaoAulaRepository.save(atribuicao);
            atribuicaoId = atribuicao.getId();
        }

        return new DisciplinaResponseDTO(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getCargaHoraria(),
                disciplina.isAtiva(),
                atribuicaoId,
                dto.turmaId(),
                dto.professorId()
        );
    }

    @Transactional(readOnly = true)
    public List<DisciplinaResponseDTO> listarDisciplinas() {
        return disciplinaRepository.findAll()
                .stream()
                .map(d -> new DisciplinaResponseDTO(
                        d.getId(), d.getNome(), d.getCargaHoraria(), d.isAtiva(), null, null, null))
                .toList();
    }

    @Transactional
    public AtribuicaoResponseDTO atribuirProfessor(AtribuicaoRequestDTO dto) {
        if (atribuicaoAulaRepository.existsByTurmaIdAndDisciplinaId(dto.turmaId(), dto.disciplinaId())) {
            throw new DuplicateResourceException("Esta disciplina já possui professor vinculado nesta turma.");
        }

        Professor professor = professorRepository.findById(dto.professorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com ID: " + dto.professorId()));
        Turma turma = turmaRepository.findById(dto.turmaId())
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com ID: " + dto.turmaId()));
        Disciplina disciplina = disciplinaRepository.findById(dto.disciplinaId())
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com ID: " + dto.disciplinaId()));

        AtribuicaoAula atribuicao = atribuicaoAulaRepository.save(new AtribuicaoAula(professor, turma, disciplina));

        return new AtribuicaoResponseDTO(
                atribuicao.getId(),
                professor.getId(),
                professor.getNome(),
                turma.getId(),
                turma.getNome(),
                disciplina.getId(),
                disciplina.getNome()
        );
    }

    @Transactional
    public MatriculaResponseDTO matricularAluno(MatriculaRequestDTO dto) {
        if (matriculaRepository.existsByAlunoIdAndTurmaId(dto.alunoId(), dto.turmaId())) {
            throw new DuplicateResourceException("Este aluno já está matriculado nesta turma.");
        }

        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + dto.alunoId()));
        Turma turma = turmaRepository.findById(dto.turmaId())
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com ID: " + dto.turmaId()));

        if (!turma.isAtiva()) {
            throw new BusinessException("Não é possível matricular alunos em uma turma inativa.");
        }

        Matricula matricula = matriculaRepository.save(new Matricula(aluno, turma));
        return toMatriculaResponse(matricula);
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> listarMatriculas() {
        return matriculaRepository.findAll()
                .stream()
                .map(this::toMatriculaResponse)
                .toList();
    }

    private ProfessorResponseDTO toProfessorResponse(Professor p) {
        return new ProfessorResponseDTO(
                p.getId(),
                p.getUsuario().getId(),
                p.getNome(),
                p.getCpf(),
                p.getRg(),
                p.getEndereco(),
                p.getBairro(),
                p.getCidade(),
                p.getFone(),
                p.getEmail(),
                p.isAtivo()
        );
    }

    private AlunoResponseDTO toAlunoResponse(Aluno a) {
        return new AlunoResponseDTO(
                a.getId(),
                a.getUsuario().getId(),
                a.getNome(),
                a.getRa(),
                a.getDataNascimento(),
                a.getCpf(),
                a.getRg(),
                a.getEndereco(),
                a.getBairro(),
                a.getCidade(),
                a.getFone(),
                a.getEmail(),
                a.isAtivo()
        );
    }

    private TurmaResponseDTO toTurmaResponse(Turma t) {
        return new TurmaResponseDTO(
                t.getId(),
                t.getNome(),
                t.getCurso(),
                t.getLocal(),
                t.getPeriodo(),
                t.getAnoLetivo(),
                t.isAtiva()
        );
    }

    private MatriculaResponseDTO toMatriculaResponse(Matricula m) {
        return new MatriculaResponseDTO(
                m.getId(),
                m.getAluno().getId(),
                m.getAluno().getNome(),
                m.getAluno().getRa(),
                m.getTurma().getId(),
                m.getTurma().getNome(),
                m.getDataMatricula(),
                m.getSituacao().name()
        );
    }
}
