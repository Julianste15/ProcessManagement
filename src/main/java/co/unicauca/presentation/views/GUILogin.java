package co.unicauca.presentation.views;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.logging.Logger;

public class GUILogin extends javax.swing.JFrame {
    private static final Logger logger = Logger.getLogger(GUILogin.class.getName());
    
    // Componentes con nombres descriptivos
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    
    public GUILogin() {
        initComponents();
        setupWindowProperties();
    }
    
    private void setupWindowProperties() {
        setTitle("Inicio de Sesión - Universidad del Cauca");
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);
        setIconImage(createAppIcon()); // Opcional: agregar icono
    }
    
    private Image createAppIcon() {
        // Puedes crear un icono simple o cargar una imagen
        return Toolkit.getDefaultToolkit().createImage("icon.png"); // Opcional
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createMainPanel(), BorderLayout.CENTER);
        pack();
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(15, 78, 151));
        panel.setPreferredSize(new Dimension(800, 600));
        
        // Panel del formulario centrado
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(15, 78, 151));
        centerPanel.add(createLoginFormPanel());
        
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createLoginFormPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createSoftBevelBorder(javax.swing.border.BevelBorder.RAISED),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(400, 450));
        
        // Usar BoxLayout para disposición vertical
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Logo/Icono de la aplicación
        JLabel lblIcon = createAppIconLabel();
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblIcon);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Título
        JLabel lblTitle = new JLabel("BIENVENIDO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(15, 78, 151));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);
        
        JLabel lblSubtitle = new JLabel("Sistema de Gestión de Trabajos de Grado");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.DARK_GRAY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSubtitle);
        
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Formulario de login
        panel.add(createEmailField());
        panel.add(Box.createRigidArea(new Dimension(0, 1)));
        panel.add(createPasswordField());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(createButtonPanel());
        panel.add(Box.createVerticalGlue());
        return panel;
    }
    
    private JLabel createAppIconLabel() {
        JLabel label;
        try {
            java.net.URL imageUrl = getClass().getClassLoader().getResource("images/Logo-unicauca.png");
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);

                int maxWidth = 100;
                int maxHeight = 107;

                int originalWidth = originalIcon.getIconWidth();
                int originalHeight = originalIcon.getIconHeight();

                double widthRatio = (double) maxWidth / originalWidth;
                double heightRatio = (double) maxHeight / originalHeight;
                double scale = Math.min(widthRatio, heightRatio);

                int newWidth = (int) (originalWidth * scale);
                int newHeight = (int) (originalHeight * scale);

                Image scaledImage = originalIcon.getImage()
                        .getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                label = new JLabel(new ImageIcon(scaledImage));
            } else {
                label = new JLabel("🎓", SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 48));
            }
        } catch (Exception e) {
            label = new JLabel("🎓", SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        }
        return label;
    }
    
    private JPanel createEmailField() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(new Color(245, 245, 245));
        
        JLabel lblEmail = new JLabel("Correo electrónico*");
        lblEmail.setPreferredSize(new Dimension(120, 20));
        
        JLabel lblRequired1 = new JLabel("*");
        lblRequired1.setForeground(Color.RED);
        
        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(250, 30));
        txtEmail.setToolTipText("Ingrese su email institucional (@unicauca.edu.co)");
        
        panel.add(lblEmail);
        panel.add(lblRequired1);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(txtEmail);
        
        return panel;
    }
    
    private JPanel createPasswordField() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(new Color(245, 245, 245));
        
        JLabel lblPassword = new JLabel("Contraseña*");
        lblPassword.setPreferredSize(new Dimension(120, 20));
        
        JLabel lblRequired2 = new JLabel("*");
        lblRequired2.setForeground(Color.RED);
        
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(250, 30));
        txtPassword.setToolTipText("Ingrese su contraseña");
        
        panel.add(lblPassword);
        panel.add(lblRequired2);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(txtPassword);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setBackground(new Color(245, 245, 245));
        // Colores definidos como constantes para consistencia
        Color darkBlue = new Color(15, 78, 151);
        Color lightBlue = new Color(217, 237, 247);
        Color white = Color.WHITE;
        // Botón Registrarse - Estilo secundario
        btnRegister = new JButton("Registrarse");
        btnRegister.setBackground(lightBlue); 
        btnRegister.setForeground(darkBlue);
        btnRegister.setPreferredSize(new Dimension(120, 30));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Botón Iniciar Sesión - Estilo primario
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(lightBlue);
        btnLogin.setForeground(darkBlue);
        btnLogin.setPreferredSize(new Dimension(120, 30));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(btnRegister);
        panel.add(btnLogin);

        return panel;
    }
    
    // Métodos públicos para acceso del controller
    public JTextField getFieldEmail() { return txtEmail; }
    public JPasswordField getFieldPassword() { return txtPassword; }
    public JButton getButtonRegister() { return btnRegister; }
    public JButton getButtonLogin() { return btnLogin; }
    
    public void clearForm() {
        txtEmail.setText("");
        txtPassword.setText("");
        txtEmail.requestFocus(); // Poner foco en el campo de email
    }
    
    public boolean validateForm() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        
        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Todos los campos obligatorios deben ser completados", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validar formato de email institucional
        if (!email.toLowerCase().endsWith("@unicauca.edu.co")) {
            showMessage("Debe usar su email institucional (@unicauca.edu.co)", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validar longitud mínima de contraseña
        if (password.length() < 6) {
            showMessage("La contraseña debe tener al menos 6 caracteres", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    public void showMessage(String message, int messageType) {
        String title = "Inicio de Sesión";
        switch (messageType) {
            case JOptionPane.ERROR_MESSAGE:
                title = "Error de Autenticación";
                break;
            case JOptionPane.WARNING_MESSAGE:
                title = "Advertencia";
                break;
            case JOptionPane.INFORMATION_MESSAGE:
                title = "Éxito";
                break;
        }
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    public void setLoginAction(Runnable action) {
        btnLogin.addActionListener(e -> action.run());
        
        // También permitir login con Enter en el campo de contraseña
        txtPassword.addActionListener(e -> action.run());
    }
    
    public void setRegisterAction(Runnable action) {
        btnRegister.addActionListener(e -> action.run());
    }
    
    public void setRememberMeFeature() {
        // Opcional: agregar funcionalidad "Recordarme"
        JCheckBox chkRemember = new JCheckBox("Recordar mis datos");
        chkRemember.setBackground(new Color(245, 245, 245));
        chkRemember.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Agregar antes de los botones
        // ((JPanel) getContentPane().getComponent(0)).add(chkRemember, 6);
    }
    
    // Main method para pruebas
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logger.severe("Error setting look and feel: " + ex.getMessage());
        }
        
        EventQueue.invokeLater(() -> {
            GUILogin loginView = new GUILogin();
            loginView.setVisible(true);
        });
    }
}