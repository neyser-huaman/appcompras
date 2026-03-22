package com.neyser.appcompras.controller;

import com.neyser.appcompras.model.ListaCompra;
import com.neyser.appcompras.service.ListaCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/listas")
@RequiredArgsConstructor
public class ListaCompraController {

    private final ListaCompraService listaCompraService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listas", listaCompraService.listarTodas());
        return "listas";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("listaCompra", new ListaCompra());
        model.addAttribute("estados", listaCompraService.listarEstadosActivos());
        return "lista-form";
    }

    @PostMapping
    public String guardar(ListaCompra listaCompra) {
        listaCompraService.guardar(listaCompra);
        return "redirect:/listas";
    }
}