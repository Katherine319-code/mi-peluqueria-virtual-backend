package com.peluqueria.mipeluqueriavirtual.config;

import com.peluqueria.mipeluqueriavirtual.entity.Estilista;
import com.peluqueria.mipeluqueriavirtual.entity.Rol;
import com.peluqueria.mipeluqueriavirtual.entity.Servicio;
import com.peluqueria.mipeluqueriavirtual.entity.Usuario;
import com.peluqueria.mipeluqueriavirtual.repository.EstilistaRepository;
import com.peluqueria.mipeluqueriavirtual.repository.ServicioRepository;
import com.peluqueria.mipeluqueriavirtual.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            UsuarioRepository usuarioRepository,
            ServicioRepository servicioRepository,
            EstilistaRepository estilistaRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // ── Admin ────────────────────────────────────────────────────────
            if (!usuarioRepository.existsByCorreo("admin@peluqueria.com")) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("Principal");
                admin.setCorreo("admin@peluqueria.com");
                admin.setPassword(passwordEncoder.encode("Admin123*"));
                admin.setRol(Rol.ADMIN);
                admin.setActivo(true);           
                usuarioRepository.save(admin);
            }

            // ── Servicios ────────────────────────────────────────────────────
            if (servicioRepository.count() == 0) {
                crearServicio(servicioRepository, "Corte",       "Estilismo personalizado",            25000.0,  45);
                crearServicio(servicioRepository, "Manicure",    "Unas con estilo y cuidado profesional", 15000.0, 50);
                crearServicio(servicioRepository, "Barba",       "Perfilado y afeitado profesional",    18000.0,  25);
                crearServicio(servicioRepository, "Tratamiento", "Hidratacion y nutricion capilar",     80000.0,  60);
                crearServicio(servicioRepository, "Tinte",       "Tecnica y color personalizado",      150000.0, 120);
                crearServicio(servicioRepository, "Maquillaje",  "Maquillaje social y de eventos",     200000.0,  80);
            }

            // ── Estilistas ───────────────────────────────────────────────────
            if (estilistaRepository.count() == 0) {
                crearEstilista(usuarioRepository, estilistaRepository, passwordEncoder,
                        "Hannah",  "Garcia",   "hannah@peluqueria.com",  "Hannah123*",  "Corte y color");
                crearEstilista(usuarioRepository, estilistaRepository, passwordEncoder,
                        "Sofia",   "Martinez", "sofia@peluqueria.com",   "Sofia123*",   "Manicure");
                crearEstilista(usuarioRepository, estilistaRepository, passwordEncoder,
                        "Laura",   "Gomez",    "laura@peluqueria.com",   "Laura123*",   "Peinados para eventos");
                crearEstilista(usuarioRepository, estilistaRepository, passwordEncoder,
                        "Valeria", "Rios",     "valeria@peluqueria.com", "Valeria123*", "Barberia y manicure");
            }
        };
    }

    private void crearServicio(ServicioRepository repository,
                               String nombre, String descripcion,
                               Double precio, Integer duracion) {
        Servicio servicio = new Servicio();
        servicio.setNombre(nombre);
        servicio.setDescripcion(descripcion);
        servicio.setPrecio(precio);
        servicio.setDuracionMinutos(duracion);
        servicio.setActivo(true);
        repository.save(servicio);
    }

    private void crearEstilista(UsuarioRepository usuarioRepository,
                                EstilistaRepository estilistaRepository,
                                PasswordEncoder passwordEncoder,
                                String nombre, String apellido,
                                String correo, String password,
                                String especialidad) {

        Usuario usuario = usuarioRepository.findByCorreo(correo).orElseGet(() -> {
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setApellido(apellido);
            nuevo.setCorreo(correo);
            nuevo.setPassword(passwordEncoder.encode(password));
            nuevo.setRol(Rol.ESTILISTA);
            nuevo.setActivo(true);               
            return usuarioRepository.save(nuevo);
        });

        if (estilistaRepository.findByUsuarioId(usuario.getId()).isEmpty()) {
            Estilista estilista = new Estilista();
            estilista.setUsuario(usuario);
            estilista.setEspecialidad(especialidad);
            estilista.setExperiencia(3);
            estilista.setDisponible(true);
            estilistaRepository.save(estilista);
        }
    }
}
