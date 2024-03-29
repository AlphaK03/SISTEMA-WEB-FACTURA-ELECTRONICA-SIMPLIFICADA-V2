package com.example.proyecto_i.data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Component
public interface ProveedorRepository /*extends<Provedor, String>*/{
    Proveedor findByIdentification(String identification);
}
