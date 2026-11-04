package com.rutaia.rutaia.calificacion;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CalificacionRequest(
        @NotNull(message = "La puntuacion es obligatoria")
        @Min(value = 1, message = "La puntuacion debe estar entre 1 y 5")
        @Max(value = 5, message = "La puntuacion debe estar entre 1 y 5") Integer puntuacion,
        String comentario) {
}