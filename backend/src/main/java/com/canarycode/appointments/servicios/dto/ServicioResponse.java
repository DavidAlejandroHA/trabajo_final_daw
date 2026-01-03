package com.canarycode.appointments.servicios.dto;

import java.math.BigDecimal;

public record ServicioResponse(
        Long id,
        String nombre,
        int duracionMin,
        BigDecimal precio
) {}
