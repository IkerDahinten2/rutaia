package com.rutaia.rutaia.estudiante;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EstudianteRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no es valido") String correo,
        @NotBlank(message = "El nivel es obligatorio")
        @Pattern(regexp = "Principiante|Intermedio|Avanzado", message = "El nivel no es valido") String nivel,
        String areaInteres) {
}