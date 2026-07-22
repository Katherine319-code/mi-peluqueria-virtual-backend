package com.peluqueria.mipeluqueriavirtual.controller;

import com.peluqueria.mipeluqueriavirtual.entity.Usuario;
import com.peluqueria.mipeluqueriavirtual.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public Usuario buscarUsuario(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.update(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.delete(id);
    }
}


