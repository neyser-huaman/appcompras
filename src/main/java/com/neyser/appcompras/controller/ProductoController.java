package com.neyser.appcompras.controller;

import com.neyser.appcompras.model.Producto;
import com.neyser.appcompras.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        return "productos/productos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Producto producto = new Producto();
        producto.setActivo(true);
        model.addAttribute("producto", producto);
        return "productos/producto-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Producto> productoOpt = productoRepository.findById(id);

        if (productoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "El producto no existe");
            return "redirect:/productos";
        }

        model.addAttribute("producto", productoOpt.get());
        return "productos/producto-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("producto") Producto producto,
                          RedirectAttributes redirectAttributes) {

        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }

        productoRepository.save(producto);
        redirectAttributes.addFlashAttribute("mensaje", "Producto guardado correctamente");
        return "redirect:/productos";
    }

    @GetMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Producto> productoOpt = productoRepository.findById(id);

        if (productoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "El producto no existe");
            return "redirect:/productos";
        }

        Producto producto = productoOpt.get();
        producto.setActivo(!producto.getActivo());
        productoRepository.save(producto);

        redirectAttributes.addFlashAttribute("mensaje",
                producto.getActivo() ? "Producto activado correctamente" : "Producto desactivado correctamente");

        return "redirect:/productos";
    }
}