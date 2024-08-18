package com.community.library.dao;

import com.community.library.model.Member;
import com.community.library.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public void addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (name, email) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.executeUpdate();
        }
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Member member = new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                members.add(member);
            }
        }
        return members;
    }

    public void deleteMember(int memberId) throws SQLException {
        String sql = "DELETE FROM members WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.executeUpdate();
        }
    }
}