package com.rutaia.rutaia.calificacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    boolean existsByRecomendacionId(Long recomendacionId);

    @Query("select avg(c.puntuacion) from Calificacion c")
    Double promedioPuntuacion();
}