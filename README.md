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
meu-projeto/
 ├── src/
 │   ├── main/
 │   │   ├── java/
 │   │   │   └── com/
 │   │   │       └── exemplo/
 │   │   │           └── app/
 │   │   │               ├── MeuProjetoApplication.java 
 │   │   │               ├── backend/
 |   |   |               |   |
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