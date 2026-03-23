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
        return "listas";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("listaCompra", new ListaCompra());
        return "lista-form";
    }

    @PostMapping
    public String guardar(@ModelAttribute ListaCompra listaCompra) {
        listaCompraService.guardar(listaCompra);
        return "redirect:/listas";
    }
}