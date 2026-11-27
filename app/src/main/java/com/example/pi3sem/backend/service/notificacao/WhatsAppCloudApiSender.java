package com.example.pi3sem.backend.service.notificacao;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.model.CanalNotificacao;
import com.example.pi3sem.backend.model.Notificacao;
import com.example.pi3sem.backend.model.Telefone;
import com.example.pi3sem.backend.repository.TelefoneRepository;

/**
 * Envio automático pela WhatsApp Cloud API (Meta). Fica ativo com petvax.whatsapp.modo=api;
 * no modo padrão (link) quem envia é o WhatsAppLinkSender.
 *
 * Com o número de teste gratuito da Meta, a API só entrega para até 5 números cadastrados no
 * painel do app. Mensagens iniciadas pela clínica precisam de um modelo (template) aprovado pela
 * Meta, com uma variável {{1}} que recebe o texto da notificação. Sem template configurado, envia
 * texto livre, que só é entregue se o tutor mandou mensagem para o número nas últimas 24 horas.
 */
@Component
@ConditionalOnProperty(name = "petvax.whatsapp.modo", havingValue = "api")
public class WhatsAppCloudApiSender implements NotificacaoSender {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final TelefoneRepository telefoneRepository;
    private final RestClient restClient = RestClient.create("https://graph.facebook.com");

    private final String token;
    private final String phoneNumberId;
    private final String template;
    private final String templateAgendamento;
    private final String idioma;
    private final String versao;

    public WhatsAppCloudApiSender(TelefoneRepository telefoneRepository,
            @Value("${petvax.whatsapp.token:}") String token,
            @Value("${petvax.whatsapp.phone-number-id:}") String phoneNumberId,
            @Value("${petvax.whatsapp.template:}") String template,
            @Value("${petvax.whatsapp.template-agendamento:}") String templateAgendamento,
            @Value("${petvax.whatsapp.template-idioma:pt_BR}") String idioma,
            @Value("${petvax.whatsapp.api-versao:v23.0}") String versao) {
        this.telefoneRepository = telefoneRepository;
        this.token = token;
        this.phoneNumberId = phoneNumberId;
        this.template = template;
        this.templateAgendamento = templateAgendamento;
        this.idioma = idioma;
        this.versao = versao;
    }

    @Override
    public CanalNotificacao getCanal() {
        return CanalNotificacao.WHATSAPP;
    }

    @Override
    public String enviar(Notificacao notificacao) {
        if (token.isBlank() || phoneNumberId.isBlank()) {
            throw new IllegalArgumentException(
                    "WhatsApp API não configurada: defina WHATSAPP_TOKEN e WHATSAPP_PHONE_NUMBER_ID");
        }
        List<Telefone> telefones = telefoneRepository.findByTutor_IdTutor(notificacao.getTutor().getIdTutor());
        if (telefones.isEmpty()) {
            throw new IllegalArgumentException("O tutor não possui telefone cadastrado para envio por WhatsApp");
        }
        String numero = formatarNumero(telefones.get(0).getNumero());
        // A API não aceita quebra de linha dentro da variável do modelo
        String mensagem = notificacao.getMensagem().replaceAll("\\s+", " ").trim();

        Agendamento agendamento = notificacao.getAgendamento();
        Map<String, Object> corpo;
        if (agendamento != null && !templateAgendamento.isBlank()) {
            // Modelo de agendamento: {{1}} animal, {{2}} dia, {{3}} horário
            corpo = mensagemDeModelo(numero, templateAgendamento, List.of(
                    agendamento.getPet().getNome(),
                    agendamento.getDataHora().format(DATA),
                    agendamento.getDataHora().format(HORA)));
        } else if (agendamento != null) {
            // Sem modelo aprovado: texto livre no mesmo formato (só chega se o tutor falou com o número nas últimas 24h)
            corpo = mensagemDeTexto(numero, "Olá, Dr(a). Vinicius! Vacinação do(a) " + agendamento.getPet().getNome()
                    + " agendada para " + agendamento.getDataHora().format(DATA)
                    + " e horário " + agendamento.getDataHora().format(HORA) + ".");
        } else if (!template.isBlank()) {
            corpo = mensagemDeModelo(numero, template, List.of(mensagem));
        } else {
            corpo = mensagemDeTexto(numero, mensagem);
        }

        try {
            restClient.post()
                    .uri("/{versao}/{id}/messages", versao, phoneNumberId)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpo)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException ex) {
            // O NotificacaoService marca a notificação como FALHA e devolve esta mensagem ao desktop
            throw new RuntimeException("WhatsApp respondeu " + ex.getStatusCode().value()
                    + ": " + ex.getResponseBodyAsString());
        }
        // Envio automático: não há link para o desktop abrir
        return null;
    }

    // Mensagem com modelo aprovado: {"type":"template","template":{"name":...,"components":[...]}}
    private Map<String, Object> mensagemDeModelo(String numero, String nomeModelo, List<String> valores) {
        List<Map<String, Object>> parametros = valores.stream()
                .map(v -> Map.<String, Object>of("type", "text", "text", v))
                .toList();
        Map<String, Object> componente = Map.of("type", "body", "parameters", parametros);
        Map<String, Object> modelo = Map.of(
                "name", nomeModelo,
                "language", Map.of("code", idioma),
                "components", List.of(componente));
        return Map.of("messaging_product", "whatsapp", "to", numero, "type", "template", "template", modelo);
    }

    // Texto livre: {"type":"text","text":{"body":...}}
    private Map<String, Object> mensagemDeTexto(String numero, String mensagem) {
        return Map.of("messaging_product", "whatsapp", "to", numero, "type", "text",
                "text", Map.of("body", mensagem));
    }

    // Mantém só os dígitos e adiciona o código do Brasil (55) quando o número vier só com DDD
    private String formatarNumero(String numero) {
        String digitos = numero.replaceAll("\\D", "");
        if (digitos.length() == 10 || digitos.length() == 11) {
            return "55" + digitos;
        }
        return digitos;
    }
}