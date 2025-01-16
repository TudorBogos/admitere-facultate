import javax.swing.*;
import java.awt.*;

public class AdmitereChart extends JPanel {

    private final int admittedCount;
    private final int totalPlaces;

    public AdmitereChart(int admittedCount, int totalPlaces) {
        this.admittedCount = admittedCount;
        this.totalPlaces = totalPlaces;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Calculate angles for the pie chart
        int admittedAngle = (int) (360.0 * admittedCount / totalPlaces);
        int remainingAngle = 360 - admittedAngle;

        // Draw admitted slice
        g2d.setColor(Color.BLUE);
        g2d.fillArc(50, 50, 200, 200, 0, admittedAngle);

        // Draw remaining slice
        g2d.setColor(Color.GRAY);
        g2d.fillArc(50, 50, 200, 200, admittedAngle, remainingAngle);

        // Draw labels
        g2d.setColor(Color.BLACK);
        g2d.drawString("Admisi : " + admittedCount, 260, 100);
        g2d.drawString("Respinsi: " + (totalPlaces - admittedCount), 260, 130);
    }
}