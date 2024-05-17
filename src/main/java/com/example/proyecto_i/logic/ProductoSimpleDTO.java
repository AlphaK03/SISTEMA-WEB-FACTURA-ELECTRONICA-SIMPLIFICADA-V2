package com.example.proyecto_i.logic;

public class ProductoSimpleDTO {
    private String nombre;
    private String precio;

    // Constructor
    public ProductoSimpleDTO(String nombre, String precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
