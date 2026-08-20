# Reflection — 18 August 2026

## Topics covered

In this class, I learned:

- How to calculate the coordinates needed to draw a square.
- How the Java graphics coordinate system works.
- How a Maven-based Java project is organized.
- Why a Java project uses a `pom.xml` file.
- The roles of AWT, Swing, Java2D, and JavaFX in graphical applications.

---

## How to draw a square

A square has four equal sides and four right angles. To draw one, we need to know:

1. The side length.
2. The square's position, usually represented by its center or top-left corner.
3. The drawing coordinate system.

### 1. Take the side length as input

Let the square's side length be `s`. The width and height are both equal to this value:

```text
width = s
height = s
```

For example, if `s = 200`, the square is 200 pixels wide and 200 pixels high.

### 2. Find the center

If the square's center is provided, let it be `(cx, cy)`.

If no center is provided, the center of the canvas is a suitable default. For a canvas with width `W` and height `H`:

```text
cx = W / 2
cy = H / 2
```

For example, the center of a 500-by-500-pixel canvas is `(250, 250)`.

### 3. Understand the graphics coordinate system

In Java graphics:

- The origin `(0, 0)` is at the top-left corner of the component.
- The x-coordinate increases toward the right.
- The y-coordinate increases downward.

```text
(0, 0) ───────────────► x
   │
   │
   │
   ▼
   y
```

This is different from the Cartesian coordinate system used in mathematics, where positive y normally points upward.

### 4. Find the top-left corner

To move from the center to the top-left corner, subtract half the side length from both center coordinates:

```text
left = cx - s / 2
top  = cy - s / 2
```

The top-left corner is therefore:

```text
(left, top)
```

### 5. Calculate all four corners

Using the center `(cx, cy)` and side length `s`, the corners are:

| Corner | Coordinate |
|---|---|
| Top-left | `(cx - s/2, cy - s/2)` |
| Top-right | `(cx + s/2, cy - s/2)` |
| Bottom-left | `(cx - s/2, cy + s/2)` |
| Bottom-right | `(cx + s/2, cy + s/2)` |

Alternatively, if `(x, y)` is the top-left corner, the points are:

| Corner | Coordinate |
|---|---|
| Top-left | `(x, y)` |
| Top-right | `(x + s, y)` |
| Bottom-left | `(x, y + s)` |
| Bottom-right | `(x + s, y + s)` |

These four points can be joined with four lines. Java2D also provides `drawRect`, which performs this operation directly:

```java
g2d.drawRect(x, y, s, s);
```

Because the final two arguments are equal, the result is a square rather than a general rectangle. A filled square can be produced with:

```java
g2d.fillRect(x, y, s, s);
```

### Centering a square in a Swing component

The square can be kept at the center of a `JPanel`, even when the window is resized, by calculating its position inside `paintComponent`:

```java
int sideLength = 200;
int x = (getWidth() - sideLength) / 2;
int y = (getHeight() - sideLength) / 2;

g2d.drawRect(x, y, sideLength, sideLength);
```

The expressions subtract the square's size from the available space and divide the remaining space equally between the two sides.

---

## Structure of a Java Maven project

A standard Maven project follows a conventional directory structure:

```text
project-name/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/          # Application source code
│   │   └── resources/     # Application configuration and assets
│   └── test/
│       ├── java/          # Test source code
│       └── resources/     # Test configuration and assets
└── target/                # Compiled classes and generated build files
```

### Important directories

- `src/main/java`: Contains the application's Java source files.
- `src/main/resources`: Contains non-Java files required by the application, such as images and configuration files.
- `src/test/java`: Contains automated test classes.
- `src/test/resources`: Contains files required only by tests.
- `target`: Created by Maven and contains compiled classes, test reports, JAR files, and other generated output. It should not normally be edited manually.

Java files are organized into packages. For example:

```java
package com.drumilbhati;
```

This package normally corresponds to the directory:

