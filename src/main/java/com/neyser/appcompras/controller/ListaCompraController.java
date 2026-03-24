package com.neyser.appcompras.controller;

import com.neyser.appcompras.model.Estado;
import com.neyser.appcompras.model.ListaCompra;
import com.neyser.appcompras.repository.EstadoRepository;
import com.neyser.appcompras.repository.ListaCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/listas")
@RequiredArgsConstructor
public class ListaCompraController {

    private final ListaCompraRepository listaCompraRepository;
    private final EstadoRepository estadoRepository;

    @GetMapping
    public String listar(Model model) {
        List<ListaCompra> listas = listaCompraRepository.findAll();
        model.addAttribute("listas", listas);
        return "listas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        ListaCompra listaCompra = new ListaCompra();
        listaCompra.setFecha(LocalDate.now());
        model.addAttribute("listaCompra", listaCompra);
        return "listas/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ListaCompra listaCompra,
                          RedirectAttributes redirectAttributes) {

        if (listaCompra.getEstado() == null) {
            Optional<Estado> estadoPendiente = estadoRepository.findByNombreIgnoreCase("PENDIENTE");
            estadoPendiente.ifPresent(listaCompra::setEstado);
        }

        if (listaCompra.getFecha() == null) {
            listaCompra.setFecha(LocalDate.now());
        }

        listaCompraRepository.save(listaCompra);
        redirectAttributes.addFlashAttribute("mensaje", "Lista guardada correctamente");
        return "redirect:/listas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ListaCompra> listaOpt = listaCompraRepository.findById(id);

        if (listaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "La lista no existe");
            return "redirect:/listas";
        }

        model.addAttribute("listaCompra", listaOpt.get());
        return "listas/form";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ListaCompra> listaOpt = listaCompraRepository.findDetalleById(id);

        if (listaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "La lista no existe");
            return "redirect:/listas";
        }

        model.addAttribute("listaCompra", listaOpt.get());
        return "listas/ver";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!listaCompraRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "La lista no existe");
            return "redirect:/listas";
        }

        listaCompraRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensaje", "Lista eliminada correctamente");
        return "redirect:/listas";
    }
}