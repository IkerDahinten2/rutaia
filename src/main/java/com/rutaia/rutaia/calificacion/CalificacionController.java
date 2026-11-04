package com.rutaia.rutaia.calificacion;

import com.rutaia.rutaia.comun.ConflictoException;
import com.rutaia.rutaia.comun.RecursoNoEncontradoException;
import com.rutaia.rutaia.consulta.Recomendacion;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalificacionController {

    private final RecomendacionRepository recomendaciones;
    private final CalificacionRepository calificaciones;

    public CalificacionController(RecomendacionRepository recomendaciones, CalificacionRepository calificaciones) {
        this.recomendaciones = recomendaciones;
        this.calificaciones = calificaciones;
    }

    @PostMapping("/api/recomendaciones/{id}/calificacion")
    @ResponseStatus(HttpStatus.CREATED)
    public CalificacionResponse calificar(@PathVariable Long id, @Valid @RequestBody CalificacionRequest solicitud) {
        Recomendacion recomendacion = recomendaciones.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Recomendacion no encontrada"));
        if (calificaciones.existsByRecomendacionId(id)) {
            throw new ConflictoException("La recomendacion ya tiene calificacion");
        }
        Calificacion calificacion = calificaciones.save(
                new Calificacion(recomendacion, solicitud.puntuacion(), solicitud.comentario()));
        return CalificacionResponse.desde(calificacion);
    }
}