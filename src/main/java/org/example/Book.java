package org.example;

public class Book {
    private String title;
    private String author;
    private String publicationDate;
    private String ibsn;
    private String bookType;
    private String otherSize;

    public Book(String title, String author, String publicationDate, String ibsn, String bookType, String otherSize) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.ibsn = ibsn;
        this.bookType = bookType;
        this.otherSize = otherSize;
    }

    public Book() {

    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getIbsn() {
        return ibsn;
    }

    public void setIbsn(String ibsn) {
        this.ibsn = ibsn;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getOtherSize() {
        return otherSize;
    }

    public void setOtherSize(String otherSize) {
        this.otherSize = otherSize;
    }
}

