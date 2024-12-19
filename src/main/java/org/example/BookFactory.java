package org.example;

public class BookFactory {
    public static Book createBook(String title, String author, String publicationDate, String ibsn, String bookType, String otherSize) {
        return new Book(title, author, publicationDate, ibsn, bookType, otherSize);
    }
}
