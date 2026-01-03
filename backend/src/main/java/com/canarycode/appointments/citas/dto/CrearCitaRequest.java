package com.canarycode.appointments.citas.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CrearCitaRequest(
        @NotNull(message = "servicioId obligatorio") Long servicioId,
        @NotNull(message = "fechaHora obligatoria") LocalDateTime fechaHora
) {}
