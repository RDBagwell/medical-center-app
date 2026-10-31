package gui.panels;

import javax.swing.*;
import java.awt.BorderLayout;

public class MainApplicationPanel extends JPanel {
    public MainApplicationPanel() {
        setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the Main Application Panel");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);
    }
}
