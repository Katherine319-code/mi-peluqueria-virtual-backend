package com.peluqueria.mipeluqueriavirtual.controller;

import com.peluqueria.mipeluqueriavirtual.entity.Servicio;
import com.peluqueria.mipeluqueriavirtual.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepository;

    // GET /api/servicios — todos los activos
    @GetMapping
    public List<Servicio> listar() {
        return servicioRepository.findByActivoTrue();
    }

    // GET /api/servicios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtener(@PathVariable Long id) {
        return servicioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/servicios (admin)
    @PostMapping
    public Servicio crear(@RequestBody Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    // PUT /api/servicios/{id} (admin)
    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizar(@PathVariable Long id, @RequestBody Servicio servicio) {
        return servicioRepository.findById(id).map(s -> {
            s.setNombre(servicio.getNombre());
            s.setDescripcion(servicio.getDescripcion());
            s.setPrecio(servicio.getPrecio());
            s.setDuracionMinutos(servicio.getDuracionMinutos());
            s.setImagen(servicio.getImagen());
            return ResponseEntity.ok(servicioRepository.save(s));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/servicios/{id}/desactivar (admin) — "eliminar" logico
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Servicio> desactivar(@PathVariable Long id) {
        return servicioRepository.findById(id).map(s -> {
            s.setActivo(false);
            return ResponseEntity.ok(servicioRepository.save(s));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/servicios/{id} (admin) — borrado permanente, usar con cuidado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}