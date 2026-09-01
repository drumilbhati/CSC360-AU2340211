# Reflection — 1 September 2026

## Topics covered

- Drawing a triangle from three coordinates
- Drawing circles on a JavaFX `Canvas` using mouse input
- Connecting circles with arrows
- Testing whether a point is inside a circle
- Trees and common tree terminology
- Binary trees and binary search trees

---

## How can a triangle be drawn using three coordinates?

A triangle is defined by three non-collinear points in two-dimensional space:

```text
A = (x₁, y₁)
B = (x₂, y₂)
C = (x₃, y₃)
```

The coordinates can be represented as a matrix:

```text
[ x₁  y₁ ]
[ x₂  y₂ ]
[ x₃  y₃ ]
```

Because the three vertices are already known, there is no need to calculate intersections between lines. The triangle can be constructed by connecting:

1. Point A to point B
2. Point B to point C
3. Point C back to point A

### JavaFX example

A triangle can be drawn on a JavaFX `Canvas` with `strokePolygon`:

```java
double[] xCoordinates = {x1, x2, x3};
double[] yCoordinates = {y1, y2, y3};

graphicsContext.strokePolygon(xCoordinates, yCoordinates, 3);
```

To create a filled triangle, use:

```java
graphicsContext.fillPolygon(xCoordinates, yCoordinates, 3);
```

### Checking whether the points form a valid triangle

Three collinear points do not form a triangle with a positive area. Their signed double area can be calculated using:

```text
D = x₁(y₂ - y₃) + x₂(y₃ - y₁) + x₃(y₁ - y₂)
```

- If `D = 0`, the points are collinear.
- If `D ≠ 0`, they form a valid triangle.
- The triangle's area is `|D| / 2`.

When using floating-point coordinates, code should compare the result with a small tolerance rather than requiring exact equality with zero.

---

## How can a circle be drawn on right-click in JavaFX?

The application needs a JavaFX `Canvas`, its `GraphicsContext`, and a mouse event handler. The handler checks for the secondary mouse button, which normally represents a right-click.

```java
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

Canvas canvas = new Canvas(800, 600);
GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

double radius = 25.0;

canvas.setOnMouseClicked(event -> {
    if (event.getButton() == MouseButton.SECONDARY) {
        double centerX = event.getX();
        double centerY = event.getY();

        graphicsContext.setFill(Color.LIGHTBLUE);
        graphicsContext.setStroke(Color.DARKBLUE);
        graphicsContext.fillOval(
            centerX - radius,
            centerY - radius,
            radius * 2,
            radius * 2
        );
        graphicsContext.strokeOval(
            centerX - radius,
            centerY - radius,
            radius * 2,
            radius * 2
        );
    }
});
```

`fillOval` and `strokeOval` receive the top-left corner, width, and height of an oval's bounding box. Subtracting the radius from the mouse coordinates places the circle's center at the clicked point. Equal width and height values create a circle rather than a general ellipse.

### Storing circles

If the circles must later be selected or connected, their data should be stored rather than only drawn as pixels. A simple model is:

```java
record CircleNode(double centerX, double centerY, double radius) {}
```

Each right-click can add a new `CircleNode` to a list:

```java
List<CircleNode> circles = new ArrayList<>();
circles.add(new CircleNode(centerX, centerY, radius));
```

Maintaining a model makes it possible to redraw, move, test, and connect the circles later.

---

## How can arrows be drawn between circles?

An arrow consists of:

1. A line representing the shaft
2. Two short lines or a filled triangle representing the arrowhead

For circles, an arrow should usually start and end at their boundaries instead of passing through their centers.

### Finding the direction

For a start center `(x₁, y₁)` and an end center `(x₂, y₂)`:

```text
dx = x₂ - x₁
dy = y₂ - y₁
length = √(dx² + dy²)
```

The unit direction vector is:

```text
ux = dx / length
uy = dy / length
```

For two circles with radii `r₁` and `r₂`, suitable endpoints are:

```text
startX = x₁ + ux × r₁
startY = y₁ + uy × r₁
endX   = x₂ - ux × r₂
endY   = y₂ - uy × r₂
```

This calculation stops the arrow at each circle's edge. The two centers must be different so that `length` is not zero.

### JavaFX arrow method

```java
private void drawArrow(
        GraphicsContext gc,
        double startX,
        double startY,
        double endX,
        double endY) {

    double arrowLength = 12.0;
    double arrowAngle = Math.toRadians(25.0);
    double angle = Math.atan2(endY - startY, endX - startX);

    gc.strokeLine(startX, startY, endX, endY);

    double firstX = endX - arrowLength * Math.cos(angle - arrowAngle);
    double firstY = endY - arrowLength * Math.sin(angle - arrowAngle);
    double secondX = endX - arrowLength * Math.cos(angle + arrowAngle);
    double secondY = endY - arrowLength * Math.sin(angle + arrowAngle);

    gc.strokeLine(endX, endY, firstX, firstY);
    gc.strokeLine(endX, endY, secondX, secondY);
}
```

