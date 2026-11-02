# Configuração da WhatsApp Cloud API (envio automático de notificações)

O PetVax envia as notificações de vacina pelo WhatsApp (RF01) de dois jeitos, escolhidos pela variável `WHATSAPP_MODO`:

| Modo | Como funciona | Configuração |
|---|---|---|
| `link` (padrão) | A API gera um link `wa.me` com o telefone do tutor e a mensagem; o desktop abre o WhatsApp Web e o veterinário clica em enviar | Nenhuma |
| `api` | A API envia a mensagem sozinha pela **WhatsApp Cloud API** da Meta | As variáveis deste documento |

No modo `api` usamos o **número de teste gratuito** da Meta (RES03: só ferramentas gratuitas). Ele só envia para **até 5 números cadastrados** no painel do app, o que basta para desenvolvimento e para a apresentação. Em produção, para tutores reais, a Meta cobra por mensagem.

## 1. Criar o app na Meta

1. Acesse [developers.facebook.com](https://developers.facebook.com) e entre com uma conta do Facebook.
2. **Meus apps → Criar app**, escolha o caso de uso **WhatsApp** (ou tipo **Empresa**) e conclua. Se pedir um portfólio empresarial, crie um (é gratuito).
3. No app, abra **WhatsApp → Configuração da API** (API Setup).

## 2. Anotar o token e o ID do número

Na tela **Configuração da API**:

- **Número de origem (From):** já vem um número de teste criado pela Meta. Copie o **ID do número de telefone** (Phone number ID), não o número em si.
- **Token de acesso temporário:** clique em gerar e copie. **Ele expira em 24 horas.** Para a apresentação, gere um token permanente: **Configurações do negócio → Usuários do sistema → Adicionar** (perfil administrador) → **Gerar token**, escolhendo o app e a permissão `whatsapp_business_messaging`.

## 3. Cadastrar os números de destino (até 5)

Ainda na **Configuração da API**, em **Para (To) → Gerenciar lista de números**, adicione o celular de cada integrante e confirme com o código que chega pelo WhatsApp.

No PetVax, cadastre esses mesmos números como telefone dos tutores de teste (com DDD, ex.: `11988887777`; o sistema acrescenta o `55`).

> Celulares do Brasil: se a API responder erro `131030` (número fora da lista permitida), o número pode estar registrado no WhatsApp **sem o 9** extra. Compare com o número exibido na lista da Meta e cadastre o telefone do tutor no mesmo formato.

## 4. Criar o modelo de mensagem (template)

Mensagens iniciadas pela clínica precisam de um **modelo aprovado pela Meta**. Em **WhatsApp → Gerenciar modelos → Criar modelo**:

- **Categoria:** Utilidade (Utility)
- **Nome:** `lembrete_petvax`
- **Idioma:** Português (BR)
- **Corpo:**
  ```
  Olá! Aqui é a clínica Vet Home. {{1}} Qualquer dúvida, é só responder esta mensagem.
  ```
- **Exemplo da variável {{1}}:** `A próxima dose da vacina V10 da Luna está prevista para 10/11/2026.`

A variável `{{1}}` recebe o texto da notificação gerado pelo sistema (ou editado na tela Notificações). Aguarde o status **Aprovado** (costuma levar de minutos a algumas horas).

> Sem modelo (`WHATSAPP_TEMPLATE` vazio), a API envia texto livre, que a Meta **só entrega se o destinatário tiver mandado uma mensagem para o número de teste nas últimas 24 horas**. Serve para um teste rápido: mande "oi" do seu celular para o número de teste e depois envie pelo PetVax.

## 5. Configurar a API do PetVax

Defina as variáveis de ambiente onde a API roda (no Render: **serviço → Environment**):

| Variável | Valor |
|---|---|
| `WHATSAPP_MODO` | `api` |
| `WHATSAPP_TOKEN` | token do passo 2 |
| `WHATSAPP_PHONE_NUMBER_ID` | ID do número do passo 2 |
| `WHATSAPP_TEMPLATE` | `lembrete_petvax` |

Depois de salvar, o Render reinicia a API. Para voltar ao envio por link, troque `WHATSAPP_MODO` para `link` (ou apague a variável).

**Nunca coloque o token no código nem no `application.properties`**: ele dá acesso à conta do WhatsApp e o repositório é público.

## 6. Testar

1. No desktop, cadastre um tutor com um dos números do passo 3 e gere uma notificação para ele (tela Notificações → **Gerar lembretes**).
2. Selecione a notificação e clique em **Enviar pelo WhatsApp**.
3. A tela mostra "Mensagem enviada pelo WhatsApp." e a mensagem chega no celular, enviada pelo número de teste.

Se der erro, a mensagem da Meta aparece em vermelho na tela e a notificação fica com status **Falha**.

## Como está no código

- `backend/service/notificacao/WhatsAppCloudApiSender.java`: monta o JSON e chama `POST https://graph.facebook.com/{versão}/{phone-number-id}/messages` com o token no cabeçalho `Authorization: Bearer`.
- `backend/service/notificacao/WhatsAppLinkSender.java`: modo `link` (padrão).
- As duas implementam `NotificacaoSender`; a configuração `petvax.whatsapp.modo` decide qual fica ativa.