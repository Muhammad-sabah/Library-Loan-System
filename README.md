# Library Loan System 📚

Developed using **Java & Swing**, this robust application features a dynamic GUI for managing library operations. It is fully integrated with a **MySQL database** via **JDBC**, supporting CRUD operations for members, books, and loans. Designed with a focus on data integrity, it ensures seamless tracking of library resources.

## 📂 Project Structure

* **WelcomeScreen.java**: The entry point of the application, featuring a professional layout and MySQL driver verification.
* **DatabaseManager.java**: The core backend component managing JDBC connectivity and CRUD operations.
* **Database.java**: The primary GUI dashboard with dynamic JTables, search functionality, and loan reporting.
* **1.sql**: Database schema script including sample data for 5 members, books, and active loans.

## 🛠️ Requirements

* Java JDK 21 or higher.
* MySQL Server (XAMPP recommended).
* MySQL Connector/J (JDBC Driver).

## 🚀 Setup Instructions

1.  **Database**: Open phpMyAdmin, create a database named `library`, and import the `1.sql` file.
2.  **IDE**: Open the project in NetBeans or VS Code.
3.  **Drivers**: Ensure `mysql-connector-java.jar` is added to your project's libraries.
4.  **Run**: Execute `WelcomeScreen.java` to start the application.
