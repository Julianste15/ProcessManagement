package co.unicauca.processmanagement;

import co.unicauca.data.access.DatabaseInitializer;
import co.unicauca.data.access.UserRepository;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.repositories.IUserRepositoriy;
import co.unicauca.domain.services.EmailValidator;
import co.unicauca.domain.services.PasswordEncryptor;
import co.unicauca.domain.services.PasswordValidator;
import co.unicauca.domain.services.UserService;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ProcessManagement {

    public static void main(String[] args) {
        DatabaseInitializer.initialize();

        IUserRepositoriy repo = new UserRepository();
        PasswordValidator passwordValidator = new PasswordValidator();
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        EmailValidator emailValidator = new EmailValidator();

        UserService userService = new UserService(repo, passwordValidator, passwordEncryptor, emailValidator);

        while (true) {
            String op = JOptionPane.showInputDialog(
                null,
                "=== Gestión de procesos de grado (FIET) ===\n\n\n" +
                "Opciones:\n" +
                "1. Registrarse\n" +
                "2. Iniciar sesión\n" +
                "0. Salir\n\n\nDigite su opción: "
            );

            if (op == null) return; // Cancelar cierra la app

            switch (op.trim()) {
                case "1" -> doRegister(userService);
                case "2" -> doLogin(userService);
                case "0" -> {
                    JOptionPane.showMessageDialog(null, "👋 Adios!");
                    return;
                }
                default -> JOptionPane.showMessageDialog(null, "❌ Opción invalida.");
            }
        }
    }
    
    private static void doRegister(UserService userService) {
        try {
            String first = JOptionPane.showInputDialog("Digite su Primer nombre:");
            if (first == null) return;

            String last = JOptionPane.showInputDialog("Digite su Apellido:");
            if (last == null) return;

            String phone = JOptionPane.showInputDialog("Digite su celular (opcional)\n\nPresione 'ok' para omitir:");
            if (phone == null) return;

            String program = chooseProgram();
            if (program == null) return;

            String role = chooseRole();
            if (role == null) return;

            String email = JOptionPane.showInputDialog("Correo institucional (@unicauca.edu.co):");
            if (email == null) return;

            PasswordValidator passwordValidator = new PasswordValidator();
            String pwd = promptSecurePassword(passwordValidator);
            if (pwd == null) return;;

            User user = new User(first, last, phone.isBlank() ? null : phone, program, role.toLowerCase(), email, pwd);
            userService.registerUser(user);

            JOptionPane.showMessageDialog(null, "✅ Registrado con exito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error: " + e.getMessage());
        }
    }

    private static String promptSecurePassword(PasswordValidator validator) {
        while (true) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JTextArea instructions = new JTextArea("""
    🔐 Cree una contraseña segura:
    • Mínimo 6 caracteres
    • Al menos un número
    • Al menos una letra mayúscula
    • Al menos un carácter especial
    """);
            instructions.setEditable(false);
            instructions.setFocusable(false);
            instructions.setOpaque(false);
            instructions.setLineWrap(true);
            instructions.setWrapStyleWord(true);
            instructions.setFont(new JLabel().getFont());

            JPanel pwdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel pwdLabel = new JLabel("Contraseña:");
            JPasswordField pwdField = new JPasswordField(15);
            pwdPanel.add(pwdLabel);
            pwdPanel.add(pwdField);

            JPanel confirmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel confirmLabel = new JLabel("Confirmar:");
            JPasswordField confirmField = new JPasswordField(15);
            confirmPanel.add(confirmLabel);
            confirmPanel.add(confirmField);

            panel.add(instructions);
            panel.add(Box.createVerticalStrut(10));
            panel.add(pwdPanel);
            panel.add(confirmPanel);

            int option = JOptionPane.showConfirmDialog(null, panel, "Crear contraseña", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option != JOptionPane.OK_OPTION) return null;

            String pwd = new String(pwdField.getPassword());
            String confirmPwd = new String(confirmField.getPassword());

            if (!pwd.equals(confirmPwd)) {
                JOptionPane.showMessageDialog(null,
                    "❌ Las contraseñas no coinciden.\nPor favor, intente nuevamente.",
                    "Error de coincidencia",
                    JOptionPane.WARNING_MESSAGE);
                continue;
            }

            if (!validator.isValid(pwd)) {
                JOptionPane.showMessageDialog(null,
                    "❌ La contraseña no cumple con los requisitos.\nIntente nuevamente.",
                    "Contraseña inválida",
                    JOptionPane.WARNING_MESSAGE);
                continue;
            }

        return pwd;
        }
    }
    
    private static void doLogin(UserService userService) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        panel.add(new JLabel("Correo institucional:"));
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Iniciar sesión", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) return;

        String email = emailField.getText().trim();
        String pwd = new String(passwordField.getPassword());

        Optional<User> logged = userService.login(email, pwd);
        if (logged.isEmpty()) {
            JOptionPane.showMessageDialog(null, "❌ Credenciales inválidas.\nVerifique su correo y contraseña.", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User u = logged.get();
        JOptionPane.showMessageDialog(null, "✅ Bienvenido, " + u.getFirstName() + " (" + u.getRole() + ")", "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);
        showRoleMenu(u);
    }

    private static void showRoleMenu(User user) {
        if ("estudiante".equalsIgnoreCase(user.getRole())) {
            studentMenu(user);
        } else if ("profesor".equalsIgnoreCase(user.getRole())) {
            teacherMenu(user);
        } else {
            JOptionPane.showMessageDialog(null, "rol desconocido.");
        }
    }

    private static void studentMenu(User user) {
        while (true) {
            String op = JOptionPane.showInputDialog(
                "--- Student Dashboard ---\n" +
                "1. Ver estado de proyecto de grado\n" +
                "2. Iniciar un nuevo proyecto de greado\n" +
                "0. Logout\n\nChoose:"
            );

            if (op == null || op.trim().equals("0")) return;

            switch (op.trim()) {
                case "1" -> JOptionPane.showMessageDialog(null, "(TODO) Show current thesis status");
                case "2" -> JOptionPane.showMessageDialog(null, "(TODO) Start new thesis flow");
                default -> JOptionPane.showMessageDialog(null, "Invalid option.");
            }
        }
    }
    
    private static String chooseRole() {
    String[] roles = { "Estudiante", "Profesor" };

    StringBuilder menu = new StringBuilder("Seleccione su rol:\n");
    for (int i = 0; i < roles.length; i++) {
        menu.append(i + 1).append(". ").append(roles[i]).append("\n");
    }

    while (true) {
        String input = JOptionPane.showInputDialog(menu.toString());
        if (input == null) return null; // Cancelar

        try {
            int choice = Integer.parseInt(input.trim());
            if (choice == 1) return "estudiante";
            if (choice == 2) return "profesor";
            JOptionPane.showMessageDialog(null, "❌ Opción inválida. Intente de nuevo.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "⚠️ Ingrese solo el número correspondiente.");
        }
    }
}
    
    private static String chooseProgram() {
        String[] programs = {
            "Ingeniería de Sistemas",
            "Ingeniería Electrónica y Telecomunicaciones",
            "Automática Industrial",
            "Tecnología en Telemática"
        };

        StringBuilder menu = new StringBuilder("Seleccione su programa académico:\n");
        for (int i = 0; i < programs.length; i++) {
            menu.append(i + 1).append(". ").append(programs[i]).append("\n");
        }

        while (true) {
            String input = JOptionPane.showInputDialog(menu.toString());
            if (input == null) return null; // Cancelar

            try {
                int choice = Integer.parseInt(input.trim());
                if (choice >= 1 && choice <= programs.length) {
                    return programs[choice - 1];
                } else {
                    JOptionPane.showMessageDialog(null, "❌ Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "⚠️ Ingrese solo el número correspondiente.");
            }
        }
    }
    
    private static void teacherMenu(User user) {
        while (true) {
            String op = JOptionPane.showInputDialog(
                "--- Teacher Dashboard ---\n" +
                "1. Evaluate proposals (anteproyectos)\n" +
                "2. Evaluate monographs\n" +
                "0. Logout\n\nChoose:"
            );

            if (op == null || op.trim().equals("0")) return;

            switch (op.trim()) {
                case "1" -> JOptionPane.showMessageDialog(null, "(TODO) Evaluate proposals");
                case "2" -> JOptionPane.showMessageDialog(null, "(TODO) Evaluate monographs");
                default -> JOptionPane.showMessageDialog(null, "Invalid option.");
            }
        }
    }
}