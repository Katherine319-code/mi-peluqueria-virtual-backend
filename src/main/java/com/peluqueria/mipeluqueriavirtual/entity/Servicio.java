package com.peluqueria.mipeluqueriavirtual.entity;
 
import jakarta.persistence.*;
 
@Entity
@Table(name = "servicios")
public class Servicio {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, length = 100)
    private String nombre;
 
    @Column(columnDefinition = "TEXT")
    private String descripcion;
 
    @Column(nullable = false)
    private Double precio;
 
    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;
 
    @Column(columnDefinition = "TEXT")
    private String imagen;
 
    private Boolean activo = true;
 
    public Servicio() {}
 
    // ── Getters y Setters ────────────────────────────────────────────────────
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
 
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
 
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
 
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
 
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
 
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}

