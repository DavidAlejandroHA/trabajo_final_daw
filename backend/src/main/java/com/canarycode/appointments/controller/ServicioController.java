package com.canarycode.appointments.controller;

import com.canarycode.appointments.dto.servicios.CrearServicioRequest;
import com.canarycode.appointments.dto.servicios.ServicioResponse;
import com.canarycode.appointments.servicios.ServicioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService service;

    public ServicioController(ServicioService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServicioResponse> listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicioResponse crear(@Valid @RequestBody CrearServicioRequest req) {
        return service.crear(req);
    }
}
