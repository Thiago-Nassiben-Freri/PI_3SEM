# PetCare (Nome provisório)
Projeto Integrador (PI) para o 3º semestre de ADS da FATEC

### Qual problema o sistema resolve?

O sistema tem como objetivo ajudar na prevenção e nos cuidados com a saúde de
animais domésticos. Atualmente, muitas clínicas veterinárias e ONGs utilizam métodos
pouco eficientes para registrar informações dos animais, como vacinas, doenças e
histórico de saúde. O sistema busca facilitar o armazenamento e o acesso a essas
informações, tornando o acompanhamento da saúde dos animais mais organizado e
prático.
***

### Para quem é o sistema? (público-alvo)
Clínicas veterinárias, ONGs de resgate de animais de rua e instituições de caridade
voltadas ao cuidado de animais domésticos.
***

### Descrição geral do sistema
O sistema armazenará o histórico de saúde dos animais domésticos, incluindo a carteira
de vacinação, doenças, tratamentos e consultas. Assim, será mais fácil acompanhar a
saúde de cada animal e manter suas informações organizadas e atualizadas.
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
