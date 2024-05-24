package com.example.proyecto_i.logic;

public class ClienteSimpleDTO {
    private String identificacion;
    private String nombre;
    private String telefono;
    private String correo;
    public ClienteSimpleDTO(String identificacion, String nombre, String telefono, String correo){
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo=correo;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }
}
