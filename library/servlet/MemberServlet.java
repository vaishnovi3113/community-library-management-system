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

@WebServlet("/members")
public class MemberServlet extends HttpServlet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Member member = fetchMemberById(id);
            req.setAttribute("memberToEdit", member);
        } else {
            req.removeAttribute("memberToEdit");
        }
        List<Member> members = fetchMembersFromDatabase();
        req.setAttribute("members", members);
        req.getRequestDispatcher("/members.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("insert".equals(action)) {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            Member newMember = new Member(0, name, email, phone); // id=0 as it's auto-incremented
            insertMemberIntoDatabase(newMember);
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            Member member = new Member(id, name, email, phone);
            updateMemberInDatabase(member);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            deleteMemberFromDatabase(id);
        } 

        // Redirect to GET to show updated list
        resp.sendRedirect("members");
    }

    private void insertMemberIntoDatabase(Member member) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, member.getName());
                pstmt.setString(2, member.getEmail());
                pstmt.setString(3, member.getPhone());
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMemberInDatabase(Member member) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "UPDATE members SET name=?, email=?, phone=? WHERE id=?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, member.getName());
                pstmt.setString(2, member.getEmail());
                pstmt.setString(3, member.getPhone());
                pstmt.setInt(4, member.getId());
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMemberFromDatabase(int id) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "DELETE FROM members WHERE id=?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Member> fetchMembersFromDatabase() {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";
        List<Member> memberList = new ArrayList<>();

        String sql = "SELECT id, name, email, phone FROM members";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    memberList.add(new Member(id, name, email, phone));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return memberList;
    }

    private Member fetchMemberById(int id) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        Member member = null;
        String sql = "SELECT id, name, email, phone FROM members WHERE id=?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        String phone = rs.getString("phone");
                        member = new Member(id, name, email, phone);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return member;
    }

    // Inner class for Member
    public static class Member {
        private int id;
        private String name;
        private String email;
        private String phone;

        public Member(int id, String name, String email, String phone) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
    }
}
