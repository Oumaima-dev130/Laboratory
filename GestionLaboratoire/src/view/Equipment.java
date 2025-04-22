/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author AYMEN
 */
// Equipment.java - Complete working version with JDBC and charts

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class Equipment extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JPanel chartPanel;
    private StatCard totalCard, goodCard, brokenCard, maintenanceCard;

    private final String DB_URL = "jdbc:mysql://localhost:3306/laboratoire";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    public Equipment() {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        // Top Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(Color.DARK_GRAY);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        totalCard = new StatCard("Total", Color.CYAN);
        goodCard = new StatCard("Good", new Color(76, 175, 80));
        brokenCard = new StatCard("Broken", new Color(244, 67, 54));
        maintenanceCard = new StatCard("Maintenance", new Color(255, 193, 7));

        statsPanel.add(totalCard);
        statsPanel.add(goodCard);
        statsPanel.add(brokenCard);
        statsPanel.add(maintenanceCard);

        add(statsPanel, BorderLayout.NORTH);

        // Chart Panel
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setPreferredSize(new Dimension(400, 250));
        chartPanel.setBackground(Color.DARK_GRAY);
        add(chartPanel, BorderLayout.WEST);

        // Table Panel
        model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Status"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // CRUD Panel
        JPanel crudPanel = new JPanel();
        JButton addBtn = new JButton("Add"), editBtn = new JButton("Edit"), deleteBtn = new JButton("Delete");
        crudPanel.add(addBtn);
        crudPanel.add(editBtn);
        crudPanel.add(deleteBtn);
        add(crudPanel, BorderLayout.SOUTH);

        // Button actions
        addBtn.addActionListener(e -> addEquipment());
        editBtn.addActionListener(e -> editEquipment());
        deleteBtn.addActionListener(e -> deleteEquipment());

        loadEquipmentsFromDB();
    }

    private void loadEquipmentsFromDB() {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM equipement")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nom");
                String category = rs.getString("categorie");
                String status = rs.getString("status");
                model.addRow(new Object[]{id, name, category, status});
            }
            updateStats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEquipment() {
        JTextField nameField = new JTextField();
        JTextField catField = new JTextField();
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Good", "Broken", "Maintenance"});

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Category:")); panel.add(catField);
        panel.add(new JLabel("Status:")); panel.add(statusBox);

        int res = JOptionPane.showConfirmDialog(null, panel, "Add Equipment", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO equipement(nom, categorie, status) VALUES (?, ?, ?)");
                ps.setString(1, nameField.getText());
                ps.setString(2, catField.getText());
                ps.setString(3, statusBox.getSelectedItem().toString());
                ps.executeUpdate();
                loadEquipmentsFromDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void editEquipment() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);
        JTextField nameField = new JTextField(model.getValueAt(row, 1).toString());
        JTextField catField = new JTextField(model.getValueAt(row, 2).toString());
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Good", "Broken", "Maintenance"});
        statusBox.setSelectedItem(model.getValueAt(row, 3).toString());

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Category:")); panel.add(catField);
        panel.add(new JLabel("Status:")); panel.add(statusBox);

        int res = JOptionPane.showConfirmDialog(null, panel, "Edit Equipment", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                PreparedStatement ps = conn.prepareStatement("UPDATE equipement SET nom=?, categorie=?, status=? WHERE id=?");
                ps.setString(1, nameField.getText());
                ps.setString(2, catField.getText());
                ps.setString(3, statusBox.getSelectedItem().toString());
                ps.setInt(4, id);
                ps.executeUpdate();
                loadEquipmentsFromDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteEquipment() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(null, "Delete this equipment?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM equipement WHERE id=?");
                ps.setInt(1, id);
                ps.executeUpdate();
                loadEquipmentsFromDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateStats() {
        int total = model.getRowCount();
        int good = 0, broken = 0, maintenance = 0;

        for (int i = 0; i < total; i++) {
            String status = model.getValueAt(i, 3).toString().toLowerCase();
            switch (status) {
                case "good" -> good++;
                case "broken" -> broken++;
                case "maintenance" -> maintenance++;
            }
        }

        totalCard.setValue(String.valueOf(total));
        goodCard.setValue(String.valueOf(good));
        brokenCard.setValue(String.valueOf(broken));
        maintenanceCard.setValue(String.valueOf(maintenance));
        updateChart(good, broken, maintenance);
    }

    private void updateChart(int good, int broken, int maintenance) {
        chartPanel.removeAll();
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Good", good);
        dataset.setValue("Broken", broken);
        dataset.setValue("Maintenance", maintenance);

        JFreeChart chart = ChartFactory.createPieChart("Equipment Status", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Good", new Color(76, 175, 80));
        plot.setSectionPaint("Broken", new Color(244, 67, 54));
        plot.setSectionPaint("Maintenance", new Color(255, 193, 7));
        plot.setBackgroundPaint(Color.DARK_GRAY);

        ChartPanel chartP = new ChartPanel(chart);
        chartPanel.add(chartP, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    class StatCard extends JPanel {
        private JLabel valueLabel;

        public StatCard(String title, Color bg) {
            setLayout(new BorderLayout());
            setBackground(bg);
            setPreferredSize(new Dimension(150, 80));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

            valueLabel = new JLabel("0", SwingConstants.CENTER);
            valueLabel.setForeground(Color.WHITE);
            valueLabel.setFont(new Font("Arial", Font.BOLD, 24));

            add(titleLabel, BorderLayout.NORTH);
            add(valueLabel, BorderLayout.CENTER);
        }

        public void setValue(String value) {
            valueLabel.setText(value);
        }
    }
}

