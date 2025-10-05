package co.unicauca.presentation.controllers;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Career;
import co.unicauca.domain.enums.Role;
import co.unicauca.domain.exceptions.UserException;
import co.unicauca.domain.services.UserService;
import co.unicauca.infrastructure.dependency_injection.Controller;
import co.unicauca.infrastructure.dependency_injection.ControllerAutowired;
import co.unicauca.presentation.interfaces.iGUIRegisterController;
import co.unicauca.presentation.observer.iObserver;
import co.unicauca.presentation.views.GUIRegister;
import co.unicauca.presentation.observer.ObservableBase;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
@Controller
public class GUIRegisterController extends ObservableBase implements iGUIRegisterController, iObserver {
    private static final Logger logger = Logger.getLogger(GUIRegisterController.class.getName());
    private final GUIRegister view;
    private boolean actionsLoaded;
    @ControllerAutowired
    private UserService userService;   
    @ControllerAutowired
    private GUILoginController loginController;   
    public GUIRegisterController() {
        this.view = new GUIRegister();
        this.actionsLoaded = false;
    } 
    @Override
    public void observersLoader() {
        if (!this.hasNoObservers()) return;
        this.addObserver(loginController);
    }   
    @Override
    public void run() {
        observersLoader();
        view.setVisible(true);
        setupEventHandlersOnce();
    }   
    private synchronized void setupEventHandlersOnce() {
        if (!actionsLoaded) {
            EventQueue.invokeLater(() -> {
                view.getButtonRegister().addActionListener(event -> handleRegister());
                view.getButtonBackLogin().addActionListener(event -> handleBackToLogin());
            });
            actionsLoaded = true;
        }
    }
    private void handleRegister() {
        if (!view.validateForm()) {
            return;
        }
        try {
            User user = createUserFromForm();
            User registeredUser = userService.registerUser(user);
            logger.log(Level.INFO, "Usuario registrado exitosamente: {0}", registeredUser.getEmail());
            view.showMessage("Usuario registrado con éxito!", JOptionPane.INFORMATION_MESSAGE);
            view.setVisible(false);
            view.clearForm();
            this.notifyOnly(GUILoginController.class, registeredUser);
        } catch (UserException ex) {
            logger.log(Level.WARNING, "Error en registro de usuario: {0}", ex.getMessage());
            view.showMessage(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error inesperado durante el registro", ex);
            view.showMessage("Error inesperado durante el registro. Por favor, intente nuevamente.", 
                           JOptionPane.ERROR_MESSAGE);
        }
    }
    private User createUserFromForm() {
        User user = new User();
        user.setNames(view.getFieldName().getText().trim());
        user.setSurnames(view.getFieldSurname().getText().trim());
        user.setEmail(view.getFieldEmail().getText().trim());
        user.setPassword(new String(view.getFieldPassword().getPassword()));
        
        // Manejar teléfono opcional
        String phoneText = view.getFieldPhone().getText().trim();
        if (!phoneText.isEmpty()) {
            user.setTelephone(Long.valueOf(phoneText));
        } else {
            user.setTelephone(null); // O 0L dependiendo de tu modelo
        }
        String careerDisplayName = view.getFieldCareer().getSelectedItem().toString();
        String roleDisplayName = view.getFieldRole().getSelectedItem().toString();
        user.setCareer(Career.fromDisplayName(careerDisplayName));
        user.setRole(Role.fromDisplayName(roleDisplayName));
        
        return user;
    }
    private void handleBackToLogin() {
        logger.info("Volviendo a pantalla de login");
        view.setVisible(false);
        view.clearForm();
        this.notifyObservers(null);
    }
    @Override
    public void register(GUIRegister prmGUIRegister) {
        handleRegister();
    }
    @Override
    public void backToLogin(GUIRegister prmGUIRegister) {
        handleBackToLogin();
    }
    @Override
    public void validateNotification(ObservableBase subject, Object model) {
        EventQueue.invokeLater(() -> {
            this.run();
        });
    }
    // Método para testing
    public GUIRegister getView() {
        return view;
    }
}