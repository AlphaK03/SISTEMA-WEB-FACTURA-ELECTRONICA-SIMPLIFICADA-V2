package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Producto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductosRepository extends CrudRepository<Producto, String> {
    List<Producto> findByProveedorByProveedorIdentificacion(String proveedorId);
}
