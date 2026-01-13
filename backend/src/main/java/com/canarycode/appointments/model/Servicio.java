package com.canarycode.appointments.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Min(value = 1, message = "La duración debe ser >= 1 minuto")
    @Column(nullable = false)
    private int duracionMin;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio debe ser >= 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    public Servicio() {}

    public Servicio(String nombre, int duracionMin, BigDecimal precio) {
        this.nombre = nombre;
        this.duracionMin = duracionMin;
        this.precio = precio;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public int getDuracionMin() { return duracionMin; }
    public BigDecimal getPrecio() { return precio; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDuracionMin(int duracionMin) { this.duracionMin = duracionMin; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
}
