package com.canarycode.appointments.controller;

import com.canarycode.appointments.dto.jwt.LoginRequest;
import com.canarycode.appointments.dto.jwt.RegisterRequest;
import com.canarycode.appointments.dto.jwt.TokenResponse;
import com.canarycode.appointments.servicios.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    AuthService authService;

    @PostMapping("/auth/register")
    public ResponseEntity<TokenResponse> register(@RequestBody final RegisterRequest request){
        try{
            final TokenResponse token = authService.register(request);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> register(@RequestBody final LoginRequest request){
        try{
            final TokenResponse token = authService.login(request);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/auth/refresh")
    public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader){
        return authService.refreshToken(authHeader);
    }


    @GetMapping("/admin")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Hola desde el Admin dashboard");
    }

}
