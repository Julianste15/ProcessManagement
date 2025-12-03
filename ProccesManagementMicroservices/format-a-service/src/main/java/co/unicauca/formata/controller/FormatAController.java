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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/format-a")
@CrossOrigin(origins = "*")
@Tag(name = "Format A Controller", description = "API for managing Format A submissions and evaluations")
public class FormatAController {    
    private static final Logger logger = Logger.getLogger(FormatAController.class.getName());    
    @Autowired
    private FormatoAService formatoAService;    
    
    /**
     * Envía un nuevo Formato A
     */
    @Operation(summary = "Submit Format A", description = "Allows a teacher to submit a new Format A for a degree project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Format A submitted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or submission failed")
    })
    @PostMapping("/submit")
    public ResponseEntity<?> submitFormatoA(@Valid @RequestBody FormatARequest request) {
        try {
            logger.info("Recibiendo Formato A: " + request.getTitulo());            
            FormatAResponse response = formatoAService.submitFormatoA(request);            
            logger.info("Formato A enviado exitosamente: " + request.getTitulo());
            return ResponseEntity.ok(response);            
        } catch (Exception e) {
            logger.severe("Error enviando Formato A: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }    
    
    /**
     * Evalúa un Formato A (aprobado/rechazado)
     */
    @Operation(summary = "Evaluate Format A", description = "Allows a coordinator to approve or reject a Format A")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Format A evaluated successfully"),
        @ApiResponse(responseCode = "400", description = "Evaluation failed")
    })
    @PutMapping("/{id}/evaluate")
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
    @Operation(summary = "Resubmit Format A", description = "Allows a teacher to resubmit a rejected Format A (max 3 attempts)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Format A resubmitted successfully"),
        @ApiResponse(responseCode = "400", description = "Resubmission failed or max attempts reached")
    })
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
    @Operation(summary = "Get Format A by ID", description = "Retrieves a specific Format A by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Format A found"),
        @ApiResponse(responseCode = "404", description = "Format A not found")
    })
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
    @Operation(summary = "Get Formats A by User", description = "Retrieves all Format A submissions for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formats found"),
        @ApiResponse(responseCode = "400", description = "Request failed")
    })
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
    @Operation(summary = "Get Formats A by Coordinator", description = "Retrieves all Format A submissions assigned to a coordinator")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formats found"),
        @ApiResponse(responseCode = "400", description = "Request failed")
    })
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