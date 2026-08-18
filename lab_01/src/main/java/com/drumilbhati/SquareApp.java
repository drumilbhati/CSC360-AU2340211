package com.drumilbhati;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Swing application that displays a rendered square centered in a window.
 */
public class SquareApp extends JFrame {

    public SquareApp() {
        setTitle("Swing Square (Outline Only)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 400));
        setLocationRelativeTo(null);

        // Add the custom panel that renders the square
        SquarePanel squarePanel = new SquarePanel();
        add(squarePanel);

        pack();
    }

    /**
     * Custom JPanel responsible for drawing the square.
     */
    public static class SquarePanel extends JPanel {
        private final int squareSize = 200;

        public SquarePanel() {
            setPreferredSize(new Dimension(500, 500));
            setBackground(new Color(245, 247, 250));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();

            // Enable anti-aliasing for smooth edges
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Calculate center coordinates
            int width = getWidth();
            int height = getHeight();

            int x = (width - squareSize) / 2;
            int y = (height - squareSize) / 2;

            // Draw square outline (no fill) with custom stroke
            g2d.setColor(Color.RED); // Vibrant indigo color
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.drawLine(100, 100, 100, 200);
            g2d.drawLine(200, 200, 100, 200);
            g2d.drawLine(100, 100, 200, 100);
            g2d.drawLine(200, 200, 200, 100);

            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SquareApp app = new SquareApp();
            app.setVisible(true);
        });
    }
}
