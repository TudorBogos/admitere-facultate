import javax.swing.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DatabaseManager extends JOptionPane {
    private static final String URL = "jdbc:mysql://localhost:3306/admitere_facultate";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static Connection connection = null;

    /// Function to open connection
    public static Connection openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connection established");
            }
            return connection;
        } catch (SQLException ex) {
            System.out.println("Error connecting to database: " + ex.getMessage());
            return null;
        }
    }

    public Connection openConnection(Connection conn) {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connection established");
            }
            return conn;
        } catch (SQLException ex) {
            System.out.println("Error connecting to database: " + ex.getMessage());
            return null;
        }
    }

    /// Function to close connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException ex) {
            System.out.println("Error closing connection: " + ex.getMessage());
        }
    }

    /// Functie pentru selectarea tuturor studentilor fara try-with-resources
    public static ResultSet selectAllStudent() {
        try {
            openConnection();
            if (connection != null) {
                Statement stmt = connection.createStatement();
                return stmt.executeQuery("SELECT idStudent,Nume,Prenume,CNP,nota,Optiune FROM Student");
            }
        } catch (SQLException ex) {
            System.out.println("Eroare selectAllStudents: " + ex.getMessage());
        }
        return null;
    }

    /// Functie pentru selectarea tuturor facultatilor fara try-with-resources
    public static ResultSet selectFacultate() {
        String sql = "SELECT * FROM Facultate";
        try {
            openConnection();
            if (connection != null) {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                return pstmt.executeQuery();
            }
        } catch (SQLException ex) {
            System.out.println("Eroare selectFacultate: " + ex.getMessage());
        }
        return null;
    }


    /// Functie pentru selectarea tuturor status-urilor fara try-with-resources
    public static ResultSet selectAdmitereStatus() {
        String sql = """
                SELECT
                    CONCAT(s.Nume, ' ', s.Prenume) AS Student,
                    f.Nume_Facultate AS Facultate,
                    a.status AS Status
                    FROM admitere_status a
                    JOIN student s ON a.idStudent = s.idStudent
                    JOIN facultate f ON a.idFacultate = f.idFacultate
                    ORDER BY f.Nume_Facultate ASC,
                    a.status ASC;
                """
        ;

        try {
            openConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Eroare selectAdmitereStatus: " + ex.getMessage());
        }
        return null;
    }

    /// Functie pentru selectarea unui student dupa campurile completate
    public static ResultSet filterStudents(int id, String nume, String prenume, String cnp, float nota, String optiune) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Student WHERE ");
        ArrayList<String> filters = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        // Add non-empty fields to the filter
        if (id != 0) {
            filters.add("idStudent=?");
            values.add(id);
        }

        if (!nume.isEmpty()) {
            filters.add("Nume LIKE ?");
            values.add("%" + nume.trim() + "%");
        }
        if (!prenume.isEmpty()) {
            filters.add("Prenume LIKE ?");
            values.add("%" + prenume.trim() + "%");
        }
        if (!cnp.isEmpty()) {
            filters.add("CNP=?");
            values.add(cnp.trim());
        }

        if (nota != 0f) {
            filters.add("Nota=?");
            values.add(nota);
        }

        if (!optiune.isEmpty()) {
            filters.add("Optiune=?");
            values.add(optiune.trim());
        }

        if (filters.isEmpty()) {
            return null;
        }

        // Build the query
        sql.append(String.join(" AND ", filters));

        //for debugging purposes
        System.out.println(sql);

        try {
            openConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());

            int paramIndex = 1;
            for (Object value : values) {
                pstmt.setObject(paramIndex++, value);
            }

            //for debugging purposes
            System.out.println(pstmt);

            return pstmt.executeQuery();

        } catch (SQLException ex) {
            System.out.println("Eroare filterStudents: " + ex.getMessage());
        }
        return null;
    }

    /// Functie insert cu try-with-resources
    public static int insertStudent(String nume, String prenume, String cnp, float nota, String optiune) {
        StringBuilder sql = new StringBuilder("INSERT INTO STUDENT (");
        ArrayList<String> inserts = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        int rowsAffected = 0;

        // Add non-empty fields to the inserts
        if (!nume.isEmpty()) {
            inserts.add("Nume");
            values.add(nume.trim());
        }

        if (!prenume.isEmpty()) {
            inserts.add("Prenume");
            values.add(prenume.trim());
        }

        if (!cnp.isEmpty()) {
            inserts.add("CNP");
            values.add(cnp.trim());
        }

        if (nota != 0f) {
            inserts.add("Nota");
            values.add(nota);
        }

        if (!optiune.isEmpty()) {
            int idFacultateOptiune = getFacultateIdByName(optiune.trim());
            inserts.add("idFacultateOptiune");
            values.add(idFacultateOptiune);
            inserts.add("Optiune");
            values.add(optiune.trim());
        }


        if (inserts.isEmpty()) {
            return 0;
        }

        // Build the query
        sql.append(String.join(", ", inserts));
        sql.append(") VALUES (");
        sql.append("?,".repeat(inserts.size() - 1));
        sql.append("?)");

        // for debugging purposes
        System.out.println(sql);

        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            // Set all the values in order
            int paramIndex = 1;
            for (Object value : values) {
                pstmt.setObject(paramIndex++, value);
            }

            //for debugging purposes
            System.out.println(pstmt);

            rowsAffected = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Eroare insertStudent: " + ex.getMessage());
        }
        return rowsAffected;
    }

    /// Function to insert a student after CSV import
    public static boolean insertStudentsFromCSV(String Nume, String Prenume, String CNP, float Nota, String Optiune, Connection conn) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Eroare: Conexiunea este închisă înainte de apelarea insertStudentsFromCSV.");
        }

        String baseSQL = "INSERT INTO STUDENT (Nume, Prenume, CNP, Nota, idFacultateOptiune, Optiune) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            int idFacultateOptiune = getFacultateIdByName(conn, Optiune);
            if (idFacultateOptiune == 0) {
                throw new Exception("Eroare: Opțiunea '" + Optiune + "' nu este validă.");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(baseSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, Nume);
                pstmt.setString(2, Prenume);
                pstmt.setString(3, CNP);
                pstmt.setFloat(4, Nota);
                pstmt.setInt(5, idFacultateOptiune);
                pstmt.setString(6, Optiune);

                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    System.out.println("Studentul a fost inserat cu idStudent: " + generatedId);
                }
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Eroare la inserarea studentului: " + ex.getMessage());
            System.out.printf("Nu s-a putut insera: %s, %s, %s, %.2f, %s\n", Nume, Prenume, CNP, Nota, Optiune);
        }
        return false;
    }

    /// Functie update cu try-with-resources
    public static int updateStudent(int id, String nume, String prenume, String cnp, float nota, String optiune) {
        StringBuilder sql1 = new StringBuilder("UPDATE Student SET ");
        String sql2 = "UPDATE admitere_status SET idFacultate = ? WHERE idStudent = ?";
        ArrayList<String> updates = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        int rowsAffected = 0;

        // Add non-empty fields to the update for Student table
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

        if (nota != 0f) {
            updates.add("Nota=?");
            values.add(nota);
        }

        Integer idFacultateOptiune = null;
        if (!optiune.isEmpty()) {
            idFacultateOptiune = getFacultateIdByName(optiune.trim());
            updates.add("idFacultateOptiune=?");
            values.add(idFacultateOptiune);
            updates.add("Optiune=?");
            values.add(optiune.trim());
        }

        if (updates.isEmpty()) {
            return 0; // No updates to make
        }

        // Build the query for Student table
        sql1.append(String.join(", ", updates));
        sql1.append(" WHERE idStudent=?");

        // Debugging: print query
        System.out.println("Query for Student: " + sql1);

        try (Connection conn = openConnection()) {
            // Update the Student table
            try (PreparedStatement pstmt1 = conn.prepareStatement(sql1.toString())) {
                int paramIndex = 1;
                for (Object value : values) {
                    pstmt1.setObject(paramIndex++, value);
                }
                // Set the ID last
                pstmt1.setInt(paramIndex, id);

                // Execute the Student table update
                rowsAffected = pstmt1.executeUpdate();
                System.out.println("Updated Student rows: " + rowsAffected);
            }

            // If faculty (optiune) has changed, update the admitere_status table
            if (idFacultateOptiune != null) {
                try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                    pstmt2.setInt(1, idFacultateOptiune);
                    pstmt2.setInt(2, id);

                    // Execute the admitere_status update
                    int statusRowsUpdated = pstmt2.executeUpdate();
                    System.out.println("Updated admitere_status rows: " + statusRowsUpdated);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Eroare updateStudent: " + ex.getMessage());
        }

        return rowsAffected;
    }



    /// Functie delete cu try-with-resources
    public static int deleteStudent(int id) {
        String sql = "DELETE FROM STUDENT WHERE idStudent=?";
        int rowsAffected = 0;
        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Eroare deleteStudent: " + ex.getMessage());
        }
        return rowsAffected;
    }

    /// Functie care updateaza admitere_status cu admis/respins automat bazat pe nr de locuri
    public static void updateAdmitereStatus() {
        String sql = """
                  UPDATE admitere_status a
                              JOIN (
                                  SELECT
                                      a.idStudent,
                                      a.idFacultate,
                                      IF(
                                          RANK() OVER (PARTITION BY a.idFacultate ORDER BY s.Nota DESC) <= f.numar_locuri,
                                          'admis',
                                          'respins'
                                      ) AS new_status
                                  FROM admitere_status a
                                  JOIN facultate f ON a.idFacultate = f.idFacultate
                                  JOIN student s ON a.idStudent = s.idStudent
                              ) ranked_status
                              ON a.idStudent = ranked_status.idStudent AND a.idFacultate = ranked_status.idFacultate
                              SET a.status = ranked_status.new_status;
                """;

        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Execute the query
            int updatedRows = pstmt.executeUpdate();
            System.out.println(updatedRows + " rows updated in admitere_status.");

        } catch (SQLException ex) {
            System.out.println("Eroare updateAdmitereStatus: " + ex.getMessage());
        }
    }


    /// Compară opțiunea completată pentru inserare cu tabelul Facultate. Returnează adevărat dacă există un rând returnat.
    public static boolean compareOptiuneFacultate(String optiune) {
        String sql = "SELECT Nume_Facultate FROM Facultate WHERE Nume_Facultate = ?";
        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, optiune.trim());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true; // Found a match
            } else {
                System.out.println("Opțiunea nu a fost găsită în tabelul Facultate: '" + optiune + "'");
            }
        } catch (SQLException ex) {
            System.out.println("Eroare compareOptiuneFacultate: " + ex.getMessage());
        }
        return false; // No match or error occurred
    }


    /// Funcție pentru obținerea automată a ID-ului la introducere.
    public static int getFacultateIdByName(String facultateName) {
        String sql = "SELECT idFacultate FROM facultate WHERE Nume_Facultate = ?";
        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, facultateName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idFacultate");
            }
        } catch (SQLException ex) {
            System.out.println("Eroare getFacultateIdByName: " + ex.getMessage());
        }
        return 0;
    }

    public static int getFacultateIdByName(Connection conn, String facultateName) throws SQLException {
        if (conn == null || conn.isClosed()) {
            System.out.println("Eroare: Conexiunea este închisă înainte de a apela getFacultateIdByName.");
            return 0; // Return 0 if the connection is invalid
        }

        String sql = "SELECT idFacultate FROM facultate WHERE Nume_Facultate = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, facultateName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idFacultate");
            }
        } catch (SQLException ex) {
            System.out.println("Eroare getFacultateIdByName: " + ex.getMessage());
        }
        return 0; // Return 0 if not found
    }


    public static String formatCNP(Object cnp) {
        if (cnp instanceof Double) {
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(0); // Ensure no decimal points
            return df.format(cnp);
        } else {
            return cnp.toString().trim(); // If it's already a string, just trim it
        }
    }


    /// Functie de authenticare a adminului cu try-with-resources
    public static boolean authenticateAdmin(String username, String password) {
        String sql1 = "SELECT 1 FROM admin WHERE username=? AND password=?";
        String sql2 = "UPDATE admin SET last_login=CURRENT_TIMESTAMP WHERE username=?";
        try (Connection conn = DatabaseManager.openConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

            pstmt1.setString(1, username);
            pstmt1.setString(2, password);
            pstmt2.setString(1, username);

            // Execute query and return true if a match is found
            ResultSet rs = pstmt1.executeQuery();
            if (rs.next()) {
                pstmt2.executeUpdate();
                return true;
            }

        } catch (SQLException ex) {
            System.out.println("Eroare authenticateAdmin: " + ex.getMessage());
        }
        return false; // Return false if any error or no match
    }


    /// Functie de autentificare a studentului cu try-with-resources
    public static Integer authenticateStudent(String nume, String prenume, String cnp) {
        String sql = "SELECT idStudent FROM student WHERE Nume = ? AND Prenume = ? AND CNP = ?";
        try (Connection conn = DatabaseManager.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nume);
            pstmt.setString(2, prenume);
            pstmt.setString(3, cnp);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idStudent"); // Return the idStudent if found
            }
        } catch (SQLException ex) {
            System.out.println("Eroare authenticateStudent: " + ex.getMessage());
        }
        return null;
    }

}


/// Functie de testare
    /*public static void selectAllStudentsTest(){
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
        } catch (SQLException ex) {
            System.out.println("Error querying students: " + ex.getMessage());
        }
    }*/
