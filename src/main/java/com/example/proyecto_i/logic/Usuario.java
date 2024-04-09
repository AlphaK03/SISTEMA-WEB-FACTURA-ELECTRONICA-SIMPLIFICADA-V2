package com.example.proyecto_i.logic;

import jakarta.persistence.*;

@Entity
public class Usuario {
    @Id
    @Column(name = "identificacion")
    private String identificacion;
    @Basic
    @Column(name = "contrasena")
    private String contrasena;
    @Basic
    @Column(name = "rol")
    private String rol;

    @Basic
    @Column(name = "activo")
    private boolean activo;


    public Usuario(String identificacion, String contrasena, String rol, boolean activo) {
        this.identificacion = identificacion;
        this.contrasena = contrasena;
        this.rol = rol;
        this.activo = false;
    }

    public Usuario() {

    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}