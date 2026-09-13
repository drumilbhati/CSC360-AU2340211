# Reflection — 8 September 2026

## Topics covered

- The Java Collections Framework
- Collection views
- Exceptions, logging, and debugging
- Events and event handling in AWT and Swing
- The Java event-class hierarchy
- Master-detail user-interface layouts

---

## What are collections in Java?

The **Java Collections Framework** provides interfaces and classes for storing, retrieving, and manipulating groups of objects. It also supplies common algorithms for operations such as sorting and searching.

### `List`

A `List` is an ordered collection that supports indexed access and normally allows duplicate elements.

Common implementations include:

- **`ArrayList`:** A resizable array. It provides fast indexed access and is a common default choice for a list.
- **`LinkedList`:** A doubly linked list that also implements `Deque`. Accessing an arbitrary index is slower than in an `ArrayList`.

```java
List<String> names = new ArrayList<>();
names.add("Asha");
names.add("Drumil");
names.add("Asha");
```

### `Set`

A `Set` does not allow duplicate elements.

Common implementations include:

- **`HashSet`:** Uses hashing and does not guarantee iteration order.
- **`LinkedHashSet`:** Uses hashing while preserving insertion order.
- **`TreeSet`:** Keeps elements sorted by their natural order or a supplied `Comparator`.

```java
Set<String> uniqueNames = new HashSet<>();
uniqueNames.add("Asha");
uniqueNames.add("Asha"); // The set still contains one occurrence.
```

### `Queue` and `Deque`

A `Queue` generally stores elements for processing in a particular order. Many queues use first-in, first-out (FIFO) behavior, but this is not true for every implementation.

- **`ArrayDeque`:** An efficient general-purpose queue and double-ended queue.
- **`LinkedList`:** Implements both `List` and `Deque`.
- **`PriorityQueue`:** Removes elements according to priority rather than insertion order.

```java
Queue<String> tasks = new ArrayDeque<>();
tasks.offer("first");
tasks.offer("second");
String nextTask = tasks.poll();
```

### `Map`

A `Map` associates unique keys with values. Each key can map to at most one value, although multiple keys may map to equal values.

Common implementations include:

- **`HashMap`:** Uses hashing and does not guarantee iteration order.
- **`LinkedHashMap`:** Preserves insertion order, or optionally access order.
- **`TreeMap`:** Keeps keys sorted by their natural order or a supplied `Comparator`.

```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Asha", 90);
scores.put("Drumil", 95);
```

`Map` belongs to the Java Collections Framework, but it does **not** extend the `Collection` interface because it stores key-value associations rather than individual elements.

### Summary

| Interface | Main characteristic | Common implementations |
|---|---|---|
| `List` | Ordered and permits duplicates | `ArrayList`, `LinkedList` |
| `Set` | Contains unique elements | `HashSet`, `LinkedHashSet`, `TreeSet` |
| `Queue` / `Deque` | Supports ordered processing at one or both ends | `ArrayDeque`, `LinkedList`, `PriorityQueue` |
| `Map` | Associates unique keys with values | `HashMap`, `LinkedHashMap`, `TreeMap` |

---

## What are collection views?

A **view** presents all or part of another collection without necessarily copying its elements. A view is commonly backed by its source, so changes made through the view may affect the original collection and vice versa.

### Sublist view

```java
List<String> values = new ArrayList<>(
    List.of("A", "B", "C", "D")
);

List<String> middle = values.subList(1, 3);
middle.set(0, "Updated");
```

After this operation, `values` contains `"A", "Updated", "C", "D"` because `middle` is backed by `values`.

Structurally modifying the original list outside the sublist while the view is in use can invalidate the view and lead to a `ConcurrentModificationException` or undefined semantics for subsequent view operations.

### Map views

A map provides three important views:

```java
Set<String> keys = scores.keySet();
Collection<Integer> values = scores.values();
Set<Map.Entry<String, Integer>> entries = scores.entrySet();
```

Removing a key through `keySet()` also removes its mapping from the original map.

### Unmodifiable views and copies

```java
List<String> view = Collections.unmodifiableList(values);
List<String> copy = List.copyOf(values);
```

