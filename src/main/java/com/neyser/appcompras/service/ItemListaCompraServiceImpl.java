package com.neyser.appcompras.service;

import com.neyser.appcompras.model.Estado;
import com.neyser.appcompras.model.ItemListaCompra;
import com.neyser.appcompras.repository.EstadoRepository;
import com.neyser.appcompras.repository.ItemListaCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ItemListaCompraServiceImpl implements ItemListaCompraService {

    private final ItemListaCompraRepository itemListaCompraRepository;
    private final EstadoRepository estadoRepository;

    @Override
    public void guardar(ItemListaCompra itemListaCompra) {
        if (itemListaCompra.getEstado() == null) {
            Estado pendiente = estadoRepository.findByNombreIgnoreCase("PENDIENTE")
                    .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no encontrado"));
            itemListaCompra.setEstado(pendiente);
        }

        if (itemListaCompra.getPrecio() == null) {
            itemListaCompra.setPrecio(BigDecimal.ZERO);
        }

        boolean sinCantidad = itemListaCompra.getCantidad() == null || itemListaCompra.getCantidad() <= 0;
        boolean sinPeso = itemListaCompra.getPeso() == null || itemListaCompra.getPeso().compareTo(BigDecimal.ZERO) <= 0;

        if (sinCantidad && sinPeso) {
            throw new RuntimeException("Debe ingresar una cantidad o un peso");
        }

        itemListaCompraRepository.save(itemListaCompra);
    }

    @Override
    public void eliminar(Long id) {
        itemListaCompraRepository.deleteById(id);
    }

    @Override
    public void marcarComoComprado(Long itemId) {
        ItemListaCompra item = itemListaCompraRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con id: " + itemId));

        Estado comprado = estadoRepository.findByNombreIgnoreCase("COMPRADO")
                .orElseThrow(() -> new RuntimeException("Estado COMPRADO no encontrado"));

        item.setEstado(comprado);
        itemListaCompraRepository.save(item);
    }

    @Override
    public void marcarComoPendiente(Long itemId) {
        ItemListaCompra item = itemListaCompraRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con id: " + itemId));

        Estado pendiente = estadoRepository.findByNombreIgnoreCase("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no encontrado"));

        item.setEstado(pendiente);
        itemListaCompraRepository.save(item);
    }
}