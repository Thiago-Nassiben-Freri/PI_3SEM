# Matriz de rastreabilidade — PetVax
 
A matriz liga cada requisito funcional aos casos de uso, diagramas, telas, rotas da API, regras e casos de teste do sistema. Os casos de teste estão descritos em `Casos_de_Teste_PetVax.md`.
 
| RF | Caso de uso | Diagramas | Tela | Rotas da API | Regras / RNF | Casos de teste |
|---|---|---|---|---|---|---|
| **RF01** Notificação via WhatsApp | Gerar lembretes; Consultar notificações; Enviar notificação pelo WhatsApp (não implementado) | Atividades: Notificação de vacinação; Sequência: Agendamento de vacina e programação de lembrete | Notificações | `POST /notificacoes/gerar-pendentes`, `GET /notificacoes`, `POST /notificacoes/{id}/enviar` | RN03, RNF01, RES01 | CT01 a CT07 |
| **RF02** Cadastro de usuários | Autenticar; Gerenciar usuários | — | Login | `POST /auth/login`, `POST /veterinarios`, `PATCH /usuarios/{id}/ativo` | RN01, RNF02 | CT08, CT09 |
| **RF03** Agenda e horários | Gerenciar agenda | Atividades: Gerenciar agenda; Sequência: Agendamento de vacina e programação de lembrete | Agenda (janela Novo agendamento) | `POST /agendamentos`, `PATCH /agendamentos/{id}/status`, `GET /especies/{id}/vacinas` | RNF04 | CT10, CT11 |
| **RF04** Cadastro de vacinas | Cadastrar vacina; Registrar aplicação de vacina | — | Vacinas (janelas Catálogo de vacinas e Registrar aplicação) | `POST /vacinas`, `POST /vacinas/{id}/especies/{especieId}`, `POST /aplicacoes` | RN02 | CT12, CT13 |
| **RF05** Prontuário do animal | Consultar prontuário do animal | — | Animais (janela Detalhes do animal) | `GET /pets/{id}/prontuario` | — | CT14 |
| **RF06** Cadastro de tutores | Cadastrar tutor; Cadastrar animal | Atividades: Cadastro de tutor; Sequência: Cadastro de tutor | Tutores, Animais | `POST /tutores`, `POST /tutores/{id}/telefones`, `POST /pets` | — | CT15, CT16 |
| **RF07** Notificações alternativas | — | — | — | — | — | Não implementado |
| **RF08** Suporte multiplataforma | — | — | — | — | — | Não implementado |
 
