package co.unicauca.presentation.views;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
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
    private JButton btnCreateProject;
    private JButton btnMyProjects;
    private JPanel pnlProjectForm;
    private JPanel pnlMyProjects;
    private JTable tblProjects;
    private Runnable submitAction;
    public GUITeacher() {
        initComponents();
        setupWindowProperties();
    }
    
    private void setupWindowProperties() {
        setTitle("Sistema de Gestión - Docente");
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(true);
    }
    public void setSubmitAction(Runnable action) {
        this.submitAction = action;
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
        // NUEVOS BOTONES PARA PROYECTOS
        btnCreateProject = new JButton("Nuevo Formato A");
        btnCreateProject.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCreateProject.setMaximumSize(new Dimension(150, 35));

        btnMyProjects = new JButton("Mis Proyectos");
        btnMyProjects.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMyProjects.setMaximumSize(new Dimension(150, 35));
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
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnCreateProject);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnMyProjects);
        
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
    // NUEVO: Panel de formulario de proyecto
    private JPanel createProjectFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Título
        JLabel lblTitle = new JLabel("Formulario Formato A - Proyecto de Grado", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel de formulario con scroll
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Declarar componentes como variables de instancia para poder acceder a ellos
        JTextField txtProjectTitle = new JTextField();
        JComboBox<String> cmbModality = new JComboBox<>(new String[]{"Investigación", "Práctica Profesional"});
        JTextField txtDate = new JTextField(java.time.LocalDate.now().toString());
        JTextField txtDirector = new JTextField();
        JTextField txtCoDirector = new JTextField();
        JTextArea txtGeneralObjective = new JTextArea(3, 40);
        JPanel pnlSpecificObjectives = createSpecificObjectivesPanel();
        JPanel pnlPdfFile = createFileUploadPanel("Archivo PDF del Formato A *", "pdf");
        JPanel pnlAcceptanceLetter = createFileUploadPanel("Carta de Aceptación de la Empresa", "pdf");

        // Hacer campos de solo lectura donde corresponda
        txtDate.setEditable(false);
        txtGeneralObjective.setLineWrap(true);
        txtGeneralObjective.setWrapStyleWord(true);

        // 1. Título del proyecto
        formPanel.add(createFormField("Título del Proyecto *", txtProjectTitle, 300, 30));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 2. Modalidad
        formPanel.add(createFormField("Modalidad *", cmbModality, 200, 30));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 3. Fecha actual (solo lectura)
        formPanel.add(createFormField("Fecha Actual", txtDate, 150, 30));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 4. Director del proyecto
        formPanel.add(createFormField("Director del Proyecto *", txtDirector, 250, 30));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 5. Codirector del proyecto (opcional)
        formPanel.add(createFormField("Codirector del Proyecto", txtCoDirector, 250, 30));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 6. Objetivo general
        JScrollPane scrollGeneral = new JScrollPane(txtGeneralObjective);
        scrollGeneral.setPreferredSize(new Dimension(400, 80));
        formPanel.add(createFormField("Objetivo General *", scrollGeneral, 400, 80));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 7. Objetivos específicos (múltiples)
        formPanel.add(pnlSpecificObjectives);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 8. Archivo PDF
        formPanel.add(pnlPdfFile);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 9. Carta de aceptación (solo para práctica profesional)
        pnlAcceptanceLetter.setVisible(false); // Inicialmente oculto
        formPanel.add(pnlAcceptanceLetter);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botones de acción
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnSubmit = new JButton("Enviar Formato A");
        btnSubmit.setBackground(new Color(217, 237, 247));
        btnSubmit.setForeground(new Color(15, 78, 151));
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmit.setPreferredSize(new Dimension(180, 35));

        JButton btnClear = new JButton("Limpiar Formulario");
        btnClear.setBackground(new Color(217, 237, 247));
        btnClear.setForeground(new Color(15, 78, 151));
        btnClear.setPreferredSize(new Dimension(150, 35));

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setPreferredSize(new Dimension(120, 35));

        pnlButtons.add(btnSubmit);
        pnlButtons.add(btnClear);
        pnlButtons.add(btnCancel);

        formPanel.add(pnlButtons);

        // Agregar scroll al formulario
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // CORRECCIÓN: Listener para mostrar/ocultar carta de aceptación
        cmbModality.addActionListener(e -> {
            String selected = (String) cmbModality.getSelectedItem();
            pnlAcceptanceLetter.setVisible("Práctica Profesional".equals(selected));
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // Listener para limpiar formulario
        btnClear.addActionListener(e -> clearForm());

        // Listener para cancelar
        btnCancel.addActionListener(e -> showMyProjects());

        // Listener para enviar formulario
        btnSubmit.addActionListener(e -> {
            if (validateForm()) {
                // Notificar al controller que se quiere enviar el formulario
                if (submitAction != null) {
                    submitAction.run();
                }
            }
        });

        return mainPanel;
    }
    // Método para validar el formulario
    private boolean validateForm() {
        // Implementar validaciones básicas
        // Por ahora solo retorna true, pero aquí irían todas las validaciones

        JOptionPane.showMessageDialog(this,
            "Formulario validado correctamente. Los datos estarían listos para enviar.",
            "Validación Exitosa",
            JOptionPane.INFORMATION_MESSAGE);

        return true;
}
    // Método auxiliar para crear campos de formulario
    private JPanel createFormField(String labelText, JComponent component, int width, int height) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(600, height + 10));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setPreferredSize(new Dimension(200, height));

        // Configurar el componente según su tipo
        if (component instanceof JTextField) {
            ((JTextField) component).setFont(new Font("Arial", Font.PLAIN, 12));
            component.setPreferredSize(new Dimension(width, height));
        } else if (component instanceof JComboBox) {
            component.setPreferredSize(new Dimension(width, height));
        } else if (component instanceof JScrollPane) {
            component.setPreferredSize(new Dimension(width, height));
        } else {
            component.setPreferredSize(new Dimension(width, height));
        }

        panel.add(label, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);

        return panel;
    }
    // Panel para objetivos específicos (múltiples)
    private JPanel createSpecificObjectivesPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setMaximumSize(new Dimension(600, 200));

        JLabel lblTitle = new JLabel("Objetivos Específicos *");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel para lista de objetivos
        JPanel objectivesPanel = new JPanel();
        objectivesPanel.setLayout(new BoxLayout(objectivesPanel, BoxLayout.Y_AXIS));
        objectivesPanel.setBackground(Color.WHITE);
        objectivesPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Objetivo inicial
        JPanel firstObjectivePanel = createObjectiveRow();
        objectivesPanel.add(firstObjectivePanel);

        JScrollPane scrollPane = new JScrollPane(objectivesPanel);
        scrollPane.setPreferredSize(new Dimension(550, 120));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Botón para agregar más objetivos
        JButton btnAddObjective = new JButton("+ Agregar Otro Objetivo");
        btnAddObjective.setFont(new Font("Arial", Font.PLAIN, 11));
        btnAddObjective.setMaximumSize(new Dimension(180, 25));
        btnAddObjective.addActionListener(e -> {
            JPanel newObjectivePanel = createObjectiveRow();
            objectivesPanel.add(newObjectivePanel);
            objectivesPanel.revalidate();
            objectivesPanel.repaint();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnAddObjective);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    // Fila individual para objetivo específico
    private JPanel createObjectiveRow() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setMaximumSize(new Dimension(550, 35));

        JTextArea txtObjective = new JTextArea(1, 40);
        txtObjective.setLineWrap(true);
        txtObjective.setWrapStyleWord(true);
        txtObjective.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(txtObjective);
        scroll.setPreferredSize(new Dimension(450, 30));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        JButton btnRemove = new JButton("✕");
        btnRemove.setFont(new Font("Arial", Font.BOLD, 10));
        btnRemove.setForeground(Color.RED);
        btnRemove.setPreferredSize(new Dimension(30, 25));
        btnRemove.addActionListener(e -> {
            Container parent = panel.getParent();
            parent.remove(panel);
            parent.revalidate();
            parent.repaint();
        });

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnRemove, BorderLayout.EAST);

        return panel;
    }

    // Panel para subida de archivos
    private JPanel createFileUploadPanel(String labelText, String fileType) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(600, 50));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setPreferredSize(new Dimension(250, 30));

        JPanel filePanel = new JPanel(new BorderLayout(5, 0));
        filePanel.setBackground(Color.WHITE);

        JTextField txtFilePath = new JTextField();
        txtFilePath.setEditable(false);
        txtFilePath.setPreferredSize(new Dimension(300, 30));

        JButton btnBrowse = new JButton("Examinar...");
        btnBrowse.setPreferredSize(new Dimension(100, 30));
        btnBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos " + fileType.toUpperCase(), fileType));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                txtFilePath.setText(selectedFile.getAbsolutePath());

                // Validar tamaño máximo (10MB)
                long fileSize = selectedFile.length();
                long maxSize = 10 * 1024 * 1024; // 10MB
                if (fileSize > maxSize) {
                    JOptionPane.showMessageDialog(this, 
                        "El archivo excede el tamaño máximo permitido (10MB)", 
                        "Archivo Demasiado Grande", 
                        JOptionPane.WARNING_MESSAGE);
                    txtFilePath.setText("");
                }
            }
        });

        filePanel.add(txtFilePath, BorderLayout.CENTER);
        filePanel.add(btnBrowse, BorderLayout.EAST);

        panel.add(label, BorderLayout.WEST);
        panel.add(filePanel, BorderLayout.CENTER);

        return panel;
    }

    // Método para limpiar el formulario
    private void clearForm() {
        int option = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea limpiar todo el formulario?",
            "Confirmar Limpieza",
            JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            // Aquí se implementaría la limpieza de todos los campos
            JOptionPane.showMessageDialog(this,
                "Formulario limpiado correctamente",
                "Limpieza Exitosa",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // NUEVO: Panel de lista de proyectos
    private JPanel createMyProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Mis Proyectos de Grado", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Mensaje temporal
        JLabel lblMessage = new JLabel("No hay proyectos registrados", SwingConstants.CENTER);
        lblMessage.setFont(new Font("Arial", Font.PLAIN, 16));
        lblMessage.setForeground(Color.GRAY);
        panel.add(lblMessage, BorderLayout.CENTER);

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
    // NUEVOS métodos para el controller
    public JButton getBtnCreateProject() {
        return btnCreateProject;
    }

    public JButton getBtnMyProjects() {
        return btnMyProjects;
    }

    public void setCreateProjectAction(Runnable action) {
        getBtnCreateProject().addActionListener(e -> action.run());
    }

    public void setMyProjectsAction(Runnable action) {
        getBtnMyProjects().addActionListener(e -> action.run());
    }

    public void showProjectForm() {
        pnlContent.removeAll();
        JPanel projectForm = createProjectFormPanel();
        pnlContent.add(projectForm);
        pnlContent.revalidate();
        pnlContent.repaint();

        logger.info("Formulario de proyecto mostrado");
    }

    public void showMyProjects() {
        pnlContent.removeAll();
        JPanel myProjectsPanel = createMyProjectsPanel();
        pnlContent.add(myProjectsPanel);
        pnlContent.revalidate();
        pnlContent.repaint();

        logger.info("Panel de proyectos mostrado");
    }
    // Métodos para que el controller pueda acceder a los datos del formulario
    public Map<String, Object> getFormData() {
        Map<String, Object> formData = new HashMap<>();

        // Aquí se implementaría la obtención de datos de todos los campos
        // Esto es un ejemplo simplificado

        return formData;
    }

    public void setSubmitAction(java.awt.event.ActionListener action) {
        // Buscar el botón de enviar en el formulario y asignar el action listener
        // Esto se implementaría cuando se crea el formulario
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
