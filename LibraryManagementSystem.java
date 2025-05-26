import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Book class representing a book in the library
class Book {
    private String title;
    private String author;
    private boolean isIssued;

    // Constructor
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public void setIssued(boolean issued) {
        isIssued = issued;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + (isIssued ? " (Issued)" : " (Available)");
    }
}

// Library class responsible for managing books
class Library {
    private List<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    // Add book to the library
    public void addBook(Book book) {
        books.add(book);
    }

    // Issue a book to a user
    public void issueBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && !book.isIssued()) {
                book.setIssued(true);
                System.out.println("Book issued: " + book.getTitle());
                return;
            }
        }
        System.out.println("Sorry, this book is either unavailable or already issued.");
    }

    // Return a book
    public void returnBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.isIssued()) {
                book.setIssued(false);
                System.out.println("Book returned: " + book.getTitle());
                return;
            }
        }
        System.out.println("This book was not issued or does not exist in the library.");
    }

    // Display the list of all books
    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }
        for (Book book : books) {
            System.out.println(book);
        }
    }
}

// Main class to run the Library Management System
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();
        // Use try-with-resources to ensure Scanner is closed
        try (Scanner obj = new Scanner(System.in)) {
            // Adding books to the library
            library.addBook(new Book("Java Programming", "James Gosling"));
            library.addBook(new Book("Data Structures", "Mark Allen Weiss"));
            library.addBook(new Book("Algorithm Design", "Jon Kleinberg"));
            library.addBook(new Book("48 Laws of Power", "Robert Green"));
            library.addBook(new Book("Mastrey", "Robert Green"));
            library.addBook(new Book("War", "Robert Green"));
            library.addBook(new Book("On the Origin of Species", "Charles Darwin"));

            while (true) {
                System.out.println("\nLibrary Management System:");
                System.out.println("1. Display Books");
                System.out.println("2. Issue Book");
                System.out.println("3. Return Book");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                int choice = obj.nextInt();
                obj.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        library.displayBooks();
                        break;
                    case 2:
                        System.out.print("Enter the title of the book to issue: ");
                        String issueTitle = obj.nextLine();
                        library.issueBook(issueTitle);
                        break;
                    case 3:
                        System.out.print("Enter the title of the book to return: ");
                        String returnTitle = obj.nextLine();
                        library.returnBook(returnTitle);
                        break;
                    case 4:
                        System.out.println("Exiting the system...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }
}