These operations are different:

- `Collections.unmodifiableList(values)` returns a read-only **view**. Changes made directly to `values` remain visible through the view.
- `List.copyOf(values)` creates an unmodifiable **copy** whose contents do not change when `values` changes later.

Views can save memory and express intent clearly, but developers must understand whether a view is backed by mutable data.

---

## Exceptions, logging, and debugging

These three concepts serve different but related purposes:

- **Exceptions** represent failures or unusual conditions during execution.
- **Logging** records information about application behavior over time.
- **Debugging** is the process of investigating a problem and identifying its cause.

### Exceptions

An exception interrupts the normal execution path. Java uses `try`, `catch`, `finally`, `throw`, and `throws` to report and handle exceptional conditions.

```java
try {
    String content = Files.readString(path);
    process(content);
} catch (IOException exception) {
    logger.log(Level.SEVERE, "Unable to read " + path, exception);
}
```

#### Checked exceptions

Checked exceptions must be caught or declared in the method signature. They commonly represent conditions from which a caller might recover.

Examples include:

- `IOException`
- `SQLException`

#### Unchecked exceptions

Unchecked exceptions extend `RuntimeException`. The compiler does not require them to be caught or declared. They commonly indicate invalid arguments, incorrect state, or programming mistakes.

Examples include:

- `NullPointerException`
- `IllegalArgumentException`
- `IndexOutOfBoundsException`

Errors such as `OutOfMemoryError` are also unchecked, but they represent serious JVM or environment failures and are separate from exceptions in the class hierarchy.

### Logging

Logging records events that help developers and operators understand an application's behavior. Useful log messages include relevant context while avoiding passwords, tokens, and other sensitive information.

Common Java logging options include:

- **`java.util.logging`:** Included with the JDK.
- **Log4j 2:** A logging implementation with configurable output and formatting.
- **SLF4J:** A logging facade that allows an application to use a selected logging implementation.

Typical levels include `TRACE`, `DEBUG`, `INFO`, `WARN`, and `ERROR`, although exact names depend on the framework.

Parameterized logging should be preferred when supported:

```java
logger.info("Loaded {} records for user {}", recordCount, userId);
```

### Debugging

Debugging is the systematic process of reproducing, isolating, understanding, and fixing a defect.

Useful debugging tools and techniques include:

- IDE breakpoints
- Stepping into, over, and out of methods
- Inspecting variables and expressions
- Viewing the call stack
- Conditional breakpoints
- Exception breakpoints
- Thread and heap inspection
- The command-line Java debugger, `jdb`
- Focused tests and carefully placed logs

Exceptions and logs provide evidence, while a debugger allows the program's state and control flow to be inspected directly.

---

## Events and event handling in Java graphics

An **event** represents an occurrence such as a button selection, mouse click, key press, focus change, or window action. **Event handling** defines how the application responds.

AWT and Swing use the **delegation event model**:

1. An event source, such as a button, produces an event object.
2. A listener is registered with the source.
3. The source sends the event object to the listener.
4. The listener's callback method responds to the event.

### Example: button action

```java
JButton saveButton = new JButton("Save");

saveButton.addActionListener(event -> {
    saveDocument();
});
```

Here:

- `saveButton` is the event source.
- `ActionEvent` is the event type.
- `ActionListener` is the listener interface.
- The lambda expression is the event handler.

### Common event categories

| Event | Listener | Typical cause |
|---|---|---|
| `ActionEvent` | `ActionListener` | Button selection or menu action |
| `MouseEvent` | `MouseListener` | Mouse entered, exited, pressed, released, or clicked |
| Mouse movement | `MouseMotionListener` | Mouse moved or dragged |
| `MouseWheelEvent` | `MouseWheelListener` | Mouse-wheel rotation |
| `KeyEvent` | `KeyListener` | Key pressed, released, or typed |
| `WindowEvent` | `WindowListener` | Window opened, closing, closed, activated, or deactivated |
| `FocusEvent` | `FocusListener` | Component gained or lost keyboard focus |
| `ItemEvent` | `ItemListener` | Selection state changed |
| `ComponentEvent` | `ComponentListener` | Component moved, resized, shown, or hidden |
| `ContainerEvent` | `ContainerListener` | Child component added or removed |
| `AdjustmentEvent` | `AdjustmentListener` | Scroll-bar value changed |
| `HierarchyEvent` | `HierarchyListener` | Component hierarchy changed |
| `InputMethodEvent` | `InputMethodListener` | Composed-text input changed |
| Property change | `PropertyChangeListener` | A bound property value changed |

