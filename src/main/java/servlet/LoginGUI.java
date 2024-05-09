package servlet;

import javax.swing.*;

import GUI.Panels.*;

import java.awt.*;

public class LoginGUI {

    public static void main(String[] args) {
        // Create a JFrame
        JFrame frame = new JFrame("Login");
        frame.setSize(800, 600);

        // Set default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginPanel loginPanel = new LoginPanel(frame);

        // Add the login panel to the frame
        frame.getContentPane().add(loginPanel, BorderLayout.CENTER);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Set frame visibility
        frame.setVisible(true);
    }
}

