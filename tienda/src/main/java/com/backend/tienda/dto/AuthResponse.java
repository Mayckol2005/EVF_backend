package com.backend.tienda.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String nombre;
    private String correo;
    private String tipo; // "administrador" o "cliente"

    public AuthResponse(String token, String nombre, String correo, String tipo) {
        this.token = token;
        this.nombre = nombre;
        this.correo = correo;
        this.tipo = tipo;
    }
}