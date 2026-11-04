package com.rutaia.rutaia.estadisticas;

public record EstadisticasResponse(
        long totalConsultas,
        long respondidas,
        long sinResultados,
        double promedioCalificacion,
        Long cursoMasRecomendado) {
}