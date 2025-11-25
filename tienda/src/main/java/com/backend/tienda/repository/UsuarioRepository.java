package com.backend.tienda.repository;

import com.backend.tienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Repositorio que maneja operaciones CRUD para la entidad Usuario
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su correo. Devuelve Optional para evitar errores si no existe
    Optional<Usuario> findByCorreo(String correo);

    // Verifica si ya existe un usuario con el correo indicado
    boolean existsByCorreo(String correo);

    // Verifica si ya existe un usuario registrado con un RUT específico
    boolean existsByRut(String rut);
}