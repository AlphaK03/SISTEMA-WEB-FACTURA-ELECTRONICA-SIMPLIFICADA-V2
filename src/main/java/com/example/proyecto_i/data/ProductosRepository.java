package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Productos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductosRepository extends CrudRepository<Productos, String> {
}
