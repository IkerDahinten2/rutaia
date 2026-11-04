package com.rutaia.rutaia.estudiante;

public record EstudianteResponse(Long id, String nombre, String correo, String nivel, String areaInteres) {

    public static EstudianteResponse desde(Estudiante e) {
        return new EstudianteResponse(e.getId(), e.getNombre(), e.getCorreo(), e.getNivel(), e.getAreaInteres());
    }
}