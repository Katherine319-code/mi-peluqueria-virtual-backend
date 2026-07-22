package com.peluqueria.mipeluqueriavirtual.service;

import com.peluqueria.mipeluqueriavirtual.entity.Usuario;
import com.peluqueria.mipeluqueriavirtual.entity.Rol;
import com.peluqueria.mipeluqueriavirtual.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Usuario save(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new ResponseStatusException(CONFLICT, "Ya existe un usuario con ese correo");
        }
        usuario.setRol(Rol.CLIENTE);
        usuario.setActivo(usuario.getActivo() == null || usuario.getActivo());
        if (usuario.getProveedorAuth() == null || usuario.getProveedorAuth().isBlank()) {
            usuario.setProveedorAuth("LOCAL");
        }
        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findByRol(Rol.CLIENTE);
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario no encontrado"));
    }

    @Override
    public Usuario update(Long id, Usuario usuario) {
        Usuario existente = findById(id);
        usuarioRepository.findByCorreo(usuario.getCorreo())
                .filter(usuarioCorreo -> !usuarioCorreo.getId().equals(id))
                .ifPresent(usuarioCorreo -> {
                    throw new ResponseStatusException(CONFLICT, "Ya existe un usuario con ese correo");
                });

        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setCorreo(usuario.getCorreo());
        existente.setTelefono(usuario.getTelefono());
        existente.setFotoPerfil(usuario.getFotoPerfil());
        existente.setRol(Rol.CLIENTE);
        if (usuario.getProveedorAuth() != null && !usuario.getProveedorAuth().isBlank()) {
            existente.setProveedorAuth(usuario.getProveedorAuth());
        }
        if (usuario.getActivo() != null) {
            existente.setActivo(usuario.getActivo());
        }

        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        Usuario existente = findById(id);
        usuarioRepository.delete(existente);
    }

    @Override
    public String login(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);
        if (usuario == null) return "Usuario no encontrado";
        if (!usuario.getActivo()) return "Usuario inactivo";
        if (!passwordEncoder.matches(contrasena, usuario.getPassword())) return "Contrasena incorrecta";
        return "Inicio de sesion exitoso";
    }
}
