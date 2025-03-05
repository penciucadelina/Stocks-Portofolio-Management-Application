package org.example.ui;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class LoginJFrame extends JFrame {
    public static final Map<String, String> accounts = new HashMap<>();

    public LoginJFrame() {
        //setup the window
        this.setTitle("Login");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(200, 200, 300, 200);

        //create and add components
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 100, 20);
        this.add(userLabel);

        JTextField userInputField = new JTextField();
        userInputField.setBounds(160, 20, 100, 20);
        this.add(userInputField);

        JLabel pwdLabel = new JLabel("Password:");
        pwdLabel.setBounds(20, 60, 100, 20);
        this.add(pwdLabel);

        JPasswordField pwdInputField = new JPasswordField();
        pwdInputField.setBounds(160, 60, 100, 20);
        this.add(pwdInputField);

        JButton button = new JButton("Login");
        button.setBounds(20, 100, 240, 20);
        this.add(button);

        // add event listener to button
        button.addActionListener(e -> verifyCredentials(userInputField, pwdInputField));

        //initialize accounts
        initAccounts();

        //display
        this.setVisible(true);
    }


    private void verifyCredentials(JTextField userInputField, JPasswordField pwdInputField) {
        String username = userInputField.getText();
        String password = new String(pwdInputField.getPassword());


        if (checkCredentials(username, password)) {
            this.setVisible(false);
            new StockMarketJFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean checkCredentials(String username, String password) {
        String storedPassword = accounts.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    private void initAccounts() {
        accounts.put("user1", "password1");
        accounts.put("user2", "password2");
    }

}
