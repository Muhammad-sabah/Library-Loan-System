package com.mycompany.database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class jdbc {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/exampledatabase";
        String username = "root";
        String password = "";

        try {
            // 1. إصلاح Connection (C كبيرة) + إصلاح الوسائط (username بدل url)
            Connection con = DriverManager.getConnection(url, username, password);

            // 2. SQL statement
            String sql = "INSERT INTO USERS(username, password) VALUES (?, ?)";

            // 3. إصلاح prepareStatement (حرف صغير p)
            PreparedStatement statement = con.prepareStatement(sql);

            // 4. الفهرسة تبدأ من 1 وليس 0
            statement.setString(1, "mohamed");
            statement.setString(2, "12345");

            // 5. تنفيذ الأمر
            int inserted = statement.executeUpdate();

            System.out.println("Inserted rows: " + inserted);

        } catch (SQLException ex) {   // إصلاح SQLEException
            ex.printStackTrace();
        }
    }
}
