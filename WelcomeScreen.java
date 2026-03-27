package com.mycompany.database;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("Library Loan System - Welcome");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(66, 103, 178)); // Blue
        headerPanel.setPreferredSize(new Dimension(600, 100));
        headerPanel.setLayout(new GridBagLayout());

        JLabel welcomeLabel = new JLabel("📚 Library Management System 📚");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        add(headerPanel, BorderLayout.NORTH);

        // --- Main Content Panel ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 242, 245)); // Light Gray
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel infoLabel = new JLabel("A simple CRUD application for managing members, books, and loans.");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        contentPanel.add(infoLabel, gbc);

        gbc.gridy = 1;
        JButton launchButton = new JButton("Launch Application");
        launchButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        launchButton.setBackground(new Color(34, 139, 34)); // Green
        launchButton.setForeground(Color.WHITE);
        launchButton.setFocusPainted(false);
        launchButton.setPreferredSize(new Dimension(200, 50));
        
        // Action to open the main frame and close the welcome screen
        launchButton.addActionListener(e -> {
            try {
                // Must ensure the driver is loaded before calling Database()
                Class.forName("com.mysql.cj.jdbc.Driver"); 
                Database databaseFrame = new Database();
                databaseFrame.setVisible(true);
                dispose(); // Close the welcome screen
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, 
                    "MySQL JDBC Driver not found. Ensure the JAR file is included in your project.", 
                    "Driver Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        contentPanel.add(launchButton, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Line 73 (approximately) - Using the fully qualified name to avoid compilation confusion.
        javax.swing.SwingUtilities.invokeLater(() -> {
            WelcomeScreen screen = new WelcomeScreen();
            screen.setVisible(true);
        });
    }
}