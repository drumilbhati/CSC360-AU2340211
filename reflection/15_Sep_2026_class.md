# Reflection — 15 September 2026

## Group 1 project implementation

In this class, Group 1 implemented a project involving coordinate geometry, systems of linear equations, and JavaFX input handling.

The main tasks were:

1. Determine whether three points form a valid triangle.
2. Find the intersection of two lines.
3. Represent three line equations in matrix form, `Ax = b`.
4. Build a JavaFX interface for entering coefficients and displaying results.

---

## Determining whether three points form a triangle

Let the three points be:

```text
P₁ = (x₁, y₁)
P₂ = (x₂, y₂)
P₃ = (x₃, y₃)
```

Three distinct points form a valid triangle only when they are not collinear. Collinear points lie on the same straight line and produce a triangle with zero area.

### Area formula

The area is:

```text
Area = ½ × |x₁(y₂ − y₃) + x₂(y₃ − y₁) + x₃(y₁ − y₂)|
```

It is useful to calculate twice the signed area first:

```text
D = x₁(y₂ − y₃) + x₂(y₃ − y₁) + x₃(y₁ − y₂)
```

Then:

```text
Area = |D| / 2
```

- If `D = 0`, the points are collinear and do not form a valid triangle.
- If `D ≠ 0`, the points form a valid triangle.

The sign of `D` also indicates the orientation of the three points:

- `D > 0`: counterclockwise order
- `D < 0`: clockwise order
- `D = 0`: collinear

### Java implementation

When the coordinates use `double`, the result should be compared with a small tolerance because floating-point calculations may not produce an exact zero.

```java
private static final double EPSILON = 1.0e-9;

private static boolean formsTriangle(
        double x1,
        double y1,
        double x2,
        double y2,
        double x3,
        double y3) {

    double twiceSignedArea =
        x1 * (y2 - y3)
        + x2 * (y3 - y1)
        + x3 * (y1 - y2);

    return Math.abs(twiceSignedArea) > EPSILON;
}
```

This test also rejects repeated points because repeated vertices produce zero area.

---

## Finding the intersection of two lines

Represent two lines in standard form:

```text
a₁x + b₁y = c₁
a₂x + b₂y = c₂
```

Standard form is preferable to slope-intercept form because it can also represent vertical lines without dividing by a slope.

The equations form a matrix system:

```text
[ a₁  b₁ ] [ x ] = [ c₁ ]
[ a₂  b₂ ] [ y ]   [ c₂ ]
```

### Determinant

The determinant of the coefficient matrix is:

```text
D = a₁b₂ − a₂b₁
```

If `D ≠ 0`, the lines have one unique intersection. Cramer's rule gives:

```text
x = (c₁b₂ − c₂b₁) / D
y = (a₁c₂ − a₂c₁) / D
```

### Parallel and coincident lines

If `D = 0`, the lines do not have a unique intersection. They may be:

- **Parallel:** They have the same direction but different positions, so there is no intersection.
- **Coincident:** They describe the same line, so there are infinitely many intersection points.

The augmented determinants can distinguish the cases:

```text
Dₓ = c₁b₂ − c₂b₁
Dᵧ = a₁c₂ − a₂c₁
```

- If `D`, `Dₓ`, and `Dᵧ` are all approximately zero, the lines are coincident.
- If `D` is approximately zero but either `Dₓ` or `Dᵧ` is not, the lines are parallel.

### Java representation

```java
record Line(double a, double b, double c) {}
record Point(double x, double y) {}
```

A line where both `a` and `b` are zero is invalid because it does not define a geometric line. The program should reject this input before attempting the intersection calculation.

---

## Representing three lines as `Ax = b`

Suppose the user enters three lines:

```text
a₁x + b₁y = c₁
a₂x + b₂y = c₂
a₃x + b₃y = c₃
```

They can be represented as:

```text
A = [ a₁  b₁ ]     x = [ x ]     b = [ c₁ ]
    [ a₂  b₂ ]         [ y ]         [ c₂ ]
    [ a₃  b₃ ]                       [ c₃ ]
```

Therefore:

```text
Ax = b
```

Here:

- `A` is a `3 × 2` coefficient matrix.
- `x` is a `2 × 1` vector containing the unknown coordinates.
- `b` is a `3 × 1` vector containing the constants.

### Important consistency issue

Three lines in a two-dimensional plane do not necessarily meet at one point. This is an overdetermined system because it contains three equations but only two unknowns.

Possible results include:

- All three lines meet at one common point.
- Each pair meets at a different point, forming a triangle.
- Two or more lines are parallel.
- Two or more lines are coincident.

