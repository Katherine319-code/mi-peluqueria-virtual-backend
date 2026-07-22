package com.peluqueria.mipeluqueriavirtual.service;

import com.peluqueria.mipeluqueriavirtual.entity.Estilista;

public interface EstilistaService {

    // Login: recibe correo y contraseña, devuelve la estilista si es válida o null
    Estilista login(String correo, String contrasena);

    // Guardar nueva estilista (solo el admin)
    Estilista save(Estilista estilista);
}
