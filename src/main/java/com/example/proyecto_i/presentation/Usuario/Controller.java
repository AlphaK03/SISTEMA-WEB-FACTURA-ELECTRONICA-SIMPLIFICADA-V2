package com.example.proyecto_i.presentation.Usuario;
import com.example.proyecto_i.logic.Producto;
import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
            Optional<Proveedor> proveedorExist = service.proveedorRead(usuario.getIdentificacion());
            if(proveedorExist.isPresent()){
                Proveedor proveedor = proveedorExist.get();
                model.addAttribute("proveedor", proveedor);
            }


            Optional<Usuario> userDB = service.usuarioRead(usuario.getIdentificacion());

            List<Producto> productos = service.productosSearchAll(usuario.getIdentificacion());

            model.addAttribute("productos", productos);



            if (userDB.isPresent()) {
                httpSession.setAttribute("usuario", userDB.get());
                String rol = userDB.get().getRol();
                httpSession.setAttribute("rol", rol); // Guarda el rol en la sesión
                if (Objects.equals(usuario.getContrasena(), userDB.get().getContrasena()) && (userDB.get().getActivo() != 0)) {
                    switch (rol) {
                        case "ADM":
                            return "redirect:/presentation/administrador";
                        case "PRO":
                            return "/presentation/productos/agregarProducto";
                    }
                }


            }
        } catch(Exception ex) {
            ex.getMessage();
        }

        return null;
    }

    @GetMapping("/presentation/login/logout")
    public String logout(HttpSession httpSession){
        httpSession.removeAttribute("facturaDetalles");
        httpSession.invalidate();
        return "presentation/login/show";
    }

    @GetMapping("/editarPerfil")
    public String perfilConfig(HttpSession httpSession, Model model) {
        // Recuperar proveedor de la sesión o de la base de datos (dependiendo de cómo se almacenen)
        Usuario usuario = (Usuario) httpSession.getAttribute("usuario");

        Optional<Proveedor> proveedor = service.proveedorRead(usuario.getIdentificacion());

        if (proveedor.isPresent()) {
            model.addAttribute("proveedor", proveedor.get());
            return "presentation/perfil";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/editarPerfil")
    public String guardarPerfil(@ModelAttribute("proveedor") Proveedor proveedorActualizado) {

        service.proveedorUpdate(proveedorActualizado.getIdentificacion(), proveedorActualizado);

        return "redirect:/editarPerfil";
    }


    @GetMapping("/xmlView")
    public String acerca(HttpSession httpSession){
        return "xmlView";
    }




}
