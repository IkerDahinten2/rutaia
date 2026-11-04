package com.rutaia.rutaia.consulta;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByEstudianteIdOrderByCreadoEnDesc(Long estudianteId);

    long countByEstado(String estado);
}