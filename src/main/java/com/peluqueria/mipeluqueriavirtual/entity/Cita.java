package com.peluqueria.mipeluqueriavirtual.entity;
 
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
 
@Entity
@Table(name = "citas")
public class Cita {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;
 
    @ManyToOne
    @JoinColumn(name = "estilista_id", nullable = false)
    private Estilista estilista;
 
    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;
 
    @Column(nullable = false)
    private LocalDate fecha;
 
    @Column(nullable = false)
    private LocalTime hora;
 
    @Enumerated(EnumType.STRING)
    private EstadoCita estado = EstadoCita.PENDIENTE;
 
    @Column(columnDefinition = "TEXT")
    private String notas;
 
    @Column
    private Double total;
 
    private Boolean pagada = false;
 
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
 
    public Cita() {}
 
    // ── Enum estado ──────────────────────────────────────────────────────────
    public enum EstadoCita {
        PENDIENTE, CONFIRMADA, CANCELADA, FINALIZADA
    }
 
    // ── Getters y Setters ────────────────────────────────────────────────────
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }
 
    public Estilista getEstilista() { return estilista; }
    public void setEstilista(Estilista estilista) { this.estilista = estilista; }
 
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
 
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
 
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
 
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
 
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
 
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
 
    public Boolean getPagada() { return pagada; }
    public void setPagada(Boolean pagada) { this.pagada = pagada; }
 
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
