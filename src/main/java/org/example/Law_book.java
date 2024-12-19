package org.example;

public class Law_book extends Book implements Learn_Math,Learn_English {
    public Law_book(String title, String author, String publicationDate, String ibsn, String bookType, String otherSize) {
        super(title, author, publicationDate, ibsn, bookType, otherSize);
    }

    public Law_book() {
        super();
    }

    @Override
    public void learn_english() {

    }

    @Override
    public void learn_math() {

    }
}