package com.neyser.appcompras.repository;

import com.neyser.appcompras.model.ListaCompra;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListaCompraRepository extends JpaRepository<ListaCompra, Long> {

    @Override
    @EntityGraph(attributePaths = {"estado", "items", "items.producto", "items.estado"})
    List<ListaCompra> findAll();

    @Override
    @EntityGraph(attributePaths = {"estado", "items", "items.producto", "items.estado"})
    Optional<ListaCompra> findById(Long id);

    default List<ListaCompra> findAllOrderByIdDesc() {
        return findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
}

