import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
       /*SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create our main panel with CardLayout
            JPanel mainPanel = new JPanel(new CardLayout());

            // Create our panels
            AuthenticationPanel authPanel = new AuthenticationPanel();
            DataBase_UI_Admin adminPanel = new DataBase_UI_Admin();

            // Add panels to the main panel with unique names
            mainPanel.add(authPanel, "auth");
            mainPanel.add(adminPanel, "admin");

            // Add the main panel to the frame
            frame.add(mainPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Give the auth panel access to the CardLayout for switching
            authPanel.setMainPanel(mainPanel);
        });*/
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new DataBase_UI_Admin());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}