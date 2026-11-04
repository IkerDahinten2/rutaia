package com.rutaia.rutaia.curso;

import com.rutaia.rutaia.comun.RecursoNoEncontradoException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

    private final CursoRepository repositorio;

    public CursoService(CursoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public CursoResponse crear(CursoRequest s) {
        Curso curso = new Curso(s.nombre(), s.descripcion(), s.categoria(), s.nivel(), s.duracionHoras());
        return CursoResponse.desde(repositorio.save(curso));
    }

    public List<CursoResponse> listar() {
        return repositorio.findAll().stream().map(CursoResponse::desde).toList();
    }

    public CursoResponse obtener(Long id) {
        return CursoResponse.desde(buscar(id));
    }

    public CursoResponse actualizar(Long id, CursoRequest s) {
        Curso curso = buscar(id);
        curso.actualizar(s.nombre(), s.descripcion(), s.categoria(), s.nivel(), s.duracionHoras());
        return CursoResponse.desde(repositorio.save(curso));
    }

    public CursoResponse desactivar(Long id) {
        Curso curso = buscar(id);
        curso.desactivar();
        return CursoResponse.desde(repositorio.save(curso));
    }

    private Curso buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado"));
    }
}