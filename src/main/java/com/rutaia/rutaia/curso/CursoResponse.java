package com.rutaia.rutaia.curso;

public record CursoResponse(Long id, String nombre, String descripcion, String categoria, String nivel, Integer duracionHoras, boolean activo) {

    public static CursoResponse desde(Curso c) {
        return new CursoResponse(c.getId(), c.getNombre(), c.getDescripcion(), c.getCategoria(), c.getNivel(), c.getDuracionHoras(), c.isActivo());
    }
}