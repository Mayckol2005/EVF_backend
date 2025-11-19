package com.backend.tienda.model;

import jakarta.persistence.*;
import lombok.Data; // Lombok genera getters/setters automáticos
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String rut;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private String direccion;
    private String region;
    private String comuna;

    // Para manejar Roles (admin vs cliente)
    // En tu frontend usas 'tipo': 'administrador' o 'cliente'
    private String tipo;
}