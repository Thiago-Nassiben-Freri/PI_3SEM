# Documentação do projeto PetVax

Este arquivo explica como o código está organizado hoje, o que cada pasta faz e como devemos continuar o desenvolvimento do sistema.

---

## 1. Visão geral do projeto

O projeto está dividido em duas partes principais:

1. Backend em Spring Boot
   - Responsável por regras de negócio, API REST e acesso ao banco de dados.
   - Está em `app/src/main/java/com/example/pi3sem/backend`.

2. Parte desktop em JavaFX
   - É a interface visual do sistema para o veterinário.
   - Consome a API do backend, publicada em `https://petvax.onrender.com`.
   - Está em `app/src/main/java/com/example/pi3sem/desktop`.

O projeto tem como objetivo dar suporte ao atendimento veterinário, com foco em:
- cadastro de tutores;
- cadastro de pets;
- cadastro de vacinas;
- agendamento de vacinas;
- histórico de vacinação;
- notificações e lembretes.

---

## 2. Como o código atual funciona

### 2.1 Classe principal da aplicação
Arquivo:
`app/src/main/java/com/example/pi3sem/backend/Pi3semApplication.java`

Ela é a classe inicial do Spring Boot. Quando a aplicação sobe, ela executa este código:

```java
@SpringBootApplication
public class Pi3semApplication {
    public static void main(String[] args) {
        SpringApplication.run(Pi3semApplication.class, args);
    }
}
```

Ela serve para iniciar o backend do sistema.

---

### 2.2 Fluxo de uma requisição

Todos os recursos seguem o mesmo fluxo. Exemplo com espécie:

`EspecieController -> EspecieService -> EspecieRepository -> Banco`

O fluxo é este:

1. O cliente acessa uma rota da API (por exemplo `/especies`)
2. O `Controller` recebe a requisição
3. O `Controller` chama o `Service`
4. O `Service` faz a regra de negócio
5. O `Service` chama o `Repository`
6. O `Repository` acessa a tabela no banco com JPA

Exemplo prático:
- `GET /especies` -> lista todas as espécies
- `POST /especies` -> salva uma espécie nova

---

## 3. Explicação das pastas principais

### 3.1 `controller`

Pasta:
`app/src/main/java/com/example/pi3sem/backend/controller`

A pasta `controller` fica responsável por receber requisições HTTP.

Nela ficam as classes que expõem endpoints da API. Exemplo:

Arquivo `EspecieController.java`

```java
@RestController
@RequestMapping("/especies")
public class EspecieController {

    private final EspecieService service;

    @GetMapping
    public List<Especie> listar() {
        return service.listar();
    }

    @PostMapping
    public Especie salvar(@RequestBody Especie especie) {
        return service.salvar(especie);
    }
}
```

Em outras palavras:
- `Controller` não decide regra de negócio
- ele apenas recebe a rota e delega a execução

Exemplo de responsabilidade:
- validar se a URL foi chamada corretamente
- receber JSON do cliente
- chamar o service
- devolver resposta

---

### 3.2 `service`

Pasta:
`app/src/main/java/com/example/pi3sem/backend/service`

A pasta `service` fica com a lógica de negócio.

Ela é o “coração” da regra do sistema. É onde você coloca validações, regras e organização da operação.

Arquivo `EspecieService.java`

```java
@Service
public class EspecieService {

    public Especie salvar(Especie especie) {
        if (especie.getNome() == null || especie.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da espécie é obrigatório");
        }
        return repository.save(especie);
    }
}
```

Aqui está a regra:
- nome não pode ser vazio
- se alguma regra falhar, a aplicação lança exceção
- se tudo estiver correto, salva no banco

Isso é importante porque o `service` centraliza a regra do sistema e mantém o `controller` mais limpo.

---

### 3.3 `model`

Pasta:
`app/src/main/java/com/example/pi3sem/backend/model`

A pasta `model` representa as entidades do banco.

Ela mapeia classes Java para tabelas do banco em JPA.

Arquivo `Especie.java`

```java
@Entity
@Table(name = "especie")
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEspecie;

    @Column(name = "nome", nullable = false, length = 30)
    private String nome;
}
```

Isso quer dizer:
- cada objeto `Especie` representa um registro da tabela `especie`
- cada atributo da classe corresponde a uma coluna
- o framework cria a estrutura de dados e salva/consulta isso automaticamente

Entidades do PetVax:
- `Usuario` (dados de acesso), `Veterinario` e `Admin`
- `Tutor` e `Telefone`
- `Especie` e `Raca`
- `Pet`
- `Vacina` e `VacinaEspecie` (espécies em que a vacina pode ser aplicada)
- `Agendamento`
- `AplicacaoVacina` (histórico de vacinação)
- `Notificacao`

---

### 3.4 `repository`

Pasta:
`app/src/main/java/com/example/pi3sem/backend/repository`

A `repository` é responsável pelo acesso ao banco de dados.

Exemplo:

```java
public interface EspecieRepository extends JpaRepository<Especie, Integer> {
}
```

Essa interface já oferece métodos prontos como:
- `findAll()`
- `save()`
- `findById()`
- `delete()`

