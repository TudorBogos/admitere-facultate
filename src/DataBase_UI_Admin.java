import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.ResultSet;

public class DataBase_UI_Admin extends JPanel {
    private JTextField numeField, prenumeField, cnpField, idField, notaField;
    private JTable table;
    private DefaultTableModel tableModel;

    public DataBase_UI_Admin() {
        setLayout(new BorderLayout());

        // Input Panel with their respective fields
        JPanel inputPanel = new JPanel(new GridLayout(10, 1, 0, 5));
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

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton insertButton = new JButton("Insert");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton viewButton = new JButton("Filter/Update tabela studenti");
        JButton clearButton = new JButton("Clear field-uri");

        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(clearButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));


        // Tabel setup
        String[] columnNames = {"ID", "Nume", "Prenume", "CNP", "Nota"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tablePanel = new JPanel(new GridBagLayout());
        tablePanel.add(scrollPane);
        scrollPane.setPreferredSize(new Dimension(450, 215));


        // Add all panels to the main panel
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);


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
                        numeField.getText(),
                        prenumeField.getText(),
                        validCNP(),
                        validNota()
                );

                JOptionPane.showMessageDialog(this,
                        "S-au inserat " + rowsAffected + " rânduri",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
                selectTableStudent();
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

            // Finally do the update
            try {
                int rowsAffected = DatabaseManager.updateStudent(
                        validID(),
                        numeField.getText(),
                        prenumeField.getText(),
                        validCNP(),
                        validNota()
                );

                JOptionPane.showMessageDialog(this,
                        "S-au actualizat " + rowsAffected + " rânduri",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
                selectTableStudent();
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

            try {
                int rowsAffected = DatabaseManager.deleteStudent(validID());

                JOptionPane.showMessageDialog(this,
                        "S-au șters " + rowsAffected + " rânduri",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
                selectTableStudent();
                clearFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Eroare la ștergere student: ID-ul trebuie să fie un număr valid",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Eroare la ștergere student: " + ex.getMessage(),
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        /// Buton view
        viewButton.addActionListener(e -> {
            if(verifyEmptyView()){
                selectTableStudent();
            }else{
            }
        });

        clearButton.addActionListener(e -> clearFields());

        //Function to make the table visible at initialization
        selectTableStudent();
    }


    /// Functie pentru validarea ID-ului
    public int validID(){
        return idField.getText().isEmpty() ? 0 : Integer.parseInt(idField.getText());
    }

    /// Functie pentru validarea CNP-ului
    public String validCNP() throws Exception{
        String cnp = cnpField.getText();
        if(cnp.length() != 13){
            throw new Exception("CNP-ul trebuie să aibă 13 cifre sau sa fie gol");
        }
        for(int i = 0; i < cnp.length(); i++){
            if(!Character.isDigit(cnp.charAt(i))){
                throw new Exception("CNP-ul trebuie să conțină doar cifre");
            }
        }
        return cnp;
    }

    /// Functie pentru validarea notei
    public float validNota(){
         return notaField.getText().isEmpty() ? 0f : Float.parseFloat(notaField.getText());
    }

    /// Verifies that the fields are empty for insert. Returns true if any of the fields are empty
    public boolean verifyEmptyInsert(){
        return numeField.getText().isEmpty() || prenumeField.getText().isEmpty() || cnpField.getText().isEmpty() || notaField.getText().isEmpty();
    }
    /// Verifies that the fields are empty for viewButton. Returns true if all of the fields are empty
    public boolean verifyEmptyView(){
        return idField.getText().isEmpty() && numeField.getText().isEmpty() && prenumeField.getText().isEmpty() && cnpField.getText().isEmpty() && notaField.getText().isEmpty();
    }

    public void selectTableStudent() {
        try {
            ResultSet rs = DatabaseManager.selectAllStudents();
            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("idStudent"),
                        rs.getString("Nume"),
                        rs.getString("Prenume"),
                        rs.getString("CNP"),
                        rs.getFloat("Nota")
                };
                tableModel.addRow(row);
            }

            autoResizeColumns();
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            rs.close();
            DatabaseManager.closeConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Eroare la reîmprospătare tabel.\n " + e.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /// Functie de filtrare a tabelului
    public void filterTableStudent() {
        try {
            ResultSet rs = DatabaseManager.filterStudents(
                    validID(),
                    numeField.getText(),
                    prenumeField.getText(),
                    validCNP(),
                    validNota()
            );
            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("idStudent"),
                        rs.getString("Nume"),
                        rs.getString("Prenume"),
                        rs.getString("CNP"),
                        rs.getFloat("Nota")
                };
                tableModel.addRow(row);
            }

            autoResizeColumns();
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            rs.close();
            DatabaseManager.closeConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Eroare la reîmprospătare tabel.\n " + e.getMessage(),
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
    }

    private void autoResizeColumns() {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            // Get width of column header
            TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Object headerValue = tableColumn.getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, column);
            preferredWidth = Math.max(preferredWidth, headerComp.getPreferredSize().width);

            // Get maximum width of column data
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width;
                preferredWidth = Math.max(preferredWidth, width);
            }

            preferredWidth += table.getIntercellSpacing().width + 10; // Add margin

            // Set the width
            tableColumn.setPreferredWidth(preferredWidth);
        }
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