Swing text components normally use a `DocumentListener` to observe text changes. The older AWT `TextListener` applies to AWT text components.

Specialized APIs also exist for drag and drop, ancestor changes, window focus, window state, and vetoable property changes. The listener should be chosen according to the component and behavior being observed.

### Adapter classes

Some listener interfaces contain several methods. Adapter classes provide empty implementations so that code can override only the needed methods:

```java
frame.addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosing(WindowEvent event) {
        confirmExit();
    }
});
```

### Event Dispatch Thread

Most Swing event handlers execute on the **Event Dispatch Thread (EDT)**. Handlers should complete quickly because long-running work blocks painting and prevents the interface from responding to new events.

Background work can be performed with `SwingWorker`, while Swing component updates remain on the EDT.

---

## What is the Java event hierarchy?

The event hierarchy organizes event classes from general to specific. This inheritance allows related event types to share common state and behavior.

A simplified AWT hierarchy is:

```text
java.util.EventObject
└── java.awt.AWTEvent
    ├── ActionEvent
    ├── AdjustmentEvent
    ├── ComponentEvent
    │   ├── ContainerEvent
    │   ├── FocusEvent
    │   ├── InputEvent
    │   │   ├── KeyEvent
    │   │   └── MouseEvent
    │   │       └── MouseWheelEvent
    │   └── WindowEvent
    ├── HierarchyEvent
    ├── InputMethodEvent
    ├── ItemEvent
    ├── PaintEvent
    └── TextEvent
```

The event-class hierarchy should not be confused with listener interfaces. For example, a `MouseEvent` object contains information about a mouse action, while a `MouseListener` receives selected types of mouse events.

Event objects often provide information such as:

- The source component
- Event time
- Mouse or keyboard modifiers
- Cursor coordinates
- Key code or character
- Click count

---

## What is a master-detail layout?

A **master-detail layout** is a user-interface pattern in which:

- The **master view** displays a collection of items.
- The **detail view** displays or edits information about the selected item.

Examples include:

- An email list with the selected message beside it
- A contact list with the selected contact's details
- A file browser with a preview panel
- A product list with an editing form

### Swing example structure

A Swing application can use:

- `JList` or `JTable` for the master view
- A custom `JPanel` for the detail view
- `JSplitPane` to display both areas
- `ListSelectionListener` to respond to selection changes

```java
JList<Customer> customerList = new JList<>(listModel);
JPanel detailPanel = createDetailPanel();

JSplitPane splitPane = new JSplitPane(
    JSplitPane.HORIZONTAL_SPLIT,
    new JScrollPane(customerList),
    detailPanel
);

customerList.addListSelectionListener(event -> {
    if (!event.getValueIsAdjusting()) {
        showCustomer(customerList.getSelectedValue());
    }
});
```

The selected object should be maintained in the application's model rather than inferred only from text displayed by the interface. This keeps data, selection state, and presentation responsibilities clear.

On narrow displays, the master and detail views may be shown on separate screens instead of side by side, while preserving the same interaction pattern.

---

## Key takeaways

- Choose a collection interface according to ordering, uniqueness, lookup, and processing requirements.
- `Map` is part of the Collections Framework but does not implement `Collection`.
- A collection view may share its data with the original collection, while a copy is independent.
- Exceptions signal problems, logs record evidence, and debugging investigates root causes.
- AWT and Swing use event sources, event objects, and listeners.
- Swing handlers run on the EDT and should not perform long-running work.
- Event classes form an inheritance hierarchy separate from listener interfaces.
- A master-detail layout connects item selection with a focused detail view.
