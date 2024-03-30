package com.example.proyecto_i.logic;

import com.example.proyecto_i.data.ProveedorRepository;
import com.example.proyecto_i.logic.Usuario;
import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.data.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class Service {
    private final UsuarioRepository usuarioRepository;
    private final ProveedorRepository proveedorRepository;

    @Autowired
    public Service(UsuarioRepository usuarioRepository, ProveedorRepository proveedorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public Usuario usuarioRead(String identification) {

        return usuarioRepository.findByIdentification(identification);
    }

    public Proveedor proveedorRead(String identification) {

        return proveedorRepository.findByIdentification(identification);
    }


    public Object clone(Proveedor proveedor, Object nombre) {
        return nombre;
    }
}

