package com.drumilbhati;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SquareApp app = new SquareApp();
            app.setVisible(true);
        });
    }
}

