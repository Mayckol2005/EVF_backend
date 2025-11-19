package com.backend.tienda.controller;

import com.backend.tienda.model.Producto;
import com.backend.tienda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // 1. Obtener todos los productos (PÚBLICO: para la tienda)
    @GetMapping
    public List<Producto> getAllProducts() {
        return productoRepository.findAll();
    }

    // 2. Obtener un producto por ID (PÚBLICO: para ver detalle)
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductById(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Crear producto (ADMIN - Requiere Token)
    @PostMapping
    public Producto createProduct(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    // 4. Actualizar producto (ADMIN - Requiere Token)
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProduct(@PathVariable Long id, @RequestBody Producto detalles) {
        return productoRepository.findById(id)
                .map(prod -> {
                    prod.setName(detalles.getName());
                    prod.setBrand(detalles.getBrand());
                    prod.setPrice(detalles.getPrice());
                    prod.setNormalPrice(detalles.getNormalPrice());
                    prod.setStock(detalles.getStock());
                    prod.setDescription(detalles.getDescription());
                    prod.setImage(detalles.getImage());
                    prod.setCategoriaId(detalles.getCategoriaId());
                    prod.setGenero(detalles.getGenero());
                    prod.setTipo(detalles.getTipo());
                    return ResponseEntity.ok(productoRepository.save(prod));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Eliminar producto (ADMIN - Requiere Token)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(prod -> {
                    productoRepository.delete(prod);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}