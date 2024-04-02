package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Factura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturasRepository extends CrudRepository <Factura, String> {

}
