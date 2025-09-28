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
import co.unicauca.presentation.views.GUILogin;
import java.awt.EventQueue;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
@Controller
public class GUIRegisterController extends ObservableBase implements iGUIRegisterController, iObserver {
    private static final Logger logger = Logger.getLogger(GUILogin.class.getName());
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
        if (!actionsLoaded) {
            EventQueue.invokeLater(() -> {
                view.getButtonRegister().addActionListener(event -> register(view));
                view.getButtonBackLogin().addActionListener(event -> backToLogin(view));
            });
            actionsLoaded = true;
        }
    }
    @Override
    public void register(GUIRegister prmGUIRegister) {
        if (!prmGUIRegister.validateForm()) {
            return; // Si la validación falla, no continuar
        }
        try {
            // Validar campos vacíos primero
            if (prmGUIRegister.getFieldName().getText().trim().isEmpty() ||
                prmGUIRegister.getFieldSurname().getText().trim().isEmpty() ||
                prmGUIRegister.getFieldEmail().getText().trim().isEmpty() ||
                new String(prmGUIRegister.getFieldPassword().getPassword()).trim().isEmpty() ||
                prmGUIRegister.getFieldPhone().getText().trim().isEmpty()) {
                prmGUIRegister.showMessage("Todos los campos son obligatorios", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User user = new User();
            user.setNames(prmGUIRegister.getFieldName().getText().trim());
            user.setSurnames(prmGUIRegister.getFieldSurname().getText().trim());
            user.setEmail(prmGUIRegister.getFieldEmail().getText().trim());
            user.setPassword(new String(prmGUIRegister.getFieldPassword().getPassword()));
            user.setTelephone(Long.valueOf(prmGUIRegister.getFieldPhone().getText().trim()));
            // Usar enums de dominio con manejo seguro
            String careerDisplayName = prmGUIRegister.getFieldCareer().getSelectedItem().toString();
            String roleDisplayName = prmGUIRegister.getFieldRole().getSelectedItem().toString();
            user.setCareer(Career.fromDisplayName(careerDisplayName));
            user.setRole(Role.fromDisplayName(roleDisplayName));
            User registeredUser = userService.registerUser(user);
            // Si llegamos aquí, el registro fue exitoso
            prmGUIRegister.showMessage("Usuario registrado con éxito!", JOptionPane.INFORMATION_MESSAGE);
            prmGUIRegister.setVisible(false);
            this.notifyOnly(GUILoginController.class, registeredUser);
            } catch (UserException ex) {
                // Mostrar el mensaje de error completo
                System.out.println("ERROR COMPLETO: " + ex.getMessage());
                prmGUIRegister.showMessage(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                System.out.println("EXCEPCIÓN GENERAL: " + ex.getMessage());
                ex.printStackTrace();
                prmGUIRegister.showMessage("Error inesperado: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
    }
    @Override
    public void backToLogin(GUIRegister prmGUIRegister) {
        prmGUIRegister.setVisible(false);
        this.notifyObservers(null);
    }
    @Override
    public void validateNotification(ObservableBase subject, Object model) {
        this.run();
    }
}