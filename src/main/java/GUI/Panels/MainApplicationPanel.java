package GUI.Panels;

import javax.swing.*;
import java.awt.*;

public class MainApplicationPanel extends JPanel{

    public MainApplicationPanel() {
        // Set layout for the main application panel
        setLayout(new BorderLayout());

        // Add components to the main panel
        JLabel welcomeLabel = new JLabel("Welcome to the Main Application Panel");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        // You can add more components and functionality as needed
    }

}
