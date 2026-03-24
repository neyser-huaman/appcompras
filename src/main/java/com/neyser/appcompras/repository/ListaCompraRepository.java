package com.neyser.appcompras.repository;

import com.neyser.appcompras.model.ListaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ListaCompraRepository extends JpaRepository<ListaCompra, Long> {

    @Query("""
           select distinct l
           from ListaCompra l
           left join fetch l.estado
           left join fetch l.items i
           left join fetch i.producto
           left join fetch i.estado
           where l.id = :id
           """)
    Optional<ListaCompra> findDetalleById(@Param("id") Long id);
}