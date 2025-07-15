package org.example.main;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("Simple Tetris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }
}