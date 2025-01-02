public class AuthenticationPanel extends JPanel {
    public AuthenticationPanel() {
        setLayout(new BorderLayout());  // Main layout

        // Create panel that will hold both authentication forms
        JPanel authFormsPanel = new JPanel(new GridLayout(1, 2, 20, 0));  // 1 row, 2 columns, 20px gap
        authFormsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Add padding

        // Create admin authentication panel (left side)
        JPanel adminPanel = createAdminPanel();
        adminPanel.setBorder(BorderFactory.createTitledBorder("Admin Login"));

        // Create student authentication panel (right side)
        JPanel studentPanel = createStudentPanel();
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Login"));

        // Add both panels to the container
        authFormsPanel.add(adminPanel);
        authFormsPanel.add(studentPanel);

        // Add the container to the main panel
        add(authFormsPanel, BorderLayout.CENTER);
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 10));  // 3 rows (user,pass,button), 2 cols
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Username:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Password:"));
        panel.add(new JPasswordField());
        panel.add(new JLabel(""));  // Empty label for spacing
        panel.add(new JButton("Login"));

        return panel;
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 10));  // 4 rows, 2 cols
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nume:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Prenume:"));
        panel.add(new JTextField());
        panel.add(new JLabel("CNP:"));
        panel.add(new JTextField());
        panel.add(new JLabel(""));  // Empty label for spacing
        panel.add(new JButton("Login"));

        return panel;
    }
}