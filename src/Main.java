import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create main panel with CardLayout
            JPanel mainPanel = new JPanel(new CardLayout());

            // Create panels
            AuthenticationPanel authPanel = new AuthenticationPanel();
            DataBase_UI_Admin adminPanel = new DataBase_UI_Admin();
            DataBase_UI_Student studentPanel = new DataBase_UI_Student();

            // Add panels to the mainPanel with unique identifiers
            mainPanel.add(authPanel, "auth");
            mainPanel.add(adminPanel, "admin");
            mainPanel.add(studentPanel, "student");

            // Pass the mainPanel and studentPanel to authPanel for navigation and updates
            authPanel.setMainPanel(mainPanel);

            // Add the mainPanel to the frame
            frame.add(mainPanel);
            frame.pack();
            frame.setMinimumSize(new Dimension(800, 600));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Optional: Set the initial panel title
            frame.setTitle("Authentication");
        });
    }
}
