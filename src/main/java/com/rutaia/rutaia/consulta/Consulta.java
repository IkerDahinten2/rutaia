package com.rutaia.rutaia.consulta;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @Column(nullable = false)
    private String pregunta;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
    private Recomendacion recomendacion;

    protected Consulta() {
    }

    public Consulta(Long estudianteId, String pregunta) {
        this.estudianteId = estudianteId;
        this.pregunta = pregunta;
        this.estado = "Pendiente";
    }

    public Long getId() {
        return id;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public Recomendacion getRecomendacion() {
        return recomendacion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setRecomendacion(Recomendacion recomendacion) {
        this.recomendacion = recomendacion;
    }
}