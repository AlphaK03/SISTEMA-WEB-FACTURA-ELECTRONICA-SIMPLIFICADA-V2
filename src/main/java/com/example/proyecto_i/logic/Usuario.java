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
    private byte activo;

    public Usuario(String identificacion, Object o, String rol) {
        this.identificacion = identificacion;
        this.rol = rol;
    }

    public Usuario() {

    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public byte getActivo() {
        return activo;
    }

    public void setActivo(byte activo) {
        this.activo = activo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        if (activo != usuario.activo) return false;
        if (identificacion != null ? !identificacion.equals(usuario.identificacion) : usuario.identificacion != null)
            return false;
        if (contrasena != null ? !contrasena.equals(usuario.contrasena) : usuario.contrasena != null) return false;
        if (rol != null ? !rol.equals(usuario.rol) : usuario.rol != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = identificacion != null ? identificacion.hashCode() : 0;
        result = 31 * result + (contrasena != null ? contrasena.hashCode() : 0);
        result = 31 * result + (rol != null ? rol.hashCode() : 0);
        result = 31 * result + (int) activo;
        return result;
    }
}
