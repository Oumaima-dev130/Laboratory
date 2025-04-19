package view;

import javax.swing.*;
import java.awt.*;

public class Card extends JPanel {
    public Card(String title, int value) {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(350, 180));

        // Panel gauche : texte
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.white);
       
        

        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        valueLabel.setForeground(Color.white);

        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        // Panel droit : cercle
        CircleProgress progress = new CircleProgress(value); // value max 100

        // Layout
        add(textPanel, BorderLayout.CENTER);
        add(progress, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 30, 30, 30)); // semi-transparent
     

        // Fond principal
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 50, 30);

        g2.dispose();
    }

}
