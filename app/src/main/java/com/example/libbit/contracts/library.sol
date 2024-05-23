// SPDX-License-Identifier: MIT
pragma solidity ^0.8.16;

contract LibrarySystem {
    address public admin;

    struct Book {
        string title;
        string author;
        uint256 copiesAvailable;
        uint256 copiesBorrowed;
        mapping(address => BorrowInfo) borrowers;
    }

    struct BorrowInfo {
        uint256 borrowDate;
        uint256 dueDate;
        bool returned;
    }

    mapping(string => Book) public books;
    uint256 public totalBooks;
    string public pdfHash;

    modifier onlyAdmin() {
        require(msg.sender == admin, "Only admin can call this function");
        _;
    }

    modifier bookAvailable(string memory _bookId) {
    require(bytes(books[_bookId].title).length != 0, "Book not found");
    _;
}

    modifier enoughCopiesB(uint256 _copiesToBorrow, string memory _bookId) {
        require(_copiesToBorrow > 0 && _copiesToBorrow <= books[_bookId].copiesAvailable, "Invalid number of copies to borrow");
        _;
    }

    modifier enoughCopiesR(uint256 _copiesToReturn, string memory _bookId) {
        require(_copiesToReturn > 0 && _copiesToReturn <= books[_bookId].copiesBorrowed, "Invalid number of copies to return");
        _;
    }

    modifier hasBorrowedBook(string memory _bookId, uint256 _copiesToReturn) {
    require(books[_bookId].borrowers[msg.sender].borrowDate > 0, "You have not borrowed this book");
    require(_copiesToReturn <= books[_bookId].copiesBorrowed, "You have not borrowed enough copies of this book");
    _;
}


    event BookAdded(string indexed bookId, string title, string author, uint256 copiesAvailable);
    event BookRemoved(string indexed bookId);
    event BookBorrowed(string indexed bookId, uint256 copiesBorrowed, uint256 borrowDate, uint256 dueDate);
    event BookReturned(string indexed bookId, uint256 copiesReturned, uint256 lateFee);

    constructor() {
        admin = msg.sender;
    }

    function addBook(string memory _bookId, string memory _title, string memory _author, uint256 _copiesAvailable) public onlyAdmin {
        require(bytes(_bookId).length > 0, "Book ID cannot be empty");
        require(bytes(books[_bookId].title).length == 0, "Book with specified ID already exists");

        Book storage newBook = books[_bookId];
        newBook.title = _title;
        newBook.author = _author;
        newBook.copiesAvailable = _copiesAvailable;
        newBook.copiesBorrowed = 0;

        totalBooks++;

        emit BookAdded(_bookId, _title, _author, _copiesAvailable);
    }

    function removeBook(string memory _bookId) public onlyAdmin bookAvailable(_bookId) {
        delete books[_bookId];

        totalBooks--;

        emit BookRemoved(_bookId);
    }

    function borrowBook(string memory _bookId, uint256 _copiesToBorrow, uint256 _dueDate) public bookAvailable(_bookId) enoughCopiesB(_copiesToBorrow, _bookId) {
        Book storage book = books[_bookId];
        address borrower = msg.sender;

        for (uint256 i = 0; i < _copiesToBorrow; i++) {
            book.borrowers[borrower] = BorrowInfo(block.timestamp, _dueDate * 86400 + block.timestamp, false);
        }

        book.copiesAvailable -= _copiesToBorrow;
        book.copiesBorrowed += _copiesToBorrow;

        emit BookBorrowed(_bookId, _copiesToBorrow, block.timestamp, _dueDate);
    }

    function returnBook(string memory _bookId, uint256 _copiesToReturn) public bookAvailable(_bookId) enoughCopiesR(_copiesToReturn, _bookId) hasBorrowedBook(_bookId, _copiesToReturn){
        Book storage book = books[_bookId];
        address borrower = msg.sender;

        uint256 lateFee = 0;

        for (uint256 i = 0; i < _copiesToReturn; i++) {
            BorrowInfo storage info = book.borrowers[borrower];
            if (!info.returned && block.timestamp > info.dueDate) {
                // Calculate late fee
                lateFee += (block.timestamp - info.dueDate) / 1 days * 0.001 ether;
                info.returned = true;
            }
        }

        book.copiesAvailable += _copiesToReturn;
        book.copiesBorrowed -= _copiesToReturn;

        // Check if there's a late fee to be paid
        if (lateFee > 0) {
            // Transfer late fee to admin
            payable(admin).transfer(lateFee);
        }

        emit BookReturned(_bookId, _copiesToReturn, lateFee);
    }

    function getBorrowInfo(string memory _bookId, address _borrower) public view returns (uint256 borrowDate, uint256 dueDate, bool returned) {
        BorrowInfo storage info = books[_bookId].borrowers[_borrower];
        return (info.borrowDate, info.dueDate, info.returned);
    }

    function checkAvailability(string memory _bookId) public view bookAvailable(_bookId) returns (uint256) {
        return books[_bookId].copiesAvailable;
    }

    function storePDFHash(string memory _ipfsHash) public onlyAdmin{
        pdfHash = _ipfsHash;
    }
}
