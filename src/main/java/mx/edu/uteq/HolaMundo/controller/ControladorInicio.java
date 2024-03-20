/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package mx.edu.uteq.HolaMundo.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import mx.edu.uteq.HolaMundo.entity.Admision;
import mx.edu.uteq.HolaMundo.entity.OfertaEducativa;
import mx.edu.uteq.HolaMundo.repository.AdmisionRepo;
import mx.edu.uteq.HolaMundo.repository.OfertaEducativaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private AdmisionRepo admisionRepo;

    @Autowired
    private OfertaEducativaRepo ofertaEducativaRepo;

    String menuInicio = "";
    String menuOferta = "";
    String menuAdmisiones = "";
    String menuUsuarios = "";

    @GetMapping("/")
    public String page(Model model) {
        menuInicio = "nav-link active fw-bold";
        menuOferta = "nav-link";
        menuAdmisiones = "nav-link";
        menuUsuarios = "nav-link";
        model.addAttribute("styleInicio", menuInicio);
        model.addAttribute("styleOferta", menuOferta);
        model.addAttribute("styleAdminiones", menuAdmisiones);
        model.addAttribute("styleUsuarios", menuUsuarios);
        return "index";
    }
    

    @GetMapping("/inicio")
    public String mostrarCarruseles(Model model) {
        // Obtener los datos reales de la base de datos
        List<OfertaEducativa> oferta = ofertaEducativaRepo.findByActivo(true);
        List<Admision> admision = (List<Admision>) admisionRepo.findByActivo(true);

        // Agregar los datos al modelo
        model.addAttribute("admision", admision);
        model.addAttribute("ofertaEducativa", oferta);

        return "carrusel"; 
    }
    @GetMapping("/recupera")
    public String recuperarContra(Model model){
        String hola = "hola";
        model.addAttribute("model", hola);
        return "recupera";
    }

}
