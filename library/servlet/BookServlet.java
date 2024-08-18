package com.community.library.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/books")
public class BookServlet extends HttpServlet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchTerm = req.getParameter("searchTerm");
        List<Book> books;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            books = searchBooksInDatabase(searchTerm);
        } else {
            books = fetchBooksFromDatabase();
        }

        req.setAttribute("books", books);
        req.getRequestDispatcher("/books.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            String title = req.getParameter("title");
            String author = req.getParameter("author");
            String isbn = req.getParameter("isbn");
            String genre = req.getParameter("genre");

            Book newBook = new Book(title, author, isbn, genre);
            insertBookIntoDatabase(newBook);
        } else if ("delete".equals(action)) {
            String isbn = req.getParameter("isbn");
            deleteBookFromDatabase(isbn);
        } else if ("update".equals(action)) {
            String oldIsbn = req.getParameter("oldIsbn");
            String title = req.getParameter("title");
            String author = req.getParameter("author");
            String isbn = req.getParameter("isbn");
            String genre = req.getParameter("genre");

            Book updatedBook = new Book(title, author, isbn, genre);
            updateBookInDatabase(oldIsbn, updatedBook);
        }

        resp.sendRedirect("books");
    }

    private List<Book> fetchBooksFromDatabase() {
        List<Book> books = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";
        String sql = "SELECT title, author, isbn, genre FROM books";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String isbn = rs.getString("isbn");
                    String genre = rs.getString("genre");
                    books.add(new Book(title, author, isbn, genre));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private List<Book> searchBooksInDatabase(String searchTerm) {
        List<Book> books = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";
        String sql = "SELECT title, author, isbn, genre FROM books WHERE title LIKE ? OR author LIKE ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + searchTerm + "%");
                pstmt.setString(2, "%" + searchTerm + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String title = rs.getString("title");
                        String author = rs.getString("author");
                        String isbn = rs.getString("isbn");
                        String genre = rs.getString("genre");
                        books.add(new Book(title, author, isbn, genre));
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private void insertBookIntoDatabase(Book book) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "INSERT INTO books (title, author, isbn, genre) VALUES (?, ?, ?, ?)";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());
                pstmt.setString(3, book.getIsbn());
                pstmt.setString(4, book.getGenre());
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteBookFromDatabase(String isbn) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "DELETE FROM books WHERE isbn = ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, isbn);
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBookInDatabase(String oldIsbn, Book book) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, genre = ? WHERE isbn = ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());
                pstmt.setString(3, book.getIsbn());
                pstmt.setString(4, book.getGenre());
                pstmt.setString(5, oldIsbn);
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Book {
        private String title;
        private String author;
        private String isbn;
        private String genre;

        public Book(String title, String author, String isbn, String genre) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.genre = genre;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getIsbn() {
            return isbn;
        }

        public String getGenre() {
            return genre;
        }
    }
}
