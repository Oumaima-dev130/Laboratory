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

public class LaboratoryDashboard extends JFrame {
    private JPanel titlePanel;
    private JPanel centerPanel;

    public LaboratoryDashboard(int userId, String userName, String userType) {
        setTitle("Laboratory Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        titlePanel = createTitlePanel(userName);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel menuPanel = createSideMenu();
        mainPanel.add(menuPanel, BorderLayout.WEST);

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.BLACK);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 🟢 Show default page at startup
        showPage(new JLabel("Bienvenue dans le tableau de bord", SwingConstants.CENTER));

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTitlePanel(String userName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setPreferredSize(new Dimension(getWidth(), 50));

        JLabel titleLabel = new JLabel("Welcome, " + userName);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        panel.add(titleLabel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createSideMenu() {
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.BLACK);
        menuPanel.setPreferredSize(new Dimension(100, getHeight()));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createIconWithLabel("Home", "src/home.png", () -> showPage(new JLabel("Accueil du système", SwingConstants.CENTER))));
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createIconWithLabel("Analytic", "src/analyse.png", () -> showPage(new JLabel("Page Analytique", SwingConstants.CENTER))));
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createIconWithLabel("Users", "src/user.png", () -> showPage(new JLabel("Gestion des utilisateurs", SwingConstants.CENTER))));
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createIconWithLabel("Budget", "src/budget.png", () -> showPage(new JLabel("Gestion du budget", SwingConstants.CENTER))));
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createIconWithLabel("Equipment", "src/equipe.png", () -> showPage(new Equipment()))); // 👈
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createIconWithLabel("Settings", "src/setting.png", () -> showPage(new JLabel("Paramètres", SwingConstants.CENTER))));
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(createIconWithLabel("Logout", "src/logout.png", () -> {
            dispose();
            new Login();
        }));
        menuPanel.add(Box.createVerticalStrut(20));

        return menuPanel;
    }

    // ⬇️ Méthode pour créer un bouton avec une icône et une action personnalisée
    private JPanel createIconWithLabel(String text, String iconPath, Runnable onClick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton button = new JButton();
        button.setPreferredSize(new Dimension(36, 36));
        button.setMaximumSize(new Dimension(36, 36));
        button.setBackground(Color.BLACK);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaled));
        }

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(Color.BLACK);
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        labelPanel.add(label);

        panel.add(button);
        panel.add(Box.createVerticalStrut(2));
        panel.add(labelPanel);

        // 🔁 Action du clic
        button.addActionListener(e -> onClick.run());

        return panel;
    }

    // 🔄 Méthode pour afficher dynamiquement une page
    private void showPage(JComponent content) {
        centerPanel.removeAll();
        centerPanel.add(content, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LaboratoryDashboard(1, "John Doe", "Admin"));
    }
}

