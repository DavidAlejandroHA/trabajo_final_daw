package com.canarycode.appointments.controller;

import com.canarycode.appointments.citas.CitaService;
import com.canarycode.appointments.dto.citas.CrearCitaRequest;
import com.canarycode.appointments.dto.citas.CitaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService service;

    public CitaController(CitaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CitaResponse> listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CitaResponse crear(@Valid @RequestBody CrearCitaRequest req) {
        return service.crear(req);
    }
}
