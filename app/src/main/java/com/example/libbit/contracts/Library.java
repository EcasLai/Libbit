package com.example.libbit.contracts;

import org.web3j.abi.datatypes.DynamicStruct;

import java.util.HashMap;
import java.util.Map;

public class Library extends DynamicStruct {
    private String admin;
    private Map<String, Book> books = new HashMap<>();
    private int totalBooks;
    private String pdfHash;

    public Library(String admin) {
        this.admin = admin;
        this.totalBooks = 0;
    }

    public void addBook(String bookId, String title, String author, int copiesAvailable) {
        if (!books.containsKey(bookId)) {
            Book newBook = new Book(title, author, copiesAvailable);
            books.put(bookId, newBook);
            totalBooks++;
        }
    }

    public void removeBook(String bookId) {
        if (books.containsKey(bookId)) {
            books.remove(bookId);
            totalBooks--;
        }
    }

    public void borrowBook(String bookId, int copiesToBorrow, long dueDate) {
        Book book = books.get(bookId);
        if (book != null && book.getCopiesAvailable() >= copiesToBorrow) {
            book.borrow(copiesToBorrow, dueDate);
        }
    }

    public void returnBook(String bookId, int copiesToReturn) {
        Book book = books.get(bookId);
        if (book != null && book.getCopiesBorrowed() >= copiesToReturn) {
            book.returnCopies(copiesToReturn);
        }
    }

    public BorrowInfo getBorrowInfo(String bookId, String borrower) {
        Book book = books.get(bookId);
        if (book != null) {
            return book.getBorrowInfo(borrower);
        }
        return null;
    }

    public int checkAvailability(String bookId) {
        Book book = books.get(bookId);
        return book != null ? book.getCopiesAvailable() : 0;
    }

    public String getAdmin() {
        return admin;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public String getPdfHash() {
        return pdfHash;
    }

    public void setPdfHash(String pdfHash) {
        this.pdfHash = pdfHash;
    }

    private class Book {
        private String title;
        private String author;
        private int copiesAvailable;
        private int copiesBorrowed;
        private Map<String, BorrowInfo> borrowers = new HashMap<>();

        public Book(String title, String author, int copiesAvailable) {
            this.title = title;
            this.author = author;
            this.copiesAvailable = copiesAvailable;
            this.copiesBorrowed = 0;
        }

        public void borrow(int copiesToBorrow, long dueDate) {
            copiesAvailable -= copiesToBorrow;
            copiesBorrowed += copiesToBorrow;
            // Implement borrower details storage
        }

        public void returnCopies(int copiesToReturn) {
            copiesAvailable += copiesToReturn;
            copiesBorrowed -= copiesToReturn;
            // Implement borrower details update
        }

        public BorrowInfo getBorrowInfo(String borrower) {
            return borrowers.get(borrower);
        }

        public int getCopiesAvailable() {
            return copiesAvailable;
        }

        public int getCopiesBorrowed() {
            return copiesBorrowed;
        }
    }

    private class BorrowInfo {
        private long borrowDate;
        private long dueDate;
        private boolean returned;

        // Constructor, getters, and setters for BorrowInfo
    }
}
