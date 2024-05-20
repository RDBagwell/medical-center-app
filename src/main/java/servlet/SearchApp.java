package servlet;

import Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SearchApp extends JFrame {
    private final JTextField searchField;
    private final DefaultTableModel tableModel;

    public SearchApp() {
        setTitle("Search Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        tableModel = new DefaultTableModel();
        JTable resultTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(resultTable);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(_ -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                executeSearch(searchTerm);
            } else {
                JOptionPane.showMessageDialog(SearchApp.this, "Please enter a search term", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void executeSearch(String searchTerm) {
        try {
//            // Establishing connection to the database
//            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdatabase", "username", "password");
//
//            // Creating SQL statement
//            String sql = "SELECT * FROM your_table WHERE column_name LIKE ?";
//            PreparedStatement statement = conn.prepareStatement(sql);
//            statement.setString(1, "%" + searchTerm + "%");

            // Executing the query
            ResultSet resultSet = User.getUser(searchTerm);

            // Clear previous table data
            tableModel.setRowCount(0);

            // Populate the table with search results
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            // Closing the resources
            resultSet.close();
        } catch (SQLException ex) {
//            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while executing the search", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchApp().setVisible(true));
    }
}
