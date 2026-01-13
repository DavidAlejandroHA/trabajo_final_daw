package com.canarycode.appointments.dto.citas;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CrearCitaRequest(
        @NotNull(message = "Es obligatorio indicar la Id del servicio") Long servicioId,
        @NotNull(message = "Es obligatorio indicar la fecha y hora de la cita") LocalDateTime fechaHora
) {}