Ou seja, ela abstrai o acesso ao banco e evita escrever SQL manualmente no código principal.

---

### 3.5 `config`

Pasta:
`app/src/main/java/com/example/pi3sem/backend/config`

A pasta `config` guarda classes de configuração da aplicação.

Arquivo `SecurityConfig.java`:

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
    .requestMatchers("/admins/**").hasRole("ADMIN")
    .requestMatchers("/usuarios/**").hasRole("ADMIN")
    .requestMatchers(HttpMethod.POST, "/veterinarios").hasRole("ADMIN")
    .anyRequest().authenticated());
```

Isso significa:
- só a rota de login é liberada; todas as outras exigem login e senha (HTTP Basic)
- as senhas são gravadas criptografadas (BCrypt)
- cadastrar veterinários e ativar/desativar usuários é restrito ao perfil de administrador
- o login aceita o login **ou** o e-mail do usuário; usuário inativo não entra

---

### 3.6 Outras pastas do backend

- `dto`: objetos de resposta montados pela API (resumo da tela Início, próximas doses, prontuário, relatório, envio de notificação, usuário logado)
- `exception`: `GlobalExceptionHandler`, que devolve os erros em JSON com mensagem em português
- `security`: busca do usuário no login (`PetvaxUserDetailsService`) e criação do primeiro administrador (`AdminInicial`)
- `service/notificacao`: geração e envio das notificações (seção 7)

---

### 3.7 `resources`

Pasta:
`app/src/main/resources`

Essa pasta guarda arquivos externos da aplicação, como:
- `application.properties`
- arquivos FXML (telas do desktop)
- configurações do banco e do servidor

Arquivo relevante:
`application.properties`

```properties
spring.datasource.url=jdbc:postgresql://...
spring.datasource.username=...
spring.datasource.password=${DB_PASSWORD}
server.port=${PORT:8080}
```

Esse arquivo define:
- qual banco será usado;
- qual usuário acessa o banco;
- qual porta do servidor;
- a geração automática de lembretes e o modo de envio pelo WhatsApp (`petvax.notificacoes.*` e `petvax.whatsapp.*`);
- outras configurações do Spring.

---

## 4. Observação importante sobre o banco

O projeto usa **PostgreSQL**, hospedado no Neon, conforme o RNF03. O rascunho inicial de arquitetura citava MySQL, mas a tecnologia foi padronizada em PostgreSQL na documentação e no código.

As tabelas são criadas e atualizadas pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`). A senha do banco fica na variável de ambiente `DB_PASSWORD`, nunca no repositório.

---

## 5. O que significa a parte desktop

Pasta:
`app/src/main/java/com/example/pi3sem/desktop`

A pasta `desktop` representa a parte visual do sistema, feita em JavaFX com telas FXML.

- `MainUI.java`: abre a tela de login
- `controller/`: um controller para cada tela ou janela
- `model/`: objetos recebidos da API
- `service/ApiService.java`: faz as chamadas HTTP à API e guarda o usuário logado

A estrutura da arquitetura é:

- `backend` = lógica e API
- `desktop` = interface do usuário

A tela em JavaFX consome a API do backend.

Exemplo de fluxo da interface:

`Tela JavaFX -> chama backend -> API -> serviço -> banco`

Telas implementadas (menu lateral) e suas janelas:

| Tela | Janelas (pop-ups) |
|---|---|
| Login | — |
| Início | — |
| Tutores | Cadastro de tutor |
| Animais | Cadastro de animal, Detalhes do animal (histórico e próximas doses) |
| Vacinas | Registrar aplicação, Catálogo de vacinas, Cadastro de vacina |
| Agenda | Novo agendamento |
| Notificações | — |

Para abrir o desktop: `cd app` e `mvnw javafx:run`.

---

## 6. Estrutura de implementação

A arquitetura do PetVax é:

```text
desktop (JavaFX)
      |
      v
API REST (Spring Boot)
      |
      v
Service
      |
      v
Repository
      |
      v
Banco de Dados (PostgreSQL - Neon)
```

A API roda no Render (Docker) e o desktop roda no computador de cada veterinário.

Isso deixa o sistema organizado porque:
- interface não lida com banco direto
- regras ficam centralizadas em `service`
- persistência fica isolada em `repository`

---

## 7. Notificações

**Destinatário:** o médico veterinário (RF01 e RN03). O tutor não recebe mensagens do sistema; o envio ao tutor fica como sugestão de evolução.

Como funciona:
1. Na tela Notificações, o veterinário clica em **Gerar lembretes** (`POST /notificacoes/gerar-pendentes`).
2. A API cria lembretes para:
   - doses que vencem nos próximos 30 dias;
   - doses já vencidas;
   - agendamentos em aberto nos próximos 2 dias.
3. Não cria lembrete repetido, nem lembrete de dose se já houver agendamento em aberto para o mesmo animal e vacina.
4. Cada lembrete fica ligado ao veterinário responsável pela aplicação ou pelo agendamento, e aparece na lista da tela Notificações.

