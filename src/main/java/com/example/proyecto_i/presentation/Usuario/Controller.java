package com.example.proyecto_i.presentation.Usuario;
import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Controller("usuario")
public class Controller {
    @Autowired
    private Service service;

    @PostMapping("/login")
    public String login(Usuario usuario, HttpSession httpSession, Model model) {
        try {
            List<Proveedor> proveedores = new ArrayList<>();
            proveedores = service.proveedorGetAll();
            model.addAttribute("proveedores", proveedores);

            Optional<Usuario> userDB = service.usuarioRead(usuario.getIdentificacion());

            if (userDB.isPresent()) {
                httpSession.setAttribute("usuario", userDB.get());
                String rol = userDB.get().getRol();
                httpSession.setAttribute("rol", rol); // Guarda el rol en la sesión
                if(Objects.equals(usuario.getContrasena(), userDB.get().getContrasena())){
                    switch (rol) {
                        case "ADM":
                            return "/presentation/administrador";
                        case "PRO":

                            return "/presentation/productos/agregarProducto";
                    }
                };

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
