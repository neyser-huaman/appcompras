package com.neyser.appcompras.service;

import com.neyser.appcompras.model.Producto;
import com.neyser.appcompras.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public void guardar(Producto producto) {
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
        productoRepository.save(producto);
    }

    @Override
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Override
    public void cambiarEstado(Long id) {
        Producto producto = buscarPorId(id);
        producto.setActivo(!producto.getActivo());
        productoRepository.save(producto);
    }
}