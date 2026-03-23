package com.neyser.appcompras.service;

import com.neyser.appcompras.model.Producto;

import java.util.List;

public interface ProductoService {

    List<Producto> listarTodos();

    List<Producto> listarActivos();

    void guardar(Producto producto);

    Producto buscarPorId(Long id);

    void cambiarEstado(Long id);
}