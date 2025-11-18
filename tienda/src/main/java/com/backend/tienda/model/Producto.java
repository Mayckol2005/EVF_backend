package com.backend.tienda.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private Integer price;
    private Integer normalPrice;
    private Integer stock;

    @Column(length = 1000) // Descripción larga
    private String description;

    // URL de la imagen (ej: /assets/img/foto.jpg)
    private String image;

    // Categorías (perfumes-varon, perfumes-dama, etc.)
    private String categoriaId;

    private String genero; // Masculino, Femenino, Unisex
    private String tipo; // Eau de Parfum, etc.
}