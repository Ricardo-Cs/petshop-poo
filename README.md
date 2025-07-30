# PetShop POO

Este projeto é uma aplicação de gerenciamento de Pet Shop desenvolvida em Java, utilizando JavaFX para a interface gráfica, Hibernate para persistência de dados e Maven para gerenciamento de dependências.

## Visão Geral

O sistema permite o gerenciamento de:
* **Tutores:** Cadastro, listagem, edição e exclusão de tutores.
* **Animais:** Cadastro, listagem, edição e exclusão de animais, associando-os a tutores.
* **Funcionários:** Cadastro, listagem, edição e exclusão de diferentes tipos de funcionários (Veterinários, Atendentes, Tosadores) com suas particularidades.
* **Atendimentos:** Registro de atendimentos de atendentes (compra, banho, tosa) e atendimentos veterinários (consulta, exames, etc.).

## Tecnologias Utilizadas

* **Linguagem:** Java 11
* **Interface Gráfica:** JavaFX 11.0.2
* **Persistência:** Hibernate 5.4.32.Final (JPA)
* **Banco de Dados:** MySQL
* **Gerenciador de Dependências:** Apache Maven

## Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

* **Java Development Kit (JDK) 11 ou superior:** Você pode baixá-lo no site oficial da Oracle ou usar uma distribuição OpenJDK (ex: Adoptium, Amazon Corretto).
* **Apache Maven:** Baixe e configure o Maven seguindo as instruções em [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi).
* **Servidor MySQL:** Instale um servidor MySQL (versão 8.0 ou superior recomendada). Você pode usar o MySQL Community Server ou o XAMPP/WAMP/MAMP para uma instalação local mais fácil.

## Configuração do Banco de Dados

O projeto utiliza um banco de dados MySQL. O Hibernate está configurado para criar/atualizar o esquema do banco de dados automaticamente (`hbm2ddl.auto=update`).

1.  **Crie o Banco de Dados:**
    Crie um banco de dados vazio no seu servidor MySQL com o nome `petshop_db`.
    Você pode fazer isso através de um cliente MySQL (como MySQL Workbench, DBeaver) ou linha de comando:
    ```sql
    CREATE DATABASE petshop_db;
    ```

2.  **Credenciais do Banco de Dados:**
    As credenciais de conexão estão configuradas no arquivo `src/main/resources/META-INF/persistence.xml`.
    * **Driver:** `com.mysql.cj.jdbc.Driver`
    * **URL:** `jdbc:mysql://localhost:3306/petshop_db?useTimezone=true&serverTimezone=UTC`
    * **Usuário:** `root`
    * **Senha:** `(vazia)`

    Se as suas credenciais do MySQL forem diferentes (por exemplo, você tem uma senha para o usuário `root`), você precisará editar o arquivo `persistence.xml` para refletir suas configurações locais:
    ```xml
    <property name="javax.persistence.jdbc.user" value="seu_usuario"/>
    <property name="javax.persistence.jdbc.password" value="sua_senha"/>
    ```

## Como Executar

Siga os passos abaixo para compilar e executar a aplicação:

1.  **Clone o Repositório:**
    Se você ainda não tem o código, clone o repositório para o seu ambiente local.
    ```bash
    git clone <URL_DO_REPOSITORIO>
    cd petshop-poo
    ```
    *(Nota: Substitua `<URL_DO_REPOSITORIO>` pelo URL real do seu repositório Git.)*

2.  **Compile o Projeto com Maven:**
    Navegue até o diretório raiz do projeto (`petshop-poo`) e execute o comando Maven para compilar:
    ```bash
    mvn clean install
    ```
    Este comando baixará todas as dependências necessárias e compilará o projeto.

3.  **Execute a Aplicação:**
    Após a compilação bem-sucedida, você pode executar a aplicação JavaFX a partir do Maven:
    ```bash
    mvn javafx:run
    ```
    Alternativamente, você pode executar a classe principal `com.poo.petshop.App` de sua IDE (IntelliJ IDEA, Eclipse, VS Code) após importar o projeto Maven.

A aplicação abrirá uma janela da interface gráfica do Pet Shop, e o Hibernate configurará automaticamente as tabelas no `petshop_db` se elas não existirem ou precisarão ser atualizadas.

## Estrutura do Projeto (Pacotes)

A estrutura de pacotes do projeto segue uma organização lógica para separar as responsabilidades:

* `src/main/java/`
    * `com.poo.petshop.App`: Classe principal da aplicação JavaFX.
    * `com.poo.petshop.config`: Classes de configuração, como a conexão JPA.
    * `com.poo.petshop.controller`: Controladores da interface de usuário JavaFX.
    * `com.poo.petshop.dao`: Objetos de Acesso a Dados (DAOs) para operações de persistência.
    * `com.poo.petshop.model`: Classes de modelo (entidades de domínio).
    * `com.poo.petshop.model.enums`: Enums utilizados nos modelos.
    * `com.poo.petshop.service`: Camada de serviços com a lógica de negócio.
    * `com.poo.petshop.util`: Classes utilitárias, como validação de CPF e exceções personalizadas.
* `src/main/resources/com/poo/petshop/view`: Arquivos FXML que definem o layout das telas da interface gráfica.
* `src/main/resources/META-INF/persistence.xml`: Arquivo de configuração do JPA para o Hibernate.

---
