package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Detalle;
import com.example.proyecto_i.logic.Factura;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DetallesRepository extends CrudRepository<Detalle, String> {
    @Query("select f from Detalle f where f.facturaByNumerofactura = ?1")
    List<Detalle> findByFacturaByNumerofactura(String idProveedor);

    @Query("select f from Detalle f where f.productoByCodigoproducto = ?1")
    List<Detalle> findByProductoByCodigoproducto(String idProveedor);

}