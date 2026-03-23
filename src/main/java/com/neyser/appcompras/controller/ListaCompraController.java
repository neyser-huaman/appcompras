package com.neyser.appcompras.controller;

import com.neyser.appcompras.model.ListaCompra;
import com.neyser.appcompras.service.ListaCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/listas")
@RequiredArgsConstructor
public class ListaCompraController {

    private final ListaCompraService listaCompraService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listas", listaCompraService.listarTodas());
        return "listas/listas";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("listaCompra", new ListaCompra());
        return "listas/lista-form";
    }

    @PostMapping
    public String guardar(@ModelAttribute ListaCompra listaCompra) {
        listaCompraService.guardar(listaCompra);
        return "redirect:/listas";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable("id") Long id, Model model) {
        ListaCompra listaCompra = listaCompraService.buscarPorId(id);
        model.addAttribute("listaCompra", listaCompra);
        return "listas/ver";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        ListaCompra listaCompra = listaCompraService.buscarPorId(id);
        model.addAttribute("listaCompra", listaCompra);
        return "listas/lista-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id) {
        listaCompraService.eliminar(id);
        return "redirect:/listas";
    }
}