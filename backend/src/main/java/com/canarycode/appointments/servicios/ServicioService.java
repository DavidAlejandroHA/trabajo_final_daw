package com.canarycode.appointments.servicios;

import com.canarycode.appointments.dto.servicios.CrearServicioRequest;
import com.canarycode.appointments.dto.servicios.ServicioResponse;
import com.canarycode.appointments.model.Servicio;
import com.canarycode.appointments.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository repo;

    public ServicioService(ServicioRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<ServicioResponse> listar() {
        return repo.findAll().stream()
                .map(s -> new ServicioResponse(s.getId(), s.getNombre(), s.getDuracionMin(), s.getPrecio()))
                .toList();
    }

    @Transactional
    public ServicioResponse crear(CrearServicioRequest req) {
        Servicio s = new Servicio(req.nombre(), req.duracionMin(), req.precio());
        Servicio saved = repo.save(s);
        return new ServicioResponse(saved.getId(), saved.getNombre(), saved.getDuracionMin(), saved.getPrecio());
    }
}
