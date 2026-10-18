package GUI.Panels;

import Model.User;
import helper.Helper;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;

public class UpdateUser extends JPanel {
    public UpdateUser(JFrame frame) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JTextField usernameField = new JTextField(77);
        JPasswordField passwordField = new JPasswordField(77);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);

        JButton updateButton = new JButton("Update User");

        add(updateButton);

        updateButton.addActionListener(event -> {
            String userName = usernameField.getText();
            String password = new String(passwordField.getPassword());
            try {
                Object[] data;
                data = new Object[]{
                        userName,
                        Helper.encryptPassword(password)
                };
                Optional<User> existing = User.getUser(userName);
                if (existing.isEmpty()) {
                    User.add(data);
                    LoginPanel loginPanel = new LoginPanel(frame);
                    // Add the main panel to the frame
                    frame.getContentPane().removeAll(); // Remove previous components
                    frame.add(loginPanel);
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "User already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
