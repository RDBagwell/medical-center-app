package GUI.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.*;

import helper.Helper;
import Model.User;

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

        updateButton.addActionListener((ActionEvent _) -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            try {
                Object[] data;
                data = new Object[]{
                        username,
                        Helper.encryptPassword(password)
                };
                ResultSet user = User.getUser(username);
                if(!user.next()){
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
