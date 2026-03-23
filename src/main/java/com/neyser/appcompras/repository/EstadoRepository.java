package com.neyser.appcompras.repository;

import com.neyser.appcompras.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    List<Estado> findByActivoTrue();

    Optional<Estado> findByNombre(String nombre);
}