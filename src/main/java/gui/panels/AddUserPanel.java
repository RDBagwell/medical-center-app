package gui.panels;

import gui.Navigator;
import service.UserService;

import javax.swing.*;
import java.awt.FlowLayout;
import java.util.List;

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

            List<String> errors = UserService.register(userName, password);
            if (!errors.isEmpty()) {
                JOptionPane.showMessageDialog(frame, String.join("\n", errors), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Navigator.show(frame, new LoginPanel(frame));
        });
    }
}
