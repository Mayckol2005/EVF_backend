package com.backend.tienda.model;

import com.fasterxml.jackson.annotation.JsonProperty; // Importación necesaria
import jakarta.persistence.*;
import lombok.Data;

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
    // CORRECCIÓN AQUÍ:
    // Usamos WRITE_ONLY en lugar de @JsonIgnore.
    // Esto permite RECIBIR la contraseña en el registro, pero NO enviarla al frontend.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String direccion;
    private String region;
    private String comuna;

    // Roles: 'administrador' o 'cliente'
    private String tipo;
}