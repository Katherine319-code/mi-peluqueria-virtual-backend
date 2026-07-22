package com.peluqueria.mipeluqueriavirtual.service;

import com.peluqueria.mipeluqueriavirtual.entity.Usuario;
import java.util.List;

public interface UsuarioService {

    Usuario save(Usuario usuario);

    List<Usuario> findAll();

    Usuario findById(Long id);

    Usuario update(Long id, Usuario usuario);

    void delete(Long id);

    String login(String correo, String contrasena);
}
