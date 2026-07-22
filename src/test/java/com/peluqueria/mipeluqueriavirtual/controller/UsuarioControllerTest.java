package com.peluqueria.mipeluqueriavirtual.controller;

import com.peluqueria.mipeluqueriavirtual.entity.Usuario;
import com.peluqueria.mipeluqueriavirtual.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;
    
    // ==========================================
    // PRUEBA 1: BUSCAR USUARIO POR ID
    // ==========================================

    @Test
    public void pruebaBuscarPorId() {
        Usuario usuarioSimulado = new Usuario();
        usuarioSimulado.setId(1L);
        usuarioSimulado.setNombre("Laura");
        usuarioSimulado.setApellido("Test");
        usuarioSimulado.setCorreo("laura.test@correo.com");

        when(usuarioService.findById(1L)).thenReturn(usuarioSimulado);

        Usuario resultado = usuarioController.buscarUsuario(1L);

        assertNotNull(resultado);
        assertEquals("Laura", resultado.getNombre());
        
      // ==========================================
      // PRUEBA 2: LISTAR TODOS LOS USUARIOS
      // ==========================================        
        
    }
    @Test
    public void pruebaListarTodos() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(2L);
        usuario1.setNombre("Carlos");
        usuario1.setApellido("Cardenas");

        Usuario usuario2 = new Usuario();
        usuario2.setId(3L);
        usuario2.setNombre("Armando");
        usuario2.setApellido("Rodriguez");

        List<Usuario> listaUsuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioService.findAll()).thenReturn(listaUsuarios);

        List<Usuario> resultado = usuarioController.listarUsuarios();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(usuario1));
        assertTrue(resultado.contains(usuario2));
    }
    
    // ==========================================
    // PRUEBA 3: ELIMINAR USUARIO
    // ==========================================
    
    @Test
    public void pruebaEliminar() {
        Long id = 1L;

        usuarioController.eliminarUsuario(id);

        verify(usuarioService, times(1)).delete(id);
    }
    
    // ==========================================
    // PRUEBA 4: ACTUALIZAR USUARIO
    // ==========================================
    
    @Test
    public void pruebaActualizar() {
        Long id = 2L;
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(id);
        usuarioActualizado.setNombre("Alberto");
        usuarioActualizado.setApellido("Ramirez");

        when(usuarioService.update(eq(id), any(Usuario.class))).thenReturn(usuarioActualizado);

        Usuario resultado = usuarioController.actualizarUsuario(id, usuarioActualizado);

        assertEquals("Alberto", resultado.getNombre());
        assertEquals("Ramirez", resultado.getApellido());

        verify(usuarioService, times(1)).update(eq(id), any(Usuario.class));
    }
}