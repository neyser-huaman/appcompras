package com.neyser.appcompras.controller;

import com.neyser.appcompras.model.Estado;
import com.neyser.appcompras.model.ItemListaCompra;
import com.neyser.appcompras.model.ListaCompra;
import com.neyser.appcompras.model.Producto;
import com.neyser.appcompras.repository.EstadoRepository;
import com.neyser.appcompras.repository.ItemListaCompraRepository;
import com.neyser.appcompras.repository.ListaCompraRepository;
import com.neyser.appcompras.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/listas/{listaId}/items")
public class ItemListaCompraController {

    private final ItemListaCompraRepository itemListaCompraRepository;
    private final ListaCompraRepository listaCompraRepository;
    private final ProductoRepository productoRepository;
    private final EstadoRepository estadoRepository;

    public ItemListaCompraController(ItemListaCompraRepository itemListaCompraRepository,
                                     ListaCompraRepository listaCompraRepository,
                                     ProductoRepository productoRepository,
                                     EstadoRepository estadoRepository) {
        this.itemListaCompraRepository = itemListaCompraRepository;
        this.listaCompraRepository = listaCompraRepository;
        this.productoRepository = productoRepository;
        this.estadoRepository = estadoRepository;
    }

    @PostMapping("/guardar")
    public String guardar(@PathVariable Long listaId,
                          @ModelAttribute("item") ItemListaCompra formItem,
                          RedirectAttributes redirectAttributes) {

        Optional<ListaCompra> listaOpt = listaCompraRepository.findById(listaId);
        if (listaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "La lista no existe");
            return "redirect:/listas";
        }

        ListaCompra lista = listaOpt.get();
        ItemListaCompra item;

        if (formItem.getId() != null) {
            Optional<ItemListaCompra> itemOpt = itemListaCompraRepository.findByIdAndListaCompraId(formItem.getId(), listaId);

            if (itemOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "El item no existe o no pertenece a la lista");
                return "redirect:/listas/ver/" + listaId;
            }

            item = itemOpt.get();
        } else {
            item = new ItemListaCompra();
            item.setListaCompra(lista);
        }

        boolean cantidadVacia = formItem.getCantidad() == null || formItem.getCantidad() <= 0;
        boolean pesoVacio = formItem.getPeso() == null || formItem.getPeso().compareTo(BigDecimal.ZERO) <= 0;

        if (cantidadVacia && pesoVacio) {
            redirectAttributes.addFlashAttribute("mensaje", "Debe ingresar una cantidad o un peso mayor a cero");
            return (formItem.getId() == null)
                    ? "redirect:/listas/" + listaId + "/items/nuevo"
                    : "redirect:/listas/" + listaId + "/items/editar/" + formItem.getId();
        }

        if (formItem.getProducto() == null || formItem.getProducto().getId() == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debe seleccionar un producto");
            return (formItem.getId() == null)
                    ? "redirect:/listas/" + listaId + "/items/nuevo"
                    : "redirect:/listas/" + listaId + "/items/editar/" + formItem.getId();
        }

        Producto producto = productoRepository.findById(formItem.getProducto().getId()).orElse(null);
        if (producto == null) {
            redirectAttributes.addFlashAttribute("mensaje", "El producto no existe");
            return (formItem.getId() == null)
                    ? "redirect:/listas/" + listaId + "/items/nuevo"
                    : "redirect:/listas/" + listaId + "/items/editar/" + formItem.getId();
        }

        item.setListaCompra(lista);
        item.setProducto(producto);
        item.setCantidad(formItem.getCantidad());
        item.setPeso(formItem.getPeso());
        item.setPrecio(formItem.getPrecio());
        item.setObservacion(formItem.getObservacion());

        if (formItem.getId() == null) {
            Estado pendiente = estadoRepository.findByNombreIgnoreCase("PENDIENTE").orElse(null);
            item.setEstado(pendiente);
        } else {
            if (formItem.getEstado() != null && formItem.getEstado().getId() != null) {
                Estado estado = estadoRepository.findById(formItem.getEstado().getId()).orElse(null);
                item.setEstado(estado);
            } else if (item.getEstado() == null) {
                Estado pendiente = estadoRepository.findByNombreIgnoreCase("PENDIENTE").orElse(null);
                item.setEstado(pendiente);
            }
        }

        itemListaCompraRepository.save(item);
        redirectAttributes.addFlashAttribute("mensaje", "Item guardado correctamente");
        return "redirect:/listas/ver/" + listaId;
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long listaId,
                         @PathVariable Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        Optional<ListaCompra> listaOpt = listaCompraRepository.findById(listaId);
        Optional<ItemListaCompra> itemOpt = itemListaCompraRepository.findByIdAndListaCompraId(id, listaId);

        if (listaOpt.isEmpty() || itemOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "No se encontró el item o la lista");
            return "redirect:/listas";
        }

        ItemListaCompra item = itemOpt.get();

        if (item.getProducto() == null) {
            item.setProducto(new Producto());
        }

        if (item.getEstado() == null) {
            item.setEstado(new Estado());
        }

        List<Producto> productos = productoRepository.findByActivoTrue();
        List<Estado> estados = estadoRepository.findAll();

        model.addAttribute("item", item);
        model.addAttribute("listaCompra", listaOpt.get());
        model.addAttribute("productos", productos);
        model.addAttribute("estados", estados);

        return "items/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long listaId,
                           @PathVariable Long id,
                           RedirectAttributes redirectAttributes) {

        Optional<ItemListaCompra> itemOpt = itemListaCompraRepository.findByIdAndListaCompraId(id, listaId);

        if (itemOpt.isPresent()) {
            itemListaCompraRepository.delete(itemOpt.get());
            redirectAttributes.addFlashAttribute("mensaje", "Item eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "El item no existe o no pertenece a la lista");
        }

        return "redirect:/listas/ver/" + listaId;
    }
}