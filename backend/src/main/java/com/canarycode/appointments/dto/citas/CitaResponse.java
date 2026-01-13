package com.canarycode.appointments.dto.citas;

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
