package com.backend.tienda.repository;

import com.backend.tienda.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <--- Importación necesaria
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    // Para que un cliente vea SOLO sus compras
    List<Orden> findByUsuarioId(Long usuarioId);

    // NUEVO: Sumar el total de todas las órdenes para el Dashboard
    // Si la tabla está vacía, podría devolver null, por eso usamos Long objeto.
    @Query("SELECT SUM(o.total) FROM Orden o")
    Long sumarVentasTotales();
}