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

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JPanel titlePanel;

    // Database Connection
    private static final String URL = "jdbc:mysql://localhost:3306/laboratory";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Login() {
        // Frame Setup
        setTitle("Laboratory Management System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(10, 10, 20));

        titlePanel = createTitlePanel();
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(); // You can customize this
        leftPanel.setPreferredSize(new Dimension(300, 0));
        leftPanel.setBackground(new Color(5, 5, 10));
        mainPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = createLoginPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(30, 50, 80), 5));
        add(mainPanel);

        makeDraggable();
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(15, 20, 30));

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(200, 220, 255));
        titleLabel.setBounds(100, 150, 400, 50);
        panel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Email");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameLabel.setForeground(new Color(150, 170, 200));
        usernameLabel.setBounds(100, 250, 400, 30);
        panel.add(usernameLabel);

        usernameField = createStyledTextField();
        usernameField.setBounds(100, 280, 400, 50);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setForeground(new Color(150, 170, 200));
        passwordLabel.setBounds(100, 350, 400, 30);
        panel.add(passwordLabel);

        passwordField = createStyledPasswordField();
        passwordField.setBounds(100, 380, 400, 50);
        panel.add(passwordField);

        // Enter key listener for password field
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });

        loginButton = createStyledButton("Login");
        loginButton.setBounds(100, 460, 400, 50);
        loginButton.addActionListener(e -> performLogin());
        panel.add(loginButton);

        registerButton = createStyledButton("Register");
        registerButton.setBounds(100, 530, 400, 50);
        registerButton.addActionListener(e -> {
            // Using your existing LabAccessCodeVerification class
            new LabAccessCodeVerification();
            dispose();
        });
        panel.add(registerButton);

        return panel;
    }
    
    private void performLogin() {
        String email = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both email and password", 
                "Login Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hash the password for authentication - using same hash algorithm as in registration
        String hashedPassword = hashPassword(password);
        
        try {
            // Get user details from database
            User user = authenticateUser(email, hashedPassword);
            
            if (user != null) {
                JOptionPane.showMessageDialog(this, 
                    "Login Successful!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Open the dashboard and pass user information
                new LaboratoryDashboard(user.getId(), user.getFullName(), user.getUserType());
                
                // Close the login window
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Invalid email or password", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Database error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 35, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(40, 60, 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        textField.setOpaque(false);
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textField.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 35, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(40, 60, 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        passwordField.setOpaque(false);
        passwordField.setForeground(Color.WHITE);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        return passwordField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(40, 70, 120) : new Color(30, 60, 100));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        return button;
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(15, 20, 30));
        panel.setPreferredSize(new Dimension(getWidth(), 30));

        JLabel titleLabel = new JLabel("Laboratory Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panel.add(titleLabel, BorderLayout.WEST);

        return panel;
    }

    private void makeDraggable() {
        MouseAdapter adapter = new MouseAdapter() {
            Point clickPoint;

            public void mousePressed(MouseEvent e) {
                clickPoint = e.getPoint();
            }

            public void mouseDragged(MouseEvent e) {
                Point current = e.getLocationOnScreen();
                setLocation(current.x - clickPoint.x, current.y - clickPoint.y);
            }
        };
        titlePanel.addMouseListener(adapter);
        titlePanel.addMouseMotionListener(adapter);
    }

    // Classe User pour stocker les informations utilisateur
    private class User {
        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String userType;
        
        public User(int id, String firstName, String lastName, String email, String userType) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.userType = userType;
        }
        
        public int getId() {
            return id;
        }
        
        public String getFullName() {
            return firstName + " " + lastName;
        }
        
        public String getUserType() {
            return userType;
        }
    }

    private User authenticateUser(String email, String hashedPassword) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM authentification WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String userType = rs.getString("user_type");
                
                return new User(id, firstName, lastName, email, userType);
            }
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    // Méthode de hachage du mot de passe (identique à celle utilisée dans Inscription)
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            // Convertir en représentation hexadécimale
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erreur de hachage du mot de passe", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return null;
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

            new Login();
        });
    }
}