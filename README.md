# **API REST FULL Candidatos**

Este é um projeto de uma API REST para gerenciar candidatos, desenvolvido como parte do teste da Maxxmobi. A API permite realizar operações CRUD (Create, Read, Update, Delete) em candidatos.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.3.2
- Spring Data JPA
- Spring Security
- Spring Boot DevTools
- Swagger/OpenAPI
- PostgreSQL
- MySQL
- JUnit
- H2 Database
- JWT (Json Web Token)
- Docker

## Dependências

### Gerenciamento de Dependências

As dependências do projeto são gerenciadas através do Maven. Abaixo está um resumo das principais dependências utilizadas:

- **Spring Boot Starter Web**: para construir a aplicação web.
- **Spring Boot Starter Data JPA**: para persistência de dados.
- **Spring Boot Starter Validation**: para validação de dados.
- **Spring Boot DevTools**: para facilitar o desenvolvimento.
- **PostgreSQL**: driver JDBC para o banco de dados PostgreSQL.
- **MySQL**: driver JDBC para o banco de dados MySQL.
- **H2 Database**: banco de dados em memória utilizado para testes.
- **Spring Boot Starter Test**: para testes unitários e de integração.
- **Spring Boot Starter Security**: para segurança da aplicação.
- **JWT**: para geração e validação de tokens JWT.
- **Swagger/OpenAPI**: para documentação da API.
- **Docker**: para containerização da aplicação.

## Configuração do Projeto

### Banco de Dados

A aplicação está configurada para utilizar MySQL em desenvolvimento local e PostgreSQL em produção no Render.

#### Configuração para Desenvolvimento Local (MySQL)

No arquivo `src/main/resources/application-dev.properties`:
```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database=mysql
spring.datasource.url=jdbc:mysql://localhost/db_maxxmobi?createDatabaseIfNotExist=true&serverTimezone=America/Sao_Paulo&useSSl=false
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQLDialect

spring.jpa.properties.jakarta.persistence.sharedCache.mode=ENABLE_SELECTIVE

spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=Brazil/East
```

#### Configuração para Produção (PostgreSQL)

No arquivo `src/main/resources/application-pro.properties`:

```properties
spring.jpa.generate-ddl=true
spring.jpa.database=postgresql
spring.datasource.url=jdbc:postgresql://${POSTGRESHOST}:${POSTGRESPORT}/${POSTGRESDATABASE}
spring.datasource.username=${POSTGRESUSER}
spring.datasource.password=${POSTGRESPASSWORD}

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.jakarta.persistence.sharedCache.mode=ENABLE_SELECTIVE

spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=Brazil/East

```

### Configuração Geral

### Configuração Geral e Swagger

No arquivo `src/main/resources/application.properties`:

```properties
spring.profiles.active=prod

springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.disable-swagger-default-url=true
springdoc.swagger-ui.use-root-path=true
springdoc.packagesToScan=br.com.maxxmobi.teste.controller

```

## Docker

Este projeto inclui um Dockerfile para facilitar a criação de contêineres Docker. O Dockerfile é configurado para construir e rodar a aplicação em produção com OpenJDK 17.0.1.

### Dockerfile

```dockerfile
FROM openjdk:17.0.1-jdk-oracle as build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod -R 777 ./mvnw

RUN ./mvnw install -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:17.0.1-jdk-oracle

VOLUME /tmp

ARG DEPENDENCY=/workspace/app/target/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","br.com.maxxmobi.teste.ApiRestFullCantidatosApplication"]

```

### Executando o Projeto

