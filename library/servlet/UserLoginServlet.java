package com.community.library.servlet; // Ensure the package matches your directory structure

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class UserLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            boolean isValidUser = validateUser(username, password);
            if (isValidUser) {
                response.sendRedirect("home.html");
            } else {
                request.setAttribute("errorMessage", "Invalid username or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private boolean validateUser(String username, String password) throws SQLException, ClassNotFoundException {
        String url = "jdbc:postgresql://localhost:5432/library";
        String dbUser = "postgres";
        String dbPassword = "3113";
        String sql = "SELECT password FROM users WHERE username = ?";

        // Load PostgreSQL JDBC driver
        Class.forName("org.postgresql.Driver");

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return password.equals(storedPassword); // Plain text comparison
                }
            }
        }
        return false; // Username not found
    }
}
