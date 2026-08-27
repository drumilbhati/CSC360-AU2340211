# Reflection — 27 August 2026

## Topics covered

- Maven build lifecycle phases
- Java source and target versions
- Character sets and text encoding
- Backward and forward compatibility
- JUnit
- Unit testing and integration testing

---

## Maven build lifecycle

Maven organizes the build process into ordered lifecycle phases. Running a phase also runs all earlier phases in that lifecycle.

### `mvn compile`

```sh
mvn compile
```

The `compile` phase compiles the application's source code from `src/main/java` and places the generated classes in `target/classes`.

It is useful for quickly checking whether the main source code compiles. It does not run tests, create a JAR, or install the artifact in the local Maven repository.

### `mvn package`

```sh
mvn package
```

The `package` phase performs the earlier phases, including compilation and testing, and then packages the application in its configured format, such as a JAR or WAR file. The generated package is normally placed in the `target` directory.

This is often what developers mean by performing a complete project build without installing the artifact locally.

### `mvn install`

```sh
mvn install
```

The `install` phase performs the preceding lifecycle phases and then copies the packaged artifact and its POM into the local Maven repository. The local repository is commonly located in the user's `.m2/repository` directory.

Installing an artifact allows other Maven projects on the same computer to use it as a dependency.

### Is `mvn build` a Maven command?

“Build” is a general term and may also appear as an action in an IDE. However, Maven's standard lifecycle does not contain a phase named `build`, so `mvn build` is not normally a valid Maven lifecycle command.

Depending on the desired result, use:

- `mvn compile` to compile the main code.
- `mvn test` to compile and run tests.
- `mvn package` to test and package the project.
- `mvn install` to package and install the artifact locally.
- `mvn clean package` to remove old output and create a fresh package.

### Phase comparison

| Command | Compiles code | Runs tests | Creates package | Installs locally |
|---|:---:|:---:|:---:|:---:|
| `mvn compile` | Yes | No | No | No |
| `mvn test` | Yes | Yes | No | No |
| `mvn package` | Yes | Yes | Yes | No |
| `mvn install` | Yes | Yes | Yes | Yes |

---

## Java source and target versions

### Source version

The source version tells the Java compiler which language syntax and features the source code may use. For example, Java 8 source compatibility prevents the use of language features added in later Java versions.

It can be configured in `pom.xml`:

```xml
<properties>
    <maven.compiler.source>8</maven.compiler.source>
</properties>
```

### Target version

The target version specifies the Java class-file version that the compiler should generate. A target of Java 8 produces bytecode intended for a Java 8 or newer JVM:

```xml
<properties>
    <maven.compiler.target>8</maven.compiler.target>
</properties>
```

A newer JVM can generally execute older Java bytecode. An older JVM cannot normally execute bytecode produced for a newer Java release and may report an `UnsupportedClassVersionError`.

### Prefer the release option

Setting only `source` and `target` does not prevent code from using Java APIs that were added after the selected target version. On supported JDKs, the compiler's `release` option is safer because it checks the language features, bytecode target, and public Java APIs together:

```xml
<properties>
    <maven.compiler.release>17</maven.compiler.release>
</properties>
```

The selected release should match the oldest Java version on which the application is required to run.

---

## Character sets and text encoding

A **character set** defines the characters that can be represented. A **character encoding** defines how those characters are converted into bytes for storage or transmission. The terms are often used together in development tools.

Common encodings include:

- **UTF-8:** A variable-length Unicode encoding that supports characters from languages around the world. It is the preferred encoding for most modern projects.
- **UTF-16:** A Unicode encoding that represents characters using one or two 16-bit code units.
- **ISO-8859-1:** A single-byte encoding limited mainly to Western European characters.
- **US-ASCII:** A small encoding for basic English letters, digits, punctuation, and control characters.

