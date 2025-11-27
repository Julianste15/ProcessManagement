package co.unicauca.formata.controller;

import co.unicauca.formata.dto.FormatARequest;
import co.unicauca.formata.dto.FormatAResponse;
import co.unicauca.formata.dto.EvaluationRequest;
import co.unicauca.formata.service.FormatoAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/format-a")
@CrossOrigin(origins = "*")
public class FormatAController {
    private static final Logger logger = Logger.getLogger(FormatAController.class.getName());
    @Autowired
    private FormatoAService formatoAService;

    /**
     * Envía un nuevo Formato A
     */
    @PostMapping("/submit")
    public ResponseEntity<?> evaluateFormatoA(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationRequest request) {
        try {
            logger.info("Evaluando Formato A ID: " + id + " - Estado: " + request.getEstado());
            FormatAResponse response = formatoAService.evaluateFormatoA(id, request);
            logger.info("Formato A evaluado: " + id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error evaluando Formato A: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Reintenta enviar un Formato A rechazado
     */
    @PutMapping("/{id}/resubmit")
    public ResponseEntity<?> resubmitFormatoA(
            @PathVariable Long id,
            @RequestBody FormatARequest request) {
        try {
            logger.info("Reintentando Formato A ID: " + id);
            FormatAResponse response = formatoAService.resubmitFormatoA(id, request);
            logger.info("Formato A reintentado: " + id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error reintentando Formato A: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtiene Formato A por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFormatoAById(@PathVariable Long id) {
        try {
            FormatAResponse response = formatoAService.getFormatoAById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene Formatos A por usuario
     */
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<?> getFormatosAByUser(@PathVariable String userEmail) {
        try {
            List<FormatAResponse> responses = formatoAService.getFormatosAByUser(userEmail);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtiene Formatos A por coordinador
     */
    @GetMapping("/coordinator/{coordinatorEmail}")
    public ResponseEntity<?> getFormatosAByCoordinator(@PathVariable String coordinatorEmail) {
        try {
            List<FormatAResponse> responses = formatoAService.getFormatosAByCoordinator(coordinatorEmail);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}