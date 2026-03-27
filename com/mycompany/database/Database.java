package com.mycompany.database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;
import java.sql.SQLException;

// --- Modified Database Class ---
public class Database extends JFrame {
    private JLabel[] labels;
    private JTextField[] textFields;

    private JButton btnAdd, btnDelete, btnModify, btnLoanReport; 
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnMembersTab, btnBooksTab, btnLoansTab;
    
    // --- NEW UI Components for Book Search ---
    private JTextField txtSearchBook;
    private JButton btnSearchBook;
    private JLabel lblSearchBook;
    // --- End NEW UI Components ---

    private final DatabaseManager dbManager; 
    private final String[][] inputLabels = {
        {"Member ID:", "Name:", "Email:"},          
        {"Book ID:", "ISBN:", "Title:", "Author:"},     
        {"Loan ID:", "Loan Date:", "Due Date:", "Member ID:", "Book ID:"} 
    };
    private final String[][] columnNames = {
        {"MemberID", "MemberName", "Email"},
        {"BookID", "ISBN", "Title", "Author"},
        {"LoanID", "LoanDate", "DueDate", "MemberID", "BookID"}
    };
    private final String[] reportColumns = {"Member Name", "Book Title", "Due Date"};
    private int currentTab = 0;

    public Database() {
        // Initialize the manager (which handles DB connections)
        dbManager = new DatabaseManager(); 
        
        // --- Frame Settings (Increased Size and Improved Design) ---
        setTitle("Library Loan System - Data Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 650); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); 
        
        // --- Top Panel (Input, Buttons, Search) ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null); 
        topPanel.setPreferredSize(new Dimension(800, 250));
        topPanel.setBackground(new Color(255, 255, 255)); 
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 

        // --- Label and Text Field Initialization ---
        labels = new JLabel[5];
        textFields = new JTextField[5];
        for (int i = 0; i < 5; i++) {
            labels[i] = new JLabel();
            labels[i].setBounds(20, 20 + i * 40, 100, 25); 
            labels[i].setFont(new Font("Segoe UI", Font.BOLD, 12));
            labels[i].setVisible(false);
            topPanel.add(labels[i]);

            textFields[i] = new JTextField();
            textFields[i].setBounds(130, 20 + i * 40, 260, 25); 
            textFields[i].setVisible(false);
            topPanel.add(textFields[i]);
        }
        
        // --- CRUD Buttons (Positioning Adjusted) ---
        int btnWidth = 160;
        int btnHeight = 35;
        int btnX = 420;

        btnAdd = createStyledButton("Add Member", new Color(66, 103, 178), e -> handleAdd());
        btnAdd.setBounds(btnX, 20, btnWidth, btnHeight);
        topPanel.add(btnAdd);

        btnDelete = createStyledButton("Delete Member", new Color(219, 68, 55), e -> handleDelete()); 
        btnDelete.setBounds(btnX, 60, btnWidth, btnHeight);
        topPanel.add(btnDelete);

        btnModify = createStyledButton("Modify Member", new Color(244, 180, 0), e -> handleModify()); 
        btnModify.setBounds(btnX, 100, btnWidth, btnHeight);
        topPanel.add(btnModify);
        
        // **Loan Report button**
        btnLoanReport = createStyledButton("Generate Loan Report", new Color(34, 139, 34), e -> handleLoanReport()); 
        btnLoanReport.setBounds(btnX, 140, btnWidth, btnHeight);
        topPanel.add(btnLoanReport);
        
        // --- NEW: Book Search Components (Hidden by default) ---
        int searchX = 600;
        int searchY = 20;
        
        lblSearchBook = new JLabel("Search Book Title:");
        lblSearchBook.setBounds(searchX, searchY, 150, 25);
        lblSearchBook.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSearchBook.setVisible(false);
        topPanel.add(lblSearchBook);
        
        txtSearchBook = new JTextField();
        txtSearchBook.setBounds(searchX, searchY + 30, 150, 25);
        txtSearchBook.setVisible(false);
        topPanel.add(txtSearchBook);

        btnSearchBook = createStyledButton("Search", new Color(66, 103, 178), e -> handleBookSearch());
        btnSearchBook.setBounds(searchX, searchY + 65, 150, 30);
        btnSearchBook.setVisible(false);
        topPanel.add(btnSearchBook);
        // --- End Search Components ---

        add(topPanel, BorderLayout.NORTH);

        // --- Table Panel ---
        tableModel = new DefaultTableModel(columnNames[0], 0); 
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(20);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Database Records"));
        
        add(tableScroll, BorderLayout.CENTER); 
        
        // --- Bottom Panel (Tab Buttons) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 242, 245)); 
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15)); 

        btnMembersTab = createTabButton("Members", 0);
        btnBooksTab = createTabButton("Books", 1);
        btnLoansTab = createTabButton("Loans", 2);

        bottomPanel.add(btnMembersTab);
        bottomPanel.add(btnBooksTab);
        bottomPanel.add(btnLoansTab);
        
        add(bottomPanel, BorderLayout.SOUTH);

