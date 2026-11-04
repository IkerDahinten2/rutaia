package com.rutaia.rutaia.calificacion;

public record CalificacionResponse(Long id, Long recomendacionId, Integer puntuacion, String comentario) {

    public static CalificacionResponse desde(Calificacion c) {
        return new CalificacionResponse(c.getId(), c.getRecomendacion().getId(), c.getPuntuacion(), c.getComentario());
    }
}