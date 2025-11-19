package com.backend.tienda.controller;

import com.backend.tienda.dto.OrdenRequest;
import com.backend.tienda.model.*;
import com.backend.tienda.repository.*;
import com.backend.tienda.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // OBTENER TODAS LAS ÓRDENES (Para AdminDashboard y Reportes)
    @GetMapping
    public List<Orden> getAllOrdenes() {
        return ordenRepository.findAll();
    }

    // CREAR ORDEN (Proceso de Compra Seguro)
    @PostMapping
    @Transactional // ¡Importante! Si algo falla, deshace todos los cambios (rollback)
    public ResponseEntity<?> createOrden(@RequestBody OrdenRequest request, HttpServletRequest httpServletRequest) {

        // 1. Identificar al Usuario desde el Token
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String correo = jwtUtil.getUsernameFromToken(token);
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Orden nuevaOrden = new Orden();
        nuevaOrden.setUsuario(usuario);
        nuevaOrden.setFecha(LocalDateTime.now());
        nuevaOrden.setEstado("Completado");
        nuevaOrden.setDetalles(new ArrayList<>());

        int totalCalculado = 0;

        // 2. Procesar cada ítem del carrito
        for (var item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

            // 3. VERIFICAR STOCK (Backend validation)
            if (producto.getStock() < item.getCantidad()) {
                return ResponseEntity.badRequest().body("Stock insuficiente para: " + producto.getName());
            }

            // 4. DESCONTAR STOCK
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            // 5. Crear detalle
            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(nuevaOrden);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrice()); // Usamos el precio REAL de la DB

            nuevaOrden.getDetalles().add(detalle);
            totalCalculado += (producto.getPrice() * item.getCantidad());
        }

        nuevaOrden.setTotal(totalCalculado);

        // 6. Guardar Orden completa
        Orden ordenGuardada = ordenRepository.save(nuevaOrden);

        return ResponseEntity.ok(ordenGuardada);
    }
}