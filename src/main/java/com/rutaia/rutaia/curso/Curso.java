package com.rutaia.rutaia.curso;

import jakarta.persistence.*;

@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false, length = 80)
    private String categoria;

    @Column(nullable = false, length = 20)
    private String nivel;

    @Column(name = "duracion_horas", nullable = false)
    private Integer duracionHoras;

    @Column(nullable = false)
    private boolean activo = true;

    protected Curso() {
    }

    public Curso(String nombre, String descripcion, String categoria, String nivel, Integer duracionHoras) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.nivel = nivel;
        this.duracionHoras = duracionHoras;
    }

    public void actualizar(String nombre, String descripcion, String categoria, String nivel, Integer duracionHoras) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.nivel = nivel;
        this.duracionHoras = duracionHoras;
    }

    public void desactivar() {
        this.activo = false;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNivel() {
        return nivel;
    }

    public Integer getDuracionHoras() {
        return duracionHoras;
    }

    public boolean isActivo() {
        return activo;
    }
}