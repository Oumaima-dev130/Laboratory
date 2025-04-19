package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
          //fenetre principale
            JFrame frame = new JFrame("Gestion Laboratoire");
            frame.setSize(1000, 800);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//pour fermer prog qd ferme fenetre

            //panel main
            JPanel mainPanel = new JPanel();
            mainPanel.setBackground(Color.black);
            mainPanel.setLayout(new BorderLayout());//borderlayout permet de structurer et placer des composants a l'int de panel 
           //menu
            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));//BoxLayout.Y_AXIS utiliser pour  Les éléments sont empilés verticalement 
            menuPanel.setBackground(new Color(30, 30, 30));
            menuPanel.setPreferredSize(new Dimension(100, 100));
            menuPanel.add(Box.createVerticalStrut(150));//ajoute espace vide depuis haut

            menuPanel.add(createMenuButton("Home", "src/resources/home.png"));
            menuPanel.add(Box.createVerticalStrut(60));
            menuPanel.add(createMenuButton("Analytic", "src/resources/Analyse.png"));
            menuPanel.add(Box.createVerticalStrut(60));
            menuPanel.add(createMenuButton("Users", "src/resources/user.png"));
            menuPanel.add(Box.createVerticalStrut(60));
            menuPanel.add(createMenuButton("Budget", "src/resources/budget.png"));
            menuPanel.add(Box.createVerticalStrut(60));
            menuPanel.add(createMenuButton("Equipment", "src/resources/material.png"));
            menuPanel.add(Box.createVerticalStrut(60));
            menuPanel.add(createMenuButton("Settings", "src/resources/setting.png"));
            menuPanel.add(Box.createVerticalStrut(200));
            menuPanel.add(createMenuButton("Logout", "src/resources/logout.png"));

            // Titre
            JLabel titleLabel = new JLabel("Users");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);//pour centrer titre

            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(Color.black);
            titlePanel.add(titleLabel);
           

            // liste 
            JPanel dropdownPanel = new JPanel();
            dropdownPanel.setBackground(Color.black);
            dropdownPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            String[] categories = {"All Categories", "Administrator", "Laboratory Manager", "Laboratory Technician", "Inventory Manager", "Quality Manager", "Accountant"};
            JComboBox<String> categoryDropdown = new JComboBox<>(categories);
            categoryDropdown.setForeground(Color.WHITE);
            categoryDropdown.setFont(new Font("Arial", Font.PLAIN, 18));
            categoryDropdown.setBackground(new Color(30, 30, 30));
            categoryDropdown.setPreferredSize(new Dimension(180, 35));
            dropdownPanel.add(categoryDropdown);

            String[] timeframes = {"Last 7 days", "Last 30 days", "Last year"};
            JComboBox<String> timeframeDropdown = new JComboBox<>(timeframes);
            timeframeDropdown.setForeground(Color.WHITE);
            timeframeDropdown.setFont(new Font("Arial", Font.PLAIN, 18));
            timeframeDropdown.setBackground(new Color(30, 30, 30));
            timeframeDropdown.setPreferredSize(new Dimension(180, 35));
            dropdownPanel.add(timeframeDropdown);

            // Cards utilisateurs
            JPanel cardsPanel = new JPanel();
            cardsPanel.setBackground(Color.black);
            cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 100, 20));//organise l'emplacement des cmpts,FlowLayout.LEFT : pour dire aligne les cmpls depuis gauche vers droite pour 100 c pixels entre les cards et pour 20 c emplacemnet verticale depuis en haut
            cardsPanel.add(new Card("Total Users", countUsers()));
            cardsPanel.add(new Card("Active Users", countActiveUsers()));
            cardsPanel.add(new Card("Inactive Users", countInactiveUsers()));


            // Panels vides
            JPanel Panelvides = new JPanel();
            Panelvides.setBackground(Color.black);
            Panelvides.setLayout(new FlowLayout(FlowLayout.LEFT, 150, 20));
            Panelvides.add(new panelvide());
            Panelvides.add(new panelvide());
            Panelvides.add(new panelvide());

            // Contenu principal
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(Color.black);
            contentPanel.add(titlePanel);
            contentPanel.add(cardsPanel);
            contentPanel.add(Panelvides);
            contentPanel.add(Box.createVerticalStrut(20));
            contentPanel.add(dropdownPanel);
            contentPanel.add(createUserTable());
            
            frame.add(menuPanel, BorderLayout.WEST);
            frame.add(mainPanel, BorderLayout.CENTER);
            mainPanel.add(contentPanel, BorderLayout.NORTH);

            frame.setVisible(true);
        });
    }
