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
        } catch (SQLException ex) {
            System.out.println("Error connecting to database: " + ex.getMessage());
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
        } catch (SQLException ex) {
            System.out.println("Error closing connection: " + ex.getMessage());
        }
    }

    /// Functie pentru selectarea tuturor studentilor fara try-with-resources
    public static ResultSet selectAllStudent() {
        try  {
            Connection conn = openConnection();
            if (conn != null) {
                Statement stmt = conn.createStatement();
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
            Connection conn = openConnection();
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
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
    """;

        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
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
            values.add("%"+ nume.trim()+"%");
        }
        if (!prenume.isEmpty()) {
            filters.add("Prenume LIKE ?");
            values.add("%"+ prenume.trim()+"%");
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
        System.out.println(sql.toString());

        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            for (Object value : values) {
                pstmt.setObject(paramIndex++, value);
            }

            //for debugging purposes
            System.out.println(pstmt.toString());

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
        sql.append("?,".repeat(inserts.size()-1));
        sql.append("?)");

        // for debugging purposes
        System.out.println(sql.toString());

        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            // Set all the values in order
            int paramIndex = 1;
            for (Object value : values) {
                pstmt.setObject(paramIndex++, value);
            }

            //for debugging purposes
            System.out.println(pstmt.toString());

            rowsAffected = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Eroare insertStudent: " + ex.getMessage());
        }
        return rowsAffected;
    }


    /// Functie update cu try-with-resources
    public static int updateStudent(int id, String nume, String prenume, String cnp, float nota, String optiune) {
        StringBuilder sql = new StringBuilder("UPDATE Student SET ");
        ArrayList<String> updates = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
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

        if (nota != 0f) {
            updates.add("Nota=?");
            values.add(nota);
        }

        if (!optiune.isEmpty()) {
            updates.add("Optiune=?");
            values.add(optiune);
        }

        if (updates.isEmpty()) {
            return 0;
        }

        // Build the query
        sql.append(String.join(", ", updates));
        sql.append(" WHERE idStudent=?");

        // for debugging purposes
        System.out.println(sql.toString());

        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            // Set all the values in order
            int paramIndex = 1;
            for (Object value : values) {
                pstmt.setObject(paramIndex++, value);
            }
            // Set the ID last
            pstmt.setInt(paramIndex, id);

            //for debugging purposes
            System.out.println(pstmt.toString());

            rowsAffected = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Eroare updateStudent: " + ex.getMessage());
        }
        return rowsAffected;
    }

    /// Functie delete cu try-with-resources
    public static int deleteStudent(int id) {
        StringBuilder sql = new StringBuilder("DELETE FROM STUDENT WHERE idStudent=?");
        int rowsAffected = 0;
        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
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
                UPDATE admitere_status a  +
                        JOIN ( +
                            SELECT  +
                                a.idStudent,  +
                                a.idFacultate,  +
                                IF( +
                                    RANK() OVER (PARTITION BY a.idFacultate ORDER BY s.Nota DESC) <= f.numar_locuri,  +
                                    'admis',  +
                                    'respins' +
                                ) AS new_status  +
                            FROM admitere_status a  +
                            JOIN facultate f ON a.idFacultate = f.idFacultate  +
                            JOIN student s ON a.idStudent = s.idStudent  +
                        ) ranked_status  +
                        ON a.idStudent = ranked_status.idStudent AND a.idFacultate = ranked_status.idFacultate  +
                        SET a.status = ranked_status.new_status;;
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
    public static boolean compareOptiuneFacultate(String optiune){
        String sql="SELECT Nume_Facultate FROM Facultate WHERE Nume_Facultate in (Select Optiune from Student where Optiune=?)";
        try(Connection conn=openConnection();){
            PreparedStatement pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,optiune);
            ResultSet rs=pstmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.out.println("Eroare compareOptiuneFacultate: " + ex.getMessage());
        }
        return false;
    }

    /// Funcție pentru obținerea automată a ID-ului la introducere.
    public static int getFacultateIdByName(String facultateName){
        String sql = "SELECT idFacultate FROM facultate WHERE Nume_Facultate = ?";
        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, facultateName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idFacultate");
            }
        }catch (SQLException ex) {
            System.out.println("Eroare getFacultateIdByName: " + ex.getMessage());
        }
        return 0;
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
            if(rs.next()){
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
