/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package mx.edu.uteq.HolaMundo.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import mx.edu.uteq.HolaMundo.entity.Admision;
import mx.edu.uteq.HolaMundo.repository.AdmisionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ControladorAdmision {

    //private static final Logger log = LoggerFactory.getLogger(ControladorAdmision.class);
    @Autowired
    private AdmisionRepo repo;
    String menuInicio = "";
    String menuOferta = "";
    String menuAdmisiones = "";

    @GetMapping("/admisiones")
    public String page(Model model) {
        menuInicio = "nav-link";
        menuOferta = "nav-link";
        menuAdmisiones = "nav-link active fw-bold";
        model.addAttribute("styleInicio", menuInicio);
        model.addAttribute("styleOferta", menuOferta);
        model.addAttribute("styleAdminiones", menuAdmisiones);
        return "admisiones";
    }

    @RequestMapping(value = "/api/admisiones", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List obtenerAdmisiones() {
        List<Admision> lista = (List<Admision>) repo.findAll();
        return lista;
    }

    @RequestMapping(value = "/guardar-admision",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> guardar(@Valid @RequestBody Admision admision,
            Errors error) throws Exception {
//        log.info("Admisión recibida para guardar o editar: " + admision.toString());
        if (error.hasErrors()) {
            throw new Exception(error.toString());
        }
        Admision admisionGuardada;
        if (admision.getId() != null && admision.getId() != 0) {
            Admision admisionExistente = repo.findById(admision.getId()).orElse(null);
            if (admisionExistente != null) {
                admisionExistente.setNombreAdmision(admision.getNombreAdmision());
                admisionExistente.setActivo(admision.isActivo());
                admisionGuardada = repo.save(admisionExistente);
//                log.info("Admisión editada: " + admisionGuardada.toString());
//                System.out.println(admision.getId());
//                System.out.println("Editada");
            } else {
                throw new Exception("No se encontró la admisión con ID: " + admision.getId());
            }
        } else {
            admisionGuardada = repo.save(admision);
//            log.info("Nueva admisión guardada: " + admisionGuardada.toString());
//            System.out.println(admision.getId());
        }
        return new ResponseEntity<>(admisionGuardada, HttpStatus.CREATED);
    }

    @GetMapping("/api/admisiones/{id}")
    public ResponseEntity<Admision> obtenerAdmisionPorId(@PathVariable Long id) {
        Admision admision = repo.findById(id).orElse(null);
        if (admision != null) {
            return ResponseEntity.ok(admision);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/admisiones-borrar", consumes = "application/json")
    public ResponseEntity<String> eliminarAdmision(@Valid @RequestBody Admision admision,
            Errors error) throws Exception {
        try {
            repo.deleteById(admision.getId());
            return ResponseEntity.ok("Admisión eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la admisión: " + e.getMessage());
        }
    }

}
