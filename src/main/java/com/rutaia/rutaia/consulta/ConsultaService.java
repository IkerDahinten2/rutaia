package com.rutaia.rutaia.consulta;

import com.rutaia.rutaia.comun.RecursoNoEncontradoException;
import com.rutaia.rutaia.curso.Curso;
import com.rutaia.rutaia.curso.CursoRepository;
import com.rutaia.rutaia.estudiante.Estudiante;
import com.rutaia.rutaia.estudiante.EstudianteRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class ConsultaService {

    private final ConsultaRepository consultas;
    private final EstudianteRepository estudiantes;
    private final CursoRepository cursos;
    private final N8nCliente n8n;

    public ConsultaService(ConsultaRepository consultas, EstudianteRepository estudiantes,
                           CursoRepository cursos, N8nCliente n8n) {
        this.consultas = consultas;
        this.estudiantes = estudiantes;
        this.cursos = cursos;
        this.n8n = n8n;
    }

    public ConsultaResponse crear(ConsultaRequest solicitud) {
        Estudiante estudiante = estudiantes.findById(solicitud.estudianteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado"));
        Consulta consulta = consultas.save(new Consulta(estudiante.getId(), solicitud.pregunta()));
        try {
            Map<String, Object> respuesta = n8n.consultar(consulta.getId(), solicitud.pregunta(),
                    estudiante.getNivel(), estudiante.getAreaInteres());
            return ConsultaResponse.desde(procesar(consulta, respuesta));
        } catch (RestClientException e) {
            consulta.setEstado("Error");
            return ConsultaResponse.desde(consultas.save(consulta));
        }
    }

    public ConsultaResponse obtener(Long id) {
        return ConsultaResponse.desde(buscar(id));
    }

    public List<ConsultaResponse> historial(Long estudianteId) {
        if (!estudiantes.existsById(estudianteId)) {
            throw new RecursoNoEncontradoException("Estudiante no encontrado");
        }
        return consultas.findByEstudianteIdOrderByCreadoEnDesc(estudianteId).stream()
                .map(ConsultaResponse::desde)
                .toList();
    }

    private Consulta buscar(Long id) {
        return consultas.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Consulta no encontrada"));
    }

    @SuppressWarnings("unchecked")
    private Consulta procesar(Consulta consulta, Map<String, Object> respuesta) {
        String estado = String.valueOf(respuesta.getOrDefault("estado", "Error"));
        if (!"Respondida".equals(estado)) {
            consulta.setEstado("Sin resultados".equals(estado) ? "Sin resultados" : "Error");
            return consultas.save(consulta);
        }
        Recomendacion recomendacion = new Recomendacion(consulta, String.valueOf(respuesta.get("respuesta")));
        List<Map<String, Object>> fuentes = (List<Map<String, Object>>) respuesta.getOrDefault("fuentes", List.of());
        for (Map<String, Object> f : fuentes) {
            Long cursoId = ((Number) f.get("cursoId")).longValue();
            BigDecimal similitud = new BigDecimal(String.valueOf(f.get("similitud"))).setScale(5, RoundingMode.HALF_UP);
            cursos.findById(cursoId)
                    .filter(Curso::isActivo)
                    .ifPresent(c -> recomendacion.agregarFuente(new Fuente(recomendacion, cursoId, similitud)));
        }
        if (recomendacion.getFuentes().isEmpty()) {
            consulta.setEstado("Sin resultados");
            return consultas.save(consulta);
        }
        consulta.setRecomendacion(recomendacion);
        consulta.setEstado("Respondida");
        return consultas.save(consulta);
    }
}