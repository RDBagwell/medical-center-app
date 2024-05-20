package servlet;

import javax.swing.*;
import GUI.Panels.*;
import java.awt.*;
import java.awt.event.*;
import helper.AutoLogout;

public class LoginGUI {

    public static void main(String[] args) {
//        AutoLogout autoLogout = new AutoLogout();
//        autoLogout.startLogoutTimer();

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
        frame.add(updateUser, BorderLayout.CENTER);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                System.out.println("GUI is closing");
//                Timer timer = new Timer(3000, new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        System.exit(0);
//                    }
//                });
//                timer.setRepeats(false);
//                timer.start();
//            }
//        });

//        frame.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                autoLogout.resetLogoutTimer();
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                autoLogout.resetLogoutTimer();
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                autoLogout.resetLogoutTimer();
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                autoLogout.resetLogoutTimer();
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                autoLogout.resetLogoutTimer();
//            }
//        });
//
//        frame.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                autoLogout.resetLogoutTimer();
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                autoLogout.resetLogoutTimer();
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                autoLogout.resetLogoutTimer();
//            }
//        });

        // Set frame visibility
        frame.setVisible(true);
    }
}

