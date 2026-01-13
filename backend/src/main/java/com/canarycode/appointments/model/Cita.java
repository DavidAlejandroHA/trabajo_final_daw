package com.canarycode.appointments.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    public Cita() {}

    public Cita(LocalDateTime fechaHora, Servicio servicio) {
        this.fechaHora = fechaHora;
        this.servicio = servicio;
    }

    public Long getId() { return id; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public Servicio getServicio() { return servicio; }

    public void setId(Long id) { this.id = id; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
}
