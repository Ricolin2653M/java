/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package mx.edu.uteq.HolaMundo.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import mx.edu.uteq.HolaMundo.entity.OcupacionProfesional;
import mx.edu.uteq.HolaMundo.entity.OfertaEducativa;
import mx.edu.uteq.HolaMundo.repository.OfertaEducativaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControladorOfertaEducativa {

    @Autowired
    private OfertaEducativaRepo repo;
    String menuInicio = "";
    String menuOferta = "";
    String menuAdmisiones = "";

    @GetMapping("/ofertaeducativa")
    public String paginaOfertaEducativa(Model model) {
        menuInicio = "nav-link";
        menuOferta = "nav-link active fw-bold";
        menuAdmisiones = "nav-link";
        //List<OfertaEducativa> oferta = repo.findAll();
        model.addAttribute("styleInicio", menuInicio);
        model.addAttribute("styleOferta", menuOferta);
        model.addAttribute("styleAdminiones", menuAdmisiones);
        //model.addAttribute("datos", oferta);
        return "ofertaeducativa";
    }

    @GetMapping("/agregarOferta")
    public String mostrarPaginaAgregarOferta(Model model) {
        model.addAttribute("oferta", new OfertaEducativa());
        model.addAttribute("styleInicio", menuInicio);
        model.addAttribute("styleOferta", menuOferta);
        model.addAttribute("styleAdminiones", menuAdmisiones);
        return "agregarOferta";
    }

    @PostMapping("/guardar-oferta")
    public String guardarOferta(@Valid @ModelAttribute("oferta") OfertaEducativa oferta, Errors errores) {
        if (errores.hasErrors()) {
            return "agregarOferta";
        }
        repo.save(oferta);
        return "redirect:/ofertaeducativa";
    }

    @GetMapping("/modificarOferta/{id}")
    public String mostrarPaginaModificarOferta(@PathVariable Long id, Model model) {
        OfertaEducativa oferta = repo.findById(id).orElse(null);

        if (oferta != null) {

            List<OcupacionProfesional> ocupaciones = oferta.getOcupaciones();

            model.addAttribute("oferta", oferta);
            model.addAttribute("ocupaciones", ocupaciones);
            model.addAttribute("styleInicio", menuInicio);
            model.addAttribute("styleOferta", menuOferta);
            model.addAttribute("styleAdminiones", menuAdmisiones);

            return "modificarOferta";
        } else {
            return "redirect:/ofertaeducativa";
        }
    }

    @PostMapping("/guardar-modificacion-oferta")
    public String guardarModificacionOferta(@Valid @ModelAttribute("oferta") OfertaEducativa oferta, Errors errores) {
        if (errores.hasErrors()) {
            return "redirect:/modificarOferta";
        }
        OfertaEducativa ofertaExistente = repo.findById(oferta.getId()).orElse(null);

        if (ofertaExistente != null) {
            oferta.setOcupaciones(ofertaExistente.getOcupaciones());
            repo.save(oferta);

            return "redirect:/ofertaeducativa";
        } else {
            return "redirect:/ofertaeducativa";
        }
    }

    @GetMapping("/eliminarOferta/{id}")
    public String eliminarOferta(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/ofertaeducativa";
    }

    @GetMapping(value = "/api/oferta-educativa")
    public String obtenerOfertas(Model model) {
        List<OfertaEducativa> lista = repo.findAll();
        model.addAttribute("datos", lista);
        return "ofertaeducativa::tbl-oferta";
    }

    @PostMapping("/consola/guardar-oferta")
    public String guardar(@Valid @ModelAttribute("oferta") OfertaEducativa oferta,
            @RequestParam(name = "ocupa", required = false) String[] ocupaciones,
            @RequestParam(name = "idOc", required = false) String[] idOc, Errors errores) {

        if (errores.hasErrors()) {
            return "modificarOferta";
        }
        if (ocupaciones != null) {
            List<OcupacionProfesional> listaO = new ArrayList<>();
            for (int i = 0; i < ocupaciones.length; i++) {
                OcupacionProfesional o = new OcupacionProfesional();
                o.setId(Integer.parseInt(idOc[i]));
                o.setOcupacion(ocupaciones[i]);
                listaO.add(o);
            }
            oferta.setOcupaciones(listaO);
        }
        repo.save(oferta);
        return "redirect:/ofertaeducativa";
    }

}
