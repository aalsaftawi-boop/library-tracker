package library;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Console menu for the Library Tracker. */
public class Main {

    private static final String DATA_FILE = "books.json";

    public static void main(String[] args) {
        BookRepository repository = new BookRepository(Path.of(DATA_FILE));
        LibraryService service = new LibraryService(repository, loadBooks(repository));
        Scanner scanner = new Scanner(System.in);

        printBanner();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine(scanner, "Choose an option (1-3): ");
            if (choice == null) { // input ended (e.g. Ctrl+D)
                break;
            }
            switch (choice.trim()) {
                case "1" -> addBook(scanner, service);
                case "2" -> listBooks(service);
                case "3" -> running = false;
                default -> System.out.println("Invalid option. Please enter a number from 1 to 3.");
            }
        }
        System.out.println("Goodbye!");
    }

    public static void printBanner() {
        System.out.println("=================================");
        System.out.println("        LIBRARY TRACKER          ");
        System.out.println("=================================");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Add a book");
        System.out.println("2. List all books");
        System.out.println("3. Exit");
    }

    /** Loads books; if the file is damaged, keeps a backup and starts empty. */
    private static List<Book> loadBooks(BookRepository repository) {
        try {
            return repository.load();
        } catch (IOException e) {
            System.out.println("Warning: " + e.getMessage());
            try {
                System.out.println("The damaged file was saved as " + repository.backupDamagedFile());
            } catch (IOException backupError) {
                System.out.println("Could not back up the damaged file: " + backupError.getMessage());
            }
            System.out.println("Starting with an empty library.");
            return new ArrayList<>();
        }
    }

    private static void addBook(Scanner scanner, LibraryService service) {
        Integer id = readInt(scanner, "Book ID (positive number): ");
        if (id == null) {
            return;
        }
        String title = readLine(scanner, "Title: ");
        String author = readLine(scanner, "Author: ");
        if (title == null || author == null) {
            return;
        }
        try {
            service.addBook(id, title, author);
            System.out.println("Book added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add book: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not save books.json: " + e.getMessage());
        }
    }

    private static void listBooks(LibraryService service) {
        List<Book> books = service.listBooks();
        if (books.isEmpty()) {
            System.out.println("The library is empty.");
            return;
        }
        System.out.println("Books (" + books.size() + "):");
        for (Book b : books) {
            System.out.println("  " + b);
        }
    }

    /** Reads a line; returns null when there is no more input. */
    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /** Reads a whole number; returns null if the input is not a valid number. */
    private static Integer readInt(Scanner scanner, String prompt) {
        String text = readLine(scanner, prompt);
        if (text == null) {
            return null;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: please enter digits only.");
            return null;
        }
    }
}
