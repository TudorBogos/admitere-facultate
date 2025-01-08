import javax.swing.*;
import java.awt.*;

public class AuthenticationPanel extends JPanel {
    private JPanel mainPanel;

    private JTextField adminUsernameField;
    private JPasswordField adminPasswordField;
    private JButton adminLoginButton;

    private JTextField studentNumeField;
    private JTextField studentPrenumeField;
    private JTextField studentCNPField;
    private JButton studentLoginButton;

    private static Integer studentId = 0;

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public AuthenticationPanel() {
        setLayout(new BorderLayout());

        // Admin Panel
        JPanel adminPanel = createAdminPanel();

        // Student Panel
        JPanel studentPanel = createStudentPanel();

        // Combine Panels in a Horizontal Layout
        JPanel authFormsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        authFormsPanel.add(adminPanel);
        authFormsPanel.add(studentPanel);

        // Add combined panel to the center of the layout
        add(authFormsPanel, BorderLayout.CENTER);

        ///  Buton admin login
        adminLoginButton.addActionListener(e -> {
            String username = adminUsernameField.getText().trim();
            String password = new String(adminPasswordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vă rugăm completați toate câmpurile",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (DatabaseManager.authenticateAdmin(username, password)) {
                // Switch to Admin Panel
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                cardLayout.show(mainPanel, "admin");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username sau parola sunt greșite!",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        /// Buton student login
        studentLoginButton.addActionListener(e -> {
            String nume = studentNumeField.getText().trim();
            String prenume = studentPrenumeField.getText().trim();
            String cnp = studentCNPField.getText().trim();

            if (nume.isEmpty() || prenume.isEmpty() || cnp.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vă rugăm completați toate câmpurile",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            studentId = DatabaseManager.authenticateStudent(nume, prenume, cnp);
            if (studentId != null) {
                DataBase_UI_Student.setStudentIdLabel(studentId);
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                cardLayout.show(mainPanel, "student");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Informatii de logare gresite",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Admin Login"));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username label and field
        panel.add(createFieldPanel("Username:", adminUsernameField = new JTextField(12)));

        // Password label and field
        panel.add(createFieldPanel("Password:", adminPasswordField = new JPasswordField(12)));

        // Login button
        adminLoginButton = new JButton("Login");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(adminLoginButton);
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Student Login"));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nume label and field
        panel.add(createFieldPanel("Nume:", studentNumeField = new JTextField(12)));

        // Prenume label and field
        panel.add(createFieldPanel("Prenume:", studentPrenumeField = new JTextField(12)));

        // CNP label and field
        panel.add(createFieldPanel("CNP:", studentCNPField = new JTextField(12)));

        // Login button
        studentLoginButton = new JButton("Login");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(studentLoginButton);
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createFieldPanel(String label, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(field);
        return panel;
    }

    public static Integer getStudentId() {
        return studentId;
    }

}
