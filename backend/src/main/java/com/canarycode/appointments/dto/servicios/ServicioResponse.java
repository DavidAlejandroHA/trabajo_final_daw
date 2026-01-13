package com.canarycode.appointments.dto.servicios;

import java.math.BigDecimal;

public record ServicioResponse(
        Long id,
        String nombre,
        int duracionMin,
        BigDecimal precio
) {}
