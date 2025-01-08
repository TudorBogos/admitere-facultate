import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class DataBase_UI_Student extends JPanel {
    private static JLabel studentIdLabel;
    private JTable resultsTable;
    private DefaultTableModel tableModel;


    public DataBase_UI_Student() {
        setLayout(new BorderLayout());

        // Label for displaying Student ID
        studentIdLabel = new JLabel("Your ID is: ");
        studentIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(studentIdLabel, BorderLayout.NORTH);

        // Table for displaying query results
        String[] columnNames = {"Student ID", "Facultate", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable = new JTable(tableModel);
        resultsTable.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        add(scrollPane, BorderLayout.CENTER);

        updateStudentData();
    }

    public void updateStudentData() {

        String sql = """
            SELECT 
                s.idStudent AS "Student ID", 
                f.Nume_Facultate AS "Facultate", 
                a.status AS "Status" 
            FROM admitere_status a
            JOIN student s ON a.idStudent = s.idStudent
            JOIN facultate f ON a.idFacultate = f.idFacultate
            ORDER BY f.Nume_Facultate ASC, a.status ASC, s.idStudent ASC;
        """;

        try (Connection conn = DatabaseManager.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("Student ID"),
                        rs.getString("Facultate"),
                        rs.getString("Status")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Eroare updateStudentData: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void setStudentIdLabel(Integer ID) {
        studentIdLabel.setText("Your ID is: " + ID);
    }

}
