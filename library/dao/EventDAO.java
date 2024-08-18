package com.community.library.dao;

import com.community.library.model.Event;
import com.community.library.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public void addEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (name, date, location) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.getName());
            pstmt.setDate(2, new java.sql.Date(event.getDate().getTime()));
            pstmt.setString(3, event.getLocation());
            pstmt.executeUpdate();
        }
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("date"),
                        rs.getString("location")
                );
                events.add(event);
            }
        }
        return events;
    }

    public void deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM events WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.executeUpdate();
        }
    }
}