package GUI.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "password";

    public LoginPanel(JFrame frame) {
        setLayout(new GridLayout(3, 2));
        // Create text fields for username and password
        JTextField usernameField = new JTextField(200);
        JPasswordField passwordField = new JPasswordField(200);

        // Create labels for text fields
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        // Add components to the login panel
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);

        // Create a JButton for login
        JButton loginButton = new JButton("Login");

        // Add the login button to the login panel
        add(loginButton);

        // Add ActionListener to the login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the entered username and password
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check if the username and password are valid
                if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
                    // Create an instance of the MainApplicationPanel class
                    MainApplicationPanel mainPanel = new MainApplicationPanel();
                    
                    // Add the main panel to the frame
                    frame.getContentPane().removeAll(); // Remove previous components
                    frame.add(mainPanel);
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Clear the fields after checking
                usernameField.setText("");
                passwordField.setText("");
            }
        });

    }
}
