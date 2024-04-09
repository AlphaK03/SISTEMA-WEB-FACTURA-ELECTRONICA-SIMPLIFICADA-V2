package com.example.proyecto_i.presentation.Clientes;

import com.example.proyecto_i.logic.Cliente;
import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.Optional;

@org.springframework.stereotype.Controller("clientes")
@SessionAttributes({"clientes","clienteSearch","clienteEdit","proveedor"})
public class Controller {
    @Autowired private Service service;
    @ModelAttribute("clientes") public Iterable<Cliente> clientes(){return new ArrayList<Cliente>();    }
    @ModelAttribute("clieneteSearch") public Cliente clieenteSearch (){return new Cliente();}
    @ModelAttribute("clienteEdit") public Cliente clienteEdit(){return new Cliente();}
    @ModelAttribute("proveedor") public Proveedor proveedor (){return new Proveedor(); }

    @PostMapping("/crearCliente")
    public String crearCliente(@ModelAttribute("cliente") Cliente cliente, Model model, HttpSession session){
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            Optional<Proveedor> proveedorOpt = service.proveedorRead(usuario.getIdentificacion());
            if(proveedorOpt.isPresent()){
                Proveedor proveedor = proveedorOpt.get();
                cliente.setProveedorByProveedor(proveedor);
            }

            service.clientesCreate(cliente);

            model.addAttribute("mensaje", "El cliente se ha creado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensaje", "Hubo un error al crear el cliente. Por favor, inténtalo de nuevo.");
        }
        return "presentation/clientes/View";
    }


    @GetMapping("/presentation/clientes")
    public String search(Proveedor proveedor, Model model){

        return "presentation/clientes/View";
    }

}

