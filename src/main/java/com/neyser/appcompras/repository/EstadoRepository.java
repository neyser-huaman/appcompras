package com.neyser.appcompras.repository;

import com.neyser.appcompras.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    // Buscar estado por nombre ignorando mayúsculas/minúsculas
    Optional<Estado> findByNombreIgnoreCase(String nombre);
}