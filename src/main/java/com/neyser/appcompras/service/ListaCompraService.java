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
public class ListaCompraService {

    private final ListaCompraRepository listaCompraRepository;
    private final EstadoRepository estadoRepository;

    public List<ListaCompra> listarTodas() {
        return listaCompraRepository.findAll();
    }

    public ListaCompra guardar(ListaCompra listaCompra) {
        Estado estadoPendiente = estadoRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("No existe el estado PENDIENTE"));

        listaCompra.setEstado(estadoPendiente);
        return listaCompraRepository.save(listaCompra);
    }
}