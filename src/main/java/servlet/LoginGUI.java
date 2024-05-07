package servlet;

import javax.swing.*;

import GUI.Panels.*;

public class LoginGUI {

    public static void main(String[] args) {
        // Create a JFrame
        JFrame frame = new JFrame("Login");

        // Set the size of the frame
        frame.setSize(300, 300);

        // Set default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginPanel loginPanel = new LoginPanel(frame);

        // Add the login panel to the frame
        frame.add(loginPanel);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Set frame visibility
        frame.setVisible(true);
    }
}

