package org.example;

public class Computer_book extends Book implements Learn_Math{
    public Computer_book(String title, String author, String publicationDate, String ibsn, String bookType, String otherSize) {
        super(title, author, publicationDate, ibsn, bookType, otherSize);
    }

    public Computer_book() {
        super();
    }

    @Override
    public void learn_math() {

    }
}
