package gui.panels;

import gui.Navigator;
import service.UserService;

import javax.swing.*;
import java.awt.FlowLayout;
import java.util.List;

public class UpdateUserPanel extends JPanel {
    public UpdateUserPanel(JFrame frame) {
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
            String userName = usernameField.getText().strip();
            String password = new String(passwordField.getPassword());

            List<String> errors = UserService.changePassword(userName, password);
            if (!errors.isEmpty()) {
                JOptionPane.showMessageDialog(frame, String.join("\n", errors), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Navigator.show(frame, new LoginPanel(frame));
        });
    }
}
