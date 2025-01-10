import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.io.FileWriter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;


public class DataBase_UI_Admin extends JPanel {
    private JTextField numeField, prenumeField, cnpField, idField, notaField, optiuneField;
    private JTable tableStudent, tableFacultate, tableAdmitereStatus;
    private DefaultTableModel tableModelStudent, tableModelFacultate, tableModelAdmitereStatus;
    private TableRowSorter<DefaultTableModel> sorter;

    public DataBase_UI_Admin() {
        setLayout(new BorderLayout());

        // Input Panel with their respective fields
        JPanel inputPanel = new JPanel(new GridLayout(12, 1, 0, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Nume:"));
        numeField = new JTextField();
        inputPanel.add(numeField);

        inputPanel.add(new JLabel("Prenume:"));
        prenumeField = new JTextField();
        inputPanel.add(prenumeField);

        inputPanel.add(new JLabel("CNP:"));
        cnpField = new JTextField();
        inputPanel.add(cnpField);

        inputPanel.add(new JLabel("Nota:"));
        notaField = new JTextField();
        inputPanel.add(notaField);

        inputPanel.add(new JLabel("Optiune:"));
        optiuneField = new JTextField();
        inputPanel.add(optiuneField);

        // Button Panel
        JPanel leftButtonPanel = new JPanel(new FlowLayout());
        JButton insertButton = new JButton("Insert");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton viewButton = new JButton("Filter/Update tabela studenti");
        JButton clearButton = new JButton("Clear field-uri");

        leftButtonPanel.add(insertButton);
        leftButtonPanel.add(updateButton);
        leftButtonPanel.add(deleteButton);
        leftButtonPanel.add(viewButton);
        leftButtonPanel.add(clearButton);

        leftButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));


        // Student Table setup
        String[] columnNames = {"ID", "Nume", "Prenume", "CNP", "Nota", "Optiune"};
        tableModelStudent = new DefaultTableModel(columnNames, 0);
        tableStudent = new JTable(tableModelStudent);
        tableStudent.setEnabled(false);
        tableStudent.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tableStudent);
        JPanel tablePanel = new JPanel(new GridBagLayout());
        tablePanel.add(scrollPane);
        scrollPane.setPreferredSize(new Dimension(550, 330));

        // Add input,button and student table panels to the left panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(leftButtonPanel, BorderLayout.CENTER);
        leftPanel.add(tablePanel, BorderLayout.SOUTH);

        // Facultate Table setup
        String[] facultateColumnNames = {"ID", "Nume Facultate", "Adresa", "Numar Locuri"};
        tableModelFacultate = new DefaultTableModel(facultateColumnNames, 0);
        tableFacultate = new JTable(tableModelFacultate);
        tableFacultate.setEnabled(false);
        tableFacultate.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPaneFacultate = new JScrollPane(tableFacultate);
        scrollPaneFacultate.setPreferredSize(new Dimension(500, 300));

        // Add exportToExcelButton and exportToPDFButton
        JPanel rightButtonPanel= new JPanel(new FlowLayout());
        JButton exportToExcelButton = new JButton("Export to Excel tabela de mai jos");
        JButton exportToPDFButton = new JButton("Export to PDF tabela de mai jos");

        rightButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0,0 , 0));

        rightButtonPanel.add(exportToExcelButton);
        rightButtonPanel.add(exportToPDFButton);

        // Admitere_Status Table setup
        String[] admitereStatusColumnNames = {"Student", "Facultate", "Status"};
        tableModelAdmitereStatus = new DefaultTableModel(admitereStatusColumnNames, 0);
        tableAdmitereStatus = new JTable(tableModelAdmitereStatus);
        tableAdmitereStatus.setEnabled(false);
        TableRowSorter<DefaultTableModel> sorterAdmitereStatus = new TableRowSorter<>(tableModelAdmitereStatus);
        tableAdmitereStatus.getTableHeader().setReorderingAllowed(false);
        tableAdmitereStatus.setRowSorter(sorterAdmitereStatus);
        JScrollPane scrollPaneAdmitereStatus = new JScrollPane(tableAdmitereStatus);
        scrollPaneAdmitereStatus.setPreferredSize(new Dimension(400, 330));


        // Panel for Facultate and Admitere_Status Tables
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(scrollPaneFacultate,BorderLayout.NORTH);
        rightPanel.add(rightButtonPanel, BorderLayout.CENTER);
        rightPanel.add(scrollPaneAdmitereStatus, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);


        /// Buton insert
        insertButton.addActionListener(e -> {

            //Verificare campuri goale
            if(verifyEmptyInsert()){
                JOptionPane.showMessageDialog(this,
                        "Trebuie să fie toate câmpurile completate pentru a adăuga un student nou. (Fară ID)",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Perform insert
            try {
                int rowsAffected = DatabaseManager.insertStudent(
                        numeField.getText().trim(),
                        prenumeField.getText().trim(),
                        validCNP(),
                        validNota(),
                        validOptiune()
                );

                JOptionPane.showMessageDialog(this,
                        "S-au inserat " + rowsAffected + " rânduri",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
                populateTableStudent();
                DatabaseManager.updateAdmitereStatus();
                populateTableAdmitereStatus();
                clearFields();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Eroare la inserare student: " + ex.getMessage(),
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        /// Buton update
        updateButton.addActionListener(e -> {

            if(verifyEmptyID()){
                JOptionPane.showMessageDialog(this,
                        "Campul ID trebuie completat",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Finally do the update
            try {
                int rowsAffected = DatabaseManager.updateStudent(
                        validID(),
                        numeField.getText().trim(),
                        prenumeField.getText().trim(),
                        validCNP(),
                        validNota(),
                        validOptiune()
                );

                JOptionPane.showMessageDialog(this,
                        "S-au actualizat " + rowsAffected + " rânduri",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
                populateTableStudent();
                populateTableAdmitereStatus();
                DatabaseManager.updateAdmitereStatus();
                clearFields();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Eroare la actualizare student: " + ex.getMessage(),
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        });


        /// Buton delete
        deleteButton.addActionListener(e -> {

            if(verifyEmptyID()){
                JOptionPane.showMessageDialog(this,
                        "Campul ID trebuie completat",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int rowsAffected = DatabaseManager.deleteStudent(validID());

                JOptionPane.showMessageDialog(this,
                        "S-au șters " + rowsAffected + " rânduri",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
                populateTableStudent();
                DatabaseManager.updateAdmitereStatus();
                populateTableAdmitereStatus();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Eroare la ștergere student:\n " + ex.getMessage(),
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        /// Buton view
        viewButton.addActionListener(e -> {
            if (verifyEmptyView()) {
                populateTableStudent();
            } else {
                try {
                    filterTableStudent();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                             ex.getMessage(),
                            "Eroare",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        /// Buton export to Excel care da export la tabela studenti
        exportToExcelButton.addActionListener(e -> {
            try {
                String filePath = "students_export.csv"; // Define the file path
                exportTableModelToCSV(filePath);
                JOptionPane.showMessageDialog(this,
                        "Succes export CSV " + filePath,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Eroare la export CSV: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        /// Buton export to PDF care da export la tabela studenti
        exportToPDFButton.addActionListener(e -> {try {
            String filePath = "students_export.PDF"; // Define the file path
            exportTableModelToPDF(filePath);
            JOptionPane.showMessageDialog(this,
                    "Succes export PDF " + filePath,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Eroare la export PDF: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }});

        clearButton.addActionListener(e -> clearFields());

        //Function to make the table visible at initialization
        populateTableStudent();
        populateTableFacultate();
        DatabaseManager.updateAdmitereStatus();
        populateTableAdmitereStatus();
    }

    /// Functie de populare a tabelului cu studenti
    public void populateTableStudent() {
        try {
            ResultSet rs = DatabaseManager.selectAllStudent();
            tableModelStudent.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("idStudent"),
                        rs.getString("Nume"),
                        rs.getString("Prenume"),
                        rs.getString("CNP"),
                        rs.getFloat("Nota"),
                        rs.getString("Optiune")
                };
                tableModelStudent.addRow(row);
            }

            autoResizeColumns(tableStudent);
            tableStudent.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            rs.close();
            DatabaseManager.closeConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Eroare la reîmprospătare tabel student:\n " + e.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /// Functie de populare a tabelului cu facultati
    public void populateTableFacultate() {
        try {
            ResultSet rs = DatabaseManager.selectFacultate();
            tableModelFacultate.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("idFacultate"),
                        rs.getString("Nume_Facultate"),
                        rs.getString("Adresa"),
                        rs.getInt("numar_locuri"),
                };
                tableModelFacultate.addRow(row);
            }

            autoResizeColumns(tableFacultate);
            tableFacultate.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            rs.close();
            DatabaseManager.closeConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Eroare la reîmprospătare tabel facultate:\n " + e.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /// Functie de populare a tabelului cu statusuri admitere
    public void populateTableAdmitereStatus() {
        try {
            ResultSet rs = DatabaseManager.selectAdmitereStatus();
            tableModelAdmitereStatus.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getString("Student"),
                        rs.getString("Facultate"),
                        rs.getString("Status"),
                };
                tableModelAdmitereStatus.addRow(row);
            }

            autoResizeColumns(tableAdmitereStatus);
            tableAdmitereStatus.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            rs.close();
            DatabaseManager.closeConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Eroare la reîmprospătare tabel admitere_status:\n " + e.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }



    /// Functie de filtrare a tabelului
    public void filterTableStudent() throws Exception {
        try {
            ResultSet rs = DatabaseManager.filterStudents(
                    validID(),
                    numeField.getText().trim(),
                    prenumeField.getText().trim(),
                    validCNP(),
                    validNota(),
                    validOptiune()
            );

            // Check if ResultSet is empty
            if (rs == null || !rs.next()) {
                throw new Exception("Nu exista nicio inregistrare cu aceste filtre");
            }

            // Clear the table
            tableModelStudent.setRowCount(0);

            // Add rows to the table
            do {
                Object[] row = {
                        rs.getInt("idStudent"),
                        rs.getString("Nume"),
                        rs.getString("Prenume"),
                        rs.getString("CNP"),
                        rs.getFloat("Nota"),
                        rs.getString("Optiune")
                };
                tableModelStudent.addRow(row);
            } while (rs.next());

            // Resize columns
            autoResizeColumns(tableStudent);
            tableStudent.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            rs.close();
            DatabaseManager.closeConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Eroare la filtrare tabel:\n " + e.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void clearFields() {
        idField.setText("");
        numeField.setText("");
        prenumeField.setText("");
        cnpField.setText("");
        notaField.setText("");
        optiuneField.setText("");
    }

    /// Functie pentru validarea ID-ului
    public int validID() throws Exception {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            throw new Exception("ID-ul trebuie să fie un număr valid");
        }
    }

    /// Functie pentru validarea CNP-ului
    public String validCNP() throws Exception {
        String cnp = cnpField.getText().trim();
        if (cnp.isEmpty()) {
            return ""; // Allow empty CNP
        }
        if (cnp.length() != 13) {
            throw new Exception("CNP-ul trebuie să aibă 13 cifre sau să fie gol");
        }
        for (int i = 0; i < cnp.length(); i++) {
            if (!Character.isDigit(cnp.charAt(i))) {
                throw new Exception("CNP-ul trebuie să conțină doar cifre");
            }
        }
        return cnp;
    }

    /// Functie pentru validarea notei
    public float validNota() throws Exception {
        String notaText = notaField.getText();
        if (notaText.isEmpty()) {
            return 0f;
        }
        try {
            float nota = Float.parseFloat(notaText);
            if (nota < 1 || nota > 10) {
                throw new Exception("Nota trebuie să fie între 1 și 10");
            }
            return nota;
        } catch (NumberFormatException e) {
            throw new Exception("Nota trebuie să fie un număr valid");
        }
    }

    /// Functie pentru validarea optiunii
    public String validOptiune() throws Exception {
        String optiuneText = optiuneField.getText().trim();
        if (optiuneText.isEmpty()) {
            return "";
        }
        if (DatabaseManager.compareOptiuneFacultate(optiuneText)) {
            return optiuneText;
        } else {
            throw new Exception("Facultatea introdusa nu exista in lista de facultati.");
        }
    }

    /// Verifies that the ID field is empty. Returns true if the field is emptpy, false if it is filled. Used for update and delete buttons.
    public boolean verifyEmptyID(){
        return idField.getText().isEmpty();
    }

    /// Verifies that the fields are empty for insert. Returns true if any of the fields are empty
    public boolean verifyEmptyInsert(){
        return numeField.getText().isEmpty() || prenumeField.getText().isEmpty() || cnpField.getText().isEmpty() || notaField.getText().isEmpty() || optiuneField.getText().isEmpty();
    }

    /// Verifies that the fields are empty for viewButton. Returns true if all the fields are empty
    public boolean verifyEmptyView(){
        return idField.getText().isEmpty() && numeField.getText().isEmpty() && prenumeField.getText().isEmpty() && cnpField.getText().isEmpty() && notaField.getText().isEmpty() && optiuneField.getText().isEmpty() ;
    }

    private void autoResizeColumns(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = 0;

            // Get width of column header
            TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    table, tableColumn.getHeaderValue(), false, false, 0, column);
            preferredWidth = headerComp.getPreferredSize().width;

            // Get maximum width of column data (limit to the first 100 rows for performance)
            for (int row = 0; row < Math.min(100, table.getRowCount()); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component cellComp = table.prepareRenderer(cellRenderer, row, column);
                preferredWidth = Math.max(preferredWidth, cellComp.getPreferredSize().width);
            }

            // Add margin
            preferredWidth += table.getIntercellSpacing().width + 10;

            // Set the width
            tableColumn.setPreferredWidth(preferredWidth);
        }



    }

    /// Functie pentru export in CSV care se poate deschide in Excel
    private void exportTableModelToCSV(String filePath) throws IOException {
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            // Write column headers
            int columnCount = tableModelAdmitereStatus.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                csvWriter.append(tableModelAdmitereStatus.getColumnName(i));
                if (i < columnCount - 1) {
                    csvWriter.append(",");
                }
            }
            csvWriter.append("\n");

            // Write rows
            int rowCount = tableModelAdmitereStatus.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    csvWriter.append(String.valueOf(tableModelAdmitereStatus.getValueAt(i, j)));
                    if (j < columnCount - 1) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }
        }
    }

    private void exportTableModelToPDF(String filePath) throws IOException {
        // Initialize PDF writer and document
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Create a table with the number of columns in the table model
        int columnCount = tableModelAdmitereStatus.getColumnCount();
        Table pdfTable = new Table(columnCount);

        // Add table headers
        for (int i = 0; i < columnCount; i++) {
            pdfTable.addCell(new Cell().add(new Paragraph(tableModelAdmitereStatus.getColumnName(i))));
        }

        // Add table data rows
        int rowCount = tableModelAdmitereStatus.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(tableModelAdmitereStatus.getValueAt(i, j)))));
            }
        }

        // Add the table to the document and close the document
        document.add(pdfTable);
        document.close();
    }



    // Getters for the fields if needed
    public String getNume() {
        return numeField.getText();
    }

    public String getPrenume() {
        return prenumeField.getText();
    }

    public String getCNP() {
        return cnpField.getText();
    }

    public String getId() {
        return idField.getText();
    }
}