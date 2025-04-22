/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author AYMEN
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LabAccessCodeVerification extends JFrame {
    private JTextField accessCodeField;
    private JButton verifyButton, backButton;
    private JPanel mainPanel;
    
    // Database Connection
    private static final String URL = "jdbc:mysql://localhost:3306/laboratory?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public LabAccessCodeVerification() {
        // Frame setup
        setTitle("Laboratory Access Verification");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with gradient background
        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(20, 30, 50), 
                    getWidth(), getHeight(), new Color(10, 15, 25)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.dispose();
            }
        };
        
        // Content panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Laboratory Access Verification");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 30, 20);
        panel.add(titleLabel, gbc);
        
        // Instructions
        JLabel instructionLabel = new JLabel("<html>Enter the laboratory access code provided by your administrator to continue with registration.</html>");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionLabel.setForeground(new Color(200, 200, 220));
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 30, 30, 30);
        panel.add(instructionLabel, gbc);
        
        // Access code field
        JLabel codeLabel = new JLabel("Access Code:");
        codeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        codeLabel.setForeground(Color.WHITE);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 30, 10, 5);
        panel.add(codeLabel, gbc);
        
        accessCodeField = createStyledTextField();
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 5, 10, 30);
        panel.add(accessCodeField, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        
        verifyButton = createStyledButton("Verify");
        backButton = createStyledButton("Back to Login");
        
        buttonPanel.add(verifyButton);
        buttonPanel.add(backButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 20, 20);
        panel.add(buttonPanel, gbc);
        
        // Add action listeners
        verifyButton.addActionListener(e -> verifyAccessCode());
        
        backButton.addActionListener(e -> {
            dispose();
            new Login();
        });
        
        // Add key listener for Enter key
        accessCodeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    verifyAccessCode();
                }
            }
        });
        
        return panel;
    }
    
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(new Color(30, 40, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Border
                g2.setColor(new Color(50, 70, 90));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        textField.setOpaque(false);
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        textField.setCaretColor(Color.WHITE);
        
        return textField;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Button state colors
                if (getModel().isPressed()) {
                    g2.setColor(new Color(40, 60, 90));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(50, 70, 100));
                } else {
                    g2.setColor(new Color(40, 50, 80));
                }
                
                // Rounded background
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Border
                g2.setColor(new Color(60, 80, 110));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));
        
        return button;
    }
    
    private void verifyAccessCode() {
        String code = accessCodeField.getText().trim();
        
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter an access code.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (verifyCodeFromDatabase(code)) {
            // Code valid, mark as used in the database
            markCodeAsUsed(code);
            
            JOptionPane.showMessageDialog(this,
                "Access code verified successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
                
            // Open the registration form
            dispose();
            new Inscription(code, null);
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid or already used access code.",
                "Verification Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean verifyCodeFromDatabase(String code) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM LabAccessCodes WHERE access_code = ? AND is_used = FALSE";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if code exists and is not used
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void markCodeAsUsed(String code) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE LabAccessCodes SET is_used = TRUE WHERE access_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Not showing an error message here since verification was successful
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Proper Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new LabAccessCodeVerification();
        });
    }
}

