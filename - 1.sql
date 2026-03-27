- 1. Create the database if it doesn't already exist
CREATE DATABASE IF NOT EXISTS library;
USE library;

-- 2. Drop existing tables to ensure a clean setup 
-- Note: 'loan' is dropped first because it depends on 'member' and 'book'
DROP TABLE IF EXISTS loan;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS member;

-- 3. Create 'member' table to store user information
CREATE TABLE member (
    MemberID INT PRIMARY KEY,
    MemberName VARCHAR(255) NOT NULL,
    Email VARCHAR(255)
) ENGINE=InnoDB;

-- 4. Create 'book' table to store library resources
CREATE TABLE book (
    BookID INT PRIMARY KEY,
    ISBN VARCHAR(50) NOT NULL,
    Title VARCHAR(255) NOT NULL,
    Author VARCHAR(255)
) ENGINE=InnoDB;

-- 5. Create 'loan' table to manage book borrowing
-- Includes Foreign Keys for relational integrity between tables
CREATE TABLE loan (
    LoanID INT PRIMARY KEY,
    LoanDate DATE NOT NULL,
    DueDate DATE NOT NULL,
    MemberID INT,
    BookID INT,
    FOREIGN KEY (MemberID) REFERENCES member(MemberID) ON DELETE CASCADE,
    FOREIGN KEY (BookID) REFERENCES book(BookID) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6. Insert sample records into 'member' table
INSERT INTO member (MemberID, MemberName, Email) VALUES 
(1, 'Mohamed Sabah', 'mohamed.sabah@email.com'),
(2, 'Mohamed Sherzad', 'sherzad.m@email.com'),
(3, 'Anas Khalid', 'anas.k@email.com'),
(4, 'Lass Anjam', 'lass.a@email.com'),
(5, 'Ahmed Ali', 'ahmed.ali@email.com');

-- 7. Insert sample records into 'book' table
INSERT INTO book (BookID, ISBN, Title, Author) VALUES 
(101, '978-01', 'Java Programming', 'James Gosling'),
(102, '978-02', 'Database Systems', 'Abraham Silberschatz'),
(103, '978-03', 'Linux Basics', 'Linus Torvalds'),
(104, '978-04', 'Software Engineering', 'Ian Sommerville'),
(105, '978-05', '12 Angry Men Script', 'Reginald Rose');

-- 8. Insert sample borrowing records into 'loan' table
INSERT INTO loan (LoanID, LoanDate, DueDate, MemberID, BookID) VALUES 
(501, '2026-03-01', '2026-03-15', 1, 101),
(502, '2026-03-05', '2026-03-20', 2, 102),
(503, '2026-03-10', '2026-03-25', 3, 103),
(504, '2026-03-12', '2026-03-27', 4, 104),
(505, '2026-03-15', '2026-03-30', 5, 105);