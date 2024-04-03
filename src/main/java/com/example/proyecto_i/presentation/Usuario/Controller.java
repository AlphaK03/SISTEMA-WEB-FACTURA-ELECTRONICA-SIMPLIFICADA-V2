package com.example.proyecto_i.presentation.Usuario;
import com.example.proyecto_i.logic.Administrador;
import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@org.springframework.stereotype.Controller("usuario")
public class Controller {
    @Autowired
    private Service service;

    @PostMapping("/login")
    public String login(Usuario usuario, HttpSession httpSession) {
        try {
            Optional<Administrador> userDB = service.usuarioRead(usuario.getIdentificacion());

            if (userDB.isPresent()) {
                httpSession.setAttribute("usuario", userDB.get());
                String rol = userDB.get().getRol();
                httpSession.setAttribute("rol", rol); // Guarda el rol en la sesión

                switch (rol) {
                    case "ADM":
                        return "/presentation/productos/agregarProducto";
                    // Agrega más casos según sea necesario para otros roles
                }
            }
        } catch(Exception ex) {
            ex.getMessage();
        }

        return null;
    }

    @GetMapping("/presentation/login/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "presentation/login/show";
    }

}
