<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.community.library.servlet.MemberServlet.Member" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Members</title>
    <script>
        function showForm() {
            document.getElementById('memberFormContainer').style.display = 'block';
        }

        function hideForm() {
            document.getElementById('memberFormContainer').style.display = 'none';
        }
    </script>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .nav-header {
            background-color: #4CAF50;
            color: white;
            padding: 15px;
            text-align: center;
        }
        .nav-header a {
            margin: 0 20px;
            text-decoration: none;
            color: white;
            font-size: 18px;
        }
        .nav-header a:hover {
            text-decoration: underline;
        }
        .container {
            padding: 20px;
            max-width: 1200px;
            margin: 0 auto;
        }
        h2 {
            color: #4CAF50;
            text-align: center;
        }
        .button-container {
            text-align: right;
            margin-bottom: 20px;
        }
        .button-container button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .button-container button:hover {
            background-color: #45a049;
        }
        .form-container {
            display: none;
            position: fixed;
            right: 20px;
            top: 20px;
            width: 300px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            z-index: 1000;
        }
        .form-container label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-container input[type="text"],
        .form-container input[type="email"],
        .form-container input[type="tel"] {
            width: calc(100% - 22px);
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .form-container button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .form-container button:hover {
            background-color: #45a049;
        }
        .member-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 10px 0;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: #fff;
        }
        .member-actions {
            display: flex;
            gap: 10px;
        }
        .member-actions form {
            display: inline;
        }
        .member-actions button {
            background-color: #4CAF50;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .member-actions button:hover {
            background-color: #45a049;
        }
        h2 {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
        }
        h3 {
            color: #4CAF50;
            text-align: center;
            padding: 15px;
        }
    </style>
</head>
<body>
    <!-- Navigation Header -->
    <div class="nav-header">
        <a href="home.html">Home</a>
        <a href="loans">Loans</a>
        <a href="events">Events</a>
        <a href="books">Books</a>
    </div>

    <div class="container">
        <h2>Members</h2>

        <!-- Button to show form -->
        <div class="button-container">
            <button onclick="showForm()">Add Member</button>
        </div>

        <!-- Form to add or edit a member -->
        <div id="memberFormContainer" class="form-container">
            <form method="post" action="members">
                <input type="hidden" name="action" value="<%= request.getAttribute("memberToEdit") != null ? "update" : "insert" %>"/>
                <input type="hidden" name="id" value="<%= request.getAttribute("memberToEdit") != null ? ((Member) request.getAttribute("memberToEdit")).getId() : "" %>"/>
                
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" placeholder="Name" required value="<%= request.getAttribute("memberToEdit") != null ? ((Member) request.getAttribute("memberToEdit")).getName() : "" %>"/>

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" placeholder="Email" required value="<%= request.getAttribute("memberToEdit") != null ? ((Member) request.getAttribute("memberToEdit")).getEmail() : "" %>"/>

                <label for="phone">Phone:</label>
                <input type="number" id="phone" name="phone" placeholder="Phone" required pattern="[0-9]{10}" title="Phone number must be exactly 10 digits" value="<%= request.getAttribute("memberToEdit") != null ? ((Member) request.getAttribute("memberToEdit")).getPhone() : "" %>"/>
                
                <button type="submit"><%= request.getAttribute("memberToEdit") != null ? "Update Member" : "Add Member" %></button>
                <button type="button" onclick="hideForm()">Cancel</button>
            </form>
        </div>

        <!-- Display existing members -->
        <h3>Member List</h3>
        <ul id="memberList">
            <% 
                @SuppressWarnings("unchecked")
                List<Member> memberList = (List<Member>) request.getAttribute("members");
                if (memberList != null) {
                    for (Member member : memberList) {
                        %>
                        <li class="member-item">
                            <span>
                                Name: <%= member.getName() %>, 
                                Email: <%= member.getEmail() %>, 
                                Phone: <%= member.getPhone() %>
                            </span>
                            <span class="member-actions">
                                <form method="get" action="members">
                                    <input type="hidden" name="action" value="edit"/>
                                    <input type="hidden" name="id" value="<%= member.getId() %>"/>
                                    <button type="submit" onclick="showForm()">Edit</button>
                                </form>
                                <form method="post" action="members">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="id" value="<%= member.getId() %>"/>
                                    <button type="submit">Delete</button>
                                </form>
                            </span>
                        </li>
                        <% 
                    }
                }
            %>
        </ul>
    </div>

    <script>
        window.onload = function() {
            // Check if we are editing a member
            <% if (request.getAttribute("memberToEdit") != null) { %>
                showForm();
            <% } %>
        };
    </script>
</body>
</html>