        // Setup initial tab (Members)
        selectTab(0);
    }
    
    // Helper method for styling CRUD and Report buttons
    private JButton createStyledButton(String text, Color background, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }
    
    // Helper method for tab buttons
    private JButton createTabButton(String text, int index) {
        JButton button = createStyledButton(text, new Color(150, 150, 150), e -> selectTab(index));
        button.setPreferredSize(new Dimension(200, 45));
        return button;
    }

    /**
     * Handles the Loan Report button click: Fetches combined member/book/loan data and displays it.
     */
    private void handleLoanReport() {
        Vector<Vector<Object>> reportData = dbManager.getLoanReportData();
        
        Vector<String> reportColsVector = new Vector<>();
        for (String col : reportColumns) {
            reportColsVector.add(col);
        }

        tableModel.setDataVector(reportData, reportColsVector);
        
        JOptionPane.showMessageDialog(this, 
            "Loan Report generated successfully. Showing " + reportData.size() + " active loans.", 
            "Report Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // --- NEW: Book Search Handler ---
    private void handleBookSearch() {
        String searchTerm = txtSearchBook.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title or partial title to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vector<Vector<Object>> searchResults = dbManager.searchBooksByTitle(searchTerm, columnNames[1]);
        
        Vector<String> colsVector = new Vector<>();
        for (String col : columnNames[1]) {
            colsVector.add(col);
        }
        
        tableModel.setDataVector(searchResults, colsVector);
        
        JOptionPane.showMessageDialog(this, 
            searchResults.size() + " books found matching '" + searchTerm + "'.", 
            "Search Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    // --- End Book Search Handler ---
    

    // --- CRUD Handlers ---
    private void handleAdd() {
        String[] values = getTextFieldValues();
        if (values.length == 0 || values[0].isEmpty()) {
            JOptionPane.showMessageDialog(this, "The ID field cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            if (dbManager.addRecord(currentTab, values)) {
                JOptionPane.showMessageDialog(this, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                selectTab(currentTab); 
                clearTextFields();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void handleDelete() {
        String idToDelete = textFields[0].getText().trim();
        if (idToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the ID of the record to delete.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete ID: " + idToDelete + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dbManager.deleteRecord(currentTab, idToDelete)) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    selectTab(currentTab); 
                    clearTextFields();
                } else {
                    JOptionPane.showMessageDialog(this, "No record found with ID: " + idToDelete, "Not Found", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void handleModify() {
        String[] values = getTextFieldValues();
        if (values.length == 0 || values[0].isEmpty()) {
            JOptionPane.showMessageDialog(this, "The ID field (first field) is required for modification.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            if (dbManager.modifyRecord(currentTab, values)) {
                JOptionPane.showMessageDialog(this, "Record modified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                selectTab(currentTab); 
                clearTextFields();
            } else {
                JOptionPane.showMessageDialog(this, "No record found to modify with ID: " + values[0], "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error modifying record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String[] getTextFieldValues() {
        String[] currentLabels = inputLabels[currentTab];
        String[] values = new String[currentLabels.length];
        for (int i = 0; i < currentLabels.length; i++) {
            values[i] = textFields[i].getText().trim();
        }
        return values;
    }
    private void clearTextFields() {
        for (JTextField tf : textFields) {
            tf.setText("");
        }
    }


    /**
     * Method to select and update UI based on tab index.
     */
    private void selectTab(int tabIndex) {
        currentTab = tabIndex;

        // --- 1. Update Input Fields Visibility ---
        int currentFieldsCount = inputLabels[tabIndex].length;
        for (int i = 0; i < 5; i++) {
            boolean visible = (i < currentFieldsCount);
            labels[i].setVisible(visible);
            textFields[i].setVisible(visible);
            textFields[i].setText("");
            if (visible) {
                labels[i].setText(inputLabels[tabIndex][i]);
            }
        }
        
        // --- 2. Update CRUD Button Text ---
        String entity = switch (tabIndex) {
            case 0 -> "Member";
            case 1 -> "Book";
            case 2 -> "Loan";
            default -> "";
        };
        btnAdd.setText("Add " + entity);
        btnDelete.setText("Delete " + entity);
        btnModify.setText("Modify " + entity);
        
        // --- 3. Toggle Book Search Components ---
        boolean isBookTab = (tabIndex == 1);
        lblSearchBook.setVisible(isBookTab);
        txtSearchBook.setVisible(isBookTab);
        btnSearchBook.setVisible(isBookTab);
        txtSearchBook.setText(""); // Clear search field on tab switch

        // --- 4. Load Data and Update Table Model ---
        String tableName = entity.toLowerCase(); 
        Vector<Vector<Object>> dbData = dbManager.loadData(tableName, columnNames[tabIndex]);
        
        Vector<String> dbColumns = new Vector<>();
        for (String col : columnNames[tabIndex]) {
            dbColumns.add(col);
        }
        tableModel.setDataVector(dbData, dbColumns);
        
        // --- 5. Update tab button colors for active tab ---
        btnMembersTab.setBackground(tabIndex == 0 ? new Color(66, 103, 178) : new Color(150, 150, 150));
        btnBooksTab.setBackground(tabIndex == 1 ? new Color(66, 103, 178) : new Color(150, 150, 150));
        btnLoansTab.setBackground(tabIndex == 2 ? new Color(66, 103, 178) : new Color(150, 150, 150));
    }

    public static void main(String[] args) {
        // The application starts from WelcomeScreen.main()
    }
}