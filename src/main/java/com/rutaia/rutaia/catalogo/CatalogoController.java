package com.rutaia.rutaia.catalogo;

import com.rutaia.rutaia.curso.CursoRepository;
import com.rutaia.rutaia.curso.CursoResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final CursoRepository repositorio;

    public CatalogoController(CursoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public List<CursoResponse> catalogo(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String nivel) {
        return repositorio.findByActivoTrue().stream()
                .filter(c -> categoria == null || c.getCategoria().equalsIgnoreCase(categoria))
                .filter(c -> nivel == null || c.getNivel().equalsIgnoreCase(nivel))
                .map(CursoResponse::desde)
                .toList();
    }
}