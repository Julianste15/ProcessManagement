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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createMainPanel(), BorderLayout.CENTER);
        pack();
    }   
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(15, 78, 151));
        panel.setPreferredSize(new Dimension(900, 870)); // Reducido de 870 a 800
        
        // Header con logo y título
        panel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Panel central con formulario
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(15, 78, 151));
        centerPanel.add(createFormPanel());
        
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(15, 78, 151));
        headerPanel.setPreferredSize(new Dimension(900, 150)); // Aumentado de 120 a 150
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Botón de volver (izquierda)
        btnBackLogin = new JButton("Volver al Login");
        btnBackLogin.setBackground(new Color(217, 237, 247));
        btnBackLogin.setForeground(new Color(15, 78, 151));
        btnBackLogin.setFocusPainted(false);
        btnBackLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(15, 78, 151), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(15, 78, 151));
        leftPanel.add(btnBackLogin);
        
        // Logo y título (centro)
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        centerPanel.setBackground(new Color(15, 78, 151));
        
        JLabel lblLogo = createLogoLabel();
        JLabel lblTitle = new JLabel("Universidad del Cauca");
        lblTitle.setFont(new Font("Sylfaen", Font.BOLD, 24)); // Aumentado de 20 a 24
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Registro de Usuario");
        lblSubtitle.setFont(new Font("Sylfaen", Font.PLAIN, 16)); // Aumentado de 14 a 16
        lblSubtitle.setForeground(Color.WHITE);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(15, 78, 151));
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSubtitle);
        
        centerPanel.add(lblLogo);
        centerPanel.add(titlePanel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JLabel createLogoLabel() {
        JLabel label;
        
        try {
            // Intentar cargar la imagen del logo
            java.net.URL imageUrl = getClass().getClassLoader().getResource("images/Logo-unicauca.png");
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Aumentado de 60 a 80
                label = new JLabel(new ImageIcon(scaledImage));
            } else {
                // Fallback a un círculo con texto si no encuentra la imagen
                label = new JLabel("UC") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(Color.WHITE);
                        g2.fillOval(0, 0, getWidth(), getHeight());
                        g2.setColor(new Color(15, 78, 151));
                        g2.setFont(new Font("Arial", Font.BOLD, 22)); // Aumentado de 14 a 18
                        FontMetrics fm = g2.getFontMetrics();
                        String text = "UC";
                        int x = (getWidth() - fm.stringWidth(text)) / 2;
                        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                        g2.drawString(text, x, y);
                        g2.dispose();
                    }
                };
                label.setPreferredSize(new Dimension(100, 100)); // Aumentado de 60 a 80
                label.setOpaque(false);
            }
        } catch (Exception e) {
            // Fallback en caso de error
            label = new JLabel("UC");
            label.setPreferredSize(new Dimension(100, 100)); // Aumentado de 60 a 80
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setForeground(new Color(15, 78, 151));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 22)); // Aumentado de 14 a 18
            label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        }
        
        return label;
    }
    
    private JPanel createFormPanel() {
        // Colores definidos como constantes para consistencia
        Color darkBlue = new Color(15, 78, 151);
        Color lightBlue = new Color(217, 237, 247);
        Color white = Color.WHITE;
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createSoftBevelBorder(javax.swing.border.BevelBorder.RAISED),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setPreferredSize(new Dimension(500, 600)); // Reducido de 650 para menos espacio
        panel.setBackground(white);
        
        // Usar BoxLayout para disposición vertical más simple
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Título del formulario
        JLabel lblFormTitle = new JLabel("Complete sus datos");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 78, 151));
        lblFormTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblFormTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); 
        
        // Campos del formulario 
        panel.add(createFieldPanel("Nombres*", txtName = new JTextField(20)));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(createFieldPanel("Apellidos*", txtSurname = new JTextField(20)));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(createFieldPanel("Teléfono", txtPhone = new JTextField(20)));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(createFieldPanel("Correo electrónico*", txtEmail = new JTextField(20)));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(createFieldPanel("Contraseña*", txtPassword = new JPasswordField(20)));
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Carrera y Rol
        panel.add(createCareerRolePanel());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Botón de registro 
        btnRegister = new JButton("Registrarse");
        btnRegister.setBackground(lightBlue);
        btnRegister.setForeground(darkBlue);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setMaximumSize(new Dimension(200, 40));
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panel.add(btnRegister);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        // Nota de campos obligatorios
        JLabel lblRequired = new JLabel("* Campos obligatorios");
        lblRequired.setForeground(Color.RED);
        lblRequired.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblRequired.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblRequired);
        
        return panel;
    }
    
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 25));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        field.setPreferredSize(new Dimension(180, 25));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Agregar tooltip para campo de contraseña
        if (labelText.equals("Contraseña*")) {
            String tooltipText = "<html>La contraseña debe contener:<br>"
                    + "• Mínimo 6 caracteres<br>"
                    + "• Al menos un número<br>"
                    + "• Al menos un carácter especial<br>"
                    + "• Al menos una mayúscula</html>";
            field.setToolTipText(tooltipText);
            label.setToolTipText(tooltipText);
        }
        // Agregar tooltip para campo de teléfono (opcional)
        if (labelText.equals("Teléfono")) {
            String tooltipText = "Campo opcional - Solo números";
            field.setToolTipText(tooltipText);
            label.setToolTipText(tooltipText);
        }
        panel.add(label);
        panel.add(field);

        return panel;
    }
    
    private JPanel createCareerRolePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setBackground(Color.WHITE);
        
        // Panel para Carrera
        JPanel careerPanel = new JPanel();
        careerPanel.setLayout(new BoxLayout(careerPanel, BoxLayout.Y_AXIS));
        careerPanel.setBackground(Color.WHITE);
        
        JLabel lblCareer = new JLabel("Carrera*");
        lblCareer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCareer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cmbCareer = new JComboBox<>();
        cmbCareer.setPreferredSize(new Dimension(180, 30));
        cmbCareer.setMaximumSize(new Dimension(180, 30));
        
        careerPanel.add(lblCareer);
        careerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        careerPanel.add(cmbCareer);
        
        // Panel para Rol
        JPanel rolePanel = new JPanel();
        rolePanel.setLayout(new BoxLayout(rolePanel, BoxLayout.Y_AXIS));
        rolePanel.setBackground(Color.WHITE);
        
        JLabel lblRole = new JLabel("Rol*");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cmbRole = new JComboBox<>();
        cmbRole.setPreferredSize(new Dimension(180, 30));
        cmbRole.setMaximumSize(new Dimension(180, 30));
        
        rolePanel.add(lblRole);
        rolePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rolePanel.add(cmbRole);
        
        panel.add(careerPanel);
        panel.add(rolePanel);
        
        return panel;
    }
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
        if (cmbCareer.getItemCount() > 0) cmbCareer.setSelectedIndex(0);
        if (cmbRole.getItemCount() > 0) cmbRole.setSelectedIndex(0);
    }
    
    public boolean validateForm() {
        if (txtName.getText().trim().isEmpty() ||
            txtSurname.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            new String(txtPassword.getPassword()).trim().isEmpty() ||
            (cmbCareer.getSelectedIndex() < 0) ||
            (cmbRole.getSelectedIndex() < 0)) {
            
            showMessage("Todos los campos obligatorios deben ser completados", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validar formato de email
        if (!txtEmail.getText().trim().toLowerCase().endsWith("@unicauca.edu.co")) {
            showMessage("El email debe ser institucional (@unicauca.edu.co)", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validar que el teléfono sea numérico solo si no esta vacio
        String phoneText = txtPhone.getText().trim();
        if(!phoneText.isEmpty()){
            try {
                Long.parseLong(txtPhone.getText().trim());
            } catch (NumberFormatException e) {
                showMessage("El teléfono debe contener solo números", JOptionPane.WARNING_MESSAGE);
                return false;
            }
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