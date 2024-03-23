/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.edu.uteq.HolaMundo.controller;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import mx.edu.uteq.HolaMundo.entity.Usuario;
import mx.edu.uteq.HolaMundo.repository.UsuarioRepository;
import mx.edu.uteq.HolaMundo.utility.EncriptarPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author x
 */
@Controller
public class ControladorRecuperarContra {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private JavaMailSender emailS;

    //Vista al formulario para enviar el token al correo
    @GetMapping("/recupera")
    public String recuperarContra(Model model) {
        return "recupera";
    }

    //Función para enviar correo (el correo debe de existir y estar en la bd)
    @PostMapping("/enviarCorreo")
    public String enviarCorreoFunc(@RequestParam("correo") String correo, Model model) {
        //System.out.println("Correo proporcionado: " + correo);

        // Buscar usuario por correo
        Usuario usuario = repo.findByCorreo(correo);

        if (usuario != null) {
            String token = usuario.getPassword();
            String tokengenerado = EncriptarPassword.encriptarPassword(token);
            usuario.setToken(tokengenerado);

            usuario.setFechaCreacionToken(LocalDateTime.now());//Fecha y hora en que se creó el token

            String correoP = correo;
            String asunto = "Recuperación de contraseña";
            String mensaje = "Ingresa al siguiente enlace para recuperar tu contraseña: http://localhost:8080/restablecerContra \n\nEl token tiene una duración de 20 minutos una vez generado \n\nEste es tu token de confirmación: " + tokengenerado;

            SimpleMailMessage mensajeCorreo = new SimpleMailMessage();
            mensajeCorreo.setTo(correoP);
            mensajeCorreo.setSubject(asunto);
            mensajeCorreo.setText(mensaje);
            emailS.send(mensajeCorreo);

            repo.save(usuario);

            return "redirect:/pantallaconfirmacion";
        } else {
            model.addAttribute("error", "El correo proporcionado no está registrado en nuestra base de datos.");
            return "errorPage";
        }
    }

    // Clase auxiliar para manejar el correo en el formulario
    public static class UsuarioCorreoForm {

        private String correo;

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }
    }

    @PostMapping("/guardarContra")
    public String guardarContra(@RequestParam("username") String username,
            @RequestParam("token") String token,
            @RequestParam("contra") String contra,
            @RequestParam("contra2") String contra2,
            Model model) {
        System.out.println("Usuario proporcionado: " + username);

        // Buscar usuario
        Usuario usuario = repo.findByUsername(username);

        // Verificar si el usuario es null
        if (usuario == null) {
            model.addAttribute("error", "Verifica que tu nombre de usuario sea correcto");
            return "error_modal";
        }

        String tokenusuario = usuario.getToken();
        LocalDateTime fechaCreacionToken = usuario.getFechaCreacionToken();

        if (!tokenusuario.equals(token)) {
            model.addAttribute("error", "El token es incorrecto");
            return "error_modal";
        }

        if (fechaCreacionToken.plusMinutes(20).isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "El token ha vencido");
            return "error_modal";
        }
        
        if (!contra.equals(contra2)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "error_modal";
        }

        String cambiarpassword = EncriptarPassword.encriptarPassword(contra2);
        usuario.setPassword(cambiarpassword);

        String cambiartoken = usuario.getPassword();
        String reemplazartoken = EncriptarPassword.encriptarPassword(cambiartoken);
        usuario.setToken(reemplazartoken);

        repo.save(usuario);

        return "redirect:/confirmacioncontra";
    }

    @GetMapping("/restablecerContra")
    public String paginaRestablecerContra(Model model) {
        return "restablecercontra";
    }

    @GetMapping("/paginaToken")
    public String paginaGeneraToken(Model model) {
        return "tokenpantalla";
    }

    @GetMapping("/pantallaconfirmacion")
    public String paginaDeConfirmacion(Model model) {
        return "correoenviado";
    }

    @GetMapping("/confirmacioncontra")
    public String contraRestaurada(Model model) {
        return "confirmacioncontra";
    }

    @GetMapping("/tokennotfound")
    public String tokenNotFound(Model model) {
        return "tokennotfound";
    }
}
