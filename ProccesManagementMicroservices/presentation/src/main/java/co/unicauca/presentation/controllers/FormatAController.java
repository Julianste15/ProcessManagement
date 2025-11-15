package co.unicauca.presentation.controllers;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Modalidad;
import co.unicauca.domain.services.FormatAService;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.views.FormatAFormView;
import co.unicauca.presentation.views.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FormatAController {
    private static final Logger logger = Logger.getLogger(FormatAController.class.getName());

    private final FormatAFormView view;
    private final Stage stage;
    private final SessionService sessionService;
    private final User user;
    private final Consumer<User> onSuccess;
    private final FormatAService formatAService;

    public FormatAController(FormatAFormView view, Stage stage, SessionService sessionService, User user, Consumer<User> onSuccess) {
        this.view = view;
        this.stage = stage;
        this.sessionService = sessionService;
        this.user = user;
        this.onSuccess = onSuccess;
        this.formatAService = new FormatAService(sessionService != null ? sessionService.getClient() : null);
    }

    public void handleSubmit() {
        view.clearError();

        try {
            String titulo = view.getTitulo();
            if (titulo.isEmpty()) {
                view.showError("El título del proyecto es obligatorio.");
                return;
            }

            Modalidad modalidad = view.getModalidad();
            if (modalidad == null) {
                view.showError("Debe seleccionar una modalidad válida.");
                return;
            }

            String objetivoGeneral = view.getObjetivoGeneral();
            if (objetivoGeneral.isEmpty()) {
                view.showError("El objetivo general es obligatorio.");
                return;
            }

            List<String> objetivosEspecificos = parseObjetivosEspecificos(view.getObjetivosEspecificos());
            if (objetivosEspecificos.isEmpty()) {
                view.showError("Debe ingresar al menos un objetivo específico.");
                return;
            }

            Map<String, Object> request = new HashMap<>();
            request.put("titulo", titulo);
            request.put("modalidad", modalidad.name());
            request.put("directorEmail", user.getEmail());

            String codirector = view.getCodirectorEmail();
            if (!codirector.isEmpty()) {
                request.put("codirectorEmail", codirector);
            }

            request.put("objetivoGeneral", objetivoGeneral);
            request.put("objetivosEspecificos", objetivosEspecificos);

            String archivoPdf = view.getArchivoPdf();
            if (!archivoPdf.isEmpty()) {
                request.put("archivoPDF", archivoPdf);
            }

            logger.info("Enviando Formato A para el docente: " + user.getEmail());
            Map<String, Object> response = formatAService.submitFormatoA(request);

            actualizarEstadoUsuario(response);

            view.showInfo("Formato A enviado exitosamente. El estado actual es: " + user.getFormatoAEstado());

            if (onSuccess != null) {
                onSuccess.accept(sessionService != null ? sessionService.getCurrentUser() : user);
            }

        } catch (IllegalStateException ex) {
            logger.severe("Error de configuración en FormatAService: " + ex.getMessage());
            view.showError("No se pudo enviar el Formato A. Verifique la configuración del cliente.");
        } catch (RuntimeException ex) {
            logger.severe("Error del microservicio FormatA: " + ex.getMessage());
            view.showError(ex.getMessage());
        } catch (Exception ex) {
            logger.severe("Error inesperado al enviar el Formato A: " + ex.getMessage());
            view.showError("Ocurrió un error al enviar el Formato A. Intente nuevamente.");
        }
    }

    public void handleLogout() {
        try {
            if (sessionService != null) {
                sessionService.logout();
            }
        } catch (Exception e) {
            logger.warning("Error al cerrar sesión desde el formulario de Formato A: " + e.getMessage());
        }

        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getRoot(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Sistema de Gestión de Trabajos de Grado - Universidad del Cauca");
    }

    private List<String> parseObjetivosEspecificos(String objetivosTexto) {
        if (objetivosTexto == null || objetivosTexto.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return objetivosTexto.lines()
            .map(String::trim)
            .filter(line -> !line.isEmpty())
            .collect(Collectors.toList());
    }

    private void actualizarEstadoUsuario(Map<String, Object> response) {
        if (response == null) {
            return;
        }

        Object estadoObj = response.get("estado");
        String estado = estadoObj != null ? estadoObj.toString() : "FORMATO_A_EN_EVALUACION";

        Object idObj = response.get("id");
        Long formatoId = (idObj instanceof Number) ? ((Number) idObj).longValue() : null;

        user.setRequiresFormatoA(false);
        user.setFormatoAEstado(estado);
        user.setFormatoAId(formatoId);

        if (sessionService != null && sessionService.getCurrentUser() != null) {
            User current = sessionService.getCurrentUser();
            current.setRequiresFormatoA(false);
            current.setFormatoAEstado(estado);
            current.setFormatoAId(formatoId);
        }
    }
}

