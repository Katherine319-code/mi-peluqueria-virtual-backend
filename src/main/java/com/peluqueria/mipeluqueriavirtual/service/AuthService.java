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
import java.security.SecureRandom;
import java.time.LocalDateTime;
import com.peluqueria.mipeluqueriavirtual.service.EmailService;

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

    @Autowired
    private EmailService emailService;

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

    private final SecureRandom random = new SecureRandom();

    public void solicitarRecuperacion(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese correo"));

        if (usuario.getRol() != Rol.CLIENTE) {
            throw new RuntimeException("La recuperacion de contrasena solo esta disponible para clientes");
        }

        String codigo = String.format("%06d", random.nextInt(1_000_000));
        usuario.setResetCodigo(codigo);
        usuario.setResetExpiracion(LocalDateTime.now().plusMinutes(15));
        usuarioRepository.save(usuario);

        emailService.enviarCodigoRecuperacion(usuario.getCorreo(), usuario.getNombre(), codigo);
    }

    public void restablecerPassword(String correo, String codigo, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese correo"));

        if (usuario.getResetCodigo() == null || !usuario.getResetCodigo().equals(codigo)) {
            throw new RuntimeException("Codigo invalido");
        }
        if (usuario.getResetExpiracion() == null || usuario.getResetExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El codigo ha expirado, solicita uno nuevo");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuario.setResetCodigo(null);
        usuario.setResetExpiracion(null);
        usuarioRepository.save(usuario);
    }


}