1. Clone o repositório:

   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd <NOME_DO_PROJETO>
   ```

2. Configure o banco de dados conforme descrito acima.

   

3. Execute o projeto usando Maven:

   ```bash
   mvn spring-boot:run
   ```

### Documentação da API

A documentação da API está disponível no Swagger. Após iniciar a aplicação, acesse a seguinte URL para visualizar a documentação:

```bash
http://localhost:8080/swagger-ui.html
```

Para acessar a documentação da API hospedada na plataforma Render, utilize a URL:

```
https://projeto-teste-maxxmobi.onrender.com/swagger-ui.html
```

### Credenciais de Acesso

Para acessar a API, utilize as seguintes credenciais:

- **Usuário**: `admin@email.com`
- **Senha**: `admin123`

### Criando um Novo Usuário

Para acessar a API localmente devemos criar um novo usuário, utilize o Insomnia ou outra ferramenta similar para fazer uma requisição POST para a URL:

```
http://localhost:8080/usuarios/cadastrar
```

## Segurança

A segurança da aplicação é gerenciada pelo Spring Security. A autenticação e autorização são feitas através de tokens JWT.

## Testes com JUnit

Os testes unitários e de integração foram realizados utilizando JUnit. Para executar os testes, utilize o comando:

```bash
mvn test
```

Foram realizados testes para garantir o funcionamento correto dos endpoints relacionados aos usuários. A seguir, estão descritos os testes realizados com o `UsuarioController`:

### Testes Realizados

#### 1. Cadastro de Usuário

```java
@Test
@DisplayName("Cadastrar um Usuário")
public void deveCriarUmUsuario() {
    HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Edvaldo Junior", "edvaldo.java@email.com.br", "12345678"));

    ResponseEntity<Usuario> corpoResposta = testRestTemplate
            .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

    assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
}
```

**Descrição**: Testa a criação de um novo usuário.

**Expectativa**: O status HTTP retornado deve ser `201 Created`.

2. #### Não Permitir Duplicação de Usuário

```java
@Test
@DisplayName("Não deve permitir duplicação do Usuário")
public void naoDeveDuplicarUsuario() {
    usuarioService.cadastrarUsuario(new Usuario(0L, "Maria da Silva", "maria_silva@email.com.br", "12345678"));

HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Maria da Silva", "maria_silva@email.com.br", "12345678"));

ResponseEntity<Usuario> corpoResposta = testRestTemplate
        .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
}
```
**Descrição**: Testa a tentativa de cadastro de um usuário com dados duplicados.

**Expectativa**: O status HTTP retornado deve ser `400 Bad Request`.

3. #### Atualização de Usuário

   ```java
   @Test
   @DisplayName("Atualizar um Usuário")
   public void deveAtualizarUmUsuario() {
       Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "Juliana Andrews", "juliana_Ramos@email.com.br", "juliana123"));
   
       Usuario usuarioUpdate = new Usuario((usuarioCadastrado.get().getId()),
               "Juliana Andrews", "juliana_Ramos@email.com.br", "juliana123");
   
       HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
   
       ResponseEntity<Usuario> corpoResposta = testRestTemplate
               .withBasicAuth("root@root.com", "rootroot")
               .exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);
   
       assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
   }
   
   ```

**Descrição**: Testa a atualização dos dados de um usuário existente.

**Expectativa**: O status HTTP retornado deve ser `200 OK`.

4. #### Listar Todos os Usuários

   ```java
   @Test
   @DisplayName("Listar todos os Usuários")
   public void deveMostrarTodosUsuarios() {
       usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123"));
       usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123"));
   
       ResponseEntity<String> resposta = testRestTemplate
               .withBasicAuth("root@root.com", "rootroot")
               .exchange("/usuarios/all", HttpMethod.GET, null, String.class);
   
       assertEquals(HttpStatus.OK, resposta.getStatusCode());
   }
   
   ```

   - **Descrição**: Testa a listagem de todos os usuários cadastrados.
   - **Expectativa**: O status HTTP retornado deve ser `200 OK`.

   ### Configuração dos Testes

   - **Ambiente**: O teste é executado em um ambiente de aplicação configurado para usar uma porta aleatória (`SpringBootTest.WebEnvironment.RANDOM_PORT`).
   - **Autenticação**: Alguns testes requerem autenticação básica para acessar endpoints protegidos.

   Esses testes garantem que o controle de usuários está funcionando conforme o esperado e validam o comportamento da aplicação em cenários típicos de operação.

## Códigos SQL Gerados pelo JPA Repository

Abaixo estão os códigos SQL que correspondem à criação da tabela `tb_candidatos` baseada na entidade `CandidadosModel`:

```sql
CREATE TABLE tb_candidatos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nascimento DATE NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    sexo CHAR(1) DEFAULT 'M' NOT NULL,
    nota INT NOT NULL,
    logradouro VARCHAR(200),
    bairro VARCHAR(50),
    cidade VARCHAR(50),
    uf CHAR(2)
);
```

### Detalhes da Tabela

- **id**: Identificador único da entidade, gerado automaticamente pelo banco de dados.
- **nome**: Nome do candidato, com comprimento máximo de 100 caracteres.
- **nascimento**: Data de nascimento do candidato, armazenada como `DATE`.
- **data_criacao**: Data e hora em que o registro foi criado, armazenada como `TIMESTAMP`.
- **sexo**: Sexo do candidato, com valor padrão 'M' (Masculino), armazenado como `CHAR(1)`.
- **nota**: Nota do candidato, armazenada como `INT`.
- **logradouro**: Endereço do candidato, armazenado como `VARCHAR(200)`.
- **bairro**: Bairro do candidato, armazenado como `VARCHAR(50)`.
- **cidade**: Cidade do candidato, armazenada como `VARCHAR(50)`.
- **uf**: Unidade Federativa (estado) do candidato, armazenada como `CHAR(2)`.

### Notas

- **Valores Padrão**: O campo `sexo` tem um valor padrão definido como 'M' (Masculino).
- **Nulabilidade**: Campos como `logradouro`, `bairro`, `cidade`, e `uf` são opcionais e podem ser `NULL`.

### Entidade `Usuario`

Aqui estão os códigos SQL que correspondem à criação da tabela `tb_usuarios` baseada na entidade `Usuario`:

```sql
CREATE TABLE tb_usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    usuario VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);
```

### Detalhes da Tabela

- **id**: Identificador único da entidade, gerado automaticamente pelo banco de dados.
- **nome**: Nome do usuário, com comprimento máximo de 255 caracteres.
- **usuario**: Email do usuário, com comprimento máximo de 255 caracteres e deve ser único.
- **senha**: Senha do usuário, com comprimento máximo de 255 caracteres.

### Notas

- **Valores Padrão**: O campo `nome`, `usuario`, e `senha` são obrigatórios e não podem ser nulos.
- **Restrições**: O campo `usuario` deve ser único e deve seguir o formato de um email válido.

Caso queira verificar os códigos SQL gerados, você pode habilitar a exibição de SQL no Hibernate através da configuração `spring.jpa.show-sql=true` no arquivo `application.properties`.

## Análise da Estrutura de Pastas e Classes do Projeto

### Estrutura de Pastas

A estrutura de pastas apresenta uma organização comum em projetos Spring Boot, com as seguintes responsabilidades:

- **config:** Contém configurações gerais do projeto, como a configuração do Swagger (para documentação da API).
- **controller:** Contém os controladores, que são responsáveis por receber as requisições HTTP e retornar as respostas.
- **model:** Contém as classes que representam os dados do domínio do negócio (entidades).
- **repository:** Contém as interfaces e classes que interagem com o banco de dados (geralmente utilizando JPA ou outra ORM).
- **security:** Contém classes relacionadas à segurança da aplicação, como filtros para autenticação e autorização.
- **service:** Contém as classes de serviço, que encapsulam a lógica de negócio e podem chamar os repositórios para acessar os dados.
- **resources:** Contém arquivos estáticos (imagens, CSS, JavaScript) e templates para a camada de apresentação (se houver).
- **test:** Contém os testes unitários e de integração.

### Descrição das Classes e Interfaces 

- **config.SwaggerConfig:**
- Configura a geração da documentação da API utilizando o Swagger.

- **controller.CandidatoController:**
- Controla as requisições relacionadas a candidatos, como listar, criar, atualizar e excluir candidatos.
- **controller.UsuarioController:**
- Controla as requisições relacionadas a usuários, como login, cadastro e gerenciamento de perfis.

- **model.CandidatosModel:**
- Representa um candidato, com atributos como nome, email, etc.
- **model.Usuario:**
- Representa um usuário do sistema, com atributos como nome de usuário, senha, etc.
- **model.UsuarioLogin:**
- Pode ser utilizado para representar os dados de login de um usuário (nome de usuário e senha).

- **repository.CandidatosRepository:**
- Interface que define os métodos para acessar e manipular os dados de candidatos no banco de dados.
- **repository.UsuarioRepository:**
- Interface que define os métodos para acessar e manipular os dados de usuários no banco de dados.

- **security.BasicSecurityConfig:**
- Configura a segurança básica da aplicação, como autenticação por basic authentication.
- **security.JwtAuthFilter:**
- Filtro que realiza a autenticação utilizando tokens JWT.
- **security.JwtService:**
- Serviço responsável por gerar, validar e armazenar tokens JWT.
- **security.UserDetailsImpl:**
- Implementação da interface UserDetails, que representa um usuário autenticado.
- **security.UserDetailsServiceImpl:**
- Serviço responsável por carregar os detalhes do usuário a partir do banco de dados.
- **service:**
-  Responsável por gerenciar usuários do sistema. Ela provê métodos para cadastro.

- **service.ApiRestFullCandidatosApplication:**
- Classe principal da aplicação Spring Boot.

## **Análise Detalhada dos Endpoints do Controlador de Usuários**

### Entendendo os Endpoints 

- **GET /usuarios/{id}**

  - **Objetivo**: Buscar um usuário específico pelo seu ID.

  - **Exemplo de Requisição**:

    ```http
    GET /usuarios/1
    ```

  - **Exemplo de Resposta (Sucesso)**:

    ```json
    {
        "id": 1,
        "nome": "João da Silva",
        "email": "joao@email.com"
        // ... outros atributos do usuário
    }
    ```

  - **Exemplo de Resposta (Erro)**:

    ```json
    {
        "mensagem": "Usuário não encontrado"
    }
    ```

  ### **GET /usuarios/all**

  - **Objetivo**: Listar todos os usuários cadastrados.

  - **Exemplo de Requisição**:

    ```json
    GET /usuarios/all
    ```

  - **Exemplo de Resposta (Sucesso)**:

    ```json
    [
        {
            "id": 1,
            "nome": "João da Silva",
            "email": "joao@email.com"
        },
        {
            "id": 2,
            "nome": "Maria da Silva",
            "email": "maria@email.com"
        }
        // ... outros usuários
    ]
    ```

  ### **POST /usuarios/logar**

  - **Objetivo**: Realizar o login de um usuário.

  - **Exemplo de Requisição**:

    ```json
    {
        "email": "joao@email.com",
        "senha": "minhaSenha123"
    }
    ```

  - **Exemplo de Resposta (Sucesso)**:

    ```json
    {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGVtYWlsLmNvbSIsInJvbGUiOiJ1c2VyIiwiaWF0IjoxNjYxNTE2NTEyLCJleHAiOjE2NjE1MjAzMTJ9.yJmM1O854aGzvZ3X-ywOo97b0nmTvjNuuo6qwU7AiM"
    }
    ```

  - **Exemplo de Resposta (Erro)**:

    ```json
    {
        "mensagem": "Credenciais inválidas"
    }
    ```

  ### **POST /usuarios/cadastrar**

  - **Objetivo**: Cadastrar um novo usuário.

  - **Exemplo de Requisição**:

    ```json
    {
        "nome": "Novo Usuário",
        "email": "novousuario@email.com",
        "senha": "minhaNovaSenha"
    }
    ```

  - **Exemplo de Resposta (Sucesso)**:

    ```json
    {
        "mensagem": "Usuário cadastrado com sucesso"
    }
    ```

  - **Exemplo de Resposta (Erro)**:

    ```json
    {
        "mensagem": "Email já cadastrado"
    }
    ```

  ### **PUT /usuarios/atualizar**

  - **Objetivo**: Atualizar os dados de um usuário existente.

  - **Exemplo de Requisição**:

    ```json
    {
        "id": 1,
        "nome": "João da Silva Atualizado",
        "email": "joao.atualizado@email.com"
    }
    ```

  - **Exemplo de Resposta (Sucesso)**:

    ```json
    {
        "mensagem": "Usuário atualizado com sucesso"
    }
    ```

  - **Exemplo de Resposta (Erro)**:

    ```json
    {
        "mensagem": "Usuário não encontrado"
    }
    ```



## **Análise Detalhada dos Endpoints do Controlador de Candidatos**

### Entendendo os Endpoints 

### 1. **DELETE /candidatos/{id}**

**Descrição**: Exclui um candidato específico, identificado pelo seu ID.

- **Requisição**:

  ```http
  DELETE /candidatos/1
  ```

- **Resposta** (Sucesso):

  ```json
  {
    "message": "Candidato excluído com sucesso."
  }
  ```

- **Resposta** (Erro, por exemplo, candidato não encontrado):

  ```json
  {
    "error": "Candidato não encontrado."
  }
  ```

### 2. **GET /candidatos/{id}**

**Descrição**: Obtém os dados de um candidato específico, identificado pelo seu ID.

- **Requisição**:

  ```http
  GET /candidatos/1
  ```

- **Resposta** (Sucesso):

  ```json
  {
    "id": 1,
    "nome": "Marina Barros",
    "nascimento": "2003-10-10",
    "dataCriacao": "2024-07-25",
    "sexo": "F",
    "nota": 9,
    "logradouro": "Avenida dos Ipês",
    "bairro": "Ipês",
    "cidade": "Manaus",
    "uf": "AM"
  }
  ```

- **Resposta** (Erro, por exemplo, candidato não encontrado):

  ```json
  {
    "error": "Candidato não encontrado."
  }
  ```

### 3. **GET /candidatos/buscarPorSexo**

**Descrição**: Busca candidatos por sexo.

- **Requisição**:

  ```http
  GET /candidatos/buscarPorSexo?sexo=F
  ```

- **Resposta** (Sucesso):

  ```json
  [
    {
      "id": 1,
      "nome": "Marina Barros",
      "nascimento": "2003-10-10",
      "dataCriacao": "2024-07-25",
      "sexo": "F",
      "nota": 9,
      "logradouro": "Avenida dos Ipês",
      "bairro": "Ipês",
      "cidade": "Manaus",
      "uf": "AM"
    },
    {
      "id": 2,
      "nome": "Ana Souza",
      "nascimento": "1999-05-22",
      "dataCriacao": "2024-07-25",
      "sexo": "F",
      "nota": 8,
      "logradouro": "Rua das Flores",
      "bairro": "Jardim",
      "cidade": "São Paulo",
      "uf": "SP"
    }
  ]
  ```

- **Resposta** (Erro, por exemplo, parâmetro inválido):

  ```json
  {
    "error": "Sexo inválido. Use 'M' para masculino ou 'F' para feminino."
  }
  ```

### 4. **GET /candidatos/buscarPorNota**

**Descrição**: Busca candidatos por nota.

- **Requisição**:

  ```http
  GET /candidatos/buscarPorNota?nota=9
  ```

- **Resposta** (Sucesso):

  ```json
  [
    {
      "id": 1,
      "nome": "Marina Barros",
      "nascimento": "2003-10-10",
      "dataCriacao": "2024-07-25",
      "sexo": "F",
      "nota": 9,
      "logradouro": "Avenida dos Ipês",
      "bairro": "Ipês",
      "cidade": "Manaus",
      "uf": "AM"
    }
  ]
  ```

- **Resposta** (Erro, por exemplo, parâmetro inválido):

  ```json
  {
    "error": "Nota inválida. Deve ser um número inteiro."
  }
  ```

### 5. **GET /candidatos/buscarPorNome**

**Descrição**: Busca candidatos por nome.

- **Requisição**:

  ```http
  GET /candidatos/buscarPorNome?nome=Marina
  ```

- **Resposta** (Sucesso):

  ```json
  [
    {
      "id": 1,
      "nome": "Marina Barros",
      "nascimento": "2003-10-10",
      "dataCriacao": "2024-07-25",
      "sexo": "F",
      "nota": 9,
      "logradouro": "Avenida dos Ipês",
      "bairro": "Ipês",
      "cidade": "Manaus",
      "uf": "AM"
    }
  ]
  ```

- **Resposta** (Erro, por exemplo, nome não encontrado):

  ```json
  {
    "error": "Nenhum candidato encontrado com o nome fornecido."
  }
  ```

### 6. **GET /candidatos/buscarPorNascimento**

**Descrição**: Busca candidatos por data de nascimento.

- **Requisição**:

  ```http
  GET /candidatos/buscarPorNascimento?nascimento=2003-10-10
  ```

- **Resposta** (Sucesso):

  ```json
  [
    {
      "id": 1,
      "nome": "Marina Barros",
      "nascimento": "2003-10-10",
      "dataCriacao": "2024-07-25",
      "sexo": "F",
      "nota": 9,
      "logradouro": "Avenida dos Ipês",
      "bairro": "Ipês",
      "cidade": "Manaus",
      "uf": "AM"
    }
  ]
  ```

- **Resposta** (Erro, por exemplo, data inválida):

  ```json
  {
    "error": "Data de nascimento inválida. Use o formato YYYY-MM-DD."
  }
  ```

### 7. **GET /candidatos/busca/ordenada**

**Descrição**: Busca todos os candidatos de forma ordenada (o critério de ordenação deve ser especificado com ASC ou DESC).

- **Requisição**:

  ```http
  GET /candidatos/busca/ordenada?ordem=nome
  ```

- **Resposta** (Sucesso):

  ```json
  [
    {
      "id": 2,
      "nome": "Ana Souza",
      "nascimento": "1999-05-22",
      "dataCriacao": "2024-07-25",
      "sexo": "F",
      "nota": 8,
      "logradouro": "Rua das Flores",
      "bairro": "Jardim",
      "cidade": "São Paulo",
      "uf": "SP"
    },
    {
      "id": 1,
      "nome": "Marina Barros",
      "nascimento": "2003-10-10",
      "dataCriacao": "2024-07-25",
      "sexo": "F",
      "nota": 9,
      "logradouro": "Avenida dos Ipês",
      "bairro": "Ipês",
      "cidade": "Manaus",
      "uf": "AM"
    }
  ]
  ```

- **Resposta** (Erro, por exemplo, critério de ordenação inválido):

  ```json
  {
    "error": "Critério de ordenação inválido. Use 'nome', 'nota', etc."
  }
  ```

### 8. **POST /candidatos**

**Descrição**: Cria um novo candidato.

- **Requisição**:

  ```http
  POST /candidatos
  ```

  **Corpo da Requisição**:

  ```json
  {
    "nome": "Marina Barros",
    "nascimento": "2003-10-10",
    "dataCriacao": "2024-07-25",
    "sexo": "F",
    "nota": 9,
    "logradouro": "Avenida dos Ipês",
    "bairro": "Ipês",
    "cidade": "Manaus",
    "uf": "AM"
  }
  ```

- **Resposta** (Sucesso):

  ```json
  {
    "message": "Candidato criado com sucesso.",
    "id": 3
  }
  ```

- **Resposta** (Erro, por exemplo, dados inválidos):

  ```json
  jsonCopiar código{
    "error": "Dados inválidos. Verifique os campos e tente novamente."
  }
  ```

### 9. **PUT /candidatos**

**Descrição**: Atualiza os dados de um candidato (provavelmente, o ID é enviado no corpo da requisição).

- **Requisição**:

  ```http
  PUT /candidatos
  ```

  **Corpo da Requisição**:

  ```json
  {
    "id": 1,
    "nome": "Marina Barros",
    "nascimento": "2003-10-10",
    "dataCriacao": "2024-07-25",
    "sexo": "F",
    "nota": 10,
    "logradouro": "Avenida dos Ipês",
    "bairro": "Ipês",
    "cidade": "Manaus",
    "uf": "AM"
  }
  ```

- **Resposta** (Sucesso):

  ```json
  {
    "message": "Candidato atualizado com sucesso."
  }
  ```

- **Resposta** (Erro, por exemplo, candidato não encontrado):

  ```json
  {
    "error": "Candidato não encontrado. Verifique o ID e tente novamente."
  }
  ```

## **ACESSE O PDF DA DOCUMENTAÇÃO EM SWAGGER E ENTENDA MELHOR CADA ENDPOINT**:

[https://github.com/edvaldoljr/Projeto-Teste-Maxxmobi/blob/main/Projeto%20Teste%20Maxxmobi.pdf]()

**Prezado Altamiro Santos**

Agradeço pela oportunidade de participar do processo seletivo para a vaga de desenvolvedor Java júnior. Estou entregando o teste conforme solicitado e estou à disposição para quaisquer esclarecimentos ou dúvidas que possam surgir.

Espero que o trabalho realizado esteja alinhado com o que vocês estão buscando. Agradeço a sua consideração e aguardo ansiosamente o retorno.

Atenciosamente,
Edvaldo Junior