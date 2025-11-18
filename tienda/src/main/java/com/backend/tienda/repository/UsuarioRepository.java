package com.backend.tienda.repository;

import com.backend.tienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método mágico: Spring entiende que buscas por 'correo'
    Optional<Usuario> findByCorreo(String correo);

    // Para validar que no se repita el RUT o Correo al registrarse
    boolean existsByCorreo(String correo);
    boolean existsByRut(String rut);
}