```text
src/main/java/com/drumilbhati/
```

Following this convention makes projects easier for Maven, IDEs, and other developers to understand.

---

## Purpose and uses of `pom.xml`

`pom.xml` is Maven's **Project Object Model** file. It is the main configuration file for a Maven project.

It can define:

- Project identity through `groupId`, `artifactId`, and `version`.
- The Java source and target versions.
- Text encoding.
- External dependencies.
- Build plugins.
- Test configuration.
- Packaging type, such as `jar` or `war`.
- Additional repositories and project metadata.

A basic example is:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.drumilbhati</groupId>
    <artifactId>graphics-project</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
</project>
```

### Common Maven commands

| Command | Purpose |
|---|---|
| `mvn clean` | Deletes generated files in `target`. |
| `mvn compile` | Compiles the application source code. |
| `mvn test` | Compiles and runs automated tests. |
| `mvn package` | Builds the project and creates its distributable package, such as a JAR. |
| `mvn clean package` | Performs a clean build and creates the package. |

One benefit of Maven is that another developer can build the project using the same configuration without manually downloading and configuring every library.

---

## Java graphical frameworks

### AWT — Abstract Window Toolkit

AWT is Java's original graphical user interface toolkit. It provides:

- Windows and dialogs.
- Buttons, labels, text fields, and menus.
- Layout managers.
- Event handling.
- Basic drawing through `Graphics` and `Graphics2D`.

Examples of AWT classes include:

```java
Frame
Button
Color
Dimension
Graphics
Graphics2D
```

Many AWT interface components are heavyweight because they rely on native operating-system components.

### Swing

Swing is built on top of AWT and provides a larger collection of mostly lightweight GUI components. Swing class names commonly begin with `J`.

Examples include:

```java
JFrame
JPanel
JButton
JLabel
JTextField
JTable
```

A `JFrame` represents the application's main window, while a `JPanel` can organize components or provide a surface for custom drawing.

A basic Swing window can be created as follows:

```java
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Square");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
```

Swing applications should create and update their components on the **Event Dispatch Thread (EDT)**. `SwingUtilities.invokeLater` schedules the interface creation on this thread.

### Java2D

Java2D is the drawing API used with AWT and Swing. It supports:

- Lines, rectangles, ellipses, curves, and other shapes.
- Text and fonts.
- Colors, gradients, and textures.
- Stroke width and style.
- Coordinate transformations such as rotation, scaling, and translation.
- Rendering hints such as anti-aliasing.

Custom Swing drawing is normally performed by extending `JPanel` and overriding `paintComponent(Graphics g)`:

```java
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();
    g2d.drawRect(100, 100, 200, 200);
    g2d.dispose();
}
```

Calling `super.paintComponent(g)` clears the previous image and redraws the panel background. Creating a copy of the graphics context prevents custom drawing settings from affecting other painting operations, and `dispose()` releases that copy when drawing is finished.

### JavaFX

JavaFX is another Java framework for desktop interfaces. It provides:

- A scene-graph-based interface model.
- CSS styling.
- Animation and visual effects.
- Property binding.
- Media support.
- FXML for describing interfaces separately from Java code.

JavaFX is distributed separately from modern JDK versions, whereas AWT, Swing, and Java2D are included with the standard desktop Java modules.

---

## Key takeaways

- A square can be defined by its center and side length or by its top-left corner and side length.
- Java's graphics origin is at the top-left, and its positive y-direction points downward.
- Equal width and height values are required when drawing a square with `drawRect` or `fillRect`.
- Maven's standard project layout separates application code, resources, tests, and generated files.
- `pom.xml` makes builds and dependency management consistent and repeatable.
- AWT provides the underlying windowing, event, and graphics facilities.
- Swing provides components such as `JFrame` and `JPanel`.
- Java2D provides the operations used to draw shapes inside Swing components.
- Swing GUI work should run on the Event Dispatch Thread.
