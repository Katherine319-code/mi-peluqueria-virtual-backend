package com.peluqueria.mipeluqueriavirtual.volumen;

import com.peluqueria.mipeluqueriavirtual.controller.UsuarioController;
import com.peluqueria.mipeluqueriavirtual.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsuarioControllerGuardarVolumenTest {

    @Autowired
    private UsuarioController usuarioController;
    
    // ==========================================
    // PRUEBA 5: PRUEBA DE VOLUMEN - REGISTRO MASIVO DE USUARIOS
    // ==========================================

    @Test
    public void guardarVolumen() {

        int cantidadRegistros = 1000;

        for (int i = 1; i <= cantidadRegistros; i++) {

            Usuario usuario = new Usuario();

            usuario.setNombre("Nombre" + i);
            usuario.setApellido("Apellido" + i);
            usuario.setCorreo("usuario" + i + "@gmail.com");
            usuario.setPassword("123456");
            usuario.setTelefono("300000" + i);

            usuarioController.registrarUsuario(usuario);
        }

        System.out.println("Se registraron " + cantidadRegistros + " usuarios correctamente.");
    }
}
