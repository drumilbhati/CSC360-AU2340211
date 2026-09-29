# Reflection — 29 September 2026

## Group 1 project: TriangleFX refinement

During this class, I worked on refining the **TriangleFX** group project. The project had progressed from its earlier planning phase into a working JavaFX application, so the focus shifted toward improving the documentation, simplifying the user interface, reviewing the implementation, and preparing the project for demonstration.

**Project repository:** [Devang280904/CSC360-Group1](https://github.com/Devang280904/CSC360-Group1)

## Progress summary

- Expanded and reorganized the project README.
- Added screenshots of the running application.
- Reviewed and merged project changes through pull request #2.
- Helped refine the user-interface direction.
- Studied the codebase and understood its execution flow.
- Confirmed how the mathematical model is represented in Java.
- Improved the distinction between implemented features, limitations, and future work.

---

## My documented contribution

The repository history records my September 29 contribution in commit `779c8aa`, titled **“Expand README documentation and add screenshots.”**

This change:

- Modified three files.
- Added 236 lines and removed 113 lines.
- Reorganized `README.md` into focused sections.
- Added two screenshots of the application.
- Documented the mathematical model and input contract.
- Explained the system architecture and execution flow.
- Added build, test, run, and Javadoc commands.
- Documented current limitations and possible roadmap items.

I also merged [pull request #2](https://github.com/Devang280904/CSC360-Group1/pull/2) into the `main` branch. This gave me additional practice with reviewing and integrating a teammate's branch through GitHub rather than making every change directly on `main`.

---

## README cleanup

The README was updated so that a new contributor can understand the project without first reading the source code.

### Information added or clarified

The revised documentation explains:

1. What TriangleFX does.
2. How three line equations are represented in matrix form.
3. How pairwise intersections create triangle vertices.
4. How the program detects invalid or degenerate triangles.
5. How the application is divided into models, parsers, geometry services, and JavaFX classes.
6. What input formats and controls the interface supports.
7. How data moves through the application.
8. How to build, test, run, and package the project.
9. Which features are complete and which remain limitations.

### Mathematical model

The application represents three lines as:

```text
A · x = B
```

where:

```text
A = 3 × 2 coefficient matrix
x = [x, y]ᵀ unknown coordinate vector
B = 3 × 1 constant vector
```

Each matrix row defines one line:

```text
aᵢ₁x + aᵢ₂y = bᵢ
```

The application finds the pairwise intersections:

```text
P₁₂ = L₁ ∩ L₂
P₂₃ = L₂ ∩ L₃
P₃₁ = L₃ ∩ L₁
```

These points become the three triangle vertices if every pair has a unique intersection and the resulting points are distinct and non-collinear.

### Build and run instructions

The README documents the main Maven commands:

```sh
mvn test
mvn javafx:run
mvn clean package
mvn javadoc:javadoc
```

Providing verified commands is important because it lets a new contributor reproduce the project setup instead of guessing how to launch or test the application.

## User-interface cleanup

The team refined the JavaFX interface to make it easier to understand and use.

### Preset selector styling

The matrix preset `ComboBox` was updated with custom JavaFX cells and clearer dark-theme styling. The changes improved:

- Text contrast
- Prompt readability
- Hover feedback
- Selected-item feedback
- Border and background consistency
- Cursor feedback for interactive controls

A custom `ListCell` is used for both the selected value and the popup items. This showed me that styling a JavaFX `ComboBox` requires handling more than the outer control: the button cell and popup list cells also need appropriate colors and states.

### Removal of the raw matrix paste panel

The raw matrix paste feature was removed from the visible interface. This eliminated an expandable text area, its toggle button, parsing action, and related state from `TriangleApp`.

The cleanup made the UI more focused around the structured coefficient grid and presets. It also reduced the number of input paths the user must understand.

This change demonstrated an important design principle: removing a feature can improve a project when that feature duplicates existing behavior or makes the primary workflow less clear.

### Main interaction flow

The refined interface emphasizes this workflow:

1. Enter coefficients in the matrix fields or select a preset.
2. Generate the diagram.
3. Review the decoded line equations.
4. Inspect the rendered triangle and calculated properties.
5. Clear the interface before entering another example.

---

## Understanding the codebase

Reviewing the project helped me understand how the application separates user-interface code from mathematical logic.

### High-level execution pipeline

```text
Matrix input or preset
        ↓
Input parsing and validation
        ↓
Three line representations
        ↓
Pairwise intersection calculations
        ↓
Triangle validity checks
        ↓
Triangle model and geometric properties
        ↓
JavaFX canvas rendering and status feedback
```

### Application entry point

The main class starts JavaFX and opens the TriangleFX application. GUI creation and updates occur through the JavaFX Application Thread.

### Model classes

The model layer represents geometric values such as:

- A point with `x` and `y` coordinates
- A line in standard form, `ax + by = c`
- A triangle containing three vertices

Keeping these values in dedicated classes makes the code easier to read than passing unrelated arrays and primitive values through every method.

### Parsing and validation

The parser and input-handling code convert user-entered values into structured coefficients. Validation is needed to detect:

- Empty fields
- Invalid numeric values
- Incorrect matrix dimensions
- Unsupported input formats
- Invalid lines whose x and y coefficients are both zero

Validation errors should be shown as readable interface messages instead of unhandled exceptions.

### Geometry service

The geometry layer performs the mathematical work. For two lines:

```text
a₁x + b₁y = c₁
a₂x + b₂y = c₂
```

it calculates the determinant:

```text
D = a₁b₂ − a₂b₁
```

- If `D` is non-zero, the pair has one unique intersection.
- If `D` is approximately zero, the lines are parallel or coincident and cannot provide one unique triangle vertex.

After calculating all three pairwise intersections, the program checks that the vertices are distinct and have a non-zero area within a floating-point tolerance.

### Triangle properties

For a valid triangle, the application can calculate:

- Vertex coordinates
- Three side lengths
- Perimeter
- Area

The distance between two points is calculated with:

```text
distance = √((x₂ − x₁)² + (y₂ − y₁)²)
```

The perimeter is the sum of the three side lengths, while the area can be calculated using the shoelace formula.

### Canvas rendering

The rendering component converts mathematical coordinates into canvas coordinates. This requires accounting for an important difference:

- In Cartesian coordinates, positive y points upward.
- In JavaFX canvas coordinates, positive y points downward.

The canvas also auto-fits the viewport so triangles of different sizes remain visible. It draws reference elements such as:

- Grid lines
- x- and y-axes
- Tick labels
- Origin marker
- Triangle edges and fill
- Vertex points and labels

Separating rendering from geometry means the mathematical calculations can be tested without opening the GUI.

---

## Testing and maintainability

The codebase includes automated tests for important geometry and parser behavior. Relevant cases include:

- Two lines with a unique intersection
- Parallel lines
- Coincident lines
- Valid triangles
- Concurrent lines producing repeated vertices
- Collinear or near-degenerate points
- Valid and invalid matrix input
- Floating-point tolerance behavior

This reinforced the value of keeping calculations in non-UI classes. JavaFX interfaces are more difficult to test directly, while small geometry and parser methods can be tested quickly and repeatedly with JUnit.

The codebase also uses Javadocs to explain classes and public behavior. Earlier cleanup removed unnecessary HTML formatting from Javadocs, making the source comments simpler and more consistent.

---

## Personal learning

By reading and tracing the code, I gained a clearer understanding of:

- How a matrix row becomes a standard-form line equation.
- How determinants are used to find and classify intersections.
- How three pairwise intersections become triangle vertices.
- Why floating-point calculations require a tolerance.
- How model, parser, geometry, and UI responsibilities can be separated.
- How JavaFX controls trigger application behavior through event handlers.
- How Cartesian points are transformed for canvas rendering.
- How presets make a mathematical interface easier to demonstrate.
- How documentation and screenshots improve project usability.
- How pull requests support collaboration and controlled integration.

I also learned that understanding a project requires more than reading one class. It is necessary to follow data from the input controls through validation and geometry calculations to the final rendered output.

---

## Current project state

By September 29, the repository had progressed substantially beyond the documentation-only state recorded earlier in the month. It included:

- A Maven build configuration
- A working JavaFX application
- Matrix-based input
- Preset examples
- Pairwise line-intersection logic
- Triangle validation
- Geometric property calculations
- An auto-scaled coordinate canvas
- A dark-themed interface
- Parser and geometry tests
- Expanded documentation and screenshots

The repository history contained 13 commits at the time of review.

## Remaining improvements

Possible future refinements include:

- Adding symbolic equation input such as `x + y = 8` directly in the UI
- Drawing the complete source lines in addition to the triangle
- Expanding automated JavaFX integration tests
- Exporting the rendered diagram as an image or vector file
- Moving more inline JavaFX styles into reusable CSS
- Improving keyboard navigation and accessibility information

## Key takeaway

The main progress in this class was turning a working group project into a clearer and more focused deliverable. The README became more useful, screenshots documented the result, the UI workflow was simplified, and reviewing the architecture helped me understand how JavaFX presentation, input parsing, geometry, testing, and rendering work together.
