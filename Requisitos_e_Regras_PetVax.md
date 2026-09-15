# Especificação de Requisitos e Regras do Projeto PetVax

Este documento reúne a especificação consolidada de **Requisitos Funcionais (RF)**, **Requisitos Não-Funcionais (RNF)**, **Regras de Negócio (RN)** e **Restrições** do sistema **PetVax**, desenvolvida com base na documentação do projeto e a partir da matriz de priorização **MoSCoW**.

---

## 1. Requisitos Funcionais (RF)

*Funcionalidades e ações operacionais que o sistema deve executar, priorizadas conforme o MoSCoW.*

| ID | Descrição | Prioridade (MoSCoW) |
|---|---|---|
| **RF01** | **Notificação via WhatsApp:** O sistema deve enviar notificações sobre o período e prazos de vacinação diretamente via WhatsApp. | *Must Have* |
| **RF02** | **Cadastro de Usuários do Software:** O sistema deve permitir o cadastro de usuários operacionais (médicos veterinários) para autenticação e uso do software. | *Must Have* |
| **RF03** | **Cadastro de Administrador do Sistema:** O sistema deve permitir o cadastro e gerenciamento de administradores para controle de acessos e configurações. | *Must Have* |
| **RF04** | **Sistema de Agenda e Horários:** O sistema deve disponibilizar um módulo de agendamento de atendimentos presenciais, com definição de datas e horários de vacinação. | *Must Have* |
| **RF05** | **Cadastro de Vacinas:** O sistema deve permitir o cadastramento do catálogo de vacinas disponíveis, incluindo tipo, dosagem e periodicidade. | *Must Have* |
| **RF06** | **Prontuário Médico do Animal:** O sistema deve registrar e manter o histórico do prontuário médico dos Pets. | *Should Have* |
| **RF07** | **Cadastro de Tutores:** O sistema deve permitir o cadastro dos tutores dos animais domésticos e seu vínculo aos respectivos Pets. | *Should Have* |
| **RF08** | **Notificações Alternativas:** O sistema poderá disponibilizar envio de lembretes e avisos via e-mail (correio eletrônico) e/ou SMS. | *Could Have* |
| **RF09** | **Suporte Multiplataforma:** O sistema poderá evoluir para interfaces móveis (aplicativo celular), versão Web ou arquiteturas SaaS/PaaS. | *Could Have* |

---

## 2. Requisitos Não-Funcionais (RNF)

*Atributos de qualidade, restrições tecnológicas e aspectos operacionais do sistema.*

| ID | Descrição | Categorização |
|---|---|---|
| **RNF01** | **Conectividade com a Internet:** O sistema deve requerer e manter conexão ativa com a internet para viabilizar as comunicações externas e o disparo de notificações (ex: integração com WhatsApp). | *Must Have / Infraestrutura* |
| **RNF02** | **Stack Tecnológica:** A aplicação desktop deve ser desenvolvida em **Java** com **JavaFX** para a interface gráfica e **Spring Boot** para a estrutura de backend/API REST. | *Arquitetura* |
| **RNF03** | **Persistência em Banco de Dados:** O sistema deve utilizar o SGBD **MySQL** para o armazenamento e gerenciamento relacional dos dados cadastrais e históricos. | *Armazenamento* |
| **RNF04** | **Usabilidade da Interface:** A interface gráfica (desenvolvida via FXML) deve ser intuitiva e otimizada para o fluxo de atendimento clínico presencial do veterinário. | *Usabilidade* |

---

## 3. Regras de Negócio (RN)

*Políticas e direcionamentos estratégicos que definem o comportamento e os limites do software.*

* **RN01 - Exclusividade de Acesso Interno (*Won't Have / Must Have*):** O acesso ao software é estritamente restrito aos usuários cadastrados da empresa (médicos veterinários e administradores). Atores externos, tais como tutores ou fornecedores, **não** possuem perfil nem acesso direto ao sistema.
* **RN02 - Ausência de Controle de Estoque (*Won't Have*):** O sistema **não** realizará gestão, baixa automática ou controle de estoque físico das vacinas.
* **RN03 - Canal Principal de Notificação (*Must Have*):** O meio primário padronizado para o envio de avisos e lembretes de vacinação aos veterinários é o **WhatsApp**.
* **RN04 - Escopo Operacional Focado:** O software destina-se exclusivamente ao acompanhamento imunológico e médico presencial do animal, estando fora do escopo funcionalidades financeiras, vendas, banho e tosa, ou hospedagem.

---

## 4. Restrições do Projeto (RES)

*Condições impositivas e limitações de contorno para a execução do projeto.*

* **RES01 - Dependência de Acesso à Internet:** A execução completa dos serviços de notificação exige conectividade constante com a internet, impedindo o funcionamento 100% offline da integração via WhatsApp.
* **RES02 - Modelo Inicial Desktop:** A entrega do sistema deve focar na aplicação desktop em Java/JavaFX, postergando soluções mobile ou web para fases futuras do projeto.
* **RES03 - Limitação Financeira e Tecnologias Gratuitas:** O desenvolvimento do software deve utilizar exclusivamente ferramentas, bibliotecas e frameworks de licença gratuita e/ou open-source (Java, Spring Boot, MySQL, Maven).
* **RES04 - Prazo Acadêmico:** O escopo do software deve ser concluído e entregue dentro das datas do cronograma acadêmico do 3º semestre do curso de ADS (FATEC).
