package com.peluqueria.mipeluqueriavirtual.service;

import com.peluqueria.mipeluqueriavirtual.entity.Cita;
import com.peluqueria.mipeluqueriavirtual.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    // Buscar por ID → usa findById de JPA

    @Override
    public Optional<Cita> findById(int id) {
        return citaRepository.findById((long) id);
    }

    // Listar todas

    @Override
    public List<Cita> findAll() {
        return citaRepository.findAll();
    }


    @Override
    public Cita save(Cita cita) {
    	cita.setEstado(Cita.EstadoCita.CONFIRMADA);
        return citaRepository.save(cita);
    }

    // Eliminar 
    @Override
    public void deleteById(int id) {
        citaRepository.deleteById((long) id);
    }

    // Actualizar
    @Override
    public Cita actualizar(int id, Cita citaNueva) {
        // Buscar la cita en base de datos
        Cita citaEnBD = citaRepository.findById((long) id).get();

        // Fijarle los datos que vienen en la petición 
        citaEnBD.setServicio(citaNueva.getServicio());
        citaEnBD.setEstilista(citaNueva.getEstilista());
        citaEnBD.setCliente(citaNueva.getCliente());
        citaEnBD.setFecha(citaNueva.getFecha());
        citaEnBD.setHora(citaNueva.getHora());
        citaEnBD.setTotal(citaNueva.getTotal());
        citaEnBD.setPagada(citaNueva.getPagada());
        citaEnBD.setNotas(citaNueva.getNotas());
        citaEnBD.setEstado(citaNueva.getEstado());

        // Guardar la cita actualizada
        return citaRepository.save(citaEnBD);
    }
}
