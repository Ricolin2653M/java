/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.edu.uteq.HolaMundo.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author x
 */
public class EncriptarPassword {

    public static void main(String[] args) {
        String password = "1235678";
        System.out.println("pwd: " + password);
        for (int i=0; i<3;i++) {
            System.out.println("pwd encriptado: " + encriptarPassword(password));
        }
    }

    private static String encriptarPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
