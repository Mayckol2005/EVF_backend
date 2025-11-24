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

    @GetMapping
    public List<Orden> getAllOrdenes() {
        return ordenRepository.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createOrden(@RequestBody OrdenRequest request, HttpServletRequest httpServletRequest) {

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

        for (var item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

            if (producto.getStock() < item.getCantidad()) {
                return ResponseEntity.badRequest().body("Stock insuficiente para: " + producto.getName());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(nuevaOrden);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrice()); 
            nuevaOrden.getDetalles().add(detalle);
            totalCalculado += (producto.getPrice() * item.getCantidad());
        }

        nuevaOrden.setTotal(totalCalculado);

        Orden ordenGuardada = ordenRepository.save(nuevaOrden);

        return ResponseEntity.ok(ordenGuardada);
    }
}