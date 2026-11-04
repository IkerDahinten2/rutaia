package com.rutaia.rutaia.estudiante;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudiante")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 160)
    private String correo;

    @Column(nullable = false, length = 20)
    private String nivel;

    @Column(name = "area_interes", length = 120)
    private String areaInteres;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    protected Estudiante() {
    }

    public Estudiante(String nombre, String correo, String nivel, String areaInteres) {
        this.nombre = nombre;
        this.correo = correo;
        this.nivel = nivel;
        this.areaInteres = areaInteres;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNivel() {
        return nivel;
    }

    public String getAreaInteres() {
        return areaInteres;
    }
}