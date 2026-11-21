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

O sistema possui três perfis de acesso:

| Perfil | Permissões |
|---|---|
| ROLE_GESTAO | Gerenciamento de alunos, professores, turmas, disciplinas e matrículas. |
| ROLE_PROFESSOR | Acesso às turmas vinculadas, chamadas, avaliações, notas e planos de aula. |
| ROLE_ALUNO | Consulta do próprio boletim, frequência e planos de aula. |

As senhas são armazenadas utilizando criptografia BCrypt.

As rotas protegidas exigem um token JWT válido. O sistema retorna:
- **401 Unauthorized:** quando não há autenticação válida.
- **403 Forbidden:** quando o usuário autenticado não possui permissão para acessar a rota.

## Requisitos

Antes de executar o projeto, é necessário ter instalado:

- Java JDK 17.
- Maven.
- PostgreSQL ou uma instância PostgreSQL disponibilizada pelo Supabase.
- Postman (para testar os endpoints).

## Como executar o projeto

### 1. Clonar o repositório

```bash
git clone https://github.com/Paozindimel/Sanquim4s.git
```

Entre na pasta do projeto:

```bash
cd Sanquim4s
```

### 2. Configurar o banco de dados

O projeto utiliza PostgreSQL como banco de dados.

Crie um banco chamado `sanquim` no PostgreSQL ou utilize uma instância PostgreSQL do Supabase.

Por padrão, a aplicação utiliza:

| Configuração | Valor padrão |
|---|---|
| Banco de dados | sanquim |
| Usuário | postgres |
| Senha | postgres |
| Porta | 5432 |

Caso utilize outras credenciais, configure as seguintes variáveis de ambiente:

| Variável | Descrição |
|---|---|
| DB_URL | URL de conexão JDBC do PostgreSQL |
| DB_USERNAME | Usuário do banco |
| DB_PASSWORD | Senha do banco |
| JWT_SECRET | Chave secreta utilizada para assinar os tokens JWT |
| JWT_EXPIRATION_MS | Tempo de validade do token em milissegundos |

A aplicação utiliza o Hibernate com `ddl-auto=update` para atualizar a estrutura das tabelas conforme as entidades do projeto.

### 3. Executar a aplicação

Na pasta raiz do projeto, execute:

```bash
mvn spring-boot:run
```

A API será iniciada na porta 8080.

Endereço local:

```text
http://localhost:8080
```

## Dados de acesso para testes

O projeto possui uma carga inicial de dados (DataSeeder), executada automaticamente quando o banco ainda não possui usuários cadastrados.

**Senha padrão para as contas de demonstração:**

```text
Sanquim@123
```

### Gestão

| Perfil | E-mail |
|---|---|
| Gestão | admin@sanquim.br |

### Professores

| Perfil | E-mail |
|---|---|
| Professor | rodrigo.martins@sanquim.br |
| Professor | swelen.roberta@sanquim.br |

### Alunos

| Perfil | E-mail |
|---|---|
| Aluno | roberto@aluno.sanquim.br |
| Aluno | gabriel.silva@aluno.sanquim.br |
| Aluno | isabella.costa@aluno.sanquim.br |
| Aluno | leo.leme@aluno.sanquim.br |
| Aluno | solange.cabral@aluno.sanquim.br |

Essas credenciais são destinadas exclusivamente à demonstração e aos testes acadêmicos.

## Documentação da API — Swagger

Com a aplicação em execução, a documentação pode ser acessada pelo endereço:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

A documentação permite visualizar os endpoints disponíveis e realizar requisições à API.

## Testes com Postman

Na raiz do repositório está disponível o arquivo:

`Sanquim_API.postman_collection.json`

Para utilizar:

1. Abra o Postman.
2. Clique em **Import**.
3. Selecione o arquivo `Sanquim_API.postman_collection.json`.
4. Importe a coleção.
5. Execute as requisições de login de acordo com o perfil desejado.
6. Utilize as demais requisições para testar as funcionalidades.

A coleção possui requisições organizadas por perfil:

- Gestão.
- Professor.
- Aluno.

Os scripts da coleção armazenam o token JWT retornado pelo login para utilização nas requisições seguintes.

Também há uma requisição para verificar o bloqueio de acesso de um aluno a uma rota administrativa, cujo resultado esperado é **403 Forbidden**.

## Estrutura do projeto

O projeto segue a organização em camadas:

| Pacote / Camada | Responsabilidade |
|---|---|
| Controller | Recebe e organiza as requisições HTTP. |
| Service | Contém as regras de negócio. |
| Repository | Realiza o acesso ao banco de dados. |
| Domain / Entity | Representa as entidades do sistema. |
| DTO | Organiza os dados de entrada e saída da API. |
| Security | Configura autenticação e autorização. |
| Config | Contém configurações e carga inicial de dados. |
| Exception | Centraliza o tratamento de exceções. |

## Tratamento de erros

A aplicação possui tratamento centralizado de exceções por meio do `GlobalExceptionHandler`, incluindo:

- Erros de validação.
- Dados JSON inválidos.
- Recursos não encontrados.
- Recursos duplicados.
- Erros de autenticação.
- Acesso não autorizado.

## Autor

Desenvolvido por **Beatriz Gonçalves Silva Costa**.

Curso: Análise e Desenvolvimento de Sistemas (ADS)  
Instituição: FATEC Arthur de Azevedo — Mogi Mirim/SP  
Disciplina: Programação de Scripts Backend
