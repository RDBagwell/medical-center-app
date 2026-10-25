package servlet;

import GUI.Panels.AddUser;
import GUI.Panels.LoginPanel;
import GUI.Panels.UpdateUser;
import helper.AutoLogout;

import javax.swing.*;
import java.awt.*;

public class LoginGUI {

    public static void main(String[] args) {
        AutoLogout autoLogout = new AutoLogout();
        autoLogout.startLogoutTimer();

        // Create a JFrame
        JFrame frame = new JFrame("Login");
        frame.setSize(800, 600);

        // Set default close operation
//        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginPanel loginPanel = new LoginPanel(frame);
        AddUser addUser = new AddUser(frame);
        UpdateUser updateUser = new UpdateUser(frame);

        // Add the login panel to the frame
        frame.add(addUser, BorderLayout.CENTER);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Set frame visibility
        frame.setVisible(true);
    }
}

