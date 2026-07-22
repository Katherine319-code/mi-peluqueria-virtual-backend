package com.peluqueria.mipeluqueriavirtual.service;

import com.peluqueria.mipeluqueriavirtual.dto.AuthResponse;
import com.peluqueria.mipeluqueriavirtual.dto.LoginRequest;
import com.peluqueria.mipeluqueriavirtual.dto.RegisterRequest;
import com.peluqueria.mipeluqueriavirtual.entity.Rol;
import com.peluqueria.mipeluqueriavirtual.entity.Usuario;
import com.peluqueria.mipeluqueriavirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${google.client.id}")
    private String googleClientId;

    private final RestTemplate restTemplate = new RestTemplate();

    // ── Registro de cliente ───────────────────────────────────────────────────
    public AuthResponse registrar(RegisterRequest req) {
        if (usuarioRepository.existsByCorreo(req.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(req.getNombre());
        usuario.setApellido(req.getApellido());
        usuario.setCorreo(req.getCorreo());
        usuario.setPassword(passwordEncoder.encode(req.getPassword()));
        usuario.setTelefono(req.getTelefono());
        usuario.setRol(Rol.CLIENTE);
        usuarioRepository.save(usuario);

        String token = jwtService.generarToken(usuario.getCorreo());
        return new AuthResponse(token, usuario.getId(), usuario.getCorreo(),
                usuario.getNombre(), usuario.getApellido(), usuario.getRol().name());
    }

    // ── Login ────────────────────────────────────────────────────────────────
    public AuthResponse login(LoginRequest req) {
        Usuario usuario = usuarioRepository.findByCorreo(req.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(req.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        String token = jwtService.generarToken(usuario.getCorreo());
        return new AuthResponse(token, usuario.getId(), usuario.getCorreo(),
                usuario.getNombre(), usuario.getApellido(), usuario.getRol().name());
    }

    // ── Login / registro con Google ───────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public AuthResponse loginConGoogle(String idToken) {
        // Verifica el token directamente con el endpoint publico de Google
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        Map<String, Object> datos;
        try {
            datos = restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Token de Google invalido o expirado");
        }

        if (datos == null || !googleClientId.equals(datos.get("aud"))) {
            throw new RuntimeException("Token de Google no corresponde a esta aplicacion");
        }

        Boolean emailVerified = Boolean.valueOf(String.valueOf(datos.get("email_verified")));
        if (!Boolean.TRUE.equals(emailVerified)) {
            throw new RuntimeException("El correo de Google no esta verificado");
        }

        String correo   = String.valueOf(datos.get("email"));
        String nombre   = String.valueOf(datos.getOrDefault("given_name", "Usuario"));
        String apellido = String.valueOf(datos.getOrDefault("family_name", ""));

        Usuario usuario = usuarioRepository.findByCorreo(correo).orElseGet(() -> {
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setApellido(apellido);
            nuevo.setCorreo(correo);
            // password aleatoria: nunca se usara para iniciar sesion con este metodo
            nuevo.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            nuevo.setRol(Rol.CLIENTE);
            nuevo.setActivo(true);
            nuevo.setProveedorAuth("GOOGLE");
            return usuarioRepository.save(nuevo);
        });

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        String token = jwtService.generarToken(usuario.getCorreo());
        return new AuthResponse(token, usuario.getId(), usuario.getCorreo(),
                usuario.getNombre(), usuario.getApellido(), usuario.getRol().name());
    }
}