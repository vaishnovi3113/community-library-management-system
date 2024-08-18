package com.community.library.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/loans")
public class LoanServlet extends HttpServlet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Loan> loans = fetchLoansFromDatabase();
        req.setAttribute("loans", loans);
        req.getRequestDispatcher("/loans.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "add":
                addLoan(req, resp);
                break;
            case "delete":
                deleteLoan(req, resp);
                break;
            case "update":
                updateLoan(req, resp);
                break;
            default:
                resp.sendRedirect("loans");
                break;
        }
    }

    private void addLoan(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String memberId = req.getParameter("memberId");
        String bookIdStr = req.getParameter("bookId");
        String loanDateStr = req.getParameter("loanDate");
        String returnDateStr = req.getParameter("returnDate");

        Date loanDate = null;
        Date returnDate = null;
        Integer bookId = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilLoanDate = sdf.parse(loanDateStr);
            java.util.Date utilReturnDate = sdf.parse(returnDateStr);
            loanDate = new Date(utilLoanDate.getTime());
            returnDate = new Date(utilReturnDate.getTime());
            bookId = Integer.parseInt(bookIdStr); // Convert bookId to Integer
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
        }

        Loan newLoan = new Loan(memberId, bookId, loanDate, returnDate);
        insertLoanIntoDatabase(newLoan);

        resp.sendRedirect("loans");
    }

    private void deleteLoan(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String memberId = req.getParameter("memberId");
        Integer bookId = Integer.parseInt(req.getParameter("bookId"));
        deleteLoanFromDatabase(memberId, bookId);
        resp.sendRedirect("loans");
    }

    private void updateLoan(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String oldMemberId = req.getParameter("oldMemberId");
        Integer oldBookId = Integer.parseInt(req.getParameter("oldBookId"));
        String memberId = req.getParameter("memberId");
        Integer bookId = Integer.parseInt(req.getParameter("bookId"));
        String loanDateStr = req.getParameter("loanDate");
        String returnDateStr = req.getParameter("returnDate");

        Date loanDate = null;
        Date returnDate = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilLoanDate = sdf.parse(loanDateStr);
            java.util.Date utilReturnDate = sdf.parse(returnDateStr);
            loanDate = new Date(utilLoanDate.getTime());
            returnDate = new Date(utilReturnDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Loan updatedLoan = new Loan(memberId, bookId, loanDate, returnDate);
        updateLoanInDatabase(oldMemberId, oldBookId, updatedLoan);

        resp.sendRedirect("loans");
    }

    private void insertLoanIntoDatabase(Loan loan) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "INSERT INTO loans (member_id, book_id, loan_date, return_date) VALUES (?, ?, ?, ?)";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(loan.getMemberId()));
                pstmt.setInt(2, loan.getBookId());
                pstmt.setDate(3, loan.getLoanDate());
                pstmt.setDate(4, loan.getReturnDate());
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteLoanFromDatabase(String memberId, Integer bookId) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "DELETE FROM loans WHERE member_id = ? AND book_id = ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(memberId));
                pstmt.setInt(2, bookId);
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateLoanInDatabase(String oldMemberId, Integer oldBookId, Loan updatedLoan) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "UPDATE loans SET member_id = ?, book_id = ?, loan_date = ?, return_date = ? WHERE member_id = ? AND book_id = ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(updatedLoan.getMemberId()));
                pstmt.setInt(2, updatedLoan.getBookId());
                pstmt.setDate(3, updatedLoan.getLoanDate());
                pstmt.setDate(4, updatedLoan.getReturnDate());
                pstmt.setInt(5, Integer.parseInt(oldMemberId));
                pstmt.setInt(6, oldBookId);
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Loan> fetchLoansFromDatabase() {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";
        List<Loan> loans = new ArrayList<>();

        String sql = "SELECT member_id, book_id, loan_date, return_date FROM loans";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String memberId = rs.getString("member_id");
                    Integer bookId = rs.getInt("book_id");
                    Date loanDate = rs.getDate("loan_date");
                    Date returnDate = rs.getDate("return_date");
                    loans.add(new Loan(memberId, bookId, loanDate, returnDate));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return loans;
    }

    public static class Loan {
        private String memberId;
        private Integer bookId;
        private Date loanDate;
        private Date returnDate;

        public Loan(String memberId, Integer bookId, Date loanDate, Date returnDate) {
            this.memberId = memberId;
            this.bookId = bookId;
            this.loanDate = loanDate;
            this.returnDate = returnDate;
        }

        public String getMemberId() {
            return memberId;
        }

        public Integer getBookId() {
            return bookId;
        }

        public Date getLoanDate() {
            return loanDate;
        }

        public Date getReturnDate() {
            return returnDate;
        }
    }
}
