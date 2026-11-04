package com.rutaia.rutaia.consulta;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recomendacion")
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "consulta_id", nullable = false, unique = true)
    private Consulta consulta;

    @Column(nullable = false)
    private String respuesta;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    @OneToMany(mappedBy = "recomendacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fuente> fuentes = new ArrayList<>();

    protected Recomendacion() {
    }

    public Recomendacion(Consulta consulta, String respuesta) {
        this.consulta = consulta;
        this.respuesta = respuesta;
    }

    public void agregarFuente(Fuente fuente) {
        fuentes.add(fuente);
    }

    public Long getId() {
        return id;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public List<Fuente> getFuentes() {
        return fuentes;
    }
}