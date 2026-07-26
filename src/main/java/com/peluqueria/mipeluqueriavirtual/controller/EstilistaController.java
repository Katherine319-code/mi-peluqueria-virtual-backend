package com.peluqueria.mipeluqueriavirtual.controller;

import com.peluqueria.mipeluqueriavirtual.entity.Cita;
import com.peluqueria.mipeluqueriavirtual.entity.Estilista;
import com.peluqueria.mipeluqueriavirtual.entity.Rol;
import com.peluqueria.mipeluqueriavirtual.entity.Usuario;
import com.peluqueria.mipeluqueriavirtual.repository.CitaRepository;
import com.peluqueria.mipeluqueriavirtual.repository.EstilistaRepository;
import com.peluqueria.mipeluqueriavirtual.repository.UsuarioRepository;
import com.peluqueria.mipeluqueriavirtual.service.EstilistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/estilistas")
public class EstilistaController {

    @Autowired EstilistaService estilistaService;
    @Autowired EstilistaRepository estilistaRepository;
    @Autowired CitaRepository citaRepository;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired PasswordEncoder passwordEncoder;

    // ── Login de estilista ───────────────────────────────────────────────────
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String correo     = body.get("correo");
        String contrasena = body.get("contrasena");
        Estilista estilista = estilistaService.login(correo, contrasena);
        if (estilista == null) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
        return ResponseEntity.ok(toResponse(estilista));
    }

    // ── Obtener estilista por usuarioId (usado tras el login general) ────────
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> porUsuario(@PathVariable Long usuarioId) {
        return estilistaRepository.findByUsuarioId(usuarioId)
            .map(e -> ResponseEntity.ok(toResponse(e)))
            .orElse(ResponseEntity.notFound().build());
    }

    // ── Listar todas las estilistas ──────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar() {
        return ResponseEntity.ok(
            estilistaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList())
        );
    }

    // ── Crear estilista (solo admin) ─────────────────────────────────────────
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        if (usuarioRepository.existsByCorreo(String.valueOf(body.get("correo")))) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(String.valueOf(body.get("nombre")));
        usuario.setApellido(String.valueOf(body.getOrDefault("apellido", "")));
        usuario.setCorreo(String.valueOf(body.get("correo")));
        usuario.setPassword(passwordEncoder.encode(String.valueOf(body.get("password"))));
        usuario.setRol(Rol.ESTILISTA);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        Estilista estilista = new Estilista();
        estilista.setUsuario(usuario);
        estilista.setEspecialidad(String.valueOf(body.getOrDefault("especialidad", "General")));
        estilista.setDescripcion(String.valueOf(body.getOrDefault("descripcion", "")));
        estilista.setExperiencia(Integer.parseInt(String.valueOf(body.getOrDefault("experiencia", "0"))));
        estilista.setDisponible(true);
        return ResponseEntity.ok(toResponse(estilistaRepository.save(estilista)));
    }

    // ── Desactivar estilista (admin) — "eliminar" logico ──────────────────────
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        return estilistaRepository.findById(id).map(estilista -> {
            Usuario usuario = estilista.getUsuario();
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
            estilista.setDisponible(false);
            estilistaRepository.save(estilista);
            return ResponseEntity.ok(toResponse(estilista));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Agenda del día — recibe estilistaId ───────────────────────────

    @GetMapping("/agenda/dia")
    public ResponseEntity<List<Map<String, Object>>> agendaDia(
            @RequestParam Long estilistaId,
            @RequestParam String fecha) {
        // fecha esperada en formato "yyyy-MM-dd" (ISO)
        LocalDate localDate = LocalDate.parse(fecha);
        List<Map<String, Object>> citas = citaRepository
            .findByEstilistaIdAndFecha(estilistaId, localDate)
            .stream()
            .map(this::citaToMap)
            .collect(Collectors.toList());
        return ResponseEntity.ok(citas);
    }

    // ── Agenda del mes — recibe estilistaId ───────────────────────────
    
    @GetMapping("/agenda/mes")
    public ResponseEntity<List<String>> agendaMes(
            @RequestParam Long estilistaId,
            @RequestParam int mes,
            @RequestParam int anio) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin    = inicio.withDayOfMonth(inicio.lengthOfMonth());
        List<String> fechas = citaRepository
            .findByEstilistaIdAndFechaBetween(estilistaId, inicio, fin)
            .stream()
            .map(c -> c.getFecha().toString())  
            .distinct()
            .collect(Collectors.toList());
        return ResponseEntity.ok(fechas);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private Map<String, Object> toResponse(Estilista estilista) {
        Map<String, Object> map = new HashMap<>();
        Usuario usuario = estilista.getUsuario();
        map.put("id",          estilista.getId());
        map.put("usuarioId",   usuario.getId());
        map.put("nombre",      usuario.getNombre());
        map.put("apellido",    usuario.getApellido());
        map.put("apellidos",   usuario.getApellido());
        map.put("correo",      usuario.getCorreo());
        map.put("especialidad",estilista.getEspecialidad());
        map.put("experiencia", estilista.getExperiencia());
        map.put("rating",      estilista.getRating());
        map.put("activo",      usuario.getActivo());
        map.put("disponible",  estilista.getDisponible());
        return map;
    }

    private Map<String, Object> citaToMap(Cita cita) {
        Map<String, Object> map = new HashMap<>();
        map.put("id",            cita.getId());
        map.put("hora",          cita.getHora().toString().substring(0, 5));
        map.put("fecha",         cita.getFecha().toString());
        map.put("estado",        cita.getEstado().name());
        map.put("servicioNombre",cita.getServicio().getNombre());
        map.put("clienteNombre", cita.getCliente().getNombre() + " " + cita.getCliente().getApellido());
        map.put("total",         cita.getTotal());
        return map;
    }
}
