package com.example.proyecto_i.presentation.login;

import com.example.proyecto_i.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.stereotype.Controller("login")
public class Controller {

    @Autowired
    private Service service;
    @GetMapping("presentation/login/show")
    public String showLoginPage() {
        return "presentation/login/show"; // nombre de la vista del login
    }

    @GetMapping("/registro")
    public String showRegisterPage() {
        return "presentation/registro/registrar"; // nombre de la vista del login
    }
}