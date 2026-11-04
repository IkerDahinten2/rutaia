package com.rutaia.rutaia.estudiante;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    boolean existsByCorreo(String correo);
}