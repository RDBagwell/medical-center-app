package GUI.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.*;

import helper.Helper;
import Model.User;

public class AddUser extends JPanel {

    public AddUser(JFrame frame) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        // Create text fields for username and password
        JTextField usernameField = new JTextField(300);
        JPasswordField passwordField = new JPasswordField(300);

        // Create labels for text fields
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        // Add components to the login panel
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);

        // Create a JButton for login
        JButton addButton = new JButton("Add User");

        // Add the login button to the login panel
        add(addButton);

        // Add ActionListener to the login button
        addButton.addActionListener((ActionEvent _) -> {
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

//            User.add();

//            if (Helper.checkUser(username,password)) {
//                // Create an instance of the MainApplicationPanel class
//                MainApplicationPanel mainPanel = new MainApplicationPanel();
//                // Add the main panel to the frame
//                frame.getContentPane().removeAll(); // Remove previous components
//                frame.add(mainPanel);
//                frame.revalidate();
//                frame.repaint();
//            } else {
//
//            }
        });
    }
}
