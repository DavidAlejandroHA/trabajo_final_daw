package com.canarycode.appointments.dto.jwt;

public record RegisterRequest(
        String email,
        String username,
        String password
) {}
