/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.edu.uteq.HolaMundo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

/**
 *
 * @author x
 */
@Entity
@Data
@Table(name = "users")
public class Usuario {

    @Id
    @Column(length = 50, unique = true)
    @NotEmpty
    private String username;
    @NotEmpty
    @Column(length = 255)
    @Size(min = 8, max = 255)
    private String password;
    private boolean enabled;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private List<Rol> authorities;

}
