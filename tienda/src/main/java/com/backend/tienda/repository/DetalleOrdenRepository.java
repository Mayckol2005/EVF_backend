package com.backend.tienda.repository;

import com.backend.tienda.model.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
    // No necesitamos métodos extra, solo el .deleteAll() que ya viene incluido
}