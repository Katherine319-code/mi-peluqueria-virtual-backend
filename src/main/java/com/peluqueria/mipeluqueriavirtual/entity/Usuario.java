package com.peluqueria.mipeluqueriavirtual.entity;
 
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "usuarios")
public class Usuario {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, length = 100)
    private String nombre;
 
    @Column(nullable = false, length = 100)
    private String apellido;
 
    @Column(nullable = false, unique = true, length = 150)
    private String correo;
 
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
 
    @Column(length = 20)
    private String telefono;
    
    @Column(name = "whatsapp", length = 20)
    private String whatsapp;
 
    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;
 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.CLIENTE;
 
    @Column(name = "proveedor_auth", length = 20)
    private String proveedorAuth = "LOCAL";
 
    @Column(nullable = false)
    private Boolean activo = true;
 
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
 
    public Usuario() {}
 
    // ── Getters y Setters ────────────────────────────────────────────────────
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
 
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
 
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
 
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
 
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
 
    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
 
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
 
    public String getProveedorAuth() { return proveedorAuth; }
    public void setProveedorAuth(String proveedorAuth) { this.proveedorAuth = proveedorAuth; }
 
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
 
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
