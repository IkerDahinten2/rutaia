package com.rutaia.rutaia.consulta;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class N8nCliente {

    private final RestClient cliente;
    private final String rutaWebhook;

    public N8nCliente(
            @Value("${n8n.url:http://localhost:5678}") String url,
            @Value("${n8n.ruta-rag:/webhook/consulta-rag}") String rutaWebhook) {
        this.cliente = RestClient.builder().baseUrl(url).build();
        this.rutaWebhook = rutaWebhook;
    }

    public Map<String, Object> consultar(Long consultaId, String pregunta, String nivel, String areaInteres) {
        return cliente.post()
                .uri(rutaWebhook)
                .body(Map.of(
                        "consultaId", consultaId,
                        "pregunta", pregunta,
                        "nivel", nivel,
                        "areaInteres", areaInteres == null ? "" : areaInteres))
                .retrieve()
                .body(Map.class);
    }
}