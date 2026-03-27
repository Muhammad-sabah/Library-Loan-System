package com.mycompany.database;

import javax.swing.JOptionPane;
import java.sql.*;
import java.util.Vector;

// --- DatabaseManager Class ---
class DatabaseManager {
    // Database credentials for your 'library' database on localhost
    private static final String URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root"; // Common default user for XAMPP/WAMP
    private static final String PASSWORD = ""; // Common default password for XAMPP/WAMP

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Loads all data from a specified table.
     */
    public Vector<Vector<Object>> loadData(String tableName, String[] columnNames) {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT * FROM " + tableName + " ORDER BY 1"; 

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int columnCount = columnNames.length;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 0; i < columnCount; i++) {
                    row.add(rs.getObject(columnNames[i]));
                }
                data.add(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return data;
    }
    
    // --- Method to search books by title ---
    public Vector<Vector<Object>> searchBooksByTitle(String title, String[] columnNames) {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT * FROM book WHERE Title LIKE ? ORDER BY Title";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + title + "%"); // Wildcards for partial match
            
            try (ResultSet rs = pstmt.executeQuery()) {
                int columnCount = columnNames.length;
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 0; i < columnCount; i++) {
                        row.add(rs.getObject(columnNames[i]));
                    }
                    data.add(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error during Book Search: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return data;
    }

    // --- Method to generate the combined Loan Report ---
    public Vector<Vector<Object>> getLoanReportData() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = """
            SELECT 
                m.MemberName, 
                b.Title AS BookTitle, 
                l.DueDate 
            FROM loan l
            JOIN member m ON l.MemberID = m.MemberID
            JOIN book b ON l.BookID = b.BookID
            ORDER BY l.DueDate
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MemberName"));
                row.add(rs.getString("BookTitle"));
                row.add(rs.getString("DueDate"));
                data.add(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error during Report Generation: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return data;
    }


    // --- CRUD Operations ---
    public boolean addRecord(int tabIndex, String[] values) throws SQLException {
        String sql = "";
        switch (tabIndex) {
            case 0 -> sql = "INSERT INTO member (MemberID, MemberName, Email) VALUES (?, ?, ?)";
            case 1 -> sql = "INSERT INTO book (BookID, ISBN, Title, Author) VALUES (?, ?, ?, ?)";
            case 2 -> sql = "INSERT INTO loan (LoanID, LoanDate, DueDate, MemberID, BookID) VALUES (?, ?, ?, ?, ?)";
        }
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, values[i]);
            }
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw e; 
        }
    }

    public boolean deleteRecord(int tabIndex, String idValue) throws SQLException {
        String sql = "";
        String idColumn = "";
        switch (tabIndex) {
            case 0 -> { idColumn = "MemberID"; sql = "DELETE FROM member WHERE " + idColumn + " = ?"; }
            case 1 -> { idColumn = "BookID"; sql = "DELETE FROM book WHERE " + idColumn + " = ?"; }
            case 2 -> { idColumn = "LoanID"; sql = "DELETE FROM loan WHERE " + idColumn + " = ?"; }
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idValue); 
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw e;
        }
    }
    
    public boolean modifyRecord(int tabIndex, String[] values) throws SQLException {
        String sql = "";
        String[] currentColumns = switch (tabIndex) {
            case 0 -> new String[]{"MemberID", "MemberName", "Email"};
            case 1 -> new String[]{"BookID", "ISBN", "Title", "Author"};
            case 2 -> new String[]{"LoanID", "LoanDate", "DueDate", "MemberID", "BookID"};
            default -> new String[]{};
        };

        if (currentColumns.length == 0) return false;

        StringBuilder setClauses = new StringBuilder();
        for (int i = 1; i < currentColumns.length; i++) {
            setClauses.append(currentColumns[i]).append(" = ?, ");
        }
        setClauses.setLength(setClauses.length() - 2); 

        sql = "UPDATE " + (tabIndex == 0 ? "member" : tabIndex == 1 ? "book" : "loan")
             + " SET " + setClauses.toString() 
             + " WHERE " + currentColumns[0] + " = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 1; i < values.length; i++) {
                pstmt.setString(i, values[i]);
            }
            pstmt.setString(values.length, values[0]); 
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw e;
        }
    }
}