package library;

/**
 * Represents one book in the library.
 * The object is immutable: once created, its values never change.
 */
public class Book {

    private final int id;
    private final String title;
    private final String author;

    public Book(int id, String title, String author) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be empty.");
        }
        this.id = id;
        this.title = title.trim();
        this.author = author.trim();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s", id, title, author);
    }
}
// Rebase demo utility note
