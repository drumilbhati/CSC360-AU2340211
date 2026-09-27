# Reflection — 17 September 2026

## Group 1 project: TriangleFX

During this class, I continued working on the Group 1 project and helped finalize the design and implementation direction for **TriangleFX**.

**Project repository:** [Devang280904/CSC360-Group1](https://github.com/Devang280904/CSC360-Group1)

## Project objective

TriangleFX is a planned JavaFX desktop application in which a user enters three linear equations. The application will:

1. Parse each equation into standard line form.
2. Calculate the pairwise intersections of the three lines.
3. Confirm that the intersections form a non-degenerate triangle.
4. Draw the triangle on a JavaFX canvas.
5. Label its vertices and report meaningful input or geometry errors.

Example equations include:

```text
x + y = 8
x - y = 2
x = 1
```

The project is designed to connect coordinate geometry, linear algebra, input validation, event handling, and JavaFX graphics.

---

## Progress made

### 1. Defined the project requirements

I documented the formal problem statement and established the scope of the application. The requirements specify that the application must:

- Provide three equation input fields.
- Parse equations containing `x`, `y`, and numeric coefficients.
- Accept integer and decimal coefficients.
- Compute the intersection of each pair of lines.
- Detect parallel or coincident lines.
- Reject intersections that do not form a valid triangle.
- Draw a valid result on a JavaFX canvas.
- Display readable error messages for invalid input and impossible geometry.

I also documented non-functional expectations, including a simple and responsive interface and clear separation between parsing, geometry, and drawing logic.

### 2. Improved the project README

I revised the repository's `README.md` so that it accurately describes the current state of the project as a **documentation and planning phase** rather than presenting unimplemented features as complete.

The README now communicates:

- The purpose of TriangleFX
- Planned application features
- Supported equation styles
- The repository's current contents
- The distinction between current progress and future implementation

This makes the repository more transparent and easier for group members or new contributors to understand.

### 3. Created the implementation plan

I created a structured implementation plan with eight phases:

| Phase | Goal | Status at this stage |
|---|---|---|
| 1. Documentation baseline | Finalize the README and problem statement | Completed |
| 2. Project skeleton | Configure a runnable JavaFX project | Planned |
| 3. Equation parser | Convert text equations to `Ax + By = C` | Planned |
| 4. Geometry engine | Calculate intersections and validate the triangle | Planned |
| 5. UI and interaction | Add inputs, actions, and feedback | Planned |
| 6. Canvas rendering | Draw and label the triangle | Planned |
| 7. Testing and QA | Test valid, invalid, and edge-case behavior | Planned |
| 8. Finalization | Refactor and prepare the demonstration | Planned |

Each phase includes tasks, deliverables, and exit criteria. This gives the group a shared definition of completion and makes the remaining work easier to divide and review.

### 4. Used a branch and pull-request workflow

The implementation plan was developed on the `drumilbhati` branch and merged into `main` through [pull request #1](https://github.com/Devang280904/CSC360-Group1/pull/1).

This provided practice with a collaborative Git workflow:

1. Work on an isolated branch.
2. Commit a focused change.
3. Open a pull request.
4. Review the proposed change.
5. Merge the approved work into the main branch.

### 5. Compared multiple design versions

The group created and discussed multiple versions of the proposed design. Comparing alternatives helped us refine:

- The exact project scope
- The expected equation syntax
- The sequence of implementation work
- The division between parsing, geometry, UI, and rendering
- The difference between completed and planned features

The final design emphasizes small modules with clear responsibilities instead of placing all functionality in one JavaFX class.

---

## Verified repository contributions

The repository history records the following contributions under my GitHub account, `drumilbhati`:

| Commit | Contribution |
|---|---|
| `e713d02` | Documented TriangleFX requirements and initial usage information |
| `e939eae` | Updated the README to represent the documentation phase accurately |
| `a9d7629` | Added the detailed JavaFX triangle-drawer implementation plan |
| `0bd8b87` | Merged pull request #1 into `main` |

At the time of this reflection, the public repository contains the project documentation and design plan. The JavaFX application code remains part of the planned implementation phases.

---

## Finalized design

The planned application separates responsibilities into distinct components:

```text
User input
    ↓
Equation parser
    ↓
Line models in Ax + By = C form
    ↓
Geometry engine
    ↓
Triangle validation
    ↓
Canvas renderer and result feedback
```

### Equation parser

The parser will accept forms such as:

```text
x + y = 8
2x - 3y = 10
y = 2x + 1
x = 4
```

It will normalize each valid equation to:

```text
Ax + By = C
```

Malformed or unsupported input must produce a clear error instead of causing the application to fail unexpectedly.

### Geometry engine

For three lines, the engine will calculate these pairwise intersections:

```text
P₁₂ = intersection of lines 1 and 2
P₂₃ = intersection of lines 2 and 3
P₃₁ = intersection of lines 3 and 1
```

A valid triangle requires three distinct, non-collinear points. The engine must detect:

- Parallel lines
- Coincident lines
- Duplicate intersection points
- Collinear vertices
- Other degenerate results

### JavaFX interface

The planned interface will contain:

- Three equation text fields
- A **Draw** button
- A **Clear** button
- A status or error-message area
- A canvas for displaying the triangle
- Optional labels for vertex coordinates

The interface layer will collect input and display results, while parsing and geometry will remain in testable non-UI classes.

---

## What I learned

This work showed that planning is an important part of software implementation. A clear problem statement prevents the group from building different interpretations of the same project, while a phased plan provides manageable milestones.

I also learned the importance of:

- Describing repository progress honestly.
- Separating completed features from proposed features.
- Defining functional and non-functional requirements.
- Dividing a graphical application into focused modules.
- Establishing edge cases before implementation begins.
- Using branches and pull requests for collaborative changes.
- Giving each phase measurable exit criteria.

## Next steps

The next phase is to create the JavaFX and Maven project skeleton. After the project can build and open a blank window, the group can implement and test the equation parser before connecting it to geometry and rendering.

## Key takeaway

The main progress was the completion of the project's documentation baseline and design plan. The project now has a clear objective, defined requirements, a modular architecture, and an eight-phase path from initial setup to final submission.
