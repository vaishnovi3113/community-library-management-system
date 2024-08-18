package com.community.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/library";
    private static final String USER = "postgres";
    private static final String PASSWORD = "3113";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // Manually load the driver
            System.out.println("Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("PostgreSQL Driver not found.");
        }

        System.out.println("Attempting to connect to: " + URL);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
