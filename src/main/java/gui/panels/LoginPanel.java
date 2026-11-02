package gui.panels;

import gui.Navigator;
import service.UserService;

import javax.swing.*;
import java.awt.FlowLayout;

public class LoginPanel extends JPanel {

    public LoginPanel(JFrame frame) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextField usernameField = new JTextField(77);
        JPasswordField passwordField = new JPasswordField(77);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);

        JButton loginButton = new JButton("Login");

        add(loginButton);

        loginButton.addActionListener(event -> {
            String username = usernameField.getText().strip();
            String password = new String(passwordField.getPassword());

            if (UserService.authenticate(username, password)) {
                Navigator.show(frame, new MainApplicationPanel(frame));
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
            usernameField.setText("");
            passwordField.setText("");
        });
    }
}
