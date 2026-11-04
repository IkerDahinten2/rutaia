package com.rutaia.rutaia.curso;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService servicio;

    public CursoController(CursoService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoResponse crear(@Valid @RequestBody CursoRequest solicitud) {
        return servicio.crear(solicitud);
    }

    @GetMapping
    public List<CursoResponse> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public CursoResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PutMapping("/{id}")
    public CursoResponse actualizar(@PathVariable Long id, @Valid @RequestBody CursoRequest solicitud) {
        return servicio.actualizar(id, solicitud);
    }

    @PatchMapping("/{id}/desactivar")
    public CursoResponse desactivar(@PathVariable Long id) {
        return servicio.desactivar(id);
    }
}