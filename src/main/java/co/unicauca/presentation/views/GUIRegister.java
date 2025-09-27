package co.unicauca.presentation.views;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;
public class GUIRegister extends javax.swing.JFrame{
    private static final Logger logger = Logger.getLogger(GUIRegister.class.getName());
    private JTextField txtName;
    private JTextField txtSurname;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JTextField txtPhone;
    private JComboBox<String> cmbCareer;
    private JComboBox<String> cmbRole;
    private JButton btnRegister;
    private JButton btnBackLogin;
    public GUIRegister() {
        initComponents();
        setupWindowProperties();
        loadComboBoxData();
    }   
    private void setupWindowProperties() {
        setTitle("Registro de Usuario - Universidad del Cauca");
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);
    }
    private void loadComboBoxData() {
        // Cargar carreras desde el enum
        cmbCareer.removeAllItems();
        for (co.unicauca.domain.enums.Career career : co.unicauca.domain.enums.Career.values()) {
            cmbCareer.addItem(career.getDisplayName());
        }
        // Cargar roles desde el enum
        cmbRole.removeAllItems();
        for (co.unicauca.domain.enums.Role role : co.unicauca.domain.enums.Role.values()) {
            cmbRole.addItem(role.getDisplayName());
        }
    }
    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Panel principal
        JPanel mainPanel = createMainPanel();
        JPanel formPanel = createFormPanel();
        // Configuración de la ventana
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        pack();
    }   
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(15, 78, 151));
        panel.setPreferredSize(new Dimension(900, 870));
        // Botón de volver
        btnBackLogin = new JButton("Volver al Login");
        btnBackLogin.setBackground(new Color(246, 246, 246));
        btnBackLogin.setMargin(new Insets(5, 10, 5, 10));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(15, 78, 151));
        topPanel.add(btnBackLogin);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(createFormPanel(), BorderLayout.CENTER);
        return panel;
    }
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createSoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panel.setPreferredSize(new Dimension(600, 770));
        // Colores definidos como constantes para consistencia
        Color darkBlue = new Color(15, 78, 151);
        Color lightBlue = new Color(217, 237, 247);
        Color white = Color.WHITE;
        panel.setBackground(white);
        // Usar GridBagLayout para mejor control de la disposición
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Título
        JLabel lblTitle = new JLabel("REGISTRO DE USUARIO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(15, 78, 151));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitle, gbc);
        // Separador
        gbc.gridy = 1; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);
        // Campos del formulario
        int row = 2;
        // Nombre
        addFormField(panel, gbc, row++, "Nombres*", txtName = new JTextField(20));
        // Apellidos
        addFormField(panel, gbc, row++, "Apellidos*", txtSurname = new JTextField(20));
        // Teléfono
        addFormField(panel, gbc, row++, "Teléfono*", txtPhone = new JTextField(20));
        // Email
        addFormField(panel, gbc, row++, "Correo electrónico*", txtEmail = new JTextField(20));
        // Contraseña
        addFormField(panel, gbc, row++, "Contraseña*", txtPassword = new JPasswordField(20));
        // Carrera y Rol en la misma fila
        gbc.gridy = row++;
        gbc.gridx = 0; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblCareer = new JLabel("Carrera*:");
        panel.add(lblCareer, gbc); 
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        cmbCareer = new JComboBox<>();
        cmbCareer.setPreferredSize(new Dimension(180, 25));
        panel.add(cmbCareer, gbc);
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        JLabel lblRole = new JLabel("Rol*:");
        panel.add(lblRole, gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        cmbRole = new JComboBox<>();
        cmbRole.setPreferredSize(new Dimension(180, 25));
        panel.add(cmbRole, gbc);
        // Espaciado
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        panel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        // Botón de registro
        gbc.gridy = row++; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        btnRegister = new JButton("Registrarse");
        btnRegister.setBackground(lightBlue);   
        btnRegister.setForeground(darkBlue);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setPreferredSize(new Dimension(150, 35));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panel.add(btnRegister, gbc);
        // Nota de campos obligatorios
        gbc.gridy = row++;
        JLabel lblRequired = new JLabel("* Campos obligatorios");
        lblRequired.setForeground(Color.RED);
        lblRequired.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        panel.add(lblRequired, gbc);
        return panel;
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        field.setPreferredSize(new Dimension(250, 25));
        panel.add(field, gbc);
    }
    // Métodos públicos para acceso del controller
    public JTextField getFieldName() { return txtName; }
    public JTextField getFieldSurname() { return txtSurname; }
    public JTextField getFieldEmail() { return txtEmail; }
    public JPasswordField getFieldPassword() { return txtPassword; }
    public JTextField getFieldPhone() { return txtPhone; }
    public JComboBox<String> getFieldCareer() { return cmbCareer; }
    public JComboBox<String> getFieldRole() { return cmbRole; }
    public JButton getButtonRegister() { return btnRegister; }
    public JButton getButtonBackLogin() { return btnBackLogin; }
    public void clearForm() {
        txtName.setText("");
        txtSurname.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtPhone.setText("");
        cmbCareer.setSelectedIndex(0);
        cmbRole.setSelectedIndex(0);
    }
    public boolean validateForm() {
        if (txtName.getText().trim().isEmpty() ||
            txtSurname.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            new String(txtPassword.getPassword()).trim().isEmpty() ||
            txtPhone.getText().trim().isEmpty() ||
            cmbCareer.getSelectedIndex() <= 0 ||
            cmbRole.getSelectedIndex() <= 0) {
            showMessage("Todos los campos obligatorios deben ser completados", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        // Validar formato de email
        if (!txtEmail.getText().trim().toLowerCase().endsWith("@unicauca.edu.co")) {
            showMessage("El email debe ser institucional (@unicauca.edu.co)", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        // Validar que el teléfono sea numérico
        try {
            Long.parseLong(txtPhone.getText().trim());
        } catch (NumberFormatException e) {
            showMessage("El teléfono debe contener solo números", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    public void showMessage(String message, int messageType) {
        String title = "Registro de Usuario";
        switch (messageType) {
            case JOptionPane.ERROR_MESSAGE:
                title = "Error de Registro";
                break;
            case JOptionPane.WARNING_MESSAGE:
                title = "Advertencia";
                break;
            case JOptionPane.INFORMATION_MESSAGE:
                title = "Registro Exitoso";
                break;
        }
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    public void setRegisterAction(Runnable action) {
        btnRegister.addActionListener(e -> action.run());
    }
    public void setBackLoginAction(Runnable action) {
        btnBackLogin.addActionListener(e -> action.run());
    }
    // Main method para pruebas
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logger.severe("Error setting look and feel: " + ex.getMessage());
        }
        
        EventQueue.invokeLater(() -> {
            GUIRegister registerView = new GUIRegister();
            registerView.setVisible(true);
        });
    }
}
