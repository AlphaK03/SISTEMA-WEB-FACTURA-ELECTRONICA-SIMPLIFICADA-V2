package com.example.proyecto_i.data;
import com.example.proyecto_i.logic.Proveedor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Component
public interface ProveedorRepository extends CrudRepository<Proveedor, String>{
    //En este caso el crud repository encuentra por id, no es necesario la función
    Proveedor findByIdentification(String identification);
}