A Maven project can explicitly configure its source encoding:

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
</properties>
```

Explicitly selecting an encoding makes builds more consistent across operating systems and prevents text from being interpreted differently on different computers.

### Why do multiple character encodings exist?

Different encodings were developed at different times for different languages, systems, storage limits, and compatibility requirements. Older systems often used small regional encodings because storage and transmission capacity were limited.

Unicode was created to provide a shared character set for many writing systems. UTF-8 has become common because it supports Unicode, remains compatible with ASCII for its first 128 characters, and is efficient for many text files.

Multiple encodings still exist because legacy files, protocols, and applications may require them. A program must use the same encoding that was used to create the data; otherwise, characters may become corrupted or appear as unreadable symbols.

---

## Software compatibility

### What is backward compatibility?

Backward compatibility means that a newer version of a system continues to support software, data, or interfaces created for an older version.

In Java, class files compiled for an older Java release can generally run on a newer JVM. However, compatibility can still be affected by removed APIs, changed behavior, unavailable external libraries, or reliance on internal JDK features.

Backward compatibility is valuable because it allows existing applications and libraries to continue working as the platform evolves.

### What is forward compatibility?

Forward compatibility means that an older system can tolerate or process information produced for a newer version, usually by safely ignoring features it does not understand.

Java bytecode is **not generally forward compatible**: code compiled for a newer Java class-file version normally cannot run on an older JVM. To support an older JVM, a project must compile for that release, avoid newer language features, and avoid APIs that are unavailable in the older Java version.

For example, an application required to run on Java 17 should be compiled with an appropriate Java 17 release target rather than compiled for Java 21 and expected to run unchanged on Java 17.

---

## What is JUnit?

JUnit is a widely used testing framework for Java. It allows developers to write automated tests and verify that application behavior matches expectations.

JUnit provides:

- Annotations such as `@Test`, `@BeforeEach`, and `@AfterEach`.
- Assertions such as `assertEquals`, `assertTrue`, and `assertThrows`.
- Test lifecycle management.
- Parameterized and nested tests.
- Integration with Maven, Gradle, and development environments.

A simple JUnit 5 test is:

```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CalculatorTest {

    @Test
    void addsTwoNumbers() {
        Calculator calculator = new Calculator();

        int result = calculator.add(2, 3);

        assertEquals(5, result);
    }
}
```

Maven projects normally place test classes under `src/test/java` and run them with:

```sh
mvn test
```

---

## What is unit testing?

Unit testing verifies a small unit of behavior—often a method or class—in isolation from unrelated systems.

Good unit tests are usually:

- Automated and repeatable
- Fast to execute
- Independent of other tests
- Focused on one behavior
- Clear about the expected result

Dependencies such as databases, networks, and external services may be replaced with test doubles when isolation is necessary. Unit tests help detect defects early and provide confidence when code is changed or refactored.

## What is integration testing?

Integration testing combines multiple components and verifies that they work together correctly. It can identify problems that isolated unit tests may not reveal, including:

- Incorrect data flow between modules
- Interface or configuration mismatches
- Database integration problems
- Serialization and deserialization errors
- Network or external-service communication issues

Integration tests are generally slower and require more setup than unit tests because they may use real databases, file systems, containers, or services.

### Unit and integration testing comparison

| Aspect | Unit testing | Integration testing |
|---|---|---|
| Scope | One small unit of behavior | Multiple connected components |
| Dependencies | Usually isolated or replaced | Often uses real implementations |
| Speed | Usually fast | Usually slower |
| Purpose | Verify local logic | Verify component interactions |
| Failure diagnosis | Usually straightforward | May require investigation across components |

Both types are important: unit tests validate individual behavior, while integration tests confirm that the pieces work together as a system.

---

## Key takeaways

- Maven lifecycle phases are ordered; running a later phase also runs the earlier phases.
- `compile`, `test`, `package`, and `install` produce different results.
- Maven does not normally provide a standard lifecycle phase named `build`.
- Java's `release` compiler option is safer than configuring only `source` and `target`.
- Older Java bytecode generally runs on newer JVMs, but newer bytecode does not generally run on older JVMs.
- UTF-8 is a practical default encoding for modern projects.
- JUnit supports repeatable automated testing in Java.
- Unit tests verify small behaviors, while integration tests verify collaboration between components.
