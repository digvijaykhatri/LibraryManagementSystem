import java.util.*;
import java.util.stream.Collectors;

// Book class representing a book in the library
class Book {
    private final String id;
    private final String title;
    private final String author;
    private final String isbn;
    private boolean isIssued;
    private String issuedTo;
    private Date issueDate;

    public Book(String title, String author) {
        this(UUID.randomUUID().toString(), title, author, null);
    }

    public Book(String id, String title, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isIssued = false;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isIssued() { return isIssued; }
    public String getIssuedTo() { return issuedTo; }
    public Date getIssueDate() { return issueDate; }

    public void issue(String borrowerName) {
        this.isIssued = true;
        this.issuedTo = borrowerName;
        this.issueDate = new Date();
    }

    public void returnBook() {
        this.isIssued = false;
        this.issuedTo = null;
        this.issueDate = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(title)
          .append(", Author: ").append(author);
        if (isbn != null) {
            sb.append(", ISBN: ").append(isbn);
        }
        if (isIssued) {
            sb.append(" (Issued to: ").append(issuedTo).append(")");
        } else {
            sb.append(" (Available)");
        }
        return sb.toString();
    }
}

// Custom exception for library operations
class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }
}

// Library class responsible for managing books
class Library {
    private final Map<String, Book> booksById;
    private final Map<String, List<Book>> booksByTitle;

    public Library() {
        this.booksById = new HashMap<>();
        this.booksByTitle = new HashMap<>();
    }

    public void addBook(Book book) throws LibraryException {
        addBook(book, true);
    }

    public void addBook(Book book, boolean showMessage) throws LibraryException {
        if (book == null) {
            throw new LibraryException("Cannot add null book");
        }
        
        // Check for duplicate by ID
        if (booksById.containsKey(book.getId())) {
            throw new LibraryException("Book with this ID already exists");
        }
        
        booksById.put(book.getId(), book);
        booksByTitle.computeIfAbsent(book.getTitle().toLowerCase(), k -> new ArrayList<>()).add(book);
        
        if (showMessage) {
            System.out.println("Book added successfully: " + book.getTitle());
        }
    }

    public void deleteBook(String title) throws LibraryException {
        List<Book> books = findBooksByTitle(title);
        
        if (books.isEmpty()) {
            throw new LibraryException("Book not found: " + title);
        }
        
        if (books.size() > 1) {
            System.out.println("Multiple books found with this title:");
            for (int i = 0; i < books.size(); i++) {
                System.out.println((i + 1) + ". " + books.get(i));
            }
            throw new LibraryException("Please specify which book to delete");
        }
        
        Book bookToDelete = books.get(0);
        if (bookToDelete.isIssued()) {
            throw new LibraryException("Cannot delete an issued book. Please return it first.");
        }
        
        booksById.remove(bookToDelete.getId());
        booksByTitle.get(title.toLowerCase()).remove(bookToDelete);
        if (booksByTitle.get(title.toLowerCase()).isEmpty()) {
            booksByTitle.remove(title.toLowerCase());
        }
        
        System.out.println("Book deleted: " + bookToDelete.getTitle());
    }

    public void issueBook(String title, String borrowerName) throws LibraryException {
        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            throw new LibraryException("Borrower name cannot be empty");
        }
        
        List<Book> availableBooks = findBooksByTitle(title).stream()
                .filter(book -> !book.isIssued())
                .collect(Collectors.toList());
        
        if (availableBooks.isEmpty()) {
            throw new LibraryException("No available copies of this book");
        }
        
