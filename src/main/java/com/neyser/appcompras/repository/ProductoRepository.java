package com.neyser.appcompras.repository;

import com.neyser.appcompras.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
