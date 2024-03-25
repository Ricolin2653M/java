/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.edu.uteq.HolaMundo.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import mx.edu.uteq.HolaMundo.utility.EncriptarPassword;
import mx.edu.uteq.HolaMundo.entity.Rol;
import mx.edu.uteq.HolaMundo.entity.Usuario;
import mx.edu.uteq.HolaMundo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author x
 */
@Controller
public class ControladorUsuarios {

    @Autowired
    private UsuarioRepository repo;
    String menuInicio = "";
    String menuOferta = "";
    String menuAdmisiones = "";
    String menuUsuarios = "";

    @GetMapping("/usuarios")
    public String paginaAdmision(Model model) {
        menuInicio = "nav-link";
        menuOferta = "nav-link";
        menuAdmisiones = "nav-link";
        menuUsuarios = "nav-link active fw-bold";
        model.addAttribute("styleInicio", menuInicio);
        model.addAttribute("styleOferta", menuOferta);
        model.addAttribute("styleAdminiones", menuAdmisiones);
        model.addAttribute("styleUsuarios", menuUsuarios);
        return "usuarios";
    }

    @GetMapping(value = "/api/usuario")
    public String obtenerAdmisiones(Model model) {
        List<Usuario> lista = (List<Usuario>) repo.findAll();
        model.addAttribute("datos", lista);
        return "usuarios::tbl-usuario";
    }

    @GetMapping("/agregarUsuario")
    public String mostrarPaginaAgregarUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("styleInicio", menuInicio);
        model.addAttribute("styleOferta", menuOferta);
        model.addAttribute("styleAdminiones", menuAdmisiones);
        model.addAttribute("styleUsuarios", menuUsuarios);
        return "agregarUsuario";
    }

    @PostMapping("/guardar-usuario")
    public String guardarNuevoUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
            @RequestParam(name = "ocupa", required = false) String[] authorities,
            @RequestParam(name = "idOc", required = false) String[] idOc, Errors errores) {

        if (errores.hasErrors()) {
            return "agregarUsuario";
        }

        // Validar si ya existe un usuario con el mismo nombre de usuario
        Usuario usuarioExistente = repo.findByUsername(usuario.getUsername());
        if (usuarioExistente != null) {
            // Mostrar un mensaje de error indicando que el usuario ya existe
            errores.rejectValue("username", "error.usuario.duplicado", "El nombre de usuario ya está en uso");
            return "agregarUsuario";
        }

        // Validar si ya existe un usuario con el mismo correo electrónico
        usuarioExistente = repo.findByCorreo(usuario.getCorreo());
        if (usuarioExistente != null) {
            // Mostrar un mensaje de error indicando que el correo electrónico ya está en uso
            errores.rejectValue("correo", "error.correo.duplicado", "El correo electrónico ya está registrado");
            return "agregarUsuario";
        }

        // Continuar con el proceso de guardar el usuario si pasa las validaciones
        String pass = usuario.getPassword();
        String passEncriptado = EncriptarPassword.encriptarPassword(pass);
        usuario.setPassword(passEncriptado);

        if (authorities != null) {
            List<Rol> listaO = new ArrayList<>();
            for (int i = 0; i < authorities.length; i++) {
                Rol o = new Rol();
                o.setId(Integer.parseInt(idOc[i]));
                o.setAuthority(authorities[i]);
                listaO.add(o);
            }
            usuario.setAuthorities(listaO);
        }

        repo.save(usuario);

        return "redirect:/usuarios";
    }

    @GetMapping("/modificarUsuario/{username}")
    public String mostrarPaginaModificarUsuario(@PathVariable String username, Model model) {
        Usuario usuario;
        usuario = repo.findByUsername(username);

        if (usuario != null) {

            List<Rol> authorities = usuario.getAuthorities();

            model.addAttribute("usuario", usuario);
            model.addAttribute("authorities", authorities);
            model.addAttribute("styleInicio", menuInicio);
            model.addAttribute("styleOferta", menuOferta);
            model.addAttribute("styleAdminiones", menuAdmisiones);
            model.addAttribute("styleUsuarios", menuUsuarios);

            return "modificarUsuario";
        } else {
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/guardar-modificacion-usuario")
    public String guardarModificacionOferta(@Valid @ModelAttribute("usuario") Usuario usuario, Errors errores) {
        if (errores.hasErrors()) {
            return "redirect:/modificarOferta";
        }

        Usuario usuarioExistente = repo.findByUsername(usuario.getUsername());

        if (usuarioExistente != null) {
            usuario.setAuthorities(usuarioExistente.getAuthorities());
            repo.save(usuario);

            return "redirect:/usuarios";
        } else {
            return "redirect:/usuarios";
        }
    }

    @RequestMapping("/api/guardar-usuario")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
            @RequestParam(name = "ocupa", required = false) String[] authorities,
            @RequestParam(name = "idOc", required = false) String[] idOc,
            Errors errores) {

        if (errores.hasErrors()) {
            return "modificarUsuario";
        }

        Usuario usuarioExistente = repo.findByCorreo(usuario.getCorreo());
        if (usuarioExistente != null && !usuarioExistente.getUsername().equals(usuario.getUsername())) {
            errores.rejectValue("correo", "error.correo", "El correo electrónico ya está registrado en otro usuario");
            return "modificarUsuario";
        }

        // Actualiza las ocupaciones si se proporcionan
        if (authorities != null) {
            List<Rol> listaO = new ArrayList<>();
            for (int i = 0; i < authorities.length; i++) {
                Rol o = new Rol();
                o.setId(Integer.parseInt(idOc[i]));
                o.setAuthority(authorities[i]);
                listaO.add(o);
            }
            usuario.setAuthorities(listaO);
        } else {
            // Elimina si ocupaciones es null o vacío
            usuario.setAuthorities(null);
        }

        // Guarda el usuario
        repo.save(usuario);

        return "redirect:/usuarios";
    }

    @Transactional
    @GetMapping("/eliminarUsuario/{username}")
    public String eliminarUsuario(@PathVariable String username) {
        repo.deleteByUsername(username);
        return "redirect:/usuarios";
    }

}
