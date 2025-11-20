package com.backend.tienda.controller;


import com.backend.tienda.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/stats")
    public ResponseEntity<?> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // 1. Ventas Totales (Si es null, devolvemos 0)
        Long ventas = ordenRepository.sumarVentasTotales();
        stats.put("ventasTotales", ventas != null ? ventas : 0);

        // 2. Cantidad de Boletas
        stats.put("totalBoletas", ordenRepository.count());

        // 3. Cantidad de Clientes (Usuarios)
        stats.put("totalClientes", usuarioRepository.count());

        // 4. Productos con Stock Crítico (La lista completa)
        stats.put("productosBajoStock", productoRepository.findLowStock());

        return ResponseEntity.ok(stats);
    }
}