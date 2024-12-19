package org.example;

public class Literature_book extends Book implements Learn_English {
    public Literature_book(String title, String author, String publicationDate, String ibsn, String bookType, String otherSize) {
        super(title, author, publicationDate, ibsn, bookType, otherSize);
    }

    public Literature_book() {
        super();
    }

    @Override
    public void learn_english() {

    }
}