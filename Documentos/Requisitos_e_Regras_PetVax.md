# Especificação de Requisitos e Regras do Projeto PetVax

Este documento reúne a especificação consolidada de **Requisitos Funcionais (RF)**, **Requisitos Não-Funcionais (RNF)**, **Regras de Negócio (RN)** e **Restrições** do sistema **PetVax**, desenvolvida com base na documentação do projeto e a partir da matriz de priorização **MoSCoW**.

---

## 1. Requisitos Funcionais (RF)

*Funcionalidades e ações operacionais que o sistema deve executar, priorizadas conforme o MoSCoW.*

| ID | Descrição | Prioridade (MoSCoW) | Situação na entrega |
|---|---|---|---|
| **RF01** | **Notificação via WhatsApp:** O sistema deve enviar notificações sobre o período e prazos de vacinação diretamente via WhatsApp. | *Must Have* | Parcial: o sistema gera os lembretes e o veterinário os acompanha na tela Notificações; o envio pelo WhatsApp não foi implementado nesta entrega. |
| **RF02** | **Cadastro de Usuários do Software:** O sistema deve permitir o cadastro de usuários operacionais (médicos veterinários) e do administrador do sistema para autenticação e uso do software. | *Must Have* | Implementado pela API (o administrador cadastra os veterinários); não há tela de cadastro no desktop. |
| **RF03** | **Sistema de Agenda e Horários:** O sistema deve disponibilizar um módulo de agendamento de atendimentos presenciais, com definição de datas e horários de vacinação. | *Must Have* | Implementado (tela Agenda). |
| **RF04** | **Cadastro de Vacinas:** O sistema deve permitir o cadastramento do catálogo de vacinas disponíveis, incluindo tipo, dosagem e periodicidade. | *Must Have* | Implementado (catálogo de vacinas). |
| **RF05** | **Prontuário Médico do Animal:** O sistema deve registrar e manter o histórico do prontuário médico dos Pets. | *Should Have* | Implementado como consulta: histórico de vacinas e próximas doses na janela Detalhes do animal. |
| **RF06** | **Cadastro de Tutores:** O sistema deve permitir o cadastro dos tutores dos animais domésticos e seu vínculo aos respectivos Pets. | *Should Have* | Implementado (telas Tutores e Animais). |
| **RF07** | **Notificações Alternativas:** O sistema poderá disponibilizar envio de lembretes e avisos via e-mail (correio eletrônico) e/ou SMS. | *Could Have* | Não implementado. |
| **RF08** | **Suporte Multiplataforma:** O sistema poderá evoluir para interfaces móveis (aplicativo celular), versão Web ou arquiteturas SaaS/PaaS. | *Could Have* | Não implementado. |

---

## 2. Requisitos Não-Funcionais (RNF)

*Atributos de qualidade, restrições tecnológicas e aspectos operacionais do sistema.*

| ID | Descrição | Categorização |
|---|---|---|
| **RNF01** | **Conectividade com a Internet:** O sistema deve requerer e manter conexão ativa com a internet, pois a API e o banco de dados estão hospedados na nuvem, e para viabilizar as comunicações externas e o disparo de notificações (ex: integração com WhatsApp). | *Must Have / Infraestrutura* |
| **RNF02** | **Stack Tecnológica:** A aplicação desktop deve ser desenvolvida em **Java** com **JavaFX** para a interface gráfica e **Spring Boot** para a estrutura de backend/API REST. | *Arquitetura* |
| **RNF03** | **Persistência em Banco de Dados:** O sistema deve utilizar o SGBD **PostgreSQL** para o armazenamento e gerenciamento relacional dos dados cadastrais e históricos. | *Armazenamento* |
| **RNF04** | **Usabilidade da Interface:** A interface gráfica (desenvolvida via FXML) deve ser intuitiva e otimizada para o fluxo de atendimento clínico presencial do veterinário: cada tela principal é aberta com 1 clique no menu lateral e as mensagens de erro aparecem em português na própria janela. | *Usabilidade* |

---

## 3. Regras de Negócio (RN)

*Políticas e direcionamentos estratégicos que definem o comportamento e os limites do software.*

* **RN01 - Exclusividade de Acesso Interno (*Must Have*):** O acesso ao software é estritamente restrito aos usuários cadastrados da empresa (médicos veterinários). Atores externos, tais como tutores ou fornecedores, **não** possuem perfil nem acesso direto ao sistema.
* **RN02 - Ausência de Controle de Estoque (*Won't Have*):** O sistema **não** realizará gestão, baixa automática ou controle de estoque físico das vacinas.
* **RN03 - Canal Principal de Notificação (*Must Have*):** O meio primário padronizado para o envio de avisos e lembretes de vacinação aos veterinários é o **WhatsApp**.
* **RN04 - Escopo Operacional Focado:** O software destina-se exclusivamente ao acompanhamento imunológico e médico presencial do animal, estando fora do escopo funcionalidades financeiras, vendas, banho e tosa, ou hospedagem.

---

## 4. Restrições do Projeto (RES)

*Condições impositivas e limitações de contorno para a execução do projeto.*

* **RES01 - Dependência de Acesso à Internet:** A execução completa dos serviços de notificação exige conectividade constante com a internet, impedindo o funcionamento offline do sistema, pois a API e o banco de dados estão hospedados na nuvem.
* **RES02 - Modelo Inicial Desktop:** A entrega do sistema deve focar na aplicação desktop em Java/JavaFX, postergando soluções mobile ou web para fases futuras do projeto.
* **RES03 - Limitação Financeira e Tecnologias Gratuitas:** O desenvolvimento do software deve utilizar exclusivamente ferramentas, bibliotecas e frameworks de licença gratuita e/ou open-source (Java, Spring Boot, PostgreSQL, Maven).
* **RES04 - Prazo Acadêmico:** O escopo do software deve ser concluído e entregue dentro das datas do cronograma acadêmico do 3º semestre do curso de ADS (FATEC).
