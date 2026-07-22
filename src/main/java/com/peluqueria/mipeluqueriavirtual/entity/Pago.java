package com.peluqueria.mipeluqueriavirtual.entity;
 
import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "pagos")
public class Pago {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;
 
    private String referencia;
 
    @Enumerated(EnumType.STRING)
    private MetodoPago metodo;
 
    @Enumerated(EnumType.STRING)
    private EstadoPago estado = EstadoPago.PENDIENTE;
 
    @Column
    private Double monto;
 
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago = LocalDateTime.now();
 
    public enum MetodoPago { PSE, TARJETA, NEQUI, DAVIPLATA, EFECTIVO }
    public enum EstadoPago { PENDIENTE, APROBADO, RECHAZADO }
 
    public Pago() {}
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
 
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
 
    public MetodoPago getMetodo() { return metodo; }
    public void setMetodo(MetodoPago metodo) { this.metodo = metodo; }
 
    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }
 
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
 
    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
}
