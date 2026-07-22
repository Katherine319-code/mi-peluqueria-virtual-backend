package com.peluqueria.mipeluqueriavirtual.dto;
 
public class AuthResponse {
    private String token;
    private String correo;
    private String nombre;
    private String apellido;
    private String rol;
    private Long id;
 
    public AuthResponse(String token, Long id, String correo, String nombre, String apellido, String rol) {
        this.token = token;
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
    }
 
    public String getToken() { return token; }
    public Long getId() { return id; }
    public String getCorreo() { return correo; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getRol() { return rol; }
}