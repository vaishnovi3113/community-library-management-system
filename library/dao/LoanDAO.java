package com.community.library.dao;

import com.community.library.model.Loan;
import com.community.library.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public void addLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO loans (book_id, member_id, loan_date, return_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loan.getBookId());
            pstmt.setInt(2, loan.getMemberId());
            pstmt.setDate(3, new java.sql.Date(loan.getLoanDate().getTime()));
            pstmt.setDate(4, new java.sql.Date(loan.getReturnDate().getTime()));
            pstmt.executeUpdate();
        }
    }

    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Loan loan = new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("loan_date"),
                        rs.getDate("return_date")
                );
                loans.add(loan);
            }
        }
        return loans;
    }
}