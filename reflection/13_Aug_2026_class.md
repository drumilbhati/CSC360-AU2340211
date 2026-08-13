# Reflection — 13 August 2026

## Topic: Drawing a Square with Java Swing and Java2D

In this class, I learned how to create a desktop graphical user interface with **Java Swing** and draw a square using the **Java2D** API. The implementation is located in `lab_01/src/main/java/com/drumilbhati/SquareApp.java`.

## Learning objectives

- Create a window using Swing.
- Create a custom drawing surface using `JPanel`.
- Override `paintComponent(Graphics g)` to perform custom drawing.
- Convert a `Graphics` object into a `Graphics2D` object.
- Draw a square with equal width and height.
- Center the square when the window changes size.
- Start the user interface safely on Swing's Event Dispatch Thread.

## Swing and Java2D classes used

- `JFrame`: Creates the main application window.
- `JPanel`: Provides the surface on which the square is drawn.
- `SwingUtilities`: Starts the GUI on the Event Dispatch Thread.
- `Graphics`: The basic drawing context supplied to `paintComponent`.
- `Graphics2D`: Extends `Graphics` with features such as strokes and rendering hints.
- `Dimension`: Specifies the preferred or minimum size of a component.
- `Color`: Sets the background and square colors.
- `BasicStroke`: Controls the thickness of the square's outline.
- `RenderingHints`: Enables anti-aliasing to produce smoother edges.

## Program structure

The `SquareApp` class extends `JFrame`, so it represents the main window. Its constructor:

1. Sets the window title.
2. specifies what happens when the window is closed.
3. Sets a minimum window size.
4. Adds a custom `SquarePanel`.
5. Calls `pack()` so that the frame uses the panel's preferred size.
6. Centers the frame on the screen with `setLocationRelativeTo(null)`.

The nested `SquarePanel` class extends `JPanel`. It is responsible only for displaying the drawing.

## Custom painting

Swing calls the panel's `paintComponent(Graphics g)` method whenever the component needs to be displayed again, such as when the window opens, is resized, or is uncovered.

The method first calls:

```java
super.paintComponent(g);
```

This clears the previous drawing and paints the panel's background. It then creates a separate Java2D drawing context:

```java
Graphics2D g2d = (Graphics2D) g.create();
```

Creating a copy prevents changes to colors, strokes, and rendering settings from affecting other Swing painting operations. The copied context is released with `g2d.dispose()` after drawing is complete.

## Drawing and centering the square

A square has four equal sides, so the same value is used for its width and height:

```java
private final int squareSize = 200;
```

The top-left corner is calculated from the current panel dimensions:

```java
int x = (getWidth() - squareSize) / 2;
int y = (getHeight() - squareSize) / 2;
```

The square is then drawn with:

```java
g2d.drawRect(x, y, squareSize, squareSize);
```

Because `x` and `y` are recalculated every time the panel is painted, the square remains centered when the window is resized. `drawRect` creates an outline; `fillRect` could be used instead to create a filled square.

## Appearance

Anti-aliasing is enabled to improve the visual quality of the edges:

```java
g2d.setRenderingHint(
    RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON
);
```

The square's color and outline thickness are configured before it is drawn:

```java
g2d.setColor(new Color(63, 81, 181));
g2d.setStroke(new BasicStroke(3.0f));
```

## Event Dispatch Thread

Swing components should be created and updated on the **Event Dispatch Thread (EDT)**. The application uses:

```java
SwingUtilities.invokeLater(() -> {
    SquareApp app = new SquareApp();
    app.setVisible(true);
});
```

This schedules the creation of the window on the EDT and helps prevent threading problems in the user interface.

## Build and run

From the `lab_01` directory, compile the Maven project with:

```sh
mvn compile
```

After compilation, run the main class with:

```sh
java -cp target/classes com.drumilbhati.Main
```

The result is a Swing window containing a 200-by-200-pixel outlined square centered on a light background.

## Key takeaways

- Swing supplies GUI components, while Java2D supplies the drawing operations.
- Custom Swing graphics belong in `paintComponent`, not in a constructor or an arbitrary method.
- `super.paintComponent(g)` must be called before custom painting.
- A square is produced by passing equal width and height values to `drawRect`.
- Coordinates can be calculated from the panel's current dimensions to make graphics responsive.
- Swing interfaces should be started on the Event Dispatch Thread.
- Temporary `Graphics2D` contexts should be disposed after use.
