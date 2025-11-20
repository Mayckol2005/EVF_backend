package com.backend.tienda.controller;

import com.backend.tienda.model.Usuario;
import com.backend.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    // --- AQUÍ ESTÁ LA CLAVE: EL MÉTODO POST ---
    @PostMapping // Faltaba este endpoint para CREAR usuarios desde el admin
    public ResponseEntity<?> createUser(@RequestBody Usuario nuevoUsuario) {
        // 1. Validar si el correo ya existe
        if (usuarioRepository.existsByCorreo(nuevoUsuario.getCorreo())) {
            return ResponseEntity.badRequest().body("El correo ya está registrado.");
        }

        // 2. Validar si el RUT ya existe (si aplica)
        if (nuevoUsuario.getRut() != null && usuarioRepository.existsByRut(nuevoUsuario.getRut())) {
            return ResponseEntity.badRequest().body("El RUT ya está registrado.");
        }

        // 3. Asegurar que tenga contraseña
        // Si viene vacía, le ponemos una por defecto (ej: '123456') o lanzamos error.
        if (nuevoUsuario.getPassword() == null || nuevoUsuario.getPassword().isEmpty()) {
            // Opción A: Asignar default
            nuevoUsuario.setPassword("123456");
            // Opción B: Retornar error
            // return ResponseEntity.badRequest().body("La contraseña es obligatoria.");
        }

        // 4. Guardar
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok(usuarioGuardado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUserById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUser(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioDetalles.getNombre());
                    usuario.setApellidos(usuarioDetalles.getApellidos());
                    usuario.setCorreo(usuarioDetalles.getCorreo());
                    usuario.setDireccion(usuarioDetalles.getDireccion());
                    usuario.setRegion(usuarioDetalles.getRegion());
                    usuario.setComuna(usuarioDetalles.getComuna());
                    usuario.setTipo(usuarioDetalles.getTipo());
                    usuario.setRut(usuarioDetalles.getRut());

                    // Solo actualizamos la contraseña si viene una nueva
                    if (usuarioDetalles.getPassword() != null && !usuarioDetalles.getPassword().isEmpty()) {
                        usuario.setPassword(usuarioDetalles.getPassword());
                    }

                    Usuario actualizado = usuarioRepository.save(usuario);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuarioRepository.delete(usuario);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}