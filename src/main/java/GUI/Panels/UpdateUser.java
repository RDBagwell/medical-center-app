package GUI.Panels;

import Model.User;
import helper.CredentialsValidator;
import helper.PasswordHasher;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
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
            String[] columns = new String[]{"password"};
            String userName = usernameField.getText().strip();
            String password = new String(passwordField.getPassword());

            List<String> errors = CredentialsValidator.validate(userName, password);
            if (!errors.isEmpty()) {
                JOptionPane.showMessageDialog(frame, String.join("\n", errors), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Optional<User> existing = User.getUser(userName);
                if (existing.isPresent()) {
                    String hashed = PasswordHasher.hash(password);
                    Object[] data = {hashed};
                    User.updateUser(columns, data, "id = ?", existing.get().id());
                    LoginPanel loginPanel = new LoginPanel(frame);
                    // Add the main panel to the frame
                    frame.getContentPane().removeAll(); // Remove previous components
                    frame.add(loginPanel);
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "No such user", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
