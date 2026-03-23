package com.neyser.appcompras.controller;

import com.neyser.appcompras.model.ItemListaCompra;
import com.neyser.appcompras.model.ListaCompra;
import com.neyser.appcompras.model.Producto;
import com.neyser.appcompras.service.ItemListaCompraService;
import com.neyser.appcompras.service.ListaCompraService;
import com.neyser.appcompras.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/listas/{listaId}/items")
@RequiredArgsConstructor
public class ItemListaCompraController {

    private final ItemListaCompraService itemListaCompraService;
    private final ListaCompraService listaCompraService;
    private final ProductoService productoService;

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoItem(@PathVariable("listaId") Long listaId, Model model) {
        ListaCompra listaCompra = listaCompraService.buscarPorId(listaId);

        model.addAttribute("listaCompra", listaCompra);
        model.addAttribute("item", new ItemListaCompra());
        model.addAttribute("productos", productoService.listarActivos());

        return "items/form";
    }

    @PostMapping
    public String guardar(@PathVariable("listaId") Long listaId,
                          @ModelAttribute("item") ItemListaCompra item,
                          @RequestParam("productoId") Long productoId) {

        ListaCompra listaCompra = listaCompraService.buscarPorId(listaId);
        Producto producto = productoService.buscarPorId(productoId);

        item.setListaCompra(listaCompra);
        item.setProducto(producto);

        itemListaCompraService.guardar(item);

        return "redirect:/listas/" + listaId;
    }

    @GetMapping("/eliminar/{itemId}")
    public String eliminar(@PathVariable("listaId") Long listaId,
                           @PathVariable("itemId") Long itemId) {
        itemListaCompraService.eliminar(itemId);
        return "redirect:/listas/" + listaId;
    }

    @GetMapping("/comprado/{itemId}")
    public String marcarComoComprado(@PathVariable("listaId") Long listaId,
                                     @PathVariable("itemId") Long itemId) {
        itemListaCompraService.marcarComoComprado(itemId);
        return "redirect:/listas/" + listaId;
    }

    @GetMapping("/pendiente/{itemId}")
    public String marcarComoPendiente(@PathVariable("listaId") Long listaId,
                                      @PathVariable("itemId") Long itemId) {
        itemListaCompraService.marcarComoPendiente(itemId);
        return "redirect:/listas/" + listaId;
    }
}