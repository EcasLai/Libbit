package com.example.libbit.contracts;

import org.web3j.abi.datatypes.DynamicStruct;

public class Book extends DynamicStruct {
    private String title;
    private String author;
    private boolean isCheckedOut;
    private String borrower;

    // Constructor
    public Book(String title, String author, boolean isCheckedOut, String borrower) {
        this.title = title;
        this.author = author;
        this.isCheckedOut = isCheckedOut;
        this.borrower = borrower;
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

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        isCheckedOut = checkedOut;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    // toString method
    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isCheckedOut=" + isCheckedOut +
                ", borrower='" + borrower + '\'' +
                '}';
    }
}
