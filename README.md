# Advanced File System — Visitor Pattern (Assignment 06)

An in-memory hierarchical file system implementing the **Visitor behavioral design pattern** in Java 17. 

---

## Project Overview

This project provides an extensible and fully encapsulated in-memory file system structure. By implementing the **Double Dispatch** mechanism, operations are completely decoupled from elements without using runtime type discrimination (no `instanceof`, `getClass()`, reflection, or explicit casts).

### Key Features & Requirements

- **R1 — Object Model:** Unified `Element` interface with concrete `FileNode` (leaf) and `DirectoryNode` (composite) elements.
- **R2 — Double Dispatch:** Overloaded `visit` methods in the `Visitor` interface coupled with `accept(Visitor)` implementations in element nodes.
- **R3 — Size Visitor (`TotalSizeVisitor`):** Computes total byte size across subtrees. Directories do not contribute byte sizes.
- **R4 — Search Visitor (`SearchVisitor`):** Combines filename pattern matching and minimum size threshold ($size \ge threshold$). Outputs root-relative, forward-slash (`/`) separated paths sorted lexicographically using `TreeMap`.
- **R5 — Statistics Visitor (`StatisticsVisitor`):** Collects total file count, directory count (including root), and maximum tree depth ($root = 0$).
- **R6 — Tree Integrity Guards:** Rejects negative file sizes, duplicate sibling names, double-parenting, and cyclic parent references predictably.
- **R7 — Traversal Ownership:** Visitor-driven recursive traversal ensures explicit state control and isolation via `reset()` methods.
- **R8 — Extensibility Boundary:** `StatisticsVisitor` was added seamlessly without modifying existing element classes (`FileNode`, `DirectoryNode`).

---

## Project Structure

```text
visitor-filesystem/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── example/
    │               └── filesystem/
    │                   ├── Element.java
    │                   ├── Visitor.java
    │                   ├── FileNode.java
    │                   ├── DirectoryNode.java
    │                   ├── TotalSizeVisitor.java
    │                   ├── SearchVisitor.java
    │                   ├── StatisticsVisitor.java
    │                   └── Main.java
    └── test/
        └── java/
            └── com/
                └── example/
                    └── filesystem/
                        └── FileSystemTest.java
```

---

## Prerequisites

- **Java Development Kit (JDK):** Version 17 or higher
- **Apache Maven:** Version 3.8+

---

## Build & Execution Instructions

### 1. Run Automated Test Suite (JUnit 5)

To run all automated verification checkpoints (T1 through T8):

```bash
mvn test
```

### 2. Execute Demonstration Program

To run the main application demonstrating all three visitor operations:

```bash
mvn compile exec:java -Dexec.mainClass=com.example.filesystem.Main
```

---

## Test Verification Matrix (T1–T8)

| Checkpoint | Target Description | Status |
| :--- | :--- | :---: |
| **T1** | Both element types (`FileNode`, `DirectoryNode`) invoke correct overloaded `visit` methods via double dispatch. | ✅ PASSED |
| **T2** | `TotalSizeVisitor` accurately calculates total file sizes across deeply nested structures. | ✅ PASSED |
| **T3** | `SearchVisitor` combines name filtering and minimum size threshold criteria properly. | ✅ PASSED |
| **T4** | `SearchVisitor` generates deterministic, root-relative, lexicographically sorted `/` paths. | ✅ PASSED |
| **T5** | `StatisticsVisitor` computes exact file count, directory count, and maximum tree depth ($root = 0$). | ✅ PASSED |
| **T6** | Edge cases (empty directories, zero-match searches) are handled safely without exceptions. | ✅ PASSED |
| **T7** | Repeated visitor executions with `reset()` prevent state leakage and duplicate counts. | ✅ PASSED |
| **T8** | Tree mutations correctly throw `IllegalArgumentException` on invalid mutations (negative size, cycles, duplicates). | ✅ PASSED |

---

## Clean Code Practices & Design Trade-Offs

### Clean Code Practices Implemented
1. **Single Responsibility Principle (SRP):** Nodes preserve graph structure; operations (size calculation, searching, statistics) are isolated inside concrete Visitor implementations.
2. **Encapsulation:** Internal child collections in `DirectoryNode` are exposed solely as read-only wrappers (`Collections.unmodifiableCollection`).
3. **Elimination of Type Chains:** Polymorphic double dispatch completely removes the need for `instanceof` conditional branching.
4. **Meaningful Naming:** Domain-specific names (`minThreshold`, `getMatchingPaths`, `isCycle`) eliminate the need for redundant comments.
5. **Small Methods:** Single-purpose helper functions (e.g., `isCycle()`) keep methods short and easily readable.

### Architectural Trade-Offs
* **Visitor-Driven Traversal:** Delegating recursive traversal to the Visitor allows stateful operations (like stack path construction in `SearchVisitor` or depth tracking in `StatisticsVisitor`) to maintain local context easily.
* **Extensibility Balance:** Adding new *operations* (e.g., `StatisticsVisitor`) requires zero changes to element classes (satisfying the Open/Closed Principle). Conversely, adding a new *element type* (e.g., `SymlinkNode`) requires updating the `Visitor` interface and modifying all existing visitors.