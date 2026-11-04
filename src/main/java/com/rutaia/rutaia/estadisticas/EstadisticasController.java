package com.rutaia.rutaia.estadisticas;

import com.rutaia.rutaia.calificacion.CalificacionRepository;
import com.rutaia.rutaia.consulta.ConsultaRepository;
import com.rutaia.rutaia.consulta.Fuente;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasController {

    private final ConsultaRepository consultas;
    private final CalificacionRepository calificaciones;
    private final FuenteRepository fuentes;

    public EstadisticasController(ConsultaRepository consultas, CalificacionRepository calificaciones,
                                  FuenteRepository fuentes) {
        this.consultas = consultas;
        this.calificaciones = calificaciones;
        this.fuentes = fuentes;
    }

    @GetMapping
    public EstadisticasResponse estadisticas() {
        Long cursoTop = fuentes.findAll().stream()
                .collect(Collectors.groupingBy(Fuente::getCursoId, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        Double promedio = calificaciones.promedioPuntuacion();
        return new EstadisticasResponse(
                consultas.count(),
                consultas.countByEstado("Respondida"),
                consultas.countByEstado("Sin resultados"),
                promedio == null ? 0.0 : promedio,
                cursoTop);
    }
}