// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract SimpleLibrary {
    // Struct to represent a book
    struct Book {
        string title;
        string author;
        bool isAvailable;
        address checkedOutBy; // Track the user who checked out the book
    }

    // Array of books
    Book[] public books;

    // Add a new book to the library
    function addBook(string memory _title, string memory _author) public {
        books.push(Book(_title, _author, true, address(0)));
    }

    // Check out a book from the library
    function checkOutBook(uint _bookId) public {
        require(_bookId < books.length, "Book does not exist.");
        require(books[_bookId].isAvailable, "Book is not available.");

        books[_bookId].isAvailable = false;
        books[_bookId].checkedOutBy = msg.sender;
    }

    // Return a book to the library
    function returnBook(uint _bookId) public {
        require(_bookId < books.length, "Book does not exist.");
        require(books[_bookId].checkedOutBy == msg.sender, "You do not have this book checked out.");

        books[_bookId].isAvailable = true;
        books[_bookId].checkedOutBy = address(0);
    }

    // Get details of a book
    function getBook(uint _bookId) public view returns (string memory title, string memory author, bool isAvailable, address checkedOutBy) {
        require(_bookId < books.length, "Book does not exist.");

        Book storage book = books[_bookId];
        return (book.title, book.author, book.isAvailable, book.checkedOutBy);
    }

    // Get the total number of books
    function getBookCount() public view returns (uint) {
        return books.length;
    }
}
