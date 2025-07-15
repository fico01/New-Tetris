package org.example.main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public GamePanel() {

        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null);
    }

}
