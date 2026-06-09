package com.hei.school.exception;

public class BookNotFoundException extends RuntimeException {

    private final Long bookId;

    public BookNotFoundException(Long id) {
        super("Book not found with id: " + id);
        this.bookId = id;
    }

    public BookNotFoundException(String message) {
        super(message);
        this.bookId = null;
    }

    public Long getBookId() {
        return bookId;
    }
}
