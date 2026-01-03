package com.canarycode.appointments.servicios.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CrearServicioRequest(
        @NotBlank(message = "nombre obligatorio") String nombre,
        @Min(value = 1, message = "duracionMin debe ser >= 1") int duracionMin,
        @NotNull(message = "precio obligatorio") BigDecimal precio
) {}
