# Library Tracker

A small console application written in Java for managing a list of books.
It was built for **Software Engineering – Assignment 2 (Team Git: GitHub Flow, Reviews & Recovery)**.
The code is intentionally simple: the goal of the project is to practise teamwork with Git and GitHub.

## Author

This is an individual submission.

| Role | Full name | GitHub username |
|------|-----------|-----------------|
| Student (individual submission) | Ahmad Mohammad Asaad Al-Saftawi | `aalsaftawi-boop` |

## Features

- Add a book (ID, title, author)
- List all books
- Search books by title or author (not case-sensitive)
- Remove a book by ID
- Data is saved automatically to `books.json` and loaded when the program starts

Input is validated: IDs must be positive numbers and unique, title and author cannot be empty,
and a missing or damaged `books.json` is handled safely.

## Requirements

- Java JDK 17 or newer (`javac -version` to check)
- No external libraries

## How to compile and run

From the project root folder:

```bash
# Compile
javac -d out src/library/*.java

# Run
java -cp out library.Main
```

The program creates or updates `books.json` in the folder you run it from.

## Project structure

```
library-tracker/
├── src/library/
│   ├── Book.java            # One book (id, title, author) with validation
│   ├── BookRepository.java  # Reads/writes books.json
│   ├── LibraryService.java  # Add, list, search, remove logic
│   └── Main.java            # Menu and user input
├── books.json               # Saved data
├── .gitignore
└── README.md
```

## Git workflow (GitHub Flow)

1. `main` always contains working code and is never pushed to directly.
2. Each feature starts with a GitHub Issue.
3. A feature branch is created from `main` for each feature:
   - `feature/add-list-books` (add and list books)
   - `feature/search-remove` (search and remove books)
4. Work is committed in small steps using [Conventional Commits](https://www.conventionalcommits.org/),
   for example `feat(books): add book creation`.
5. A Pull Request is opened and linked to its issue with `Closes #N`.
6. Each Pull Request goes through a review step, and requested changes are fixed before merging.
7. After approval the Pull Request is merged and the feature branch is deleted.
## Usage example

1. Compile and run the program (see the commands above).
2. Choose `1` to add a book, then enter an ID, a title and an author.
3. Choose `2` to list all stored books.

Example of adding a book and listing it:

```
Book ID (positive number): 1
Title: Clean Code
Author: Robert Martin
Book added.
Books (1):
  [1] Clean Code - Robert Martin
```

The books are saved in `books.json`, so they are still there the next time you start the program.

### Search & Remove
- Choose `3` to search for books by title or author.
- Choose `4` to remove a book by its ID.
- Note: Search is case-insensitive.
- Updated library banner to V2 Smart Edition.
