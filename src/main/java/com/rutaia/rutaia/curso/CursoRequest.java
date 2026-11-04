package com.rutaia.rutaia.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CursoRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "La descripcion es obligatoria") String descripcion,
        @NotBlank(message = "La categoria es obligatoria") String categoria,
        @NotBlank(message = "El nivel es obligatorio")
        @Pattern(regexp = "B\u00e1sico|Intermedio|Avanzado", message = "El nivel no es valido") String nivel,
        @NotNull(message = "La duracion es obligatoria")
        @Positive(message = "La duracion debe ser mayor a cero") Integer duracionHoras) {
}