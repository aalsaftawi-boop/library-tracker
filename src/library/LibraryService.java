package library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the library rules: add, list, search, and remove books.
 * Every change is saved to the repository (books.json) immediately.
 */
public class LibraryService {

    private final List<Book> books;
    private final BookRepository repository;

    public LibraryService(BookRepository repository, List<Book> initialBooks) {
        this.repository = repository;
        this.books = new ArrayList<>(initialBooks);
    }

    /** Adds a new book. */
    public void addBook(int id, String title, String author) throws IOException {
        if (findById(id) != null) {
            throw new IllegalArgumentException("A book with ID " + id + " already exists.");
        }
        books.add(new Book(id, title, author));
        repository.save(books);
    }

    /** Returns a copy of all books. */
    public List<Book> listBooks() {
        return new ArrayList<>(books);
    }

    /** Searches books by title or author (case-insensitive). */
    public List<Book> searchBooks(String query) {
        if (query == null || query.isBlank()) {
            return new ArrayList<>();
        }
        String lower = query.trim().toLowerCase();
        List<Book> matches = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(lower) || b.getAuthor().toLowerCase().contains(lower)) {
                matches.add(b);
            }
        }
        return matches;
    }

    /** Removes a book by its ID. Returns true if removed, false if not found. */
    public boolean removeBook(int id) throws IOException {
        Book target = findById(id);
        if (target == null) {
            return false;
        }
        books.remove(target);
        repository.save(books);
        return true;
    }

    private Book findById(int id) {
        for (Book b : books) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }
}
