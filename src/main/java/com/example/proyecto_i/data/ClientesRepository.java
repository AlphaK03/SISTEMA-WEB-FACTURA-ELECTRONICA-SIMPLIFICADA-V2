package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Cliente;
import com.example.proyecto_i.logic.Producto;
import com.example.proyecto_i.logic.Proveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientesRepository extends CrudRepository <Cliente, String>{
    List<Cliente> findByProveedorByProveedorIdentificacion(String proveedorId);

    @Query("select c from Cliente c where c.proveedorByProveedor = ?1 and c.nombre like  %?2%")
    List<Cliente> findByProveedorAndName(String idProveedor, String nombreProveedor);

    @Query("select c from Cliente c where c.proveedorByProveedor = ?1 and c.identificacion =?2")
    List<Cliente> findByProveedorAndIdentificacion(String idProveedor, String identificacionCliente);

}
