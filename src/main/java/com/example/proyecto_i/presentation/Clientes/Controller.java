package com.example.proyecto_i.presentation.Clientes;

import com.example.proyecto_i.logic.Clientes;
import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;

@org.springframework.stereotype.Controller("clientes")
@SessionAttributes({"clientes","clienteSearch","clienteEdit","proveedor"})
public class Controller {
    @Autowired private Service service;
    @ModelAttribute("clientes") public Iterable<Clientes> clientes(){return new ArrayList<Clientes>();    }
    @ModelAttribute("clieneteSearch") public Clientes clieenteSearch (){return new Clientes();}
    @ModelAttribute("clienteEdit") public Clientes clienteEdit(){return new Clientes();}
    @ModelAttribute("proveedor") public Proveedor proveedor (){return new Proveedor(); }


    @PostMapping("/presentation/clientes/search")
    public String search(
            @ModelAttribute("clienteSearch") Clientes clienteSearch,
            @ModelAttribute(name="proveedor", binding = false) Proveedor proveedor,
            Model model){
        model.addAttribute("clientes",service.clone(proveedor, clienteSearch.getNombre()));
        model.addAttribute("clienteEdit", new Clientes());
        return "presentation/clientes/View";
    }


}
