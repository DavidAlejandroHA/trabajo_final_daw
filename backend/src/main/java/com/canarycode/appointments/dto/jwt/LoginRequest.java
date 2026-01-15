package com.canarycode.appointments.dto.jwt;


public record LoginRequest(
        String email,
        String password
){}