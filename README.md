# 📚 Library Management System (Java - Console Based)

## Overview

This Java application is a simple **Library Management System** that allows users to manage books with functionalities including adding, deleting, issuing, returning, displaying, and searching books. It uses `HashMap` and `ArrayList` internally to efficiently manage the collection of books.

---

## Features

- Add new books with title, author, and optional ISBN.
- Delete books by title (with checks to prevent deleting issued books).
- Issue books to borrowers.
- Return issued books.
- Display all books with their availability status.
- Search books by title or author.
- Preloaded sample books on startup.

---

## Project Structure

### Book Class
- Represents a book with fields: ID, title, author, ISBN, issue status, borrower, and issue date.
- Methods to issue and return books.
- Custom `toString()` to display book details and status.

### LibraryException Class
- Custom exception to handle library-specific errors.

### Library Class
- Stores books using:
  - `HashMap<String, Book>` for quick access by book ID.
  - `HashMap<String, List<Book>>` to group books by title (case-insensitive).
- Supports adding, deleting, issuing, returning, displaying, and searching books.
- Handles multiple copies of books with the same title.

### LibraryManagementSystem Class
- Console-based menu interface.
- Handles user inputs and calls library operations.
- Loads sample books at startup.
- Gracefully manages errors and invalid inputs.

---

## Data Structures & Algorithms

- Uses `HashMap` for O(1) average lookup by book ID and title.
- Titles stored in lowercase for case-insensitive operations.
- Books sorted alphabetically by title using Java's Comparator for display and search results.
- Java Stream API used for filtering and sorting collections.

---

## How to Run

1. Compile:
    ```bash
    javac LibraryManagementSystem.java
    ```

2. Run:
    ```bash
    java LibraryManagementSystem
    ```

3. Use the interactive menu to manage library books.

---

## Exception Handling

- Prevents adding duplicate book IDs.
- Prevents deleting issued books.
- Ensures valid borrower names when issuing books.
- Handles attempts to return non-issued books.
- Provides clear error messages on invalid operations.

---

## Potential Improvements

- Add support for multiple copies with unique copy IDs.
- Implement due dates and late fees.
- Enhance search with author and ISBN filters.
- Persist data using files or databases.
- Create a GUI for better user experience.

---

## Author

Created by Digvijay Khatri, Amit Pandey, Chetan Kumar, Abhishek Chauhan

