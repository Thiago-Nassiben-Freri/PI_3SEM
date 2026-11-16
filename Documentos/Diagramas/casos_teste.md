# Casos de teste — PetVax

Os requisitos de cada caso estão ligados na `Matriz_Rastreabilidade_PetVax.md`. Execução manual pelo desktop, com um veterinário logado.

## 1. Caso de uso rastreado: notificação de vacinação (RF01)

### CT01 — Gerar lembrete de próxima dose

| Campo | Descrição |
|---|---|
| **Requisito** | RF01 |
| **Pré-condição** | Animal com aplicação de vacina cuja próxima dose vence nos próximos 30 dias; sem agendamento em aberto para esse animal e vacina |
| **Passos** | Notificações → Gerar lembretes |
| **Resultado esperado** | Mensagem "N lembrete(s) novo(s) gerado(s)."; lembrete com motivo "Próxima dose de vacina" e status Pendente na lista |

### CT02 — Gerar lembrete de dose atrasada

| Campo | Descrição |
|---|---|
| **Requisito** | RF01 |
| **Pré-condição** | Animal com próxima dose já vencida |
| **Passos** | Notificações → Gerar lembretes |
| **Resultado esperado** | Lembrete com motivo "Vacina atrasada" e status Pendente |

### CT03 — Gerar lembrete de agendamento

| Campo | Descrição |
|---|---|
| **Requisito** | RF01 |
| **Pré-condição** | Agendamento Agendado ou Confirmado para os próximos 2 dias |
| **Passos** | Notificações → Gerar lembretes |
| **Resultado esperado** | Lembrete com motivo "Lembrete de agendamento" com a data e o horário do agendamento |

### CT04 — Não repetir lembretes

| Campo | Descrição |
|---|---|
| **Requisito** | RF01 |
| **Pré-condição** | CT01 a CT03 executados |
| **Passos** | Clicar de novo em Gerar lembretes |
| **Resultado esperado** | Mensagem "0 lembrete(s) novo(s) gerado(s)."; a lista não ganha itens repetidos |

### CT05 — Não gerar lembrete de dose com agendamento em aberto

| Campo | Descrição |
|---|---|
| **Requisito** | RF01 |
| **Pré-condição** | Dose próxima (como no CT01) e agendamento em aberto, no futuro, para o mesmo animal e vacina |
| **Passos** | Notificações → Gerar lembretes |
| **Resultado esperado** | Não é criado lembrete "Próxima dose de vacina" para essa dose |

### CT06 — Lembrete ligado ao veterinário responsável

| Campo | Descrição |
|---|---|
| **Requisito** | RF01 |
| **Pré-condição** | CT01 executado |
| **Passos** | Consultar o lembrete pela API (`GET /notificacoes/{id}`) |
| **Resultado esperado** | O campo `veterinario` é o veterinário que registrou a aplicação (ou do agendamento, no CT03) |

### CT07 — Enviar o lembrete ao veterinário pelo WhatsApp

| Campo | Descrição |
|---|---|
| **Requisito** | RF01 |
| **Pré-condição** | Lembrete Pendente; veterinário com telefone cadastrado |
| **Passos** | Selecionar o lembrete → enviar pelo WhatsApp |
| **Resultado esperado** | Mensagem entregue no WhatsApp do veterinário; status Enviada |
| **Observação** | Envio ao veterinário pelo WhatsApp não implementado nesta entrega |

## 2. Demais requisitos

### CT08 — Entrar com login ou e-mail

| Campo | Descrição |
|---|---|
| **Requisito** | RF02 |
| **Pré-condição** | Veterinário ativo cadastrado |
| **Passos** | Informar o e-mail (ou o login) e a senha → Entrar |
| **Resultado esperado** | Abre a tela Início com o nome do veterinário no menu |

### CT09 — Recusar login inválido

| Campo | Descrição |
|---|---|
| **Requisito** | RF02 |
| **Pré-condição** | — |
| **Passos** | Informar senha errada → Entrar |
| **Resultado esperado** | Mensagem "Login ou senha inválidos"; continua na tela de login |

### CT10 — Agendar vacinação

| Campo | Descrição |
|---|---|
| **Requisito** | RF03 |
| **Pré-condição** | Animal e vacina indicada para a espécie dele cadastrados |
| **Passos** | Agenda → Novo agendamento → escolher animal, vacina, veterinário, data futura e horário → Salvar |
| **Resultado esperado** | Só aparecem vacinas da espécie do animal; agendamento listado no dia escolhido com status Agendado |

### CT11 — Recusar agendamento no passado

| Campo | Descrição |
|---|---|
| **Requisito** | RF03 |
| **Pré-condição** | — |
| **Passos** | Novo agendamento com data e hora já passadas → Salvar |
| **Resultado esperado** | Mensagem "A data e hora do agendamento não podem estar no passado"; nada é salvo |

### CT12 — Cadastrar vacina

| Campo | Descrição |
|---|---|
| **Requisito** | RF04 |
| **Pré-condição** | — |
| **Passos** | Vacinas → Catálogo de vacinas → Novo → nome, tipo, espécie, dosagem e periodicidade → Salvar |
| **Resultado esperado** | Vacina aparece no catálogo, ligada à espécie escolhida. Repetir com o mesmo nome, tipo e dosagem mostra "Já existe uma vacina cadastrada com este nome, tipo e dosagem" |

### CT13 — Registrar aplicação de vacina

| Campo | Descrição |
|---|---|
| **Requisito** | RF04 |
| **Pré-condição** | Animal e vacina da espécie cadastrados |
| **Passos** | Vacinas → Registrar aplicação → animal, vacina, veterinário, data de hoje, dose, lote e validade → Salvar |
| **Resultado esperado** | Aplicação registrada; a próxima dose (data + periodicidade) aparece em Vacinas e nos detalhes do animal |

### CT14 — Consultar o prontuário do animal

| Campo | Descrição |
|---|---|
| **Requisito** | RF05 |
| **Pré-condição** | Animal com aplicação registrada |
| **Passos** | Animais → selecionar o animal → Detalhes |
| **Resultado esperado** | Janela com o histórico de vacinas e as próximas doses |

### CT15 — Recusar tutor com CPF repetido

| Campo | Descrição |
|---|---|
| **Requisito** | RF06 |
| **Pré-condição** | Tutor já cadastrado com o CPF usado |
| **Passos** | Tutores → Novo → mesmo CPF → Salvar |
| **Resultado esperado** | Mensagem "Já existe um tutor cadastrado com este CPF"; nada é salvo |

### CT16 — Cadastrar animal ligado ao tutor

| Campo | Descrição |
|---|---|
| **Requisito** | RF06 |
| **Pré-condição** | Tutor cadastrado |
| **Passos** | Animais → Novo → nome, tutor, espécie, raça, sexo, data de nascimento e peso → Salvar |
| **Resultado esperado** | Animal aparece na lista com o tutor e a espécie corretos |
