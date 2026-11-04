package com.rutaia.rutaia.estudiante;

import com.rutaia.rutaia.comun.ConflictoException;
import com.rutaia.rutaia.comun.RecursoNoEncontradoException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService {

    private final EstudianteRepository repositorio;

    public EstudianteService(EstudianteRepository repositorio) {
        this.repositorio = repositorio;
    }

    public EstudianteResponse crear(EstudianteRequest solicitud) {
        if (repositorio.existsByCorreo(solicitud.correo())) {
            throw new ConflictoException("Ya existe un estudiante con ese correo");
        }
        Estudiante estudiante = new Estudiante(solicitud.nombre(), solicitud.correo(), solicitud.nivel(), solicitud.areaInteres());
        return EstudianteResponse.desde(repositorio.save(estudiante));
    }

    public List<EstudianteResponse> listar() {
        return repositorio.findAll().stream().map(EstudianteResponse::desde).toList();
    }

    public EstudianteResponse obtener(Long id) {
        return repositorio.findById(id)
                .map(EstudianteResponse::desde)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado"));
    }
}