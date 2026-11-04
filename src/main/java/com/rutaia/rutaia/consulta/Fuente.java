package com.rutaia.rutaia.consulta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "fuente")
public class Fuente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recomendacion_id", nullable = false)
    private Recomendacion recomendacion;

    @Column(name = "curso_id", nullable = false)
    private Long cursoId;

    @Column(nullable = false, precision = 6, scale = 5)
    private BigDecimal similitud;

    protected Fuente() {
    }

    public Fuente(Recomendacion recomendacion, Long cursoId, BigDecimal similitud) {
        this.recomendacion = recomendacion;
        this.cursoId = cursoId;
        this.similitud = similitud;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public BigDecimal getSimilitud() {
        return similitud;
    }
}