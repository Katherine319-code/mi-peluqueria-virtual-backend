package com.peluqueria.mipeluqueriavirtual.entity;
 
import jakarta.persistence.*;
 
@Entity
@Table(name = "estilistas")
public class Estilista {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
 
    @Column(length = 100)
    private String especialidad;
 
    @Column(columnDefinition = "TEXT")
    private String descripcion;
 
    private Integer experiencia;
 
    @Column(nullable = false)
    private Double rating = 0.0;
 
    private Boolean disponible = true;
 
    public Estilista() {}
 
    // ── Getters y Setters ────────────────────────────────────────────────────
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
 
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
 
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
 
    public Integer getExperiencia() { return experiencia; }
    public void setExperiencia(Integer experiencia) { this.experiencia = experiencia; }
 
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
 
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
}