package com.neyser.appcompras.controller;

import com.neyser.appcompras.model.Estado;
import com.neyser.appcompras.model.ItemListaCompra;
import com.neyser.appcompras.model.ListaCompra;
import com.neyser.appcompras.model.Producto;
import com.neyser.appcompras.repository.EstadoRepository;
import com.neyser.appcompras.repository.ItemListaCompraRepository;
import com.neyser.appcompras.repository.ListaCompraRepository;
import com.neyser.appcompras.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/listas/{listaId}/items")
@RequiredArgsConstructor
public class ItemListaCompraController {

    private final ItemListaCompraRepository itemListaCompraRepository;
    private final ListaCompraRepository listaCompraRepository;
    private final ProductoRepository productoRepository;
    private final EstadoRepository estadoRepository;

    @GetMapping("/nuevo")
    public String nuevo(@PathVariable Long listaId,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        Optional<ListaCompra> listaOpt = listaCompraRepository.findById(listaId);
        if (listaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "La lista no existe");
            return "redirect:/listas";
        }

        ItemListaCompra item = new ItemListaCompra();
        item.setListaCompra(listaOpt.get());

        List<Producto> productos = productoRepository.findAll();

        model.addAttribute("listaCompra", listaOpt.get());
        model.addAttribute("item", item);
        model.addAttribute("productos", productos);
        model.addAttribute("estados", estadoRepository.findAll());

        return "items/form";
    }

    @GetMapping("/editar/{itemId}")
    public String editar(@PathVariable Long listaId,
                         @PathVariable Long itemId,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        Optional<ListaCompra> listaOpt = listaCompraRepository.findById(listaId);
        if (listaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "La lista no existe");
            return "redirect:/listas";
        }

        Optional<ItemListaCompra> itemOpt = itemListaCompraRepository.findByIdAndListaCompraId(itemId, listaId);
        if (itemOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "El item no existe o no pertenece a la lista");
            return "redirect:/listas/ver/" + listaId;
        }

        model.addAttribute("listaCompra", listaOpt.get());
        model.addAttribute("item", itemOpt.get());
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("estados", estadoRepository.findAll());

        return "items/form";
    }

    @PostMapping("/guardar")
    public String guardar(@PathVariable Long listaId,
                          @ModelAttribute("item") ItemListaCompra formItem,
                          RedirectAttributes redirectAttributes,
                          Model model) {

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

        if (formItem.getProducto() == null || formItem.getProducto().getId() == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debe seleccionar un producto");
            return redireccionFormulario(listaId, formItem.getId());
        }

        Optional<Producto> productoOpt = productoRepository.findById(formItem.getProducto().getId());
        if (productoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "El producto seleccionado no existe");
            return redireccionFormulario(listaId, formItem.getId());
        }

        boolean cantidadValida = formItem.getCantidad() != null && formItem.getCantidad() > 0;
        boolean pesoValido = formItem.getPeso() != null && formItem.getPeso().compareTo(BigDecimal.ZERO) > 0;

        if (!cantidadValida && !pesoValido) {
            redirectAttributes.addFlashAttribute("mensaje", "Debe ingresar una cantidad o un peso mayor a cero");
            return redireccionFormulario(listaId, formItem.getId());
        }

        item.setProducto(productoOpt.get());
        item.setCantidad(formItem.getCantidad());
        item.setPeso(formItem.getPeso());
        item.setPrecio(formItem.getPrecio());
        item.setObservacion(formItem.getObservacion());
        item.setListaCompra(lista);

        if (formItem.getId() == null) {
            Optional<Estado> pendienteOpt = estadoRepository.findByNombreIgnoreCase("PENDIENTE");
            pendienteOpt.ifPresent(item::setEstado);
        } else {
            if (formItem.getEstado() != null && formItem.getEstado().getId() != null) {
                Optional<Estado> estadoOpt = estadoRepository.findById(formItem.getEstado().getId());
                estadoOpt.ifPresent(item::setEstado);
            }
        }

        itemListaCompraRepository.save(item);

        redirectAttributes.addFlashAttribute("mensaje", "Item guardado correctamente");
        return "redirect:/listas/ver/" + listaId;
    }

    @GetMapping("/eliminar/{itemId}")
    public String eliminar(@PathVariable Long listaId,
                           @PathVariable Long itemId,
                           RedirectAttributes redirectAttributes) {

        Optional<ItemListaCompra> itemOpt = itemListaCompraRepository.findByIdAndListaCompraId(itemId, listaId);

        if (itemOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "El item no existe o no pertenece a la lista");
            return "redirect:/listas/ver/" + listaId;
        }

        itemListaCompraRepository.delete(itemOpt.get());
        redirectAttributes.addFlashAttribute("mensaje", "Item eliminado correctamente");
        return "redirect:/listas/ver/" + listaId;
    }

    private String redireccionFormulario(Long listaId, Long itemId) {
        if (itemId == null) {
            return "redirect:/listas/" + listaId + "/items/nuevo";
        }
        return "redirect:/listas/" + listaId + "/items/editar/" + itemId;
    }
}