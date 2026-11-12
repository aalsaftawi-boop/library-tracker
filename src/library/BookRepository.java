package library;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes the list of books to a JSON file (books.json).
 * A tiny hand-written JSON reader/writer is used so the project
 * needs no external libraries.
 */
public class BookRepository {

    private final Path file;

    public BookRepository(Path file) {
        this.file = file;
    }

    /**
     * Loads all books from the file.
     * A missing or empty file is not an error: it just means "no books yet".
     *
     * @throws IOException if the file cannot be read or contains invalid data
     */
    public List<Book> load() throws IOException {
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }
        String json = Files.readString(file, StandardCharsets.UTF_8);
        if (json.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return new JsonReader(json).readBooks();
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid data in " + file + ": " + e.getMessage(), e);
        }
    }

    /** Writes all books to the file as a JSON array. */
    public void save(List<Book> books) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            sb.append("  {\"id\": ").append(b.getId())
              .append(", \"title\": \"").append(escape(b.getTitle()))
              .append("\", \"author\": \"").append(escape(b.getAuthor()))
              .append("\"}");
            if (i < books.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]\n");
        Files.writeString(file, sb.toString(), StandardCharsets.UTF_8);
    }

    /**
     * Renames a damaged file to books.json.bak so that it is not
     * overwritten by the next save. Returns the backup path.
     */
    public Path backupDamagedFile() throws IOException {
        Path backup = file.resolveSibling(file.getFileName() + ".bak");
        Files.move(file, backup, StandardCopyOption.REPLACE_EXISTING);
        return backup;
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /** Minimal JSON reader for: [ {"id": 1, "title": "...", "author": "..."}, ... ] */
    private static final class JsonReader {

        private final String s;
        private int pos = 0;

        JsonReader(String s) {
            this.s = s;
        }

        List<Book> readBooks() {
            List<Book> books = new ArrayList<>();
            skipSpaces();
            expect('[');
            skipSpaces();
            if (peek() == ']') {
                pos++;
            } else {
                while (true) {
                    books.add(readBook());
                    skipSpaces();
                    char c = next();
                    if (c == ']') {
                        break;
                    }
                    if (c != ',') {
                        throw error("Expected ',' or ']'");
                    }
                }
            }
            skipSpaces();
            if (pos < s.length()) {
                throw error("Unexpected text after the closing ']'");
            }
            return books;
        }

        private Book readBook() {
            skipSpaces();
            expect('{');
            Integer id = null;
            String title = null;
            String author = null;
            while (true) {
                skipSpaces();
                String key = readString();
                skipSpaces();
                expect(':');
                skipSpaces();
                switch (key) {
                    case "id" -> id = readInt();
                    case "title" -> title = readString();
                    case "author" -> author = readString();
                    default -> throw error("Unknown field '" + key + "'");
                }
                skipSpaces();
                char c = next();
                if (c == '}') {
                    break;
                }
                if (c != ',') {
                    throw error("Expected ',' or '}'");
                }
            }
            if (id == null || title == null || author == null) {
                throw error("A book must have id, title and author");
            }
            return new Book(id, title, author);
        }

        private String readString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (true) {
                char c = next();
                if (c == '"') {
                    return sb.toString();
                }
                if (c == '\\') {
                    char escaped = next();
                    switch (escaped) {
                        case '"' -> sb.append('"');
                        case '\\' -> sb.append('\\');
                        case 'n' -> sb.append('\n');
                        case 't' -> sb.append('\t');
                        default -> throw error("Unsupported escape \\" + escaped);
                    }
                } else {
                    sb.append(c);
                }
            }
        }

        private int readInt() {
            int start = pos;
            while (pos < s.length() && Character.isDigit(s.charAt(pos))) {
                pos++;
            }
            if (start == pos) {
                throw error("Expected a number");
            }
            try {
                return Integer.parseInt(s.substring(start, pos));
            } catch (NumberFormatException e) {
                throw error("Number is too large");
            }
        }

        private void skipSpaces() {
            while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) {
                pos++;
            }
        }

        private char peek() {
            if (pos >= s.length()) {
                throw error("Unexpected end of file");
            }
            return s.charAt(pos);
        }

        private char next() {
            char c = peek();
            pos++;
            return c;
        }

        private void expect(char expected) {
            char c = next();
            if (c != expected) {
                pos--;
                throw error("Expected '" + expected + "' but found '" + c + "'");
            }
        }

        private IllegalArgumentException error(String message) {
            return new IllegalArgumentException(message + " (at position " + pos + ")");
        }
    }
}
