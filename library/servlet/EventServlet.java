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

@WebServlet("/events")
public class EventServlet extends HttpServlet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Event> events = fetchEventsFromDatabase();
        req.setAttribute("events", events);
        req.getRequestDispatcher("/events.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        switch (action) {
            case "add":
                addEvent(req, resp);
                break;
            case "delete":
                deleteEvent(req, resp);
                break;
            case "update":
                updateEvent(req, resp);
                break;
            default:
                resp.sendRedirect("events");
                break;
        }
    }

    private void addEvent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String eventName = req.getParameter("eventName");
        String eventDateStr = req.getParameter("eventDate");
        String eventLocation = req.getParameter("eventLocation");

        Date eventDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(eventDateStr);
            eventDate = new Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Event newEvent = new Event(eventName, eventDate, eventLocation);
        insertEventIntoDatabase(newEvent);

        resp.sendRedirect("events");
    }

    private void deleteEvent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String eventName = req.getParameter("eventName");
        deleteEventFromDatabase(eventName);
        resp.sendRedirect("events");
    }

    private void updateEvent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String oldEventName = req.getParameter("oldEventName");
        String eventName = req.getParameter("eventName");
        String eventDateStr = req.getParameter("eventDate");
        String eventLocation = req.getParameter("eventLocation");

        Date eventDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(eventDateStr);
            eventDate = new Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Event updatedEvent = new Event(eventName, eventDate, eventLocation);
        updateEventInDatabase(oldEventName, updatedEvent);

        resp.sendRedirect("events");
    }

    private void insertEventIntoDatabase(Event event) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "INSERT INTO events (event_name, event_date, event_location) VALUES (?, ?, ?)";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, event.getEventName());
                pstmt.setDate(2, event.getEventDate());
                pstmt.setString(3, event.getEventLocation());
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEventFromDatabase(String eventName) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "DELETE FROM events WHERE event_name = ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, eventName);
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateEventInDatabase(String oldEventName, Event updatedEvent) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";

        String sql = "UPDATE events SET event_name = ?, event_date = ?, event_location = ? WHERE event_name = ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, updatedEvent.getEventName());
                pstmt.setDate(2, updatedEvent.getEventDate());
                pstmt.setString(3, updatedEvent.getEventLocation());
                pstmt.setString(4, oldEventName);
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Event> fetchEventsFromDatabase() {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "postgres";
        String password = "3113";
        List<Event> eventList = new ArrayList<>();

        String sql = "SELECT event_name, event_date, event_location FROM events";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String eventName = rs.getString("event_name");
                    Date eventDate = rs.getDate("event_date");
                    String eventLocation = rs.getString("event_location");
                    eventList.add(new Event(eventName, eventDate, eventLocation));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return eventList;
    }

    public static class Event {
        private String eventName;
        private Date eventDate;
        private String eventLocation;

        public Event(String eventName, Date eventDate, String eventLocation) {
            this.eventName = eventName;
            this.eventDate = eventDate;
            this.eventLocation = eventLocation;
        }

        public String getEventName() {
            return eventName;
        }

        public Date getEventDate() {
            return eventDate;
        }

        public String getEventLocation() {
            return eventLocation;
        }
    }
}
