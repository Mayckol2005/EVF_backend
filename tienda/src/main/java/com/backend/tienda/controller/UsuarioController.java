package com.backend.tienda.controller;



import com.backend.tienda.model.Usuario;
import com.backend.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios") // Ruta base para administración
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUserById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar usuario (La función que faltaba)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUser(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    // Actualizamos los campos
                    usuario.setNombre(usuarioDetalles.getNombre());
                    usuario.setApellidos(usuarioDetalles.getApellidos());
                    usuario.setCorreo(usuarioDetalles.getCorreo());
                    usuario.setDireccion(usuarioDetalles.getDireccion());
                    usuario.setRegion(usuarioDetalles.getRegion());
                    usuario.setComuna(usuarioDetalles.getComuna());
                    usuario.setTipo(usuarioDetalles.getTipo());
                    // Nota: En un caso real, deberías encriptar la password si viene cambiada
                    // usuario.setPassword(usuarioDetalles.getPassword());

                    Usuario actualizado = usuarioRepository.save(usuario);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar usuario
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