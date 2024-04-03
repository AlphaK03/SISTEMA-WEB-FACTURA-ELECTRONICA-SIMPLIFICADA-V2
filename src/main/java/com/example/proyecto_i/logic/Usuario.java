package com.example.proyecto_i.logic;

public class Usuario {
    private String identificacion;
    private String password;
    private String rol;


    public Usuario(String identificacion, String password, String rol) {
        this.identificacion = identificacion;
        this.password = password;
        this.rol = rol;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}