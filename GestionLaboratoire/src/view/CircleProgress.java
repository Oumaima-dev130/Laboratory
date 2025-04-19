package view;

import javax.swing.*;
import java.awt.*;

public class CircleProgress extends JPanel {
    private int value;

    public CircleProgress(int value) {
        this.value = value;
        setPreferredSize(new Dimension(100, 100));
        setOpaque(false);
    }

    public void setValue(int value) {
        this.value = value;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int size = Math.min(getWidth(), getHeight());

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int thickness = 6;
        int arcAngle = (int) (value * 3.6); // 100% = 360°
        int margin = 10;

        // Fond (gris clair)
        g2.setColor(new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(thickness));
        g2.drawArc(margin, margin, size - 2 * margin, size - 2 * margin, 90, 360);

        // Arc de progression
        g2.setColor(Color.BLUE); // Teal
        g2.drawArc(margin, margin, size - 2 * margin, size - 2 * margin, 90, -arcAngle);

        // Texte au centre
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String text = "+" + value;
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g2.setColor(Color.white);
        g2.drawString(text, (size - textWidth) / 2, (size + textHeight) / 2 - 4);
    }
}
