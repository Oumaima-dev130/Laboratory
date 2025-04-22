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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class Inscription extends JFrame {
    private JTextField labCodeField, cniField, firstNameField, 
                       lastNameField, emailField, 
                       phoneNumberField, dateOfBirthField;
    private JPasswordField passwordField;
    private JRadioButton maleRadio, femaleRadio;
    private JComboBox<String> userTypeComboBox;
    private JButton submitButton;

    // Database Connection Parameters
    private static final String URL = "jdbc:mysql://localhost:3306/laboratory";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Constructor with pre-filled lab code and user type
    public Inscription(String prefilledLabCode, String prefilledUserType) {
        initializeFrame(prefilledLabCode, prefilledUserType);
    }

    private void initializeFrame(String prefilledLabCode, String prefilledUserType) {
        // Frame Setup
        setTitle("Laboratory User Registration");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Panel with Gradient Background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
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
        mainPanel.setLayout(new BorderLayout());

        // Registration Form Panel
        JPanel registrationPanel = createRegistrationPanel(prefilledLabCode, prefilledUserType);
        mainPanel.add(registrationPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createRegistrationPanel(String prefilledLabCode, String prefilledUserType) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("User Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // User Type Options
        String[] userTypes = {
            "Administrator", 
            "Laboratory Manager", 
            "Laboratory Technician", 
            "Inventory Manager", 
            "Quality Manager", 
            "Accountant"
        };

        // Registration Fields
        String[] labels = {
            "Laboratory Code:", 
            "CNI:", 
            "First Name:", 
            "Last Name:", 
            "Email:", 
            "Phone Number:", 
            "Password:", 
            "Date of Birth (YYYY-MM-DD):", 
            "Gender:", 
            "User Type:"
        };

        for (int i = 0; i < labels.length; i++) {
            // Label
            JLabel label = new JLabel(labels[i]);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            panel.add(label, gbc);

            // Field
            gbc.gridx = 1;
            switch (i) {
                case 0: // Laboratory Code
                    labCodeField = createTextField(prefilledLabCode, true);
                    panel.add(labCodeField, gbc);
                    break;
                case 1: // CNI
                    cniField = createTextField(null, false);
                    panel.add(cniField, gbc);
                    break;
                case 2: // First Name
                    firstNameField = createTextField(null, false);
                    panel.add(firstNameField, gbc);
                    break;
                case 3: // Last Name
                    lastNameField = createTextField(null, false);
                    panel.add(lastNameField, gbc);
                    break;
                case 4: // Email
                    emailField = createTextField(null, false);
                    panel.add(emailField, gbc);
                    break;
                case 5: // Phone Number
                    phoneNumberField = createTextField(null, false);
                    panel.add(phoneNumberField, gbc);
                    break;
                case 6: // Password
                    passwordField = createPasswordField();
                    panel.add(passwordField, gbc);
                    break;
                case 7: // Date of Birth
                    dateOfBirthField = createTextField(null, false);
                    panel.add(dateOfBirthField, gbc);
                    break;
                case 8: // Gender
                    panel.add(createGenderPanel(), gbc);
                    break;
                case 9: // User Type
                    userTypeComboBox = createUserTypeComboBox(userTypes, prefilledUserType);
                    panel.add(userTypeComboBox, gbc);
                    break;
            }
        }

        // Submit Button
        submitButton = createSubmitButton();
        gbc.gridx = 1;
        gbc.gridy = labels.length + 1;
        panel.add(submitButton, gbc);

        return panel;
    }

    private JTextField createTextField(String prefilledText, boolean isReadOnly) {
        JTextField textField = new JTextField(20) {
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
        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        if (prefilledText != null) {
            textField.setText(prefilledText);
        }
        
        if (isReadOnly) {
            textField.setEditable(false);
        }
        
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField(20) {
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
        
        passwordField.setOpaque(false);
        passwordField.setForeground(Color.WHITE);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        return passwordField;
    }

    private JPanel createGenderPanel() {
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setOpaque(false);
        
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        
        maleRadio.setForeground(Color.WHITE);
        femaleRadio.setForeground(Color.WHITE);
        
        maleRadio.setOpaque(false);
        femaleRadio.setOpaque(false);
        
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        
        return genderPanel;
    }

    private JComboBox<String> createUserTypeComboBox(String[] userTypes, String prefilledUserType) {
        JComboBox<String> comboBox = new JComboBox<>(userTypes);
        comboBox.setBackground(new Color(30, 40, 60));
        comboBox.setForeground(Color.WHITE);
        
        if (prefilledUserType != null) {
            comboBox.setSelectedItem(prefilledUserType);
            comboBox.setEnabled(false);
        }
        
        return comboBox;
    }

    private JButton createSubmitButton() {
        JButton button = new JButton("Submit") {
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
        button.setFont(new Font("Arial", Font.BOLD, 16));
        
        button.addActionListener(e -> {
            if (validateAndSubmitForm()) {
                JOptionPane.showMessageDialog(this, 
                    "Registration Successful!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new Login();
            }
        });
        
        return button;
    }

    private boolean validateAndSubmitForm() {
        // Validate all fields
        if (!validateFields()) {
            return false;
        }

        // Database insertion
        return insertUserData();
    }

    private boolean validateFields() {
        // Check for empty fields
        if (labCodeField.getText().trim().isEmpty() || 
            cniField.getText().trim().isEmpty() ||
            firstNameField.getText().trim().isEmpty() || 
            lastNameField.getText().trim().isEmpty() || 
            emailField.getText().trim().isEmpty() ||
            phoneNumberField.getText().trim().isEmpty() ||
            passwordField.getPassword().length == 0 ||
            dateOfBirthField.getText().trim().isEmpty() ||
            (!maleRadio.isSelected() && !femaleRadio.isSelected())) {
            
            JOptionPane.showMessageDialog(this, 
                "Please fill all fields", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate email
        if (!isValidEmail(emailField.getText().trim())) {
            JOptionPane.showMessageDialog(this, 
                "Invalid email format", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate phone number
        if (!isValidPhoneNumber(phoneNumberField.getText().trim())) {
            JOptionPane.showMessageDialog(this, 
                "Invalid phone number", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate date of birth
        try {
            LocalDate.parse(dateOfBirthField.getText().trim());
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid date format. Use YYYY-MM-DD", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Basic phone number validation
        String phoneRegex = "^\\+?\\d{10,14}$";
        return Pattern.matches(phoneRegex, phoneNumber);
    }
    
    private boolean insertUserData() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Start transaction
            conn.setAutoCommit(false);

            try {
                // Insert user information
                String sql = "INSERT INTO authentification " +
                    "(cni, firstname, lastname, email, phone_number, password, " +
                    "Date_de_naissance, gender, user_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, cniField.getText().trim());
                    stmt.setString(2, firstNameField.getText().trim());
                    stmt.setString(3, lastNameField.getText().trim());
                    stmt.setString(4, emailField.getText().trim());
                    stmt.setString(5, phoneNumberField.getText().trim());
                    stmt.setString(6, hashPassword(new String(passwordField.getPassword())));
                    stmt.setDate(7, Date.valueOf(dateOfBirthField.getText().trim()));
                    
                    // Determine selected gender
                    String gender = maleRadio.isSelected() ? "Male" : "Female";
                    stmt.setString(8, gender);
                    
                    stmt.setString(9, (String) userTypeComboBox.getSelectedItem());
                    
                    stmt.executeUpdate();
                }

                // Commit transaction
                conn.commit();
                return true;
            } catch (SQLException ex) {
                // Rollback in case of error
                conn.rollback();
                
                // Check for duplicate entry
                if (ex.getMessage().contains("Duplicate entry")) {
                    if (ex.getMessage().contains("email")) {
                        JOptionPane.showMessageDialog(this, 
                            "Email already exists", 
                            "Registration Error", 
                            JOptionPane.ERROR_MESSAGE);
                    } else if (ex.getMessage().contains("phone_number")) {
                        JOptionPane.showMessageDialog(this, 
                            "Phone number already exists", 
                            "Registration Error", 
                            JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Registration failed: Duplicate entry", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Database error: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
                return false;
            } finally {
                // Restore auto-commit to default
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Database connection error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Password Hashing Method
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            // Convert to hexadecimal representation
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Password hashing error", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Constructor for testing
    public Inscription() {
        this("SAMPLE-CODE", null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Inscription());
    }
}