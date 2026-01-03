package com.canarycode.appointments.citas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CitaResponse(
        Long id,
        LocalDateTime fechaHora,
        Long servicioId,
        String servicioNombre,
        int servicioDuracionMin,
        BigDecimal servicioPrecio
) {}
