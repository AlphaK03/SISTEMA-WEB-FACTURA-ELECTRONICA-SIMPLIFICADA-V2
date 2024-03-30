package com.example.proyecto_i.presentation.Usuario;
import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.stereotype.Controller("usuario")
public class Controller {
    @Autowired
    private Service service;

    @PostMapping("/presentation/login/login")
    public String login(Usuario usuario, HttpSession httpSession){
        try{
            /*Falta verificar la contraseña*/
            Usuario userDB = service.usuarioRead(usuario.getIdentification());
            httpSession.setAttribute("Usuario",userDB);
            httpSession.setAttribute("proveedor",service.proveedorRead(userDB.getIdentification()));
            switch (userDB.getRol()){
                case "PRO":
                    return "redirect:/presentation/facturar/show";
            }
        }catch(Exception ex){
            ex.getMessage();
        }
        return null;
    }
    @GetMapping("/presentation/login/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "presentation/logout/View";
    }

}
