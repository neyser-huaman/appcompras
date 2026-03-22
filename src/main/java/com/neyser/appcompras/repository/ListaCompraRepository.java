package com.neyser.appcompras.repository;

import com.neyser.appcompras.model.ListaCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaCompraRepository extends JpaRepository<ListaCompra, Long> {
}