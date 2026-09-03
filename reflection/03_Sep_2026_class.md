# Reflection — 3 September 2026

## Topics covered

- Text terminals compared with graphical user interfaces
- Accessing remote computers with SSH
- Finding matches between two lists
- Visualizing matching elements with arrows
- Reproducing software failures
- Defining JavaFX interfaces with FXML

---

## Text terminals and graphical user interfaces

A text terminal provides a command-line interface in which users enter commands and receive text output. A graphical user interface (GUI) provides visual components such as windows, menus, buttons, icons, and dialogs.

Neither interface is always better; the appropriate choice depends on the user and the task.

### Advantages of a text terminal

- **Low resource usage:** A terminal usually requires less memory, processing power, and network bandwidth than a GUI.
- **Fast interaction:** Experienced users can execute commands quickly without navigating through menus.
- **Automation:** Commands can be combined into scripts for consistent and repeatable workflows.
- **Remote access:** Text-based sessions work effectively over slow or unstable network connections.
- **Precise control:** Commands and options explicitly describe the requested operation.
- **Reproducibility:** Commands can be recorded, shared, and executed again.

Terminals are especially useful for software development, server administration, deployment, file processing, and automated tasks.

### Advantages of a GUI

- **Discoverability:** Visible controls can make available actions easier to find.
- **Visual feedback:** Graphics are useful for diagrams, images, dashboards, and direct manipulation.
- **Accessibility for new users:** A GUI may require less knowledge of command names and syntax.
- **Interactive workflows:** Dragging, selecting, resizing, and previewing are often easier visually.

Many development environments combine both approaches. For example, an IDE provides graphical tools while also including an integrated terminal.

---

## Accessing remote machines with SSH

SSH stands for **Secure Shell**. It is a protocol used to connect securely to another computer over a network.

A basic command is:

```sh
ssh username@example.com
```

SSH encrypts communication between the local and remote computers. Authentication can use a password, but public-key authentication is generally more secure and convenient for regular use.

After connecting, a user can:

- Run commands on the remote computer.
- Monitor and administer servers.
- Build or deploy applications.
- Edit configuration files.
- Transfer files with tools such as `scp` or `sftp`.
- Create secure tunnels for other network connections.

Because only text and control information must normally be transmitted, an SSH terminal session usually consumes less bandwidth than a remote graphical desktop. Terminal commands can also be placed in scripts to automate repetitive administrative tasks.

---

## Finding common elements between two lists

Suppose two lists contain values that must be compared:

```java
List<String> leftItems = List.of("A", "B", "C");
List<String> rightItems = List.of("C", "A", "D");
```

The goal is to identify matching values and preserve the positions needed to draw a connection between them.

### Basic algorithm

1. Store the values in two lists.
2. Compare each item in the first list with the items in the second list.
3. When two values are equal, record their indexes or graphical positions.
4. Draw an arrow from the matching item in the first list to the item in the second list.

A nested-loop implementation is straightforward:

```java
for (int leftIndex = 0; leftIndex < leftItems.size(); leftIndex++) {
    for (int rightIndex = 0; rightIndex < rightItems.size(); rightIndex++) {
        if (leftItems.get(leftIndex).equals(rightItems.get(rightIndex))) {
            matches.add(new Match(leftIndex, rightIndex));
        }
    }
}
```

If the first list has `n` elements and the second has `m` elements, this approach takes `O(n × m)` time.

### Improving lookup performance

For large lists, a map can associate each value in the second list with its index or indexes:

```java
Map<String, List<Integer>> rightIndexes = new HashMap<>();

for (int index = 0; index < rightItems.size(); index++) {
    rightIndexes
        .computeIfAbsent(rightItems.get(index), key -> new ArrayList<>())
        .add(index);
}
```

The first list can then look up matching positions directly. Storing a list of indexes is important when duplicate values are allowed.

### Equality and duplicate rules

Before implementing the comparison, the program should define:

- Whether matching is case-sensitive.
- Whether whitespace should be ignored.
- Whether duplicate values create multiple arrows.
- Whether one item may connect to more than one item.
- How `null` values should be handled.

These decisions affect both the matching algorithm and the resulting visualization.

---

## Drawing arrows between matches in JavaFX

Each displayed item needs a known graphical position. For example, the first list can appear on the left side of a pane and the second list on the right.

For every recorded match:

1. Find the center-right point of the left item.
2. Find the center-left point of the matching right item.
3. Draw a line between those points.
4. Add two short lines or a triangle at the destination to form an arrowhead.

