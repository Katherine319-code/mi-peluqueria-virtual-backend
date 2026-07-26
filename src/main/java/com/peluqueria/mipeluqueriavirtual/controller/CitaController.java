package com.peluqueria.mipeluqueriavirtual.controller;
 
import com.peluqueria.mipeluqueriavirtual.entity.*;
import com.peluqueria.mipeluqueriavirtual.repository.*;
import com.peluqueria.mipeluqueriavirtual.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/citas")
public class CitaController {
 
    @Autowired private CitaRepository citaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private EstilistaRepository estilistaRepository;
    @Autowired private ServicioRepository servicioRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private EmailService emailService;

    @GetMapping
    public List<Map<String, Object>> listar() {
        return citaRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }
 
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        try {
            Long clienteId   = Long.parseLong(body.get("clienteId").toString());
            Long estilistaId = Long.parseLong(body.get("estilistaId").toString());
            Long servicioId  = Long.parseLong(body.get("servicioId").toString());
            String fechaStr  = body.get("fecha").toString();
            String horaStr   = body.get("hora").toString();
 
            Usuario cliente   = usuarioRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            Estilista estilista = estilistaRepository.findById(estilistaId)
                    .orElseThrow(() -> new RuntimeException("Estilista no encontrada"));
            Servicio servicio  = servicioRepository.findById(servicioId)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
 
            Cita cita = new Cita();
            cita.setCliente(cliente);
            cita.setEstilista(estilista);
            cita.setServicio(servicio);
            cita.setFecha(LocalDate.parse(fechaStr));
            cita.setHora(LocalTime.parse(horaStr));
            cita.setEstado(Cita.EstadoCita.CONFIRMADA);
            cita.setTotal(servicio.getPrecio());
            cita.setPagada("PSE".equalsIgnoreCase(String.valueOf(body.get("metodoPago"))));
 
            if (body.get("notas") != null) cita.setNotas(body.get("notas").toString());
            if (body.get("total") != null)
                cita.setTotal(Double.parseDouble(body.get("total").toString()));
 
            Cita guardada = citaRepository.save(cita);

            Pago pago = new Pago();
            pago.setCita(guardada);
            pago.setMetodo(Pago.MetodoPago.valueOf(String.valueOf(body.getOrDefault("metodoPago", "EFECTIVO")).toUpperCase()));
            pago.setEstado(cita.getPagada() ? Pago.EstadoPago.APROBADO : Pago.EstadoPago.PENDIENTE);
            pago.setMonto(guardada.getTotal());
            pago.setReferencia("CITA-" + guardada.getId());
            pagoRepository.save(pago);

            emailService.enviarConfirmacionCita(guardada);

            return ResponseEntity.ok(toResponse(guardada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear cita: " + e.getMessage());
        }
    }
 
    @GetMapping("/cliente/{clienteId}")
    public List<Map<String, Object>> porCliente(@PathVariable Long clienteId) {
        return citaRepository.findByClienteId(clienteId).stream().map(this::toResponse).collect(Collectors.toList());
    }
 
    @GetMapping("/estilista/{estilistaId}")
    public List<Map<String, Object>> porEstilista(@PathVariable Long estilistaId) {
        return citaRepository.findByEstilistaId(estilistaId).stream().map(this::toResponse).collect(Collectors.toList());
    }
 
    @GetMapping("/estilista/{estilistaId}/dia")
    public List<Map<String, Object>> agendaDia(
            @PathVariable Long estilistaId,
            @RequestParam String fecha) {
        return citaRepository.findByEstilistaIdAndFecha(estilistaId, LocalDate.parse(fecha))
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
 
    @GetMapping("/estilista/{estilistaId}/mes")
    public List<Map<String, Object>> agendaMes(
            @PathVariable Long estilistaId,
            @RequestParam int anio,
            @RequestParam int mes) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin    = inicio.withDayOfMonth(inicio.lengthOfMonth());
        return citaRepository.findByEstilistaIdAndFechaBetween(estilistaId, inicio, fin)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
 
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return citaRepository.findById(id).map(cita -> {
            cita.setEstado(Cita.EstadoCita.valueOf(body.get("estado")));
            return ResponseEntity.ok(toResponse(citaRepository.save(cita)));
        }).orElse(ResponseEntity.notFound().build());
    }
 
    // DELETE /api/citas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        citaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private Map<String, Object> toResponse(Cita cita) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", cita.getId());
        map.put("clienteId", cita.getCliente().getId());
        map.put("clienteNombre", cita.getCliente().getNombre() + " " + cita.getCliente().getApellido());
        map.put("estilistaId", cita.getEstilista().getId());
        map.put("estilistaNombre", cita.getEstilista().getUsuario().getNombre() + " " + cita.getEstilista().getUsuario().getApellido());
        map.put("servicioId", cita.getServicio().getId());
        map.put("servicioNombre", cita.getServicio().getNombre());
        map.put("servicioPrecio", cita.getServicio().getPrecio());
        map.put("servicioDuracion", cita.getServicio().getDuracionMinutos());
        map.put("fecha", cita.getFecha().toString());
        map.put("hora", cita.getHora().toString());
        map.put("estado", cita.getEstado().name());
        map.put("total", cita.getTotal());
        map.put("pagada", cita.getPagada());
        return map;
    }
}
