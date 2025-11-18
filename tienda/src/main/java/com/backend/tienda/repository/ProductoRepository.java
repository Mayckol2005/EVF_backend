package com.backend.tienda.repository;


import com.backend.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar por categoría (ej: perfumes-varon)
    List<Producto> findByCategoriaId(String categoriaId);

    // Buscar ofertas (donde precio < precio normal)
    // Aquí usamos JPQL (SQL de Java)
    @Query("SELECT p FROM Producto p WHERE p.price < p.normalPrice")
    List<Producto> findOfertas();

    // Buscar productos con poco stock (para el Dashboard Admin)
    @Query("SELECT p FROM Producto p WHERE p.stock < 10")
    List<Producto> findLowStock();
}