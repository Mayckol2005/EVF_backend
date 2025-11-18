package com.backend.tienda.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordenes")
@Data
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private Integer total;
    private String estado; // "Pendiente", "Completado"

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL)
    private List<DetalleOrden> detalles;
}