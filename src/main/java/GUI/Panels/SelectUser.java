package GUI.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.User;

public class SelectUser extends JPanel {
    public SelectUser(JFrame frame){
        try {
            ResultSet users = User.getUsers();
            ArrayList<String> options = new ArrayList<>();
            options.add(" ");
            while (users.next()){
                options.add(users.getString("user_name"));
            }
            JComboBox<String> userComboBox = new JComboBox<>(options.toArray(new String[0]));
            add(userComboBox);
            userComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectOption = (String) userComboBox.getSelectedItem();
                    assert selectOption != null;
                    if(!selectOption.isEmpty()){
                        System.out.println(selectOption);
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
