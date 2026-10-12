# Documentação do projeto PetVax

Este arquivo explica como o código está organizado hoje, o que cada pasta faz e como devemos continuar o desenvolvimento do sistema.

---

## 1. Visão geral do projeto

O projeto está dividido em duas partes principais:

1. Backend em Spring Boot
   - Responsável por regras de negócio, API REST e acesso ao banco de dados.
   - Está em `app/src/main/java/com/example/pi3sem/backend`.

2. Parte desktop em JavaFX
   - Será a interface visual do sistema para o veterinário.
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

Hoje o código mais importante é o exemplo de espécie:

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

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;
}
```

Isso quer dizer:
- cada objeto `Especie` representa um registro da tabela `especie`
- cada atributo da classe corresponde a uma coluna
- o framework cria a estrutura de dados e salva/consulta isso automaticamente

Exemplo de futuras entidades do PetVax:
- `Tutor`
- `Pet`
- `Vacina`
- `Agendamento`
- `Usuario`
- `HistoricoVacina`

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
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
```

Isso significa:
- no momento, a segurança está relaxada para facilitar o desenvolvimento
- qualquer rota pode entrar sem autenticação
- isso é temporário e depois precisa ser ajustado para login do veterinário

---

### 3.6 `resources`

Pasta:
`app/src/main/resources`

Essa pasta guarda arquivos externos da aplicação, como:
- `application.properties`
- arquivos HTML, templates e FXML
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
- outras configurações do Spring.

---

## 4. Observação importante sobre o banco

Há uma inconsistência no projeto atual:

- a documentação do projeto fala em MySQL;
- o código atual está configurado com PostgreSQL (Neon).

Isso não é um problema para começar, mas é algo que precisamos padronizar.

Recomendação:
- definir uma tecnologia única para o projeto;
- escolher MySQL ou PostgreSQL;
- manter a configuração alinhada com a documentação e com o ambiente do grupo.

Para o projeto acadêmico, o mais comum é usar MySQL, conforme a documentação.

---

## 5. O que significa a parte desktop

Pasta:
`app/src/main/java/com/example/pi3sem/desktop`

A pasta `desktop` representa a parte visual do sistema, que vai usar JavaFX.

Hoje o arquivo está só como base:

```java
public class MainUI {
}
```

Isso significa que a interface ainda não foi montada, mas a estrutura da arquitetura já está pensada:

- `backend` = lógica e API
- `desktop` = interface do usuário

A ideia é que, quando o sistema ficar mais avançado, a tela em JavaFX vai consumir a API do backend.

Exemplo de fluxo da interface:

`Tela JavaFX -> chama backend -> API -> serviço -> banco`

---

## 6. Estrutura de implementação pensada para o futuro

A arquitetura ideal do PetVax é:

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
Banco de Dados
```

Isso deixa o sistema organizado porque:
- interface não lida com banco direto
- regras ficam centralizadas em `service`
- persistência fica isolada em `repository`

---

## 7. Sugestão de próximos passos do projeto

### Fase 1: Base do backend
Objetivo: deixar a API funcionando de verdade para o domínio do PetVax.

Implementar as entidades principais:
- `Usuario`
- `Tutor`
- `Pet`
- `Vacina`
- `Agenda`
- `HistoricoVacina`

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
7. Tela de histórico

Esse fluxo faz sentido porque a parte mais crítica é o cadastro e a gestão dos dados.

---

## 8. Quando dá para começar a tela?

A resposta curta é:

- dá para começar logo, mas com foco em uma estrutura simples;
- o ideal é começar a tela depois da base do backend ter sido validada;
- o primeiro passo para a interface não precisa esperar o sistema completo.

Recomendação prática:

### Melhor ordem:
1. criar o modelo de dados base;
2. montar os endpoints básicos;
3. testar com Postman/Insomnia;
4. depois criar a tela do sistema.

Isso reduz retrabalho, porque a interface depende da API e das regras que estão sendo criadas no backend.

---

## 9. Planejamento sugerido para as próximas implementações

### Sprint 1 - Estrutura base
- finalizar padronização do banco
- garantir que o projeto compile corretamente
- revisar `application.properties`
- padronizar MySQL x PostgreSQL
- manter `SecurityConfig` em desenvolvimento

### Sprint 2 - Módulo de cadastro
- `Tutor`
- `Pet`
- `Vacina`
- `Usuario`

### Sprint 3 - Módulo de agenda e histórico
- `Agenda`
- `HistoricoVacina`
- regras de próxima dose
- consultas por pet

### Sprint 4 - JavaFX / telas
- login
- menu principal
- cadastro de tutor
- cadastro de pet
- cadastro de vacina
- agenda de vacinação

### Sprint 5 - Notificações
- lembretes por WhatsApp
- avisos de vacinação pendente
- relatórios simples

---

## 10. Visão prática para continuar o código

Se quiser seguir o padrão que já começou, a regra é:

- `model`: entidade do banco
- `repository`: acesso ao banco
- `service`: regra do negócio
- `controller`: rota HTTP
- `config`: configurações gerais
- `desktop`: interface do usuário

Esse padrão é o mais comum em Java + Spring Boot e ajuda muito a manter o projeto organizado.

---

## 11. Conclusão

O projeto está começando a ganhar a estrutura correta de uma aplicação Java com Spring Boot. O exemplo de `Especie` já mostra o padrão que deve ser repetido para o resto do sistema.

A estrutura atual já está no caminho certo porque:
- separa responsabilidades;
- organiza as regras em camadas;
- deixa os futuros cadastros e telas mais fáceis de implementar;
- facilita manutenção e evolução do sistema.

O próximo grande passo é evoluir do exemplo de uma entidade básica para o domínio real do PetVax: tutor, pet, vacina, agenda e histórico de vacinação.

Quando isso estiver consolidado, aí sim a tela desktop deve entrar com força, já com a base pronta e com o backend funcionando corretamente.

---

## 12. Ponto de partida recomendado

Se o grupo quiser continuar de forma organizada, o melhor caminho é:

1. criar a entidade `Tutor`
2. criar `Pet`
3. criar `Vacina`
4. criar `Agendamento`
5. criar `Usuario`
6. depois montar as telas iniciais

Esse é o caminho mais lógico para evoluir o projeto sem misturar regra de negócio com interface.
