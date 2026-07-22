package com.peluqueria.mipeluqueriavirtual.volumen;

import com.peluqueria.mipeluqueriavirtual.controller.UsuarioController;
import com.peluqueria.mipeluqueriavirtual.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UsuarioControllerListarPorIdVolumenTest {

    @Autowired
    private UsuarioController usuarioController;
    
    // ==========================================
    // PRUEBA 6: PRUEBA DE VOLUMEN - CONSULTA MASIVA DE USUARIOS POR ID
    // ==========================================

    @Test
    public void listarPorIdVolumen() {

        int registrosEsperados = 1000;

        for (long i = 1; i <= registrosEsperados; i++) {

            Usuario usuario = usuarioController.buscarUsuario(i);

            assertNotNull(usuario);
        }

        System.out.println("Se consultaron " + registrosEsperados + " usuarios correctamente.");
    }
}
