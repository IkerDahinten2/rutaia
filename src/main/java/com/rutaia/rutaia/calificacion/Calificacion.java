package com.rutaia.rutaia.calificacion;

import com.rutaia.rutaia.consulta.Recomendacion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "calificacion")
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "recomendacion_id", nullable = false, unique = true)
    private Recomendacion recomendacion;

    @Column(nullable = false)
    private Integer puntuacion;

    private String comentario;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    protected Calificacion() {
    }

    public Calificacion(Recomendacion recomendacion, Integer puntuacion, String comentario) {
        this.recomendacion = recomendacion;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }

    public Long getId() {
        return id;
    }

    public Recomendacion getRecomendacion() {
        return recomendacion;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public String getComentario() {
        return comentario;
    }
}