package com.peluqueria.mipeluqueriavirtual.service;

import com.peluqueria.mipeluqueriavirtual.entity.Servicio;
import java.util.List;
import java.util.Optional;

public interface ServicioService {

    // Buscar un servicio por ID 
    Optional<Servicio> findById(int id);

    // Listar todos los servicios
    List<Servicio> findAll();

    // Guardar (crear) un servicio nuevo
    Servicio save(Servicio servicio);

    // Eliminar un servicio por ID
    void deleteById(int id);

    // Actualizar un servicio existente
    Servicio actualizar(int id, Servicio servicio);
}