//clair
    private static JPanel createMenuButton(String text, String iconPath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));//pour empiler verticalement premierement icon puis text
       panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon icon = recolorIcon(iconPath, Color.WHITE);//ImageIcon est un class en java swing 
        ImageIcon resizedIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));//icon.getImage() pour recuperer image  etgetScaledInstance(24, 24, pour Redimensionne l’image à 24x24 pixels, 

        JLabel iconLabel = new JLabel(resizedIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(Color.WHITE);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(textLabel);

        return panel;
    }
//clair
    private static ImageIcon recolorIcon(String path, Color color) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int pixel = img.getRGB(x, y);
                    int alpha = (pixel >> 24) & 0xff;
                    if (alpha > 0) {
                        newImg.setRGB(x, y, (color.getRGB() & 0x00FFFFFF) | (alpha << 24));
                    }
                }
            }

            return new ImageIcon(newImg);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JScrollPane createUserTable() {
        String[] columns = {"Id","Name", "Category", "Status", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);//DefaultTableModel C’est une classe Java qui sert à gérer les données d’un JTable et pour 0 c nombre de colonnes au depart

        JTable table = new JTable(model);
        table.setRowHeight(50);
        table.setBackground(Color.black);
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 20));
        table.setShowVerticalLines(false); 

        // Personnalisation de l'en-tête du tableau
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.HANGING_BASELINE, 20));

        // Centrer le texte des cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Appliquer ce renderer à toutes les colonnes
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(10000, 10000));
        scrollPane.getViewport().setBackground(Color.BLACK);

        RecupData(model);
        return scrollPane;
    }



    private static void RecupData(DefaultTableModel model) {
        String url = "jdbc:mysql://localhost:3306/users";
        String user = "root"; 
        String password = ""; 

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT id,fullname, category, status, email FROM users";
            Statement stmt = conn.createStatement();//permet d'executer une requete
            ResultSet rs = stmt.executeQuery(sql);//contient result deun requete select

            while (rs.next()) {//pour next c pour deplacer curseur vers next ligne 
                Object[] row = {//tab d'objet pour contenir n’importe quel type de données
                		rs.getString("id"),
                    rs.getString("fullname"),
                    rs.getString("category"),
                    rs.getString("status"),
                    rs.getString("email"),
                };
                model.addRow(row);
            }
        } catch (SQLException e) {//Capture les erreurs liées à SQL une erreur se produit pendant :la connexion à MySQL exécution d’une requête SQL ou la lecture des données
            JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données : " + e.getMessage());
        }
    }
    private static int countUsers() {
        return countQuery("SELECT COUNT(*) FROM users");
    }

    private static int countActiveUsers() {
        return countQuery("SELECT COUNT(*) FROM users WHERE status = 'active'");
    }

    private static int countInactiveUsers() {
        return countQuery("SELECT COUNT(*) FROM users WHERE status = 'inactive'");
    }

    private static int countQuery(String sql) {
        String url = "jdbc:mysql://localhost:3306/users";
        String user = "root"; 
        String password = ""; 
        int count = 0;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du comptage : " + e.getMessage());
        }
        return count;
    }

}
