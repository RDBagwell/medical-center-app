package gui.panels;

import gui.Navigator;
import helper.CredentialsValidator;
import helper.PasswordHasher;
import model.User;

import javax.swing.*;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AddUserPanel extends JPanel {

    public AddUserPanel(JFrame frame) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JTextField usernameField = new JTextField(77);
        JPasswordField passwordField = new JPasswordField(77);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);

        JButton addButton = new JButton("Add User");

        add(addButton);

        addButton.addActionListener(event -> {
            String userName = usernameField.getText().strip();
            String password = new String(passwordField.getPassword());

            List<String> errors = CredentialsValidator.validate(userName, password);
            if (!errors.isEmpty()) {
                JOptionPane.showMessageDialog(frame, String.join("\n", errors), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Optional<User> existing = User.getUser(userName);
                if (existing.isEmpty()) {
                    String hashed = PasswordHasher.hash(password);
                    Object[] data = {userName, hashed};
                    User.add(data);
                    Navigator.show(frame, new LoginPanel(frame));
                } else {
                    JOptionPane.showMessageDialog(frame, "User already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
