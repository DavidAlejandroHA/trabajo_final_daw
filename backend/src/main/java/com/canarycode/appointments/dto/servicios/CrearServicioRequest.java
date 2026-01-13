package com.canarycode.appointments.dto.servicios;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CrearServicioRequest(
        @NotBlank(message = "Es obligatorio introducir el nombre del servicio") String nombre,
        @Min(value = 1, message = "La duración mínima del servicio debe ser de al menos 1 minuto") int duracionMin,
        @NotNull(message = "El obligatorio especificar un precio") BigDecimal precio
) {}
