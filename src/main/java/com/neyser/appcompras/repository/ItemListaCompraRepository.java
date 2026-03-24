package com.neyser.appcompras.repository;

import com.neyser.appcompras.model.ItemListaCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemListaCompraRepository extends JpaRepository<ItemListaCompra, Long> {

    Optional<ItemListaCompra> findByIdAndListaCompraId(Long id, Long listaCompraId);

    boolean existsByIdAndListaCompraId(Long id, Long listaCompraId);

    List<ItemListaCompra> findByListaCompraId(Long listaCompraId);
}