The circle coordinates can be stored in a list. When two circles are selected, the program can calculate the boundary points and call `drawArrow` to connect them.

### Redrawing order

A `Canvas` uses immediate-mode drawing: once a shape is drawn, it is stored as pixels rather than as a JavaFX node. For a maintainable application, store circles and arrows in collections and redraw the entire canvas whenever the model changes:

1. Clear the canvas.
2. Draw all arrows.
3. Draw all circles on top of the arrows.

Drawing the circles last hides any small overlap between an arrow and a circle boundary.

---

## How can we check whether a point is inside a circle?

Given:

- Circle center: `(cx, cy)`
- Circle radius: `r`
- Test point: `(px, py)`

Calculate the squared distance between the point and the center:

```text
(px - cx)² + (py - cy)²
```

The point is inside or on the circle when:

```text
(px - cx)² + (py - cy)² ≤ r²
```

### Java method

```java
private boolean containsPoint(
        double centerX,
        double centerY,
        double radius,
        double pointX,
        double pointY) {

    double deltaX = pointX - centerX;
    double deltaY = pointY - centerY;

    return deltaX * deltaX + deltaY * deltaY <= radius * radius;
}
```

Using squared distances avoids the unnecessary square-root operation and produces the same inside/outside result.

This test can be used to determine which circle the user clicked. If circles overlap, iterating through the list in reverse drawing order selects the topmost matching circle first.

---

## What is a tree?

A tree is a hierarchical data structure made of **nodes** connected by **edges**. A rooted tree begins with one root node and branches into child nodes.

Important properties include:

- The root has no parent.
- Every other node has exactly one parent.
- A node may have zero or more children.
- A tree contains no cycles.
- There is exactly one path from the root to each node.
- A tree with `n` nodes contains `n - 1` edges.

### Tree terminology

| Term | Meaning |
|---|---|
| Root | The first or highest node in the hierarchy |
| Parent | A node directly above another node |
| Child | A node directly below another node |
| Sibling | A node that has the same parent as another node |
| Leaf | A node with no children |
| Internal node | A node with one or more children |
| Edge | A connection between two nodes |
| Path | A sequence of connected nodes |
| Depth | Number of edges from the root to a node |
| Height | Length of the longest downward path to a leaf |
| Subtree | A node and all of its descendants |

Trees are used to represent file systems, user-interface hierarchies, organization structures, expression syntax, search indexes, and many other forms of hierarchical data.

---

## What is a binary tree?

A binary tree is a tree in which each node has at most two children, identified as the **left child** and **right child**.

```text
        Root
       /    \
    Left    Right
```

A general binary tree does not automatically provide efficient searching. Search performance depends on the tree's rules and shape.

### Binary search tree

A binary search tree (BST) adds an ordering rule. For each node:

- Values in the left subtree are smaller than the node's value.
- Values in the right subtree are greater than the node's value.
- A consistent policy must be chosen if duplicate values are allowed.

Search, insertion, and deletion take `O(h)` time, where `h` is the tree's height:

- In a balanced BST, `h` is approximately `log n`, so operations are typically `O(log n)`.
- In a highly unbalanced BST, `h` can become `n`, so operations may degrade to `O(n)`.

### Other uses of binary trees

Binary trees are also used for:

- Binary heaps
- Expression trees
- Syntax trees
- Decision trees
- Huffman coding trees

These structures use different rules and should not be confused with binary search trees.

### Common traversal orders

- **Preorder:** Root, left subtree, right subtree
- **Inorder:** Left subtree, root, right subtree
- **Postorder:** Left subtree, right subtree, root
- **Level order:** Visit nodes one level at a time

Inorder traversal of a correctly constructed binary search tree visits its values in sorted order.

---

## Key takeaways

- Three known, non-collinear vertices can be connected directly to create a triangle.
- A JavaFX `Canvas` uses a `GraphicsContext` for immediate-mode drawing.
- Right-click events can be detected with `MouseButton.SECONDARY`.
- Circle data should be stored in a model if shapes must be selected, connected, or redrawn.
- Arrows can be positioned at circle boundaries using a normalized direction vector.
- Squared distance is sufficient for checking whether a point lies inside a circle.
- A tree represents acyclic hierarchical relationships between nodes.
- A binary tree limits each node to two children.
- A binary search tree adds ordering, but its efficiency depends on its height and balance.
