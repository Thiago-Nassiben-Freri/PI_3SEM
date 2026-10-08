# PetVax
Projeto Integrador (PI) para o 3º semestre de ADS da FATEC

### Qual problema o sistema resolve?

O acompanhamento adequado da vacinação dos animais é uma atividade importante na rotina de atendimento veterinário. O uso de registros manuais ou planilhas pode dificultar o controle das datas de vacinação, dos históricos dos Pets e das próximas doses, aumentando a possibilidade de falhas de organização.
O PetVax busca oferecer uma solução simples e prática para centralizar essas informações e auxiliar os médicos veterinários durante os atendimentos presenciais. Por meio do cadastro, agendamento e registro das vacinas, os profissionais poderão acompanhar a situação de cada Pet e receber notificações sobre vacinações programadas ou próximas do prazo, tornando o processo mais organizado e reduzindo a possibilidade de esquecimento.
***

### Para quem é o sistema? (público-alvo)
O sistema será utilizado exclusivamente pelos médicos veterinários responsáveis pela empresa, que também são os proprietários da empresa e realizam os atendimentos de forma presencial.
***

### Descrição geral do sistema
O sistema em desenvolvimento para a Vet Home, denominado PetVax, visa facilitar o processo de agendamento, acompanhamento e notificação de vacinação de animais de estimação por meio de um programa desktop desenvolvido em Java.
O PetVax permitirá aos médicos veterinários cadastrar e consultar tutores, seus respectivos Pets e as vacinas disponíveis, além de realizar o agendamento das aplicações e registrar o histórico de vacinação. O tutor não terá acesso direto ao sistema; suas informações serão registradas e administradas pelos veterinários durante ou a partir dos atendimentos presenciais.
***

### Técnologias utilizadas
**Java + Springboot + JavaFX:** Utilizados no desenvolvimento do sistema, implementação das regras de nogócio, criação da API e interface gráfica.

**MySQL:** Utilizado para o armazenamento e gerenciamento dos dados dos animais, incluindo histórico de saúde, vacinação, doenças, tratamentos e consultas.
***

### Estrutura Inicial (Provisório)
```
app/
 ├── src/
 │   ├── main/
 │   │   ├── java/
 │   │   │   └── com/
 │   │   │       └── exemple/
 │   │   │           └── pi3sem/
 │   │   │               ├── backend/
 |   |   |               |   ├── Pi3semApplication.java
 │   │   │               │   ├── controller/ 
 │   │   │               │   ├── service/
 │   │   │               │   ├── repository/
 │   │   │               │   └── model/
 |   |   |               |   |
 │   │   │               └── desktop/
 │   │   │                   ├── MainUI.java
 │   │   │                   ├── controller/
 │   │   │                   ├── view/                    
 │   │   │                   └── service/
 │   │   └── resources/
 │   │       ├── application.properties
 │   │       └── fxml/
 |   |
 ├── pom.xml ou build.gradle
```

**MeuProjetoApplication.java** - Classe principal Spring \
**controller/** - Controladora REST \
**service/** - Lógica de negócio \
**repository/** - Acesso ao DB \
**model/** - Entidades JPA \
**MainUI.java** - Inicialização JavaFX \
**view/** - Telas FXML \ 
**service/** - Chamadas HTTP ao backend \
**application.properties** - Configs Spring Boot \
**fxml/** - Telas JavaFX \
***

### Passo a Passo

Pré-requisitos:
- **JDK 21** (precisa do `javac` 21, não só o JRE)
- **MySQL** rodando em `localhost:3306`
- Git

Confira as versões:
```bash
java -version
javac -version
```
Os dois devem mostrar 21. Se o `javac` for 8, instale o JDK 21:
```bash
sudo apt install openjdk-21-jdk
```

#### 1. Clonar o repositório
```bash
git clone https://github.com/Gabriel-Verdin/PI_3SEM.git
cd PI_3SEM
```

#### 2. Criar o banco e o usuário no MySQL
```bash
sudo mysql
```
```sql
CREATE DATABASE IF NOT EXISTS petvax
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER 'petvax'@'localhost' IDENTIFIED BY 'petvax123';
GRANT ALL PRIVILEGES ON petvax.* TO 'petvax'@'localhost';
FLUSH PRIVILEGES;
```
Ajuste `app/src/main/resources/application.properties` com o **mesmo** usuário e senha:
```properties
spring.datasource.username=petvax
spring.datasource.password=petvax123
```
Não commite senha de produção. Se usar `${DB_PASSWORD}`, defina a variável **antes** de subir o servidor:
```bash
export DB_PASSWORD='petvax123'
```

#### 3. Subir o backend (Spring Boot)
O `pom.xml` está em `app/`. Use o Maven Wrapper (não precisa instalar Maven):
```bash
cd app
./mvnw spring-boot:run    # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```
O servidor sobe em **http://localhost:8080**. Deixe esse terminal aberto.

#### 4. Testar a API
No navegador:
```
http://localhost:8080/especies
```
Lista vazia (`[]`) é o esperado se ainda não houver cadastro.

Criar uma espécie (outro terminal):
```bash
curl -X POST http://localhost:8080/especies \
  -H "Content-Type: application/json" \
  -d '{"nome":"Canina"}'
```
Atualize a página; deve aparecer o JSON com `idEspecie` e `nome`.

#### 5. Desktop (JavaFX)
Ainda pendente. Quando existir, será um **segundo processo** (não misturar com o Spring):
```bash
cd app
./mvnw javafx:run
```

