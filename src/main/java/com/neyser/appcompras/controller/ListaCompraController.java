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
@RequestMapping("/listas")
public class ListaCompraController {

    private final ListaCompraRepository listaCompraRepository;
    private final ItemListaCompraRepository itemListaCompraRepository;
    private final ProductoRepository productoRepository;
    private final EstadoRepository estadoRepository;

    public ListaCompraController(ListaCompraRepository listaCompraRepository,
                                 ItemListaCompraRepository itemListaCompraRepository,
                                 ProductoRepository productoRepository,
                                 EstadoRepository estadoRepository) {
        this.listaCompraRepository = listaCompraRepository;
        this.itemListaCompraRepository = itemListaCompraRepository;
        this.productoRepository = productoRepository;
        this.estadoRepository = estadoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        List<ListaCompra> listas = listaCompraRepository.findAll();
        model.addAttribute("listas", listas);
        return "listas/listas";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        ListaCompra lista = new ListaCompra();
        model.addAttribute("lista", lista);
        return "listas/lista-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ListaCompra> listaOpt = listaCompraRepository.findById(id);

        if (listaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "La lista no existe");
            return "redirect:/listas";
        }

        model.addAttribute("lista", listaOpt.get());
        return "listas/lista-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("lista") ListaCompra lista,
                          RedirectAttributes redirectAttributes) {

        if (lista.getEstado() == null) {
            Estado pendiente = estadoRepository.findByNombreIgnoreCase("PENDIENTE").orElse(null);
            lista.setEstado(pendiente);
        }

        listaCompraRepository.save(lista);
        redirectAttributes.addFlashAttribute("mensaje", "Lista guardada correctamente");
        return "redirect:/listas";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ListaCompra> listaOpt = listaCompraRepository.findById(id);

        if (listaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "La lista no existe");
            return "redirect:/listas";
        }

        model.addAttribute("listaCompra", listaOpt.get());
        return "listas/ver";
    }

    @GetMapping("/{listaId}/items/nuevo")
    public String nuevoItem(@PathVariable Long listaId, Model model, RedirectAttributes redirectAttributes) {
        Optional<ListaCompra> listaOpt = listaCompraRepository.findById(listaId);

        if (listaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "La lista no existe");
            return "redirect:/listas";
        }

        List<Producto> productos = productoRepository.findByActivoTrue();

        ItemListaCompra item = new ItemListaCompra();
        item.setListaCompra(listaOpt.get());
        item.setProducto(new Producto());

        model.addAttribute("item", item);
        model.addAttribute("listaCompra", listaOpt.get());
        model.addAttribute("productos", productos);

        return "items/form";
    }
}