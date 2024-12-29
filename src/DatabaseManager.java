import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager extends JOptionPane {
    private static final String URL = "jdbc:mysql://localhost:3306/admitere_facultate";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static Connection connection = null;

    // Function to open connection
    public static Connection openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connection established");
            }
            return connection;
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }

    // Function to close connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    // Function to get all students
    public static ResultSet selectAllStudents() {
        try {
            Connection conn = openConnection();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                return stmt.executeQuery("SELECT * FROM Student");
            }
        } catch (SQLException e) {
            System.out.println("Error querying students: " + e.getMessage());
        }
        return null;
    }



    public static void deleteStudent(int id) {
        StringBuilder sql=new StringBuilder("DELETE FROM STUDENT WHERE ID=?");

    }

    public static void insertStudent() {

    }

    public static int updateStudent(String id, String nume, String prenume, String cnp) {
        StringBuilder sql = new StringBuilder("UPDATE Student SET ");
        ArrayList<String> updates = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        int rowsAffected = 0;  // Declare here

        // Add non-empty fields to the update
        if (!nume.isEmpty()) {
            updates.add("Nume=?");
            values.add(nume);
        }
        if (!prenume.isEmpty()) {
            updates.add("Prenume=?");
            values.add(prenume);
        }
        if (!cnp.isEmpty()) {
            updates.add("CNP=?");
            values.add(cnp);
        }

        // If nothing to update, return
        if (updates.isEmpty()) {
            return 0;
        }

        // Build the query
        sql.append(String.join(", ", updates));
        sql.append(" WHERE idStudent=?");

        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            // Set all the values in order
            int paramIndex = 1;
            for (String value : values) {
                pstmt.setString(paramIndex++, value);
            }
            // Set the ID last
            pstmt.setInt(paramIndex, Integer.parseInt(id));

            rowsAffected = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static void selectAllStudentsTest(){
        try {
            Connection conn = openConnection();
            if (conn != null) {
                String query="SELECT * FROM Student";
                Statement stmt = conn.createStatement();
                ResultSet rs= stmt.executeQuery(query);

                while (rs.next()) {
                    int id = rs.getInt("idStudent");
                    String nume = rs.getString("Nume");
                    String prenume = rs.getString("Prenume");
                    String cnp = rs.getString("CNP");
                    float nota = rs.getFloat("nota");

                    System.out.printf("ID: %d, Nume: %s, Prenume: %s, CNP: %s, Nota: %.2f %n ",
                            id, nume, prenume, cnp, nota);
                }
            }
            closeConnection();
        } catch (SQLException e) {
            System.out.println("Error querying students: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        selectAllStudentsTest();
    }
}