import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AuthenticationPanel extends JPanel {
    private JPanel mainPanel;

    private JTextField adminUsernameField;
    private JPasswordField adminPasswordField;
    private JButton adminLoginButton;

    private JTextField studentNumeField;
    private JTextField studentPrenumeField;
    private JTextField studentCNPField;
    private JButton studentLoginButton;


    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public AuthenticationPanel() {

        setLayout(new BorderLayout());

        JPanel authFormsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        authFormsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel adminPanel = createAdminPanel();
        JPanel studentPanel = createStudentPanel();

        adminPanel.setBorder(BorderFactory.createTitledBorder("Admin Login"));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Login"));

        authFormsPanel.add(adminPanel);
        authFormsPanel.add(studentPanel);
        add(authFormsPanel, BorderLayout.CENTER);

        // Admin login logic
        adminLoginButton.addActionListener(e -> {
            String username = adminUsernameField.getText();
            String password = new String(adminPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vă rugăm completați toate câmpurile",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("Admin attempting login with: " + username);
        });

        // Student login logic
        studentLoginButton.addActionListener(e -> {
            String nume = studentNumeField.getText();
            String prenume = studentPrenumeField.getText();
            String cnp = studentCNPField.getText();

            if (nume.isEmpty() || prenume.isEmpty() || cnp.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vă rugăm completați toate câmpurile",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("Student attempting login: " + nume + " " + prenume);
        });
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Add some padding

        // Username label and field
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        adminUsernameField = new JTextField(15);  // Width of 15 characters
        panel.add(adminUsernameField, gbc);

        // Password label and field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        adminPasswordField = new JPasswordField(15);
        panel.add(adminPasswordField, gbc);

        // Login button
        gbc.gridx = 1; gbc.gridy = 2;
        adminLoginButton = new JButton("Login");
        panel.add(adminLoginButton, gbc);

        return panel;
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Initialize components with smaller size
        studentNumeField = new JTextField(12);  // Reduced column size
        studentPrenumeField = new JTextField(12);
        studentCNPField = new JTextField(12);
        studentLoginButton = new JButton("Login");

        panel.add(new JLabel("Nume:"));
        panel.add(studentNumeField);
        panel.add(new JLabel("Prenume:"));
        panel.add(studentPrenumeField);
        panel.add(new JLabel("CNP:"));
        panel.add(studentCNPField);
        panel.add(new JLabel(""));
        panel.add(studentLoginButton);

        adminLoginButton.addActionListener(e -> {
            String username = adminUsernameField.getText();
            String password = new String(adminPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vă rugăm completați toate câmpurile",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (DatabaseManager.authenticateAdmin(username, password)) {
                // Switch to the admin panel
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                cardLayout.show(mainPanel, "admin");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username sau parolă incorecte",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    public String getAdminUsernameField() {
        return adminUsernameField.getText();
    }

    public String getStudentNumeField() {
        return studentNumeField.getText();
    }

    public String getStudentPrenumeField() {
        return studentPrenumeField.getText();
    }

    public String getStudentCNPField() {
        return studentCNPField.getText();
    }
}