To test for one exact common intersection:

1. Find a unique intersection from a non-parallel pair of lines.
2. Substitute that point into the third equation.
3. Check whether the third equation is satisfied within a tolerance.

For a candidate point `(x, y)`, line 3 is satisfied when:

```text
|a₃x + b₃y − c₃| ≤ tolerance
```

If no exact common point exists but an approximate best-fit point is required, a least-squares method can be used. That is a different problem and should be identified clearly in the requirements.

---

## JavaFX input design

A JavaFX interface can use a `GridPane` to arrange the coefficients in rows and columns.

### Suggested layout

| Line | x coefficient | y coefficient | Constant |
|---|---|---|---|
| Line 1 | `a₁` | `b₁` | `c₁` |
| Line 2 | `a₂` | `b₂` | `c₂` |
| Line 3 | `a₃` | `b₃` | `c₃` |

The interface needs:

- Nine `TextField` controls for the coefficients and constants.
- Descriptive `Label` controls.
- A calculate button.
- A clear or reset button.
- A result area for intersections and line relationships.
- Validation feedback for missing or invalid values.

### Building the coefficient matrix

After successful parsing, the input can be stored as arrays:

```java
double[][] coefficients = {
    {a1, b1},
    {a2, b2},
    {a3, b3}
};

double[] constants = {c1, c2, c3};
```

These arrays represent `A` and `b`. The unknown point is represented by:

```java
double[] unknowns = {x, y};
```

### Reading and validating a field

```java
private double readNumber(TextField field, String fieldName) {
    String text = field.getText().trim();

    if (text.isEmpty()) {
        throw new IllegalArgumentException(fieldName + " is required.");
    }

    try {
        return Double.parseDouble(text);
    } catch (NumberFormatException exception) {
        throw new IllegalArgumentException(
            fieldName + " must be a valid number."
        );
    }
}
```

Validation should reject:

- Empty fields
- Non-numeric values
- `NaN` and infinite values, if `Double.parseDouble` is used
- A line whose x and y coefficients are both zero

For finite-value validation:

```java
if (!Double.isFinite(value)) {
    throw new IllegalArgumentException(fieldName + " must be finite.");
}
```

### Handling the calculate action

```java
calculateButton.setOnAction(event -> {
    try {
        Line first = readLine(1);
        Line second = readLine(2);
        Line third = readLine(3);

        String result = analyzeLines(first, second, third);
        resultLabel.setText(result);
    } catch (IllegalArgumentException exception) {
        resultLabel.setText(exception.getMessage());
    }
});
```

The event handler should coordinate the operation, while parsing and geometry calculations should be kept in separate methods or classes. This separation makes the mathematical logic easier to test independently of JavaFX.

---

## Possible outcomes to display

The interface should report a precise result rather than only displaying “success” or “failure.” Possible messages include:

- `The three lines intersect at (x, y).`
- `Lines 1 and 2 are parallel.`
- `Lines 1 and 2 are coincident.`
- `The three pairwise intersections form a triangle.`
- `The three lines do not have one common intersection.`
- `Line 3 is invalid because both coefficients are zero.`

If pairwise intersections form a triangle, the application can apply the three-point area test to confirm that the intersection points are distinct and non-collinear.

---

## Suggested project structure

The project can separate responsibilities into small classes:

```text
src/main/java/
├── application/
│   └── GeometryApplication.java
├── controller/
│   └── GeometryController.java
├── model/
│   ├── Line.java
│   └── Point.java
└── service/
    └── GeometryService.java
```

- **Application:** Starts JavaFX and loads the interface.
- **Controller:** Reads input and updates the displayed result.
- **Model:** Represents lines and points.
- **Service:** Performs triangle and intersection calculations.

Unit tests should focus on the geometry service, including:

- Three points forming a triangle
- Collinear points
- Repeated points
- Intersecting lines
- Parallel lines
- Coincident lines
- Three lines with one common intersection
- Three lines with different pairwise intersections
- Invalid line coefficients

---

## Key takeaways

- Three points form a triangle only when their area is non-zero.
- The signed-area formula also identifies clockwise or counterclockwise point order.
- Standard-form line equations support vertical lines without special handling.
- A non-zero determinant indicates one unique intersection between two lines.
- Three 2D lines create a `3 × 2` system and may not have one exact common solution.
- JavaFX `GridPane` and `TextField` controls provide a clear coefficient-entry interface.
- Numeric and geometric validation should occur before calculations.
- Keeping interface code separate from mathematical logic improves testing and maintainability.
