package com.community.library.servlet;

import com.community.library.dao.UserDAO;
import com.community.library.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class UserRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User newUser = new User(username, email, password);

        try {
            userDAO.addUser(newUser);
            // Redirect to login page on successful registration
            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to registration page on failure
            response.sendRedirect("index.html");
        }
    }
}
