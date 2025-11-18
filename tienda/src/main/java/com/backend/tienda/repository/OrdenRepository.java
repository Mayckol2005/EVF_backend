package com.backend.tienda.repository;

import com.backend.tienda.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    // Para que un cliente vea SOLO sus compras
    List<Orden> findByUsuarioId(Long usuarioId);
}