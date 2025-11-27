package co.unicauca.formata.service;

import co.unicauca.formata.dto.FormatARequest;
import co.unicauca.formata.dto.FormatAResponse;
import co.unicauca.formata.dto.EvaluationRequest;
import co.unicauca.formata.model.FormatoA;
import co.unicauca.formata.model.EstadoProyecto;
import co.unicauca.formata.model.Modalidad;
import co.unicauca.formata.repository.FormatARepository;
import co.unicauca.formata.events.FormatAEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import co.unicauca.formata.exceptions.FormatoAException;

@Service
public class FormatoAService {
    private static final Logger logger = Logger.getLogger(FormatoAService.class.getName());
    @Autowired
    private FormatARepository formatoARepository;
    @Autowired
    private FormatAEventPublisher eventPublisher;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${user.service.url:http://user-service/api/users}")
    private String userServiceUrl;
    @Value("${format-a.storage.path:./storage/formata}")
    private String storagePath;
    @Value("${format-a.storage.max-size-bytes:5242880}")
    private long maxPdfSizeBytes;

    /**
     * Envía un nuevo Formato A
     */
    /**
     * Envía un nuevo Formato A
     */
    public FormatAResponse submitFormatoA(FormatARequest request) throws FormatoAException {
        logger.info("Procesando envío de Formato A: " + request.getTitulo());

        // Regla de negocio: Validar si el estudiante puede crear un nuevo formato
        if (!canStudentCreateFormatA(request.getStudentEmail())) {
            throw new FormatoAException(
                    "El estudiante ya tiene un Formato A activo o con menos de 3 rechazos. No puede crear otro.");
        }

        validateUserExists(request.getDirectorEmail());
        if (request.getCodirectorEmail() != null && !request.getCodirectorEmail().isEmpty()) {
            validateUserExists(request.getCodirectorEmail());
        }
        String archivoPdf = resolvePdfPath(request, null);
        String cartaAceptacion = null;
        if (request.getModalidad() == Modalidad.PRACTICA_PROFESIONAL) {
            if (request.getCartaAceptacionEmpresaContenido() == null
                    || request.getCartaAceptacionEmpresaContenido().isBlank()) {
                throw new FormatoAException(
                        "Para la modalidad de Práctica Profesional, la carta de aceptación de la empresa es obligatoria.");
            }
            cartaAceptacion = resolveAcceptanceLetterPath(request);
        }
        FormatoA formatoA = new FormatoA(
                request.getTitulo(),
                request.getModalidad(),
                LocalDate.now(),
                request.getDirectorEmail(),
                request.getCodirectorEmail(),
                request.getStudentEmail(),
                request.getObjetivoGeneral(),
                request.getObjetivosEspecificos(),
                archivoPdf,
                cartaAceptacion);

        // Inicializar intentos
        formatoA.setIntentos(1);

        FormatoA savedFormatoA = formatoARepository.save(formatoA);
        eventPublisher.publishFormatoAEnviado(savedFormatoA);
        logger.info("Formato A guardado con ID: " + savedFormatoA.getId());
        return convertToResponse(savedFormatoA);
    }

    /**
     * Regla de negocio:
     * - Un estudiante solo puede tener un Formato A activo.
     * - Puede crear otro solo si el último Formato A fue RECHAZADO
     * y ya alcanzó el 3er rechazo (intentos >= 3).
     */
    public boolean canStudentCreateFormatA(String studentEmail) {
        List<FormatoA> formatos = formatoARepository.findByStudentEmailOrderByFechaCreacionDesc(studentEmail);

        if (formatos.isEmpty()) {
            // Nunca ha creado un Formato A -> permitido
            return true;
        }

        FormatoA ultimo = formatos.get(0);

        // Si el último NO está rechazado -> aún tiene un Formato A activo
        if (ultimo.getEstado() != EstadoProyecto.FORMATO_A_RECHAZADO) {
            return false;
        }

        // Si está rechazado pero tiene menos de 3 intentos -> no puede crear otro
        Integer intentos = ultimo.getIntentos();
        if (intentos == null) {
            intentos = 0;
        }

        return intentos >= 3;
    }

    /**
     * Evalúa un Formato A
     */
    public FormatAResponse evaluateFormatoA(Long id, EvaluationRequest request) {
        logger.info("Evaluando Formato A ID: " + id);
        FormatoA formatoA = formatoARepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formato A no encontrado con ID: " + id));
        formatoA.setEstado(request.getEstado());
        formatoA.setObservaciones(request.getObservaciones());
        FormatoA updatedFormatoA = formatoARepository.save(formatoA);
        if (request.getEstado() == EstadoProyecto.FORMATO_A_ACEPTADO) {
            assignEvaluatorsForAnteproyecto(updatedFormatoA);
        }
        eventPublisher.publishFormatoAEvaluado(updatedFormatoA, request.getObservaciones());
        logger.info("Formato A evaluado: " + id + " - Estado: " + request.getEstado());
        return convertToResponse(updatedFormatoA);
    }

    /**
     * Asigna evaluadores automáticamente cuando un Formato A es aprobado
     */
    private void assignEvaluatorsForAnteproyecto(FormatoA formatoA) {
        try {
            logger.info("Asignando evaluadores automáticamente para Formato A: " + formatoA.getId());
            String evaluator1Email = findEvaluatorBySpecialty(formatoA.getModalidad());
            String evaluator2Email = findSecondEvaluator(evaluator1Email);
            Map<String, Object> request = new HashMap<>();
            request.put("projectId", formatoA.getId());
            request.put("evaluator1Email", evaluator1Email);
            request.put("evaluator2Email", evaluator2Email);
            request.put("assignedBy", "sistema_format_a");
            logger.info("Evaluadores asignados exitosamente para Formato A: " + formatoA.getId());
            logger.info("Evaluador 1: " + evaluator1Email);
            logger.info("Evaluador 2: " + evaluator2Email);
        } catch (Exception e) {
            logger.severe("Error asignando evaluadores automáticamente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Encuentra un evaluador por especialidad
     */
    private String findEvaluatorBySpecialty(Modalidad modalidad) {
        if (modalidad == Modalidad.INVESTIGACION) {
            return "evaluador.investigacion@unicauca.edu.co";
        } else {
            return "evaluador.practica@unicauca.edu.co";
        }
    }

    /**
     * Encuentra un segundo evaluador diferente
     */
    private String findSecondEvaluator(String firstEvaluator) {
        if ("evaluador.investigacion@unicauca.edu.co".equals(firstEvaluator)) {
            return "evaluador.sistemas@unicauca.edu.co";
        } else {
            return "evaluador.investigacion@unicauca.edu.co";
        }
    }

    /**
     * Reintenta enviar un Formato A rechazado
     */
    public FormatAResponse resubmitFormatoA(Long id, FormatARequest request) {
        logger.info("Reintentando Formato A ID: " + id);
        FormatoA formatoA = formatoARepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formato A no encontrado con ID: " + id));
        if (formatoA.getIntentos() >= 3) {
            formatoA.setEstado(EstadoProyecto.FORMATO_A_RECHAZADO);
            formatoARepository.save(formatoA);
            throw new RuntimeException("Máximo de intentos alcanzado (3). No se puede reintentar.");
        }
        formatoA.setTitulo(request.getTitulo());
        formatoA.setModalidad(request.getModalidad());
        formatoA.setDirectorEmail(request.getDirectorEmail());
        formatoA.setCodirectorEmail(request.getCodirectorEmail());
        formatoA.setStudentEmail(request.getStudentEmail());
        formatoA.setObjetivoGeneral(request.getObjetivoGeneral());
        formatoA.setObjetivosEspecificos(request.getObjetivosEspecificos());
        formatoA.setArchivoPDF(resolvePdfPath(request, formatoA.getArchivoPDF()));
        formatoA.setFechaCreacion(LocalDate.now());
        formatoA.setEstado(EstadoProyecto.FORMATO_A_EN_EVALUACION);
        formatoA.incrementarIntentos();
        FormatoA updatedFormatoA = formatoARepository.save(formatoA);
        eventPublisher.publishFormatoAReintentado(updatedFormatoA);
        logger.info("Formato A reintentado: " + id + " - Intento: " + updatedFormatoA.getIntentos());
        return convertToResponse(updatedFormatoA);
    }

    /**
     * Obtiene Formato A por ID
     */
    public FormatAResponse getFormatoAById(Long id) {
        FormatoA formatoA = formatoARepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formato A no encontrado con ID: " + id));
        return convertToResponse(formatoA);
    }

    /**
     * Obtiene Formatos A por usuario (director o codirector)
     */
    public List<FormatAResponse> getFormatosAByUser(String userEmail) {
        List<FormatoA> formatos = formatoARepository.findByDirectorEmailOrCodirectorEmailOrStudentEmail(userEmail,
                userEmail, userEmail);
        return formatos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene Formatos A pendientes de evaluación (para coordinadores)
     */
    public List<FormatAResponse> getFormatosAByCoordinator(String coordinatorEmail) {
        List<FormatoA> formatos = formatoARepository.findByEstado(EstadoProyecto.FORMATO_A_EN_EVALUACION);
        return formatos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Valida que un usuario existe llamando al user-service
     */
    private void validateUserExists(String email) {
        try {
            Object response = restTemplate.getForObject(
                    userServiceUrl + "/" + email,
                    Object.class);
            if (response == null) {
                throw new RuntimeException("Usuario no encontrado: " + email);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error validando usuario: " + email + " - " + e.getMessage());
        }
    }

    private FormatAResponse convertToResponse(FormatoA formatoA) {
        FormatAResponse response = new FormatAResponse();
        response.setId(formatoA.getId());
        response.setTitulo(formatoA.getTitulo());
        response.setModalidad(formatoA.getModalidad());
        response.setFechaCreacion(formatoA.getFechaCreacion());
        response.setDirectorEmail(formatoA.getDirectorEmail());
        response.setCodirectorEmail(formatoA.getCodirectorEmail());
        response.setObjetivoGeneral(formatoA.getObjetivoGeneral());
        response.setObjetivosEspecificos(formatoA.getObjetivosEspecificos());
        response.setArchivoPDF(formatoA.getArchivoPDF());
        response.setEstado(formatoA.getEstado());
        response.setCartaAceptacionEmpresa(formatoA.getCartaAceptacionEmpresa());
        response.setIntentos(formatoA.getIntentos());
        response.setStudentEmail(formatoA.getStudentEmail());
        response.setObservaciones(formatoA.getObservaciones());
        return response;
    }

    private String resolvePdfPath(FormatARequest request, String currentPath) {
        if (request == null) {
            return currentPath;
        }
        if (hasEmbeddedPdf(request)) {
            return storePdfFile(request.getArchivoPdfNombre(), request.getArchivoPdfContenido());
        }
        if (request.getArchivoPDF() != null && !request.getArchivoPDF().isBlank()) {
            return request.getArchivoPDF();
        }
        return currentPath;
    }

    private boolean hasEmbeddedPdf(FormatARequest request) {
        return request.getArchivoPdfContenido() != null && !request.getArchivoPdfContenido().isBlank();
    }

    private String storePdfFile(String originalName, String base64Content) {
        try {
            byte[] pdfBytes = Base64.getDecoder().decode(base64Content);
            if (pdfBytes.length == 0) {
                throw new RuntimeException("El archivo PDF está vacío.");
            }
            if (pdfBytes.length > maxPdfSizeBytes) {
                throw new RuntimeException("El archivo PDF supera el tamaño máximo permitido ("
                        + (maxPdfSizeBytes / (1024 * 1024)) + " MB).");
            }
            Path directory = Paths.get(storagePath).toAbsolutePath();
            Files.createDirectories(directory);
            String sanitizedName = sanitizeFileName(Optional.ofNullable(originalName).orElse("formato_a.pdf"));
            if (!sanitizedName.toLowerCase().endsWith(".pdf")) {
                sanitizedName = sanitizedName + ".pdf";
            }
            String uniqueName = System.currentTimeMillis() + "_" + sanitizedName;
            Path destination = directory.resolve(uniqueName);
            Files.write(destination, pdfBytes);
            return destination.toString();
        } catch (IllegalArgumentException e) {
            logger.severe("Error decodificando Base64 del PDF: " + e.getMessage());
            throw new RuntimeException("El contenido del archivo PDF es inválido.");
        } catch (IOException e) {
            logger.severe("Error IO almacenando PDF: " + e.getMessage());
            throw new RuntimeException("No se pudo almacenar el PDF adjunto. Verifique permisos o espacio en disco.");
        }
    }

    private String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    private String resolveAcceptanceLetterPath(FormatARequest request) {
        if (request.getCartaAceptacionEmpresaContenido() != null
                && !request.getCartaAceptacionEmpresaContenido().isBlank()) {
            return storePdfFile(request.getCartaAceptacionEmpresaNombre(),
                    request.getCartaAceptacionEmpresaContenido());
        }
        return null;
    }
}
