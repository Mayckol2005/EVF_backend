package com.backend.tienda.controller;

import com.backend.tienda.dto.AuthResponse;
import com.backend.tienda.dto.LoginRequest;
import com.backend.tienda.model.Usuario;
import com.backend.tienda.repository.UsuarioRepository;
import com.backend.tienda.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Opcional si usas encriptación
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint para Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Buscar usuario por correo
        Optional<Usuario> userOpt = usuarioRepository.findByCorreo(request.getCorreo());

        if (userOpt.isPresent()) {
            Usuario usuario = userOpt.get();
            // 2. Verificar contraseña (simple por ahora, idealmente usar BCrypt)
            if (usuario.getPassword().equals(request.getPassword())) {

                // 3. Generar Token
                String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getTipo());

                // 4. Devolver respuesta con Token y datos del usuario
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

    // Endpoint para Registro
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario nuevoUsuario) {
        if (usuarioRepository.existsByCorreo(nuevoUsuario.getCorreo())) {
            return ResponseEntity.badRequest().body("El correo ya está registrado.");
        }
        // Por defecto, los registros nuevos son clientes
        if (nuevoUsuario.getTipo() == null) {
            nuevoUsuario.setTipo("cliente");
        }

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok(usuarioGuardado);
    }
}