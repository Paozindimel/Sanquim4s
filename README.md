# API REST — Sistema de Gestão Escolar Colégio Sanquim

Projeto desenvolvido para a disciplina **Programação de Scripts Backend**, do curso de Análise e Desenvolvimento de Sistemas (ADS), da FATEC Arthur de Azevedo — Mogi Mirim/SP.

A API REST do Colégio Sanquim tem como objetivo gerenciar informações escolares, permitindo o controle de alunos, professores, turmas, disciplinas, matrículas, avaliações, notas, frequência e planos de aula.

## Tecnologias utilizadas

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- JWT (JSON Web Token)
- BCrypt
- PostgreSQL
- Supabase
- MySQL (perfil alternativo)
- Maven
- Swagger / OpenAPI
- Jakarta Bean Validation

## Funcionalidades

### Gestão escolar
- Cadastro e consulta de professores.
- Cadastro e consulta de alunos.
- Cadastro e consulta de turmas.
- Cadastro de disciplinas.
- Atribuição de professores às disciplinas e turmas.
- Matrícula de alunos.

### Diário escolar
- Consulta das turmas vinculadas ao professor.
- Registro de chamadas.
- Cadastro de avaliações.
- Lançamento e atualização de notas.
- Cadastro de planos de aula.

### Portal do aluno
- Consulta do boletim.
- Consulta das notas e médias parciais.
- Consulta da frequência geral e por disciplina.
- Consulta dos planos de aula.

## Segurança e autenticação

A API utiliza autenticação JWT (JSON Web Token) e controle de acesso baseado em funções (RBAC).

O sistema possui três perfis:

| Perfil | Permissões |
|---|---|
| ROLE_GESTAO | Ger