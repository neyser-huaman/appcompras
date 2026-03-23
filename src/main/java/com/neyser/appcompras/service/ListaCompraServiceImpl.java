package com.neyser.appcompras.service;

import com.neyser.appcompras.model.Estado;
import com.neyser.appcompras.model.ListaCompra;
import com.neyser.appcompras.repository.EstadoRepository;
import com.neyser.appcompras.repository.ListaCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListaCompraServiceImpl implements ListaCompraService {

    private final ListaCompraRepository listaCompraRepository;
    private final EstadoRepository estadoRepository;

    @Override
    public List<ListaCompra> listarTodas() {
        return listaCompraRepository.findAll();
    }

    @Override
    public void guardar(ListaCompra listaCompra) {
        if (listaCompra.getId() == null) {
            Estado estadoPendiente = estadoRepository.findByNombre("PENDIENTE")
                    .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no encontrado"));
            listaCompra.setEstado(estadoPendiente);
        }
        listaCompraRepository.save(listaCompra);
    }

    @Override
    public ListaCompra buscarPorId(Long id) {
        return listaCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada con id: " + id));
    }

    @Override
    public void eliminar(Long id) {
        listaCompraRepository.deleteById(id);
    }
}