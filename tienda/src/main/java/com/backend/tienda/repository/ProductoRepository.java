package com.backend.tienda.repository;

import com.backend.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaId(String categoriaId);

    @Query("SELECT p FROM Producto p WHERE p.price < p.normalPrice")
    List<Producto> findOfertas();

    @Query("SELECT p FROM Producto p WHERE p.stock < 10")
    List<Producto> findLowStock();
}