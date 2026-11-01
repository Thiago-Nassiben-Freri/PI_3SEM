package com.example.pi3sem.desktop.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;

import com.example.pi3sem.desktop.model.UsuarioLogado;

import tools.jackson.databind.ObjectMapper;

// Faz as chamadas HTTP para a API do PetVax.
// Todas as telas usam esta classe. Exemplo de uso numa tela:
//   String json = ApiService.get("/tutores");
//   Tutor[] tutores = ApiService.getMapper().readValue(json, Tutor[].class);
public class ApiService {

    private static final String URL_API = "https://petvax.onrender.com";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    // Guardados depois do login, para usar nas próximas requisições
    private static UsuarioLogado usuarioLogado;
    private static String autorizacao;

    public static UsuarioLogado login(String email, String senha) throws Exception {
        String json = mapper.writeValueAsString(Map.of("login", email, "senha", senha));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_API + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new Exception("Não foi possível conectar ao servidor.");
        }

        if (response.statusCode() == 401) {
            throw new Exception("E-mail ou senha inválidos.");
        }
        if (response.statusCode() != 200) {
            throw new Exception("Erro no servidor. Tente novamente.");
        }

        usuarioLogado = mapper.readValue(response.body(), UsuarioLogado.class);

        // A API usa HTTP Basic: as próximas requisições mandam login e senha neste
        // cabeçalho
        String credenciais = usuarioLogado.getLogin() + ":" + senha;
        autorizacao = "Basic " + Base64.getEncoder().encodeToString(credenciais.getBytes());

        return usuarioLogado;
    }

    // ----- Métodos usados pelas telas (precisam de login feito antes) -----

    // Retorna o JSON da resposta. Ex.: ApiService.get("/pets")
    public static String get(String caminho) throws Exception {
        return enviar("GET", caminho, null);
    }

    // Envia o objeto como JSON. Ex.: ApiService.post("/tutores", tutor)
    public static String post(String caminho, Object corpo) throws Exception {
        return enviar("POST", caminho, corpo);
    }

    // Ex.: ApiService.put("/tutores/5", tutor)
    public static String put(String caminho, Object corpo) throws Exception {
        return enviar("PUT", caminho, corpo);
    }

    // Altera só parte do registro. Ex.: ApiService.patch("/agendamentos/5/status",
    // agendamento)
    public static String patch(String caminho, Object corpo) throws Exception {
        return enviar("PATCH", caminho, corpo);
    }

    // Ex.: ApiService.delete("/tutores/5")
    public static void delete(String caminho) throws Exception {
        enviar("DELETE", caminho, null);
    }

    // Para converter o JSON em objetos: getMapper().readValue(json, Tutor[].class)
    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static UsuarioLogado getUsuarioLogado() {
        return usuarioLogado;
    }

    private static String enviar(String metodo, String caminho, Object corpo) throws Exception {
        if (autorizacao == null) {
            throw new Exception("Faça o login antes de usar o sistema.");
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(URL_API + caminho))
                .header("Authorization", autorizacao)
                .header("Content-Type", "application/json");

        if (corpo == null) {
            builder.method(metodo, HttpRequest.BodyPublishers.noBody());
        } else {
            builder.method(metodo, HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(corpo)));
        }

        HttpResponse<String> response;
        try {
            response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new Exception("Não foi possível conectar ao servidor.");
        }

        if (response.statusCode() >= 400) {
            throw new Exception(mensagemDeErro(response));
        }
        return response.body();
    }

    // A API devolve os erros assim: {"status": 400, "mensagem": "Nome é
    // obrigatório"}
    private static String mensagemDeErro(HttpResponse<String> response) {
        try {
            Map<?, ?> erro = mapper.readValue(response.body(), Map.class);
            if (erro.get("mensagem") != null) {
                return erro.get("mensagem").toString();
            }
        } catch (Exception e) {
            // a resposta não era JSON; usa a mensagem genérica abaixo
        }
        return "Erro no servidor (código " + response.statusCode() + ").";
    }
}