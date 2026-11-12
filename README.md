# Library Tracker CLI

A clean, lightweight Java command-line application for managing a personal or small library collection. All book records are automatically persisted to a local `books.json` file with zero external dependencies.

## Features
- **Add Books:** Register new books with a unique positive ID, title, and author.
- **List Books:** Display all saved books in a formatted list.
- **Search Books:** Perform case-insensitive partial searches by book title or author name.
- **Remove Books:** Delete a book record safely using its unique ID.
- **Automatic Recovery:** Backs up corrupted JSON data automatically (`books.json.bak`) without crashing.

## Prerequisites
- **Java Development Kit (JDK):** Version 17 or higher.
- **Git:** For cloning and version control.

## Setup & Run Instructions
1. **Clone the repository:**
   ```bash
   git clone https://github.com/aalsaftawi-boop/library-tracker.git
   cd library-tracker
   ```
2. **Compile the Java source files:**
   ```bash
   javac src/library/*.java
   ```
3. **Run the application:**
   ```bash
   java -cp src library.Main
   ```

## Usage Example
```text
========================================
      LIBRARY TRACKER - V2 SMART EDITION
========================================

1. Add a book
2. List all books
3. Search books
4. Remove a book
5. Exit
Choose an option (1-5): 1
Book ID (positive number): 101
Title: Clean Code
Author: Robert C. Martin
Book added.

Choose an option (1-5): 2
Books (1):
  [101] Clean Code - Robert C. Martin

Choose an option (1-5): 3
Search by title or author: clean
Matching books (1):
  [101] Clean Code - Robert C. Martin
```

## Contributors
See [CONTRIBUTORS.md](CONTRIBUTORS.md) for project contributor details.
