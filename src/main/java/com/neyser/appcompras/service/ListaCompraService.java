package com.neyser.appcompras.service;

import com.neyser.appcompras.model.ListaCompra;

import java.util.List;

public interface ListaCompraService {

    List<ListaCompra> listarTodas();

    void guardar(ListaCompra listaCompra);

    ListaCompra buscarPorId(Long id);

    void eliminar(Long id);
}