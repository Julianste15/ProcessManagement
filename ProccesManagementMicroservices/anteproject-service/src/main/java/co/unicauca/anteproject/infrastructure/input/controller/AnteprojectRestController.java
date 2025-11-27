package co.unicauca.anteproject.infrastructure.input.controller;

import co.unicauca.anteproject.domain.model.Anteproject;
import co.unicauca.anteproject.domain.model.AnteprojectStatus;
import co.unicauca.anteproject.domain.ports.in.CreateAnteprojectUseCase;
import co.unicauca.anteproject.domain.ports.in.GetAnteprojectUseCase;
import co.unicauca.anteproject.domain.ports.in.ManageAnteprojectUseCase;
import co.unicauca.anteproject.dto.AnteprojectDTO;
import co.unicauca.anteproject.dto.CreateAnteprojectRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/anteprojects")
@CrossOrigin(origins = "*")
public class AnteprojectRestController {
    private static final Logger logger = Logger.getLogger(AnteprojectRestController.class.getName());

    private final CreateAnteprojectUseCase createAnteprojectUseCase;
    private final ManageAnteprojectUseCase manageAnteprojectUseCase;
    private final GetAnteprojectUseCase getAnteprojectUseCase;

    public AnteprojectRestController(CreateAnteprojectUseCase createAnteprojectUseCase,
                                     ManageAnteprojectUseCase manageAnteprojectUseCase,
                                     GetAnteprojectUseCase getAnteprojectUseCase) {
        this.createAnteprojectUseCase = createAnteprojectUseCase;
        this.manageAnteprojectUseCase = manageAnteprojectUseCase;
        this.getAnteprojectUseCase = getAnteprojectUseCase;
    }

    @PostMapping
    public ResponseEntity<?> createAnteproject(@Valid @RequestBody CreateAnteprojectRequest request,
                                                HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");
            logger.info("Creando anteproyecto - Usuario: " + currentUser);

            if (!"TEACHER".equals(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol TEACHER");
            }
            if (!request.getDirectorEmail().equals(currentUser)) {
                return ResponseEntity.status(403)
                        .body("Acceso denegado: Solo puede crear anteproyectos donde usted es el director");
            }

            Anteproject anteproject = createAnteprojectUseCase.createAnteproject(request);
            AnteprojectDTO dto = convertToDTO(anteproject);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.severe("Error creando anteproyecto: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/submit-document")
    public ResponseEntity<?> submitDocument(@PathVariable Long id, @RequestParam String documentUrl,
                                            HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            logger.info("Subiendo documento para anteproyecto " + id + " - Usuario: " + currentUser);

            Anteproject anteproject = manageAnteprojectUseCase.submitDocument(id, documentUrl, currentUser);
            AnteprojectDTO dto = convertToDTO(anteproject);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.severe("Error subiendo documento: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/assign-evaluators")
    public ResponseEntity<?> assignEvaluators(@PathVariable Long id,
                                               @RequestParam String evaluator1Email,
                                               @RequestParam String evaluator2Email,
                                               HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");
            String isDeptHead = httpRequest.getHeader("X-User-IsDepartmentHead");

            logger.info("Asignando evaluadores - Usuario: " + currentUser + ", Rol: " + userRole + ", Es Jefe: " + isDeptHead);

            boolean isCoordinator = "COORDINATOR".equals(userRole);
            boolean isDepartmentHead = "TEACHER".equals(userRole) && "true".equalsIgnoreCase(isDeptHead);

            if (!isCoordinator && !isDepartmentHead) {
                return ResponseEntity.status(403)
                        .body("Acceso denegado: Se requiere ser Coordinador o Jefe de Departamento");
            }

            Anteproject anteproject = manageAnteprojectUseCase.assignEvaluators(id, evaluator1Email, evaluator2Email, currentUser);
            AnteprojectDTO dto = convertToDTO(anteproject);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.severe("Error asignando evaluadores: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnteprojectById(@PathVariable Long id) {
        try {
            Anteproject anteproject = getAnteprojectUseCase.getAnteprojectById(id);
            AnteprojectDTO dto = convertToDTO(anteproject);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.severe("Error obteniendo anteproyecto: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/student/{email}")
    public ResponseEntity<?> getAnteprojectsByStudent(@PathVariable String email) {
        try {
            List<Anteproject> anteprojects = getAnteprojectUseCase.getAnteprojectsByStudent(email);
            List<AnteprojectDTO> dtos = anteprojects.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            logger.severe("Error obteniendo anteproyectos del estudiante: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/director/{email}")
    public ResponseEntity<?> getAnteprojectsByDirector(@PathVariable String email) {
        try {
            List<Anteproject> anteprojects = getAnteprojectUseCase.getAnteprojectsByDirector(email);
            List<AnteprojectDTO> dtos = anteprojects.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            logger.severe("Error obteniendo anteproyectos del director: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/submitted")
    public ResponseEntity<?> getSubmittedAnteprojects(HttpServletRequest httpRequest) {
        try {
            String userRole = httpRequest.getHeader("X-User-Role");
            String isDeptHead = httpRequest.getHeader("X-User-IsDepartmentHead");

            boolean isCoordinator = "COORDINATOR".equals(userRole);
            boolean isDepartmentHead = "TEACHER".equals(userRole) && "true".equalsIgnoreCase(isDeptHead);

            if (!isCoordinator && !isDepartmentHead) {
                return ResponseEntity.status(403)
                        .body("Acceso denegado: Se requiere ser Coordinador o Jefe de Departamento");
            }

            List<Anteproject> anteprojects = getAnteprojectUseCase.getSubmittedAnteprojectsForDepartmentHead();
            List<AnteprojectDTO> dtos = anteprojects.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            logger.severe("Error obteniendo anteproyectos enviados: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestParam AnteprojectStatus status,
                                          HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            Anteproject anteproject = manageAnteprojectUseCase.updateStatus(id, status, currentUser);
            AnteprojectDTO dto = convertToDTO(anteproject);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.severe("Error actualizando estado: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    private AnteprojectDTO convertToDTO(Anteproject anteproject) {
        AnteprojectDTO dto = new AnteprojectDTO();
        dto.setId(anteproject.getId());
        dto.setFormatoAId(anteproject.getFormatoAId());
        dto.setTitulo(anteproject.getTitulo());
        dto.setStudentEmail(anteproject.getStudentEmail());
        dto.setDirectorEmail(anteproject.getDirectorEmail());
        dto.setDocumentUrl(anteproject.getDocumentUrl());
        dto.setStatus(anteproject.getStatus());
        dto.setSubmissionDate(anteproject.getSubmissionDate());
        dto.setEvaluationDeadline(anteproject.getEvaluationDeadline());
        dto.setCreatedAt(anteproject.getCreatedAt());
        return dto;
    }
}
