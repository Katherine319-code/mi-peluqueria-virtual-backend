package com.peluqueria.mipeluqueriavirtual.service;

import com.peluqueria.mipeluqueriavirtual.entity.Cita;
import java.util.List;
import java.util.Optional;


public interface CitaService {

    // Buscar una cita por ID
    Optional<Cita> findById(int id);

    // Listar todas las citas
    List<Cita> findAll();

    // Guardar (agendar) una nueva cita
    Cita save(Cita cita);

    // Eliminar (cancelar) una cita por ID
    void deleteById(int id);

    // Actualizar una cita existente
    Cita actualizar(int id, Cita cita);
    
    
}
