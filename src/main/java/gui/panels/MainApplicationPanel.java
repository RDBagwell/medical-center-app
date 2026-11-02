package gui.panels;

import gui.Navigator;

import javax.swing.*;
import java.awt.BorderLayout;

public class MainApplicationPanel extends JPanel {
    public MainApplicationPanel(JFrame frame) {

        setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the Main Application Panel");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton addUserButton = new JButton("Add user");
        JButton changePasswordButton = new JButton("Change password");

        buttons.add(addUserButton);
        buttons.add(changePasswordButton);

        add(buttons, BorderLayout.SOUTH);

        addUserButton.addActionListener(event -> {
            Navigator.show(frame, new AddUserPanel(frame));
        });
        changePasswordButton.addActionListener(event -> {
            Navigator.show(frame, new UpdateUserPanel(frame));
        });
    }
}
