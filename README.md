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

#### 1. Clonar o repositório
```
git clone https://github.com/Gabriel-Verdin/PI_3SEM.git
cd pi-3sem
```

#### 2. Verificar se o Maven está presente
Se o projeto tem os arquivos mvnw e mvnw.cmd, você pode usar o wrapper sem precisar instalar Maven globalmente:
```
./mvnw clean install   # Linux/Mac
mvnw.cmd clean install # Windows
```
Se não tiver wrapper, basta usar o Maven instalado:
```
mvn clean install
```

#### 3. Rodar o Backend (Spring Boot)
```
mvn spring-boot: run
```
ou
```
java -jar target/pi3sem-0.0.1-SNAPSHOT.jar
```

#### 4. Rodar o desktop (JavaFX) // (Pendente)
Entre na pasta desktop/ e rode a classe Main.java pela sua IDE ou com:
```
mvn javafx:run
```
(se você configurar o plugin JavaFX no pom.xml).

#### 5. Localhost
http://localhost:8080/hello
