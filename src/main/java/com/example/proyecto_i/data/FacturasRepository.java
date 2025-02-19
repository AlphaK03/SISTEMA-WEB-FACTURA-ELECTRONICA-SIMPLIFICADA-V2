package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Factura;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FacturasRepository extends CrudRepository<Factura, String> {
    @Query("select f from Factura f where f.proveedorByProveedor = ?1")
    List<Factura> findByProveedor(String idProveedor);


}
