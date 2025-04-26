package com.kasir.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/kasir_db"; // ganti sesuai DB kamu
    private static final String USER = "root"; // username db kamu
    private static final String PASSWORD = ""; // password db kamu

    // Singleton: hanya 1 koneksi aktif
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Koneksi database ditutup.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menutup koneksi: " + e.getMessage());
        }
    }
}
