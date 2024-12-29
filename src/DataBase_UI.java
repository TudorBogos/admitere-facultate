import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.ResultSet;

public class DataBase_UI extends JPanel {
    private JTextField numeField, prenumeField, cnpField, idField, notaField;
    private JTable table;
    private DefaultTableModel tableModel;

    public DataBase_UI() {
        setLayout(new BorderLayout());

        // Input Panel cu butoanele aferente
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
        JButton viewButton = new JButton("Vezi toti studentii");
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
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tablePanel = new JPanel(new GridBagLayout());
        tablePanel.add(scrollPane);
        scrollPane.setPreferredSize(new Dimension(450, 200));


        // Adaug componente la Jpanel
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        // Button actions
        insertButton.addActionListener(e -> {
            try {
                DatabaseManager.insertStudent();
                refreshTableStudent();
                clearFields();

            } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Eroare insertStudent: " + ex.getMessage());
        }
        }
        );

        updateButton.addActionListener(e -> {
            try {
                if(idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Introduceti un ID",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int rowsAffected = DatabaseManager.updateStudent(
                        idField.getText(),
                        numeField.getText(),
                        prenumeField.getText(),
                        cnpField.getText()
                );

                JOptionPane.showMessageDialog(this,
                        "Updated " + rowsAffected + " rows",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshTableStudent();
                clearFields();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error updating student: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                DatabaseManager.deleteStudent(id);
                refreshTableStudent();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Eroare deleteStudent: " + ex.getMessage());
            }
        });

        viewButton.addActionListener(e -> refreshTableStudent());

        clearButton.addActionListener(e -> clearFields());

        //functii de apelat pentru forma intitiala a GUI-ului
        refreshTableStudent();
    }

    public void refreshTableStudent() {
        try {
            ResultSet rs = DatabaseManager.selectAllStudents();
            tableModel.setRowCount(0);  // Clear existing rows

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
            JOptionPane.showMessageDialog(this, "Eroare refreshTable: " + e.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("");
        numeField.setText("");
        prenumeField.setText("");
        cnpField.setText("");
    }

    //functie pentru autoresize de coloane
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