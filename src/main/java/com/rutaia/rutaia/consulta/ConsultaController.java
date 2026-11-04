package com.rutaia.rutaia.consulta;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsultaController {

    private final ConsultaService servicio;

    public ConsultaController(ConsultaService servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/api/consultas")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaResponse crear(@Valid @RequestBody ConsultaRequest solicitud) {
        return servicio.crear(solicitud);
    }

    @GetMapping("/api/consultas/{id}")
    public ConsultaResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @GetMapping("/api/estudiantes/{id}/historial")
    public List<ConsultaResponse> historial(@PathVariable Long id) {
        return servicio.historial(id);
    }
}