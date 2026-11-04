package com.rutaia.rutaia.openrouter;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenRouterCliente {

    private final RestClient cliente;
    private final String modeloEmbedding;
    private final String modeloChat;

    public OpenRouterCliente(
            @Value("${OPENROUTER_API_KEY:}") String apiKey,
            @Value("${openrouter.modelo-embedding:openai/text-embedding-3-small}") String modeloEmbedding,
            @Value("${openrouter.modelo-chat:openai/gpt-4o-mini}") String modeloChat) {
        this.cliente = RestClient.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        this.modeloEmbedding = modeloEmbedding;
        this.modeloChat = modeloChat;
    }

    @SuppressWarnings("unchecked")
    public List<Double> embedding(String texto) {
        Map<String, Object> respuesta = cliente.post()
                .uri("/embeddings")
                .body(Map.of("model", modeloEmbedding, "input", texto))
                .retrieve()
                .body(Map.class);
        List<Map<String, Object>> datos = (List<Map<String, Object>>) respuesta.get("data");
        return (List<Double>) datos.get(0).get("embedding");
    }

    @SuppressWarnings("unchecked")
    public String chat(String instruccion, String pregunta) {
        Map<String, Object> respuesta = cliente.post()
                .uri("/chat/completions")
                .body(Map.of(
                        "model", modeloChat,
                        "messages", List.of(
                                Map.of("role", "system", "content", instruccion),
                                Map.of("role", "user", "content", pregunta))))
                .retrieve()
                .body(Map.class);
        List<Map<String, Object>> opciones = (List<Map<String, Object>>) respuesta.get("choices");
        Map<String, Object> mensaje = (Map<String, Object>) opciones.get(0).get("message");
        return (String) mensaje.get("content");
    }
}