package servlet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.User;

public class LinkListExample extends JFrame {
    private final JTextField searchField;
    private final JTextArea resultArea;

    public LinkListExample() {
        setTitle("Search Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        resultArea = new JTextArea();

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().trim();
                if (!searchTerm.isEmpty()) {
                    executeSearch(searchTerm);
                } else {
                    JOptionPane.showMessageDialog(
                            LinkListExample.this,
                            "Please enter a search term",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    private void executeSearch(String searchTerm) {
        try {
            ResultSet resultSet = User.getUser(searchTerm);
            StringBuilder resultText = new StringBuilder();
            while (resultSet.next()) {
//                resultText.append(resultSet.getInt("id")).append("\n");
                resultText
                        .append(resultSet.getString("password"))
                        .append("\n")
                        .append("User Name: ")
                        .append(resultSet.getString("user_name"))
                        .append("\n");
            }
            resultArea.setText(resultText.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LinkListExample().setVisible(true);
            }
        });
    }
}
