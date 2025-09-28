package co.unicauca.presentation.views;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;
public class GUIStudent extends javax.swing.JFrame {
    private static final Logger logger = Logger.getLogger(GUIStudent.class.getName());
    private JLabel lblWelcome;
    private JLabel lblEmail;
    private JLabel lblCareer;
    private JLabel lblStudentId;
    private JPanel pnlContent;
    public GUIStudent() {
        initComponents();
        setupWindowProperties();
    }
    private void setupWindowProperties() {
        setTitle("Sistema de Gestión - Estudiante");
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
                .addContainerGap(168, Short.MAX_VALUE))
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
                        .addGap(66, 66, 66)
                        .addComponent(sidebarPanel, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(15, 71, 151));
        // Logo/Imagen
        JLabel lblLogo = new JLabel();
        lblLogo.setPreferredSize(new Dimension(100, 100));
        try{
            java.net.URL imageUrl = getClass().getClassLoader().getResource("images/Logo-unicauca.png");
            if (imageUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            // Escalar la imagen al tamaño deseado
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            lblLogo.setIcon(scaledIcon);
            lblLogo.setText(""); // Eliminar el texto "LOGO"
            lblLogo.setOpaque(false); // Hacer el fondo transparente
        }else{
            lblLogo.setOpaque(true);
            lblLogo.setBackground(Color.WHITE);
            lblLogo.setText("LOGO");
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            logger.warning("No se encontró la imagen del logo en: images/Logo-unicauca.png");
            }  
        }catch(Exception e){
            lblLogo.setOpaque(true);
            lblLogo.setBackground(Color.WHITE);
            lblLogo.setText("LOGO");
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            logger.warning("No se encontró la imagen del logo en: images/Logo-unicauca.png");
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
        // Información del estudiante
        lblWelcome = new JLabel("Bienvenido Estudiante");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEmail = new JLabel("Email: ");
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT); 
        lblCareer = new JLabel("Carrera: ");
        lblCareer.setAlignmentX(Component.CENTER_ALIGNMENT); 
        lblStudentId = new JLabel("Código: ");
        lblStudentId.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Botones de menú
        JButton btnUserMenu = new JButton("Menú de Usuario");
        btnUserMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnUserMenu.setMaximumSize(new Dimension(150, 35));
        JButton btnMyProjects = new JButton("Mis Proyectos");
        btnMyProjects.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMyProjects.setMaximumSize(new Dimension(150, 35));
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Estudiante"});
        cmbRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbRole.setMaximumSize(new Dimension(150, 35));
        cmbRole.setEnabled(false); // Solo lectura
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(150, 35));
        // Espaciado entre componentes
        panel.add(lblWelcome);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblCareer);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblStudentId);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnUserMenu);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnMyProjects);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(cmbRole);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnLogout); 
        return panel;
    }
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(209, 223, 229));
        panel.setPreferredSize(new Dimension(1120, 650));
        panel.setLayout(new BorderLayout());
        // Contenido específico para estudiantes
        JLabel lblContentTitle = new JLabel("Panel del Estudiante", SwingConstants.CENTER);
        lblContentTitle.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(lblContentTitle, BorderLayout.NORTH);
        // Aquí se puede agregar componentes específicos para estudiantes
        JPanel studentContent = new JPanel(new GridLayout(2, 2, 10, 10));
        studentContent.setBackground(new Color(209, 223, 229));
        studentContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Algunos componentes para estudiantes(se pueden modificar)
        JButton btnViewProjects = new JButton("Ver Proyectos Disponibles");
        JButton btnMyApplications = new JButton("Mis Postulaciones");
        JButton btnUploadDocuments = new JButton("Subir Documentos");
        JButton btnAcademicProgress = new JButton("Progreso Académico");  
        studentContent.add(btnViewProjects);
        studentContent.add(btnMyApplications);
        studentContent.add(btnUploadDocuments);
        studentContent.add(btnAcademicProgress);  
        panel.add(studentContent, BorderLayout.CENTER); 
        return panel;
    }
    // Métodos públicos para que el controller pueda interactuar con la vista
    public JButton getBtnUserMenu() {
        return (JButton) ((JPanel) getContentPane().getComponent(1)).getComponent(4);
    }
    public JButton getBtnMyProjects() {
        return (JButton) ((JPanel) getContentPane().getComponent(1)).getComponent(5);
    }
    public JButton getBtnLogout() {
        return (JButton) ((JPanel) getContentPane().getComponent(1)).getComponent(7);
    }
    public void setStudentInfo(String name, String email, String career, String studentId) {
        lblWelcome.setText("Bienvenido: " + name);
        lblEmail.setText("Email: " + email);
        lblCareer.setText("Carrera: " + career);
        lblStudentId.setText("Código: " + (studentId != null ? studentId : "No asignado"));
    }
    public void setLogoutAction(Runnable action) {
        getBtnLogout().addActionListener(e -> action.run());
    }
    public void setUserMenuAction(Runnable action) {
        getBtnUserMenu().addActionListener(e -> action.run());
    }
    public void setMyProjectsAction(Runnable action) {
        getBtnMyProjects().addActionListener(e -> action.run());
    }
    // Main method 
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logger.severe("Error setting look and feel: " + ex.getMessage());
        }
        EventQueue.invokeLater(() -> {
            GUIStudent studentView = new GUIStudent();
            studentView.setVisible(true);
        });
    }
}