        Book bookToIssue = availableBooks.get(0);
        bookToIssue.issue(borrowerName.trim());
        System.out.println("Book issued successfully: " + bookToIssue.getTitle() + " to " + borrowerName);
    }

    public void returnBook(String title) throws LibraryException {
        List<Book> issuedBooks = findBooksByTitle(title).stream()
                .filter(Book::isIssued)
                .collect(Collectors.toList());
        
        if (issuedBooks.isEmpty()) {
            throw new LibraryException("No issued copies of this book found");
        }
        
        if (issuedBooks.size() > 1) {
            System.out.println("Multiple issued copies found:");
            for (int i = 0; i < issuedBooks.size(); i++) {
                System.out.println((i + 1) + ". " + issuedBooks.get(i));
            }
            throw new LibraryException("Please specify which copy to return");
        }
        
        Book bookToReturn = issuedBooks.get(0);
        String borrower = bookToReturn.getIssuedTo();
        bookToReturn.returnBook();
        System.out.println("Book returned successfully: " + bookToReturn.getTitle() + " from " + borrower);
    }

    public void displayBooks() {
        if (booksById.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }
        
        System.out.println("\n=== Library Inventory ===");
        System.out.println("Total books: " + booksById.size());
        System.out.println("Available: " + booksById.values().stream().filter(b -> !b.isIssued()).count());
        System.out.println("Issued: " + booksById.values().stream().filter(Book::isIssued).count());
        System.out.println("\nBooks:");
        
        booksById.values().stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    public void searchBooks(String query) {
        String lowerQuery = query.toLowerCase();
        List<Book> results = booksById.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerQuery) ||
                               book.getAuthor().toLowerCase().contains(lowerQuery))
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
        
        if (results.isEmpty()) {
            System.out.println("No books found matching: " + query);
        } else {
            System.out.println("Found " + results.size() + " book(s):");
            results.forEach(System.out::println);
        }
    }

    private List<Book> findBooksByTitle(String title) {
        return booksByTitle.getOrDefault(title.toLowerCase(), new ArrayList<>());
    }
}

// Main class to run the Library Management System
public class LibraryManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Library library = new Library();

    public static void main(String[] args) {
        preloadBooks();
        
        System.out.println("Welcome to the Library Management System!");
        
        while (true) {
            displayMenu();
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        library.displayBooks();
                        break;
                    case 2:
                        issueBook();
                        break;
                    case 3:
                        returnBook();
                        break;
                    case 4:
                        addBook();
                        break;
                    case 5:
                        deleteBook();
                        break;
                    case 6:
                        searchBooks();
                        break;
                    case 7:
                        System.out.println("Thank you for using the Library Management System!");
                        return;
                    default:
                        System.out.println("Invalid option. Please choose between 1-7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. Display All Books");
        System.out.println("2. Issue Book");
        System.out.println("3. Return Book");
        System.out.println("4. Add Book");
        System.out.println("5. Delete Book");
        System.out.println("6. Search Books");
        System.out.println("7. Exit");
        System.out.print("Choose an option (1-7): ");
    }

    private static void issueBook() throws LibraryException {
        System.out.print("Enter the title of the book to issue: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter borrower's name: ");
        String borrowerName = scanner.nextLine().trim();
        library.issueBook(title, borrowerName);
    }

    private static void returnBook() throws LibraryException {
        System.out.print("Enter the title of the book to return: ");
        String title = scanner.nextLine().trim();
        library.returnBook(title);
    }

    private static void addBook() throws LibraryException {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter the author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Enter ISBN (optional, press Enter to skip): ");
        String isbn = scanner.nextLine().trim();
        
        if (title.isEmpty() || author.isEmpty()) {
            throw new LibraryException("Title and author cannot be empty");
        }
        
        Book newBook = new Book(UUID.randomUUID().toString(), title, author, 
                               isbn.isEmpty() ? null : isbn);
        library.addBook(newBook);
    }

    private static void deleteBook() throws LibraryException {
        System.out.print("Enter the title of the book to delete: ");
        String title = scanner.nextLine().trim();
        library.deleteBook(title);
    }

    private static void searchBooks() {
        System.out.print("Enter search query (title or author): ");
        String query = scanner.nextLine().trim();
        library.searchBooks(query);
    }

    private static void preloadBooks() {
        try {
            library.addBook(new Book("Java Programming", "James Gosling"), false);
            library.addBook(new Book("Data Structures", "Mark Allen Weiss"), false);
            library.addBook(new Book("Algorithm Design", "Jon Kleinberg"), false);
            library.addBook(new Book("The 48 Laws of Power", "Robert Greene"), false);
            library.addBook(new Book("Mastery", "Robert Greene"), false);
            library.addBook(new Book("The 33 Strategies of War", "Robert Greene"), false);
            library.addBook(new Book("On the Origin of Species", "Charles Darwin"), false);
        } catch (LibraryException e) {
            System.err.println("Error loading initial books: " + e.getMessage());
        }
    }
}