A JavaFX `Line` can represent the arrow shaft:

```java
Line shaft = new Line(startX, startY, endX, endY);
```

The arrowhead angle can be calculated with `Math.atan2`:

```java
double angle = Math.atan2(endY - startY, endX - startX);
double arrowLength = 10.0;
double arrowAngle = Math.toRadians(25.0);

double firstX = endX - arrowLength * Math.cos(angle - arrowAngle);
double firstY = endY - arrowLength * Math.sin(angle - arrowAngle);
double secondX = endX - arrowLength * Math.cos(angle + arrowAngle);
double secondY = endY - arrowLength * Math.sin(angle + arrowAngle);

Line firstSide = new Line(endX, endY, firstX, firstY);
Line secondSide = new Line(endX, endY, secondX, secondY);
```

The shaft and arrowhead lines can be added to a JavaFX `Pane`. If the window or list items can move, the arrow coordinates must also be recalculated or bound to the relevant node positions.

### Separate data from presentation

A maintainable design keeps three responsibilities separate:

- **Data:** The two lists of values.
- **Matching logic:** The algorithm that produces matching index pairs.
- **Presentation:** The labels, positions, and arrows shown in JavaFX.

This separation makes the comparison logic easier to test without launching the graphical interface.

---

## Reproducibility of software failures

A reproducible failure is one that can be triggered consistently by following a known sequence of steps. Reproducibility is essential because developers need reliable evidence before they can identify a root cause and verify a fix.

A useful failure report should include:

- A clear summary of the problem.
- Exact steps needed to reproduce it.
- Expected behavior.
- Actual behavior.
- Input data or a minimal example.
- Error messages and relevant log output.
- Application, library, JDK, and operating-system versions.
- Configuration and environment details.
- The frequency of the failure.
- Screenshots or recordings when they provide useful evidence.

### Example structure

```text
Summary:
The application closes when a user right-clicks an empty canvas.

Steps to reproduce:
1. Start the application.
2. Open a new empty canvas.
3. Right-click near the center.

Expected result:
A circle appears at the clicked position.

Actual result:
The application closes and reports a NullPointerException.

Environment:
Java 21, JavaFX 21, macOS
```

### Why reproducibility matters

Reproducing a failure allows developers to:

1. Observe the incorrect behavior.
2. Reduce it to the smallest failing case.
3. Inspect the relevant program state.
4. identify the root cause.
5. Add an automated regression test.
6. Apply a fix.
7. Repeat the same test to verify the solution.

If a failure is intermittent, logs, timestamps, random seeds, thread information, and detailed environment data can help identify the conditions that trigger it.

---

## What is FXML?

FXML is an XML-based markup language used to describe JavaFX user interfaces. It represents interface components and their hierarchy separately from most application logic.

An FXML document can define:

- Layout containers
- Buttons, labels, and text fields
- Menus and tables
- Component identifiers
- Event-handler method names
- Stylesheets and other resources
- A controller class

### Example FXML file

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.VBox?>

<VBox xmlns:fx="http://javafx.com/fxml"
      fx:controller="com.example.MainController"
      alignment="CENTER"
      spacing="12">
    <Label fx:id="messageLabel" text="Ready" />
    <Button text="Continue" onAction="#handleContinue" />
</VBox>
```

The controller contains the behavior associated with the interface:

```java
public class MainController {

    @FXML
    private Label messageLabel;

    @FXML
    private void handleContinue() {
        messageLabel.setText("Button selected");
    }
}
```

The FXML document can be loaded with `FXMLLoader`:

```java
FXMLLoader loader = new FXMLLoader(
    getClass().getResource("main-view.fxml")
);
Parent root = loader.load();
```

### Benefits of FXML

- Separates interface structure from Java behavior.
- Makes large interfaces easier to organize.
- Allows designers and developers to work on different parts of the application.
- Supports visual design tools such as Scene Builder.
- Encourages controller-based application structure.

FXML is optional. Small or highly dynamic interfaces can still be created directly in Java code.

---

## Key takeaways

- Terminals are efficient for automation and remote administration, while GUIs are useful for discoverability and visual interaction.
- SSH provides encrypted command-line access to remote computers.
- Matching list elements requires clearly defined equality and duplicate-handling rules.
- Maps can improve matching performance for large lists.
- Matching logic should be kept separate from JavaFX presentation code.
- Reproducible failure reports make debugging and fix verification more reliable.
- FXML separates JavaFX interface structure from controller logic.