A geração também pode rodar sozinha uma vez por dia (`NotificacaoAgendador`), ligando `petvax.notificacoes.geracao-automatica=true`.

**Pendente:** o envio do lembrete ao veterinário pelo WhatsApp. A estrutura de envio já existe (`NotificacaoSender`, `WhatsAppLinkSender` e `WhatsAppCloudApiSender`), mas hoje usa o telefone do tutor, como protótipo. Para concluir, basta usar o telefone do veterinário (`notificacao.getVeterinario().getTelefone()`) e ajustar o texto das mensagens em `NotificacaoService`. Configuração da Meta em `Documentos/Configuracao_WhatsApp_API.md`.

---

## 8. Fases do desenvolvimento (executadas)

### Fase 1: Base do backend
Objetivo: deixar a API funcionando de verdade para o domínio do PetVax.

Implementar as entidades principais:
- `Usuario`
- `Tutor`
- `Pet`
- `Vacina`
- `Agendamento`
- `AplicacaoVacina`

Para cada uma, criar:
- `Model`
- `Repository`
- `Service`
- `Controller`

---

### Fase 2: CRUD básico
Implementar operações CRUD:
- cadastrar
- listar
- buscar por id
- atualizar
- excluir

Exemplo de endpoints:
- `POST /usuarios`
- `GET /pets`
- `POST /vacinas`
- `GET /agendamentos`

---

### Fase 3: regras de negócio do sistema
Definir regras importantes como:
- um pet deve estar ligado a um tutor
- uma vacina deve ter periodicidade
- não pode agendar vacinação em data inválida
- não pode repetir cadastro de usuário com o mesmo e-mail
- não pode cadastrar vacina sem nome ou periodicidade

Essas regras ficam no `service`.

---

### Fase 4: começar a parte visual
Quando o backend estiver estável, dá para começar a interface desktop.

É recomendado começar pela sequência:

1. Tela de login
2. Tela inicial / dashboard
3. Tela de cadastro de tutor
4. Tela de cadastro de pet
5. Tela de cadastro de vacina
6. Tela de agendamento
7. Tela de histórico (feita como janela Detalhes do animal)
8. Tela de notificações

Esse fluxo faz sentido porque a parte mais crítica é o cadastro e a gestão dos dados.

---

## 9. Planejamento das implementações

Situação na entrega final:  feito · ⏳ pendente

### Sprint 1 - Estrutura base
- finalizar padronização do banco 
- garantir que o projeto compile corretamente 
- revisar `application.properties` 
- padronizar o banco em PostgreSQL 
- configurar o `SecurityConfig` com login do veterinário 

### Sprint 2 - Módulo de cadastro
- `Tutor` 
- `Pet` 
- `Vacina` 
- `Usuario`  (pela API, feito pelo administrador)

### Sprint 3 - Módulo de agenda e histórico
- `Agendamento` 
- `AplicacaoVacina` 
- regras de próxima dose 
- consultas por pet 

### Sprint 4 - JavaFX / telas
- login 
- menu principal 
- cadastro de tutor 
- cadastro de pet 
- cadastro de vacina 
- agenda de vacinação 
- notificações 

### Sprint 5 - Notificações
- lembretes para o veterinário  (tela Notificações)
- envio dos lembretes ao veterinário por WhatsApp 
- avisos de vacinação pendente 
- relatórios simples  (rota `GET /relatorios/vacinacoes` na API)

---

## 10. Visão prática para continuar o código

Se quiser seguir o padrão que já começou, a regra é:

- `model`: entidade do banco
- `repository`: acesso ao banco
- `service`: regra do negócio
- `controller`: rota HTTP
- `config`: configurações gerais
- `dto`, `exception`, `security`: respostas, erros e login
- `desktop`: interface do usuário

Esse padrão é o mais comum em Java + Spring Boot e ajuda muito a manter o projeto organizado.

---

## 11. Conclusão

O projeto tem a estrutura de uma aplicação Java com Spring Boot. O padrão do exemplo de `Especie` foi repetido para o resto do sistema.

A estrutura atual é adequada porque:
- separa responsabilidades;
- organiza as regras em camadas;
- deixa os futuros cadastros e telas mais fáceis de implementar;
- facilita manutenção e evolução do sistema.

O domínio do PetVax (tutor, pet, vacina, agenda e histórico de vacinação) e as telas do desktop estão implementados. Fica pendente o envio das notificações ao veterinário pelo WhatsApp (seção 7).

---

## 12. Pendências conhecidas

| Item | Situação |
|---|---|
| Envio das notificações ao veterinário pelo WhatsApp (RF01) | Pendente (seção 7) |
| Data da aplicação da vacina | `AplicacaoVacinaService.validar` exige data **no futuro**, o que impede registrar uma dose aplicada hoje ou antes (inclusive pela Agenda). O correto é recusar datas no futuro |
| Conflito de horário na agenda | A API não recusa dois agendamentos do mesmo veterinário no mesmo horário |
| Seletor de veterinário | Mostra "CRMV 12345" em vez do nome do veterinário |
| Cadastro de usuários no desktop | Não existe tela; é feito pela API, pelo administrador |
