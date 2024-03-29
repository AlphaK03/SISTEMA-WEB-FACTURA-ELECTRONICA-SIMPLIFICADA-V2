package com.example.proyecto_i.data;

public class Usuario {
    private String identification;
    private String password;
    private String rol;

    public Usuario() {
    }

    public Usuario(String identification, String password, String rol) {
        this.identification = identification;
        this.password = password;
        this.rol = rol;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
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
