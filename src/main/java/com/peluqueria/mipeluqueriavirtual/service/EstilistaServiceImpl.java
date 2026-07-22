package com.peluqueria.mipeluqueriavirtual.service;
 
import com.peluqueria.mipeluqueriavirtual.entity.Estilista;
import com.peluqueria.mipeluqueriavirtual.entity.Rol;
import com.peluqueria.mipeluqueriavirtual.entity.Usuario;
import com.peluqueria.mipeluqueriavirtual.repository.EstilistaRepository;
import com.peluqueria.mipeluqueriavirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 
@Service
public class EstilistaServiceImpl implements EstilistaService {
 
    @Autowired EstilistaRepository estilistaRepository;
    @Autowired UsuarioRepository   usuarioRepository;
    @Autowired PasswordEncoder     passwordEncoder;
 
    @Override
    public Estilista login(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);
        if (usuario == null) return null;
 
     
        if (usuario.getRol() != Rol.ESTILISTA) return null;
        if (!Boolean.TRUE.equals(usuario.getActivo())) return null;
        if (!passwordEncoder.matches(contrasena, usuario.getPassword())) return null;
 
        return estilistaRepository.findByUsuarioId(usuario.getId()).orElse(null);
    }
 
    @Override
    public Estilista save(Estilista estilista) {
        return estilistaRepository.save(estilista);
    }
}
