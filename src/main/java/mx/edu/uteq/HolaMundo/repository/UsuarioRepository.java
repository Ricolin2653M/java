/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.edu.uteq.HolaMundo.repository;

import mx.edu.uteq.HolaMundo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author x
 */
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    
    public Usuario findByUsername(String username);
}

