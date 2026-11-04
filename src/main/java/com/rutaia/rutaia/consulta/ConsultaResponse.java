package com.rutaia.rutaia.consulta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ConsultaResponse(
        Long id,
        Long estudianteId,
        String pregunta,
        String estado,
        LocalDateTime creadoEn,
        String respuesta,
        List<FuenteResponse> fuentes) {

    public record FuenteResponse(Long cursoId, BigDecimal similitud) {
    }

    public static ConsultaResponse desde(Consulta c) {
        Recomendacion r = c.getRecomendacion();
        String respuesta = r == null ? null : r.getRespuesta();
        List<FuenteResponse> fuentes = r == null ? List.of() : r.getFuentes().stream()
                .map(f -> new FuenteResponse(f.getCursoId(), f.getSimilitud()))
                .toList();
        return new ConsultaResponse(c.getId(), c.getEstudianteId(), c.getPregunta(), c.getEstado(), c.getCreadoEn(), respuesta, fuentes);
    }
}