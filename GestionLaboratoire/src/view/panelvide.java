package view;

import javax.swing.*;
import java.awt.*;

public class panelvide extends JPanel {

    public panelvide() {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(450, 300));
    }
}
