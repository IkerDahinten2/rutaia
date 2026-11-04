package com.rutaia.rutaia.consulta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConsultaRequest(
        @NotNull(message = "El estudiante es obligatorio") Long estudianteId,
        @NotBlank(message = "La pregunta es obligatoria") String pregunta) {
}