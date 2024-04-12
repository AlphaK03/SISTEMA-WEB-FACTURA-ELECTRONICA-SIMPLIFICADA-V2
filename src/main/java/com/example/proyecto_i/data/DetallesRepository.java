package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Detalle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallesRepository extends CrudRepository<Detalle, Integer> {

    @Query("SELECT d FROM Detalle d WHERE d.facturaByNumerofactura.numero = :numerofactura")
    List<Detalle> findByFacturaByNumerofactura(int numerofactura);

    @Query("SELECT d FROM Detalle d WHERE d.productoByCodigoproducto.codigo = :codigoproducto")
    List<Detalle> findByProductoByCodigoproducto(int codigoproducto);

}

