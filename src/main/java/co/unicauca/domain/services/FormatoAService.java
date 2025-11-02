/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.domain.services;

import co.unicauca.domain.entities.EstadoProyecto;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.domain.ports.IFormatoAEventPublisher;
import co.unicauca.domain.repositories.IFormatoARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

/**
 *
 * @author AN515-54-55MX
 */
@Service
public class FormatoAService {
    private final IFormatoARepository repository;
    private final IFormatoAEventPublisher eventPublisher;
    
    @Autowired
    public FormatoAService(IFormatoARepository repository, IFormatoAEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Registra un nuevo Formato A (Proyecto de Grado) en el sistema.
     * Guarda el proyecto y publica un evento asíncrono.
     *
     * @param proyecto Proyecto de grado a registrar.
     */
    public void registrarFormatoA(FormatoA proyecto) {
        proyecto.setFechaCreacion(LocalDate.now());
        proyecto.setEstado(EstadoProyecto.FORMATO_A_EN_EVALUACION);

        repository.save(proyecto);

        // Publicar evento asíncrono (no sabe si es RabbitMQ o email)
        eventPublisher.publishFormatoAEnviado(proyecto);
    }

    /**
     * Actualiza el estado del Formato A (aceptado o rechazado).
     *
     * @param titulo título del proyecto
     * @param nuevoEstado nuevo estado a asignar
     */
    public void actualizarEstado(String titulo, EstadoProyecto nuevoEstado) {
        Optional<FormatoA> optional = repository.findById(titulo);
        if (optional.isPresent()) {
            FormatoA proyecto = optional.get();
            proyecto.setEstado(nuevoEstado);
            repository.update(proyecto);
        } else {
            throw new IllegalArgumentException("No se encontró el proyecto con título: " + titulo);
        }
    }

    /**
     * Permite subir una nueva versión del Formato A si fue rechazado.
     */
    public void reintentarEnvio(String titulo, String nuevoArchivoPDF) {
        Optional<FormatoA> optional = repository.findById(titulo);
        if (optional.isPresent()) {
            FormatoA proyecto = optional.get();

            if (proyecto.getIntentos() >= 3) {
                proyecto.setEstado(EstadoProyecto.FORMATO_A_RECHAZADO);
                repository.update(proyecto);
                return;
            }

            proyecto.incrementarIntentos();
            proyecto.setArchivoPDF(nuevoArchivoPDF);
            proyecto.setFechaCreacion(LocalDate.now());
            proyecto.setEstado(EstadoProyecto.FORMATO_A_EN_EVALUACION);

            repository.update(proyecto);
            eventPublisher.publishFormatoAEnviado(proyecto);
        } else {
            throw new IllegalArgumentException("No se encontró el proyecto con título: " + titulo);
        }
    }
}
