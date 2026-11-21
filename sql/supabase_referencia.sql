-- =====================================================================
-- SCRIPT DE REFERÊNCIA POSTGRESQL / SUPABASE — COLÉGIO SANQUIM API
-- Observação: O Spring Boot (Hibernate ddl-auto=update + DataSeeder)
-- cria todas as tabelas e popula os dados iniciais automaticamente ao
-- iniciar a aplicação! Use este script apenas para consulta/DER.
-- =====================================================================

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS professores (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE REFERENCES usuarios(id),
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(20) NOT NULL UNIQUE,
    rg VARCHAR(20),
    endereco VARCHAR(120),
    bairro VARCHAR(60),
    cidade VARCHAR(60),
    fone VARCHAR(30),
    email VARCHAR(100) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS alunos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE REFERENCES usuarios(id),
    nome VARCHAR(100) NOT NULL,
    ra VARCHAR(25) NOT NULL UNIQUE,
    data_nascimento DATE,
    cpf VARCHAR(20) NOT NULL UNIQUE,
    rg VARCHAR(20),
    endereco VARCHAR(120),
    bairro VARCHAR(60),
    cidade VARCHAR(60),
    fone VARCHAR(30),
    email VARCHAR(100) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS turmas (
    id BIGSERIAL PRIMARY KEY,
    nome_turma VARCHAR(80) NOT NULL UNIQUE,
    curso VARCHAR(60) NOT NULL,
    local VARCHAR(40),
    periodo VARCHAR(30) NOT NULL,
    ano_letivo INTEGER NOT NULL DEFAULT 2026,
    ativa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS disciplinas (
    id BIGSERIAL PRIMARY KEY,
    nome_disciplina VARCHAR(80) NOT NULL UNIQUE,
    carga_horaria INTEGER NOT NULL,
    ativa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS atribuicoes_aula (
    id BIGSERIAL PRIMARY KEY,
    professor_id BIGINT NOT NULL REFERENCES professores(id),
    turma_id BIGINT NOT NULL REFERENCES turmas(id),
    disciplina_id BIGINT NOT NULL REFERENCES disciplinas(id),
    UNIQUE (turma_id, disciplina_id)
);

CREATE TABLE IF NOT EXISTS matriculas (
    id BIGSERIAL PRIMARY KEY,
    aluno_id BIGINT NOT NULL REFERENCES alunos(id),
    turma_id BIGINT NOT NULL REFERENCES turmas(id),
    data_matricula DATE NOT NULL DEFAULT CURRENT_DATE,
    situacao VARCHAR(25) NOT NULL DEFAULT 'ATIVA',
    UNIQUE (aluno_id, turma_id)
);

CREATE TABLE IF NOT EXISTS frequencias (
    id BIGSERIAL PRIMARY KEY,
    aluno_id BIGINT NOT NULL REFERENCES alunos(id),
    turma_id BIGINT NOT NULL REFERENCES turmas(id),
    disciplina_id BIGINT NOT NULL REFERENCES disciplinas(id),
    professor_id BIGINT REFERENCES professores(id),
    data_aula DATE NOT NULL,
    presente BOOLEAN NOT NULL,
    UNIQUE (aluno_id, turma_id, disciplina_id, data_aula)
);

CREATE TABLE IF NOT EXISTS avaliacoes (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(120) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    peso NUMERIC(5,2) NOT NULL,
    nota_maxima NUMERIC(5,2) NOT NULL,
    data_entrega DATE NOT NULL,
    turma_id BIGINT NOT NULL REFERENCES turmas(id),
    disciplina_id BIGINT NOT NULL REFERENCES disciplinas(id),
    professor_id BIGINT REFERENCES professores(id)
);

CREATE TABLE IF NOT EXISTS notas (
    id BIGSERIAL PRIMARY KEY,
    avaliacao_id BIGINT NOT NULL REFERENCES avaliacoes(id),
    aluno_id BIGINT NOT NULL REFERENCES alunos(id),
    valor NUMERIC(5,2) NOT NULL,
    observacao VARCHAR(255),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (avaliacao_id, aluno_id)
);

CREATE TABLE IF NOT EXISTS planos_aula (
    id BIGSERIAL PRIMARY KEY,
    turma_id BIGINT NOT NULL REFERENCES turmas(id),
    disciplina_id BIGINT NOT NULL REFERENCES disciplinas(id),
    professor_id BIGINT REFERENCES professores(id),
    data_aula DATE NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    conteudo TEXT NOT NULL,
    objetivos TEXT
);
