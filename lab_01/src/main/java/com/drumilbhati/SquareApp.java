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
        this(200);
    }

    public SquareApp(int squareSize) {
        setTitle("Swing Square (Outline Only)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        int panelDimension = Math.max(500, squareSize + 100);
        setMinimumSize(new Dimension(Math.min(400, panelDimension), Math.min(400, panelDimension)));

        // Add the custom panel that renders the square
        SquarePanel squarePanel = new SquarePanel(squareSize);
        add(squarePanel);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Custom JPanel responsible for drawing the square.
     */
    public static class SquarePanel extends JPanel {
        private final int squareSize;

        public SquarePanel() {
            this(200);
        }

        public SquarePanel(int squareSize) {
            this.squareSize = squareSize;
            int panelDimension = Math.max(500, squareSize + 100);
            setPreferredSize(new Dimension(panelDimension, panelDimension));
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
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.drawLine(x, y, x, y + squareSize);
            g2d.drawLine(x, y + squareSize, x + squareSize, y + squareSize);
            g2d.drawLine(x, y, x + squareSize, y);
            g2d.drawLine(x + squareSize, y, x + squareSize, y + squareSize);

            g2d.dispose();
        }
    }

    /**
     * Prints the help message and argument order to standard output.
     */
    public static void printHelp() {
        System.out.println("Usage: java com.drumilbhati.SquareApp <length>");
        System.out.println();
        System.out.println("Arguments (in order):");
        System.out.println("  1. <length>   The side length of the square in pixels (must be a positive integer)");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  -h, --help    Show this help message");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  java com.drumilbhati.SquareApp 200");
    }

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help") || args[0].equalsIgnoreCase("help")) {
            printHelp();
            return;
        }

        int length;
        try {
            length = Integer.parseInt(args[0]);
            if (length <= 0) {
                System.err.println("Error: Length must be a positive integer (got " + length + ").\n");
                printHelp();
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid length argument '" + args[0] + "'. Must be an integer.\n");
            printHelp();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            SquareApp app = new SquareApp(length);
            app.setVisible(true);
        });
    }
}

