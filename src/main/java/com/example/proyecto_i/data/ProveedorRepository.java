package com.example.proyecto_i.data;
import com.example.proyecto_i.logic.Proveedor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface ProveedorRepository extends CrudRepository <Proveedor, String>{

    Proveedor findByIdentification(String identification);
}
