package com.backend.tienda.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detalles_orden")
@Data
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;
    private Integer precioUnitario; // Precio al momento de comprar

    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}