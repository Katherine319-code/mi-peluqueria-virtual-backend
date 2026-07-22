package com.peluqueria.mipeluqueriavirtual.service;

import com.peluqueria.mipeluqueriavirtual.entity.Servicio;
import com.peluqueria.mipeluqueriavirtual.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    // Buscar por ID 
    @Override
    public Optional<Servicio> findById(int id) {
        return servicioRepository.findById((long) id);
    }

    // Listar todos 
    @Override
    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    // Guardar nuevo 
    @Override
    public Servicio save(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    // Eliminar 
    @Override
    public void deleteById(int id) {
        servicioRepository.deleteById((long) id);
    }

    // Actualizar 
    @Override
    public Servicio actualizar(int id, Servicio servicioNuevo) {
        // Buscar el servicio en base de datos
        Servicio servicioEnBD = servicioRepository.findById((long) id).get();

        // Fijarle los datos que vienen en la petición 
        servicioEnBD.setNombre(servicioNuevo.getNombre());
        servicioEnBD.setDescripcion(servicioNuevo.getDescripcion());
        servicioEnBD.setPrecio(servicioNuevo.getPrecio());
        servicioEnBD.setDuracionMinutos(servicioNuevo.getDuracionMinutos());
        servicioEnBD.setImagen(servicioNuevo.getImagen());
        servicioEnBD.setActivo(servicioNuevo.getActivo());

        // Guardar el servicio actualizado
        return servicioRepository.save(servicioEnBD);
    }
}
