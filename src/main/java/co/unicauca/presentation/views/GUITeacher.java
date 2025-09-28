package co.unicauca.presentation.views;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class GUITeacher extends javax.swing.JFrame {
    private static final Logger logger = Logger.getLogger(GUITeacher.class.getName());
    
    // Variables mejoradas con nombres descriptivos
    private JLabel lblWelcome;
    private JLabel lblEmail;
    private JLabel lblCareer;
    private JPanel pnlContent;
    private JButton btnUserMenu;
    private JButton btnLogout;
    public GUITeacher() {
        initComponents();
        setupWindowProperties();
    }
    
    private void setupWindowProperties() {
        setTitle("Sistema de Gestión - Docente");
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(true);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Paneles principales
        JPanel headerPanel = createHeaderPanel();
        JPanel sidebarPanel = createSidebarPanel();
        pnlContent = createContentPanel();

        // Configuración del layout principal
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(240, 240, 240));
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(sidebarPanel, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87)
                .addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(633, Short.MAX_VALUE))
        );
        
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(pnlContent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(sidebarPanel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 435, Short.MAX_VALUE))
        );

        pack();
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(15, 71, 151));
        // Logo/Imagen
        JLabel lblLogo = new JLabel();
        lblLogo.setPreferredSize(new Dimension(100, 100));
        try {
            java.net.URL imageUrl = getClass().getClassLoader().getResource("images/Logo-unicauca.png");
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                // Escalar la imagen al tamaño deseado
                Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                lblLogo.setIcon(scaledIcon);
                lblLogo.setText(""); // Eliminar el texto "LOGO"
                lblLogo.setOpaque(false); // Hacer el fondo transparente
            } else {
                // Si no encuentra la imagen, mantener el texto
                lblLogo.setOpaque(true);
                lblLogo.setBackground(Color.WHITE);
                lblLogo.setText("LOGO");
                lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
                logger.warning("No se encontró la imagen del logo en: images/Logo-unicauca.png");
            }
        } catch (Exception e) {
            // En caso de error, mantener el texto
            lblLogo.setOpaque(true);
            lblLogo.setBackground(Color.WHITE);
            lblLogo.setText("LOGO");
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            logger.warning("Error cargando el logo: " + e.getMessage());
        }
        // Textos de la universidad
        JLabel lblUniversity1 = new JLabel("Universidad");
        lblUniversity1.setFont(new Font("Sylfaen", Font.PLAIN, 24));
        lblUniversity1.setForeground(Color.WHITE);
        JLabel lblUniversity2 = new JLabel("del Cauca");
        lblUniversity2.setFont(new Font("Sylfaen", Font.PLAIN, 24));
        lblUniversity2.setForeground(Color.WHITE);
        JLabel lblTitle = new JLabel("Gestión del Proceso de Trabajo de Grado");
        lblTitle.setFont(new Font("Baskerville Old Face", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(lblLogo, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblUniversity1)
                    .addComponent(lblUniversity2))
                .addGap(31, 31, 31)
                .addComponent(lblTitle)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblLogo, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(lblUniversity1)
                        .addGap(0, 0, 0)
                        .addComponent(lblUniversity2))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(lblTitle)))
                .addGap(25, 25, 25))
        );
        return panel;
    }
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(217, 237, 247));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Información del usuario
        lblWelcome = new JLabel("Bienvenido Docente");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblEmail = new JLabel("Email: ");
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblCareer = new JLabel("Departamento: ");
        lblCareer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Botones de menú
        btnUserMenu = new JButton("Menú de Usuario");
        btnUserMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnUserMenu.setMaximumSize(new Dimension(150, 35));
        
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Docente"});
        cmbRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbRole.setMaximumSize(new Dimension(150, 35));
        cmbRole.setEnabled(false); // Solo lectura
        
        btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(150, 35));
        
        // Espaciado entre componentes
        panel.add(lblWelcome);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblCareer);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(btnUserMenu);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(cmbRole);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnLogout);
        
        return panel;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(209, 223, 229));
        panel.setPreferredSize(new Dimension(900, 650));
        panel.setLayout(new BorderLayout());
        
        // Aquí puedes agregar el contenido específico para docentes
        JLabel lblContentTitle = new JLabel("Panel del Docente", SwingConstants.CENTER);
        lblContentTitle.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(lblContentTitle, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Métodos públicos para que el controller pueda interactuar con la vista
    public JButton getBtnUserMenu() {
        return btnUserMenu;
    }
    
    public JButton getBtnLogout() {
        return btnLogout;
    }
    
    public void setUserInfo(String name, String email, String career) {
        lblWelcome.setText("Bienvenido: " + name);
        lblEmail.setText("Email: " + email);
        lblCareer.setText("Departamento: " + career);
    }
    
    public void setLogoutAction(Runnable action) {
        getBtnLogout().addActionListener(e -> action.run());
    }
    
    public void setUserMenuAction(Runnable action) {
        getBtnUserMenu().addActionListener(e -> action.run());
    }
    
    // Main method mejorado
    public static void main(String[] args) {
            // Opción 1: Look and Feel específico
        try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                logger.severe("Error setting look and feel: " + ex.getMessage());
            }
            EventQueue.invokeLater(() -> {
                GUITeacher teacherView = new GUITeacher();
                teacherView.setVisible(true);
            });
    }
}
