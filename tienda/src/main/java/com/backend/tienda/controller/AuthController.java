package com.backend.tienda.controller;

import com.backend.tienda.dto.AuthResponse;
import com.backend.tienda.dto.LoginRequest;
import com.backend.tienda.model.Usuario;
import com.backend.tienda.repository.UsuarioRepository;
import com.backend.tienda.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> userOpt = usuarioRepository.findByCorreo(request.getCorreo());

        if (userOpt.isPresent()) {
            Usuario usuario = userOpt.get();
            if (usuario.getPassword().equals(request.getPassword())) {
                String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getTipo());
                return ResponseEntity.ok(new AuthResponse(
                        token,
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        usuario.getTipo()
                ));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    // Registro (Público y usado por el Admin actualmente)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario nuevoUsuario) {
        if (usuarioRepository.existsByCorreo(nuevoUsuario.getCorreo())) {
            return ResponseEntity.badRequest().body("El correo ya está registrado.");
        }

        // Asignar rol por defecto si no viene
        if (nuevoUsuario.getTipo() == null) {
            nuevoUsuario.setTipo("cliente");
        }

        // --- PROTECCIÓN EXTRA ---
        // Si la contraseña viene vacía (por error del form), asignamos una default temporal
        if (nuevoUsuario.getPassword() == null || nuevoUsuario.getPassword().trim().isEmpty()) {
            nuevoUsuario.setPassword("123456");
        }

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok(usuarioGuardado);
    }
}