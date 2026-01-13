package com.canarycode.appointments.citas;

import com.canarycode.appointments.dto.citas.CrearCitaRequest;
import com.canarycode.appointments.dto.citas.CitaResponse;
import com.canarycode.appointments.config.ApiExceptionHandler.NotFoundException;
import com.canarycode.appointments.model.Cita;
import com.canarycode.appointments.model.Servicio;
import com.canarycode.appointments.repository.CitaRepository;
import com.canarycode.appointments.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citasRepo;
    private final ServicioRepository serviciosRepo;

    public CitaService(CitaRepository citasRepo, ServicioRepository serviciosRepo) {
        this.citasRepo = citasRepo;
        this.serviciosRepo = serviciosRepo;
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        return citasRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CitaResponse crear(CrearCitaRequest req) {

        if (req.fechaHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede crear una cita en el pasado");
        }

        Servicio servicio = serviciosRepo.findById(req.servicioId())
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado con id=" + req.servicioId()));

        Cita cita = new Cita(req.fechaHora(), servicio);
        Cita saved = citasRepo.save(cita);

        return toResponse(saved);
    }

    private CitaResponse toResponse(Cita c) {
        Servicio s = c.getServicio();
        return new CitaResponse(
                c.getId(),
                c.getFechaHora(),
                s.getId(),
                s.getNombre(),
                s.getDuracionMin(),
                s.getPrecio()
        );
    }
}
