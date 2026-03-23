package com.neyser.appcompras.service;

import com.neyser.appcompras.model.ItemListaCompra;

public interface ItemListaCompraService {

    void guardar(ItemListaCompra itemListaCompra);

    void eliminar(Long id);

    void marcarComoComprado(Long itemId);

    void marcarComoPendiente(Long itemId);
}