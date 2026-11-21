package br.com.sanquim.api.config;

import br.com.sanquim.api.domain.entity.*;
import br.com.sanquim.api.domain.enums.Role;
import br.com.sanquim.api.domain.enums.TipoAvaliacao;
import br.com.sanquim.api.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static final String SENHA_PADRAO = "Sanquim@123";

    private final UsuarioRepository usuarioRepository;
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
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            UsuarioRepository usuarioRepository,
            ProfessorRepository professorRepository,
            AlunoRepository alunoRepository,
            TurmaRepository turmaRepository,
            DisciplinaRepository disciplinaRepository,
            AtribuicaoAulaRepository atribuicaoAulaRepository,
            MatriculaRepository matriculaRepository,
            FrequenciaRepository frequenciaRepository,
            AvaliacaoRepository avaliacaoRepository,
            NotaRepository notaRepository,
            PlanoAulaRepository planoAulaRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
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
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("Banco de dados do Colégio Sanquim já possui dados iniciais. Seed ignorado.");
            return;
        }

        log.info("Executando Carga Inicial de Dados (Seed) do Colégio Sanquim...");
        String hashPadrao = passwordEncoder.encode(SENHA_PADRAO);

        // 1. GESTOR (admin@sanquim.br)
        Usuario gestor = new Usuario("Secretaria Colégio Sanquim", "admin@sanquim.br", hashPadrao, Role.ROLE_GESTAO);
        usuarioRepository.save(gestor);

        // 2. DOIS PROFESSORES (migrados de sanquim.sql com BCrypt)
        Professor profRodrigo = criarProfessor(
                "Rodrigo Martins", "44597854901", "16456824-2",
                "Rua Professor Roberto Junior, 100", "Alameda Arborizada", "Mogi Mirim",
                "19 99855-4568", "rodrigo.martins@sanquim.br", hashPadrao);

        Professor profSwelen = criarProfessor(
                "Swelen Roberta", "14578925601", "14578954-5",
                "Avenida Brasil, 556", "Centro", "Mogi Guaçu",
                "19 98452-2201", "swelen.roberta@sanquim.br", hashPadrao);

        // 3. TURMAS E DISCIPLINAS (migradas de sanquim.sql)
        Turma turmaEmed = criarTurma("1º Ano EM - EMED", "Ensino Médio", "Sala 3", "Integral", 2026);
        Turma turmaPvest = criarTurma("Extensivo - PVEST", "Pré-Vestibular", "Sala 1", "Noturno", 2026);

        Disciplina discPortugues = criarDisciplina("Português", 65);
        Disciplina discMatematica = criarDisciplina("Matemática", 65);
        Disciplina discHistoria = criarDisciplina("História", 60);

        // Atribuições de turmas aos 2 professores
        atribuicaoAulaRepository.save(new AtribuicaoAula(profRodrigo, turmaEmed, discMatematica));
        atribuicaoAulaRepository.save(new AtribuicaoAula(profSwelen, turmaEmed, discHistoria));
        atribuicaoAulaRepository.save(new AtribuicaoAula(profSwelen, turmaPvest, discPortugues));

        // 4. CINCO ALUNOS ENTURMADOS (migrados de sanquim.sql)
        Aluno a1 = criarAluno("Roberto", "12345678900", LocalDate.of(2008, 3, 14),
                "11111111101", "36963231-0", "Rua Armando da Silva, 42", "Santa Cruz", "Mogi Mirim",
                "19 99999-9998", "roberto@aluno.sanquim.br", hashPadrao);

        Aluno a2 = criarAluno("Gabriel Silva", "16304125462", LocalDate.of(2008, 4, 11),
                "11233455607", "12333444-5", "Rua 53, nº 02", "Centro", "Estiva Gerbi",
                "19 99299-4444", "gabriel.silva@aluno.sanquim.br", hashPadrao);

        Aluno a3 = criarAluno("Isabella Costa", "63155849105", LocalDate.of(2008, 7, 4),
                "11122233304", "11222333-4", "Rua Amanda, 90", "Ipê Amarelo", "Mogi Guaçu",
                "19 99299-5510", "isabella.costa@aluno.sanquim.br", hashPadrao);

        Aluno a4 = criarAluno("Léo Leme Oliveira", "66699966699", LocalDate.of(2008, 2, 7),
                "40909504801", "69666777-1", "Rua do Tucura, 148", "Saúde", "Mogi Mirim",
                "19 99880-2847", "leo.leme@aluno.sanquim.br", hashPadrao);

        Aluno a5 = criarAluno("Solange Cabral", "19885564780", LocalDate.of(2007, 8, 18),
                "77855691402", "52598713-6", "Rua Amada, 903", "Bairro da Luz", "Mogi Mirim",
                "19 99525-6524", "solange.cabral@aluno.sanquim.br", hashPadrao);

        List<Aluno> alunosEmed = List.of(a1, a2, a3, a4, a5);
        for (Aluno al : alunosEmed) {
            matriculaRepository.save(new Matricula(al, turmaEmed));
        }

        // 5. CHAMADAS REGISTRADAS
        LocalDate aula1 = LocalDate.of(2026, 11, 5);
        registrarPresencaSeed(a1, turmaEmed, discMatematica, profRodrigo, aula1, true);
        registrarPresencaSeed(a2, turmaEmed, discMatematica, profRodrigo, aula1, true);
        registrarPresencaSeed(a3, turmaEmed, discMatematica, profRodrigo, aula1, false);
        registrarPresencaSeed(a4, turmaEmed, discMatematica, profRodrigo, aula1, true);
        registrarPresencaSeed(a5, turmaEmed, discMatematica, profRodrigo, aula1, true);

        LocalDate aula2 = LocalDate.of(2026, 11, 6);
        registrarPresencaSeed(a1, turmaEmed, discHistoria, profSwelen, aula2, true);
        registrarPresencaSeed(a2, turmaEmed, discHistoria, profSwelen, aula2, false);
        registrarPresencaSeed(a3, turmaEmed, discHistoria, profSwelen, aula2, true);
        registrarPresencaSeed(a4, turmaEmed, discHistoria, profSwelen, aula2, true);
        registrarPresencaSeed(a5, turmaEmed, discHistoria, profSwelen, aula2, true);

        // 6. AVALIAÇÃO COM NOTAS JÁ REGISTRADAS
        Avaliacao provaMat = new Avaliacao();
        provaMat.setTitulo("Prova Bimestral — Funções Afim e Quadrática");
        provaMat.setTipo(TipoAvaliacao.PROVA);
        provaMat.setPeso(new BigDecimal("2.00"));
        provaMat.setNotaMaxima(new BigDecimal("10.00"));
        provaMat.setDataEntrega(LocalDate.of(2026, 11, 12));
        provaMat.setTurma(turmaEmed);
        provaMat.setDisciplina(discMatematica);
        provaMat.setProfessor(profRodrigo);
        provaMat = avaliacaoRepository.save(provaMat);

        registrarNotaSeed(provaMat, a1, "8.50", "Ótimo domínio de gráficos.");
        registrarNotaSeed(provaMat, a2, "7.00", "Bom desempenho.");
        registrarNotaSeed(provaMat, a3, "9.25", "Excelente resolução algébrica.");
        registrarNotaSeed(provaMat, a4, "6.50", "Atenção aos sinais em Bhaskara.");
        registrarNotaSeed(provaMat, a5, "8.00", "Muito consistente.");

        Avaliacao trabHist = new Avaliacao();
        trabHist.setTitulo("Seminário — Segunda Guerra Mundial");
        trabHist.setTipo(TipoAvaliacao.SEMINARIO);
        trabHist.setPeso(new BigDecimal("1.00"));
        trabHist.setNotaMaxima(new BigDecimal("10.00"));
        trabHist.setDataEntrega(LocalDate.of(2026, 11, 14));
        trabHist.setTurma(turmaEmed);
        trabHist.setDisciplina(discHistoria);
        trabHist.setProfessor(profSwelen);
        trabHist = avaliacaoRepository.save(trabHist);

        registrarNotaSeed(trabHist, a1, "9.00", "Apresentação clara e bem fundamentada.");
        registrarNotaSeed(trabHist, a2, "7.50", "Boa participação.");
        registrarNotaSeed(trabHist, a3, "9.50", "Pesquisa histórica exemplar.");
        registrarNotaSeed(trabHist, a4, "8.00", "Bom domínio do tema.");
        registrarNotaSeed(trabHist, a5, "8.75", "Ótima contextualização.");

        // 7. PLANOS DE AULA INICIAIS (para consulta imediata em GET /api/aluno/aulas)
        PlanoAula p1 = new PlanoAula();
        p1.setTurma(turmaEmed);
        p1.setDisciplina(discMatematica);
        p1.setProfessor(profRodrigo);
        p1.setDataAula(aula1);
        p1.setTitulo("Aula 03 — Funções do 1º e 2º Grau");
        p1.setConteudo("Estudo de coeficientes, raízes, vértice da parábola e aplicações práticas em problemas de vestibular.");
        p1.setObjetivos("Compreender o comportamento gráfico e resolver equações associadas.");
        planoAulaRepository.save(p1);

        PlanoAula p2 = new PlanoAula();
        p2.setTurma(turmaEmed);
        p2.setDisciplina(discHistoria);
        p2.setProfessor(profSwelen);
        p2.setDataAula(aula2);
        p2.setTitulo("Aula 05 — Segunda Guerra Mundial e Era Vargas");
        p2.setConteudo("Antecedentes do conflito na Europa, participação da FEB e reflexos políticos no Brasil.");
        p2.setObjetivos("Relacionar o contexto geopolítico mundial com as transformações brasileiras.");
        planoAulaRepository.save(p2);

        log.info("Carga Inicial concluída! Gestor: admin@sanquim.br | Senha padrão: {}", SENHA_PADRAO);
    }

    private Professor criarProfessor(String nome, String cpf, String rg, String endereco,
                                     String bairro, String cidade, String fone, String email, String hash) {
        Usuario u = usuarioRepository.save(new Usuario(nome, email, hash, Role.ROLE_PROFESSOR));
        Professor p = new Professor();
        p.setUsuario(u);
        p.setNome(nome);
        p.setCpf(cpf);
        p.setRg(rg);
        p.setEndereco(endereco);
        p.setBairro(bairro);
        p.setCidade(cidade);
        p.setFone(fone);
        p.setEmail(email);
        p.setAtivo(true);
        return professorRepository.save(p);
    }

    private Aluno criarAluno(String nome, String ra, LocalDate nasc, String cpf, String rg,
                             String endereco, String bairro, String cidade, String fone, String email, String hash) {
        Usuario u = usuarioRepository.save(new Usuario(nome, email, hash, Role.ROLE_ALUNO));
        Aluno a = new Aluno();
        a.setUsuario(u);
        a.setNome(nome);
        a.setRa(ra);
        a.setDataNascimento(nasc);
        a.setCpf(cpf);
        a.setRg(rg);
        a.setEndereco(endereco);
        a.setBairro(bairro);
        a.setCidade(cidade);
        a.setFone(fone);
        a.setEmail(email);
        a.setAtivo(true);
        return alunoRepository.save(a);
    }

    private Turma criarTurma(String nome, String curso, String local, String periodo, int ano) {
        Turma t = new Turma();
        t.setNome(nome);
        t.setCurso(curso);
        t.setLocal(local);
        t.setPeriodo(periodo);
        t.setAnoLetivo(ano);
        t.setAtiva(true);
        return turmaRepository.save(t);
    }

    private Disciplina criarDisciplina(String nome, int cargaHoraria) {
        Disciplina d = new Disciplina();
        d.setNome(nome);
        d.setCargaHoraria(cargaHoraria);
        d.setAtiva(true);
        return disciplinaRepository.save(d);
    }

    private void registrarPresencaSeed(Aluno a, Turma t, Disciplina d, Professor p, LocalDate data, boolean presente) {
        Frequencia f = new Frequencia();
        f.setAluno(a);
        f.setTurma(t);
        f.setDisciplina(d);
        f.setProfessor(p);
        f.setDataAula(data);
        f.setPresente(presente);
        frequenciaRepository.save(f);
    }

    private void registrarNotaSeed(Avaliacao av, Aluno a, String valor, String obs) {
        Nota n = new Nota();
        n.setAvaliacao(av);
        n.setAluno(a);
        n.setValor(new BigDecimal(valor));
        n.setObservacao(obs);
        notaRepository.save(n);
    }
}
