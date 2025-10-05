package co.unicauca.presentation.controllers;
import co.unicauca.domain.entities.Project;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.ProjectModality;
import co.unicauca.domain.enums.Role;
import co.unicauca.domain.exceptions.ProjectException;
import co.unicauca.domain.services.ProjectService;
import co.unicauca.domain.services.SessionService;
import co.unicauca.infrastructure.dependency_injection.Controller;
import co.unicauca.infrastructure.dependency_injection.ControllerAutowired;
import co.unicauca.presentation.observer.ObservableBase;
import co.unicauca.presentation.views.GUITeacher;
import co.unicauca.presentation.observer.iObserver;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
@Controller
public class GUITeacherController extends ObservableBase implements iObserver {
    private static final Logger logger = Logger.getLogger(GUIStudentController.class.getName());
    private GUITeacher view;
    private User currentTeacher;
    @ControllerAutowired
    private SessionService sessionService;
    @ControllerAutowired
    private GUILoginController loginController;
    @ControllerAutowired
    private ProjectService projectService;
    public GUITeacherController() {
        // La vista se crea cuando se necesita
    }
    private void initializeView() {
        if (view == null) {
            try {
                view = new GUITeacher();
                setupEventHandlers();
                logger.info("Vista Teacher inicializada correctamente");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error inicializando vista Teacher", e);
                throw new RuntimeException("Error creando vista de profesor", e);
            }
        }
    }
    private void setupEventHandlers() {
        try {
            view.setLogoutAction(this::handleLogout);
            view.setUserMenuAction(this::handleUserMenu);
            view.setCreateProjectAction(this::handleCreateProject);
            view.setMyProjectsAction(this::handleMyProjects);
        logger.info("Event handlers de Teacher configurados");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error configurando event handlers", e);
        }
    }
    private void handleCreateProject() {
        try {
            logger.info("Mostrando formulario de Formato A");
            view.showProjectForm();
    // CONECTAR el botón de enviar del formulario
        view.setSubmitAction(this::handleSubmitForm);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error mostrando formulario de proyecto", e);
            JOptionPane.showMessageDialog(view, "Error al cargar el formulario", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // NUEVO método para manejar el envío del formulario
    private void handleSubmitForm() {
        try {
            logger.info("Procesando envío de Formato A");

            // Obtener datos del formulario y crear proyecto
            Project project = createProjectFromFormData();

            // Enviar el proyecto
            Project savedProject = projectService.submitFormatA(project, currentTeacher);

            JOptionPane.showMessageDialog(view, 
                "¡Formato A enviado exitosamente!\n" +
                "ID del Proyecto: " + savedProject.getId() + "\n" +
                "Estado: " + savedProject.getStatus().getDisplayName(),
                "Envío Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);

            // Volver a la lista de proyectos
            handleMyProjects();

        } catch (ProjectException e) {
            logger.log(Level.WARNING, "Error de validación en Formato A", e);
            JOptionPane.showMessageDialog(view, 
                "Error en el formulario:\n" + e.getMessage(), 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado enviando Formato A", e);
            JOptionPane.showMessageDialog(view, 
                "Error inesperado al enviar el formulario", 
                "Error del Sistema", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    // NUEVO método para crear proyecto desde los datos del formulario
    private Project createProjectFromFormData() throws ProjectException {
        Project project = new Project();

        // NOTA: Aquí necesitaríamos métodos en la vista para obtener los datos
        // Por ahora usamos datos de ejemplo

        project.setTitle("Proyecto de Ejemplo - " + System.currentTimeMillis());
        project.setModality(ProjectModality.RESEARCH);
        project.setGeneralObjective("Implementar un sistema de gestión de trabajos de grado");

        List<String> specificObjectives = new ArrayList<>();
        specificObjectives.add("Diseñar la arquitectura del sistema");
        specificObjectives.add("Implementar el módulo de autenticación");
        specificObjectives.add("Desarrollar la interfaz de usuario");
        project.setSpecificObjectives(specificObjectives);

        project.setPdfFilePath("/ruta/ejemplo/formato_a.pdf");

        return project;
    }
    private void handleMyProjects() {
        try {
            logger.info("Cargando proyectos del docente");
            List<Project> projects = projectService.getTeacherProjects(currentTeacher);
            view.showMyProjects();
            // Aquí actualizarías la tabla con los proyectos
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando proyectos", e);
            JOptionPane.showMessageDialog(view, "Error al cargar proyectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Método para manejar el envío del formulario (se llamaría desde la vista)
    public void handleSubmitFormatA(Project project) {
        try {
            Project savedProject = projectService.submitFormatA(project, currentTeacher);
            JOptionPane.showMessageDialog(view, 
                "Formato A enviado exitosamente!\nID: " + savedProject.getId(), 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            handleMyProjects(); // Volver a la lista de proyectos
        } catch (ProjectException e) {
            logger.log(Level.WARNING, "Error enviando Formato A", e);
            JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado enviando Formato A", e);
            JOptionPane.showMessageDialog(view, "Error inesperado al enviar el formulario", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    @Override
    public void validateNotification(ObservableBase subject, Object model) {
        logger.info("TeacherController recibió notificación");
        
        if (model instanceof User) {
            User user = (User) model;
            if (user.getRole() == Role.TEACHER) {
                logger.info("Usuario profesor recibido: " + user.getNames());
                this.currentTeacher = user;
                
                EventQueue.invokeLater(() -> {
                    try {
                        initializeView();
                        loadTeacherData(user);
                        view.setVisible(true);
                        logger.info("Vista de profesor mostrada exitosamente");
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error mostrando vista de profesor", e);
                        JOptionPane.showMessageDialog(
                            null, 
                            "Error al abrir la vista de profesor: " + e.getMessage(),
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
            } else {
                logger.warning("Usuario recibido no es profesor: " + user.getRole());
            }
        } else {
            logger.warning("Model recibido no es User: " + (model != null ? model.getClass() : "null"));
        }
    }
    private void loadTeacherData(User teacher) {
        try {
            view.setUserInfo(
                teacher.getNames() + " " + teacher.getSurnames(),
                teacher.getEmail(),
                teacher.getCareer().getDisplayName()
            );
            logger.info("Datos de profesor cargados: " + teacher.getNames());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando datos de profesor", e);
        }
    }
    private void handleLogout() {
        try {
            int option = JOptionPane.showConfirmDialog(
                view, 
                "¿Está seguro que desea cerrar sesión?", 
                "Confirmar cierre de sesión", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (option == JOptionPane.YES_OPTION) {
                logger.info("Cerrando sesión de profesor: " + currentTeacher.getNames());
                
                // 1. Cerrar sesión en el servicio
                if (sessionService != null) {
                    sessionService.logout();
                } else {
                    logger.warning("SessionService es null durante logout");
                }
                
                // 2. Cerrar vista actual
                if (view != null) {
                    view.setVisible(false);
                    view.dispose();
                    view = null;
                }
                
                // 3. Limpiar datos
                currentTeacher = null;
                
                // 4. Volver al login - OPCIÓN 1: Notificar al login controller
                if (loginController != null) {
                    logger.info("Notificando a LoginController para volver al login");
                    loginController.showLogin();
                } 
                
                logger.info("Sesión de profesor cerrada exitosamente");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error durante logout de profesor", e);
            JOptionPane.showMessageDialog(
                view, 
                "Error al cerrar sesión: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void handleUserMenu() {
        try {
            JOptionPane.showMessageDialog(
                view, 
                "Menú de usuario del docente: " + (currentTeacher != null ? currentTeacher.getNames() : "N/A"),
                "Menú de Usuario", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en menú de usuario", e);
        }
    }
    private void setupFormSubmitHandler() {
        // Aquí conectarías el botón de enviar del formulario con la lógica del controller
        // Esto depende de cómo implementes la comunicación vista-controller

        // Ejemplo:
        /*
        view.setSubmitAction(e -> {
            try {
                Project project = createProjectFromForm();
                handleSubmitFormatA(project);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error procesando formulario", ex);
                JOptionPane.showMessageDialog(view, "Error en el formulario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        */
    }
    @Override
    public void observersLoader() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
