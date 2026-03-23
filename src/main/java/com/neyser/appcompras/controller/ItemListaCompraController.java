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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/items")
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
    public String guardar(@ModelAttribute("item") ItemListaCompra item,
                          @RequestParam("listaCompra.id") Long listaId,
                          RedirectAttributes redirectAttributes) {

        Optional<ListaCompra> listaOpt = listaCompraRepository.findById(listaId);
        if (listaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "La lista no existe");
            return "redirect:/listas";
        }

        item.setListaCompra(listaOpt.get());

        if (item.getProducto() != null && item.getProducto().getId() != null) {
            Producto producto = productoRepository.findById(item.getProducto().getId()).orElse(null);
            item.setProducto(producto);
        } else {
            item.setProducto(null);
        }

        if (item.getEstado() != null && item.getEstado().getId() != null) {
            Estado estado = estadoRepository.findById(item.getEstado().getId()).orElse(null);
            item.setEstado(estado);
        } else {
            Estado pendiente = estadoRepository.findByNombreIgnoreCase("PENDIENTE").orElse(null);
            item.setEstado(pendiente);
        }

        itemListaCompraRepository.save(item);

        redirectAttributes.addFlashAttribute("mensaje", "Item guardado correctamente");
        return "redirect:/listas/ver/" + listaId;
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                         @RequestParam("listaId") Long listaId,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        Optional<ItemListaCompra> itemOpt = itemListaCompraRepository.findById(id);
        Optional<ListaCompra> listaOpt = listaCompraRepository.findById(listaId);

        if (itemOpt.isEmpty() || listaOpt.isEmpty()) {
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
    public String eliminar(@PathVariable Long id,
                           @RequestParam("listaId") Long listaId,
                           RedirectAttributes redirectAttributes) {

        if (itemListaCompraRepository.existsById(id)) {
            itemListaCompraRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Item eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "El item no existe");
        }

        return "redirect:/listas/ver/" + listaId;
    }
}