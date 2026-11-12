package library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the library rules: add and list books.
 * Every change is saved to the repository (books.json) immediately.
 */
public class LibraryService {

    private final List<Book> books;
    private final BookRepository repository;

    public LibraryService(BookRepository repository, List<Book> initialBooks) {
        this.repository = repository;
        this.books = new ArrayList<>(initialBooks);
    }

    /**
     * Adds a new book.
     *
     * @throws IllegalArgumentException if the data is invalid or the ID already exists
     * @throws IOException              if saving fails
     */
    public void addBook(int id, String title, String author) throws IOException {
        if (findById(id) != null) {
            throw new IllegalArgumentException("A book with ID " + id + " already exists.");
        }
        books.add(new Book(id, title, author)); // validates the fields
        repository.save(books);
    }

    /** Returns a copy of all books (the internal list is never exposed). */
    public List<Book> listBooks() {
        return new ArrayList<>(books);
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
