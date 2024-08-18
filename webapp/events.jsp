<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.community.library.servlet.EventServlet.Event" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Events</title>
    <script>
        function showUpdateForm(eventName, eventDate, eventLocation) {
            document.getElementById('updateForm').style.display = 'block';
            document.getElementById('updateEventName').value = eventName;
            document.getElementById('updateEventDate').value = eventDate;
            document.getElementById('updateEventLocation').value = eventLocation;
            document.getElementById('oldEventName').value = eventName;
        }

        function showAddForm() {
            document.getElementById('addForm').style.display = 'block';
        }

        function hideForms() {
            document.getElementById('addForm').style.display = 'none';
            document.getElementById('updateForm').style.display = 'none';
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
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
        }
        h3 {
            color: #4CAF50;
            text-align: center;
        }
        .form-container {
            margin: 20px 0;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #fff;
            max-width: 600px;
            margin: 0 auto;
        }
        .form-container label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-container input[type="text"],
        .form-container input[type="date"],
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
            font-size: 16px;
        }
        .form-container button:hover {
            background-color: #45a049;
        }
        .event-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 10px 0;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: #fff;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
        }
        .event-actions {
            display: flex;
            gap: 10px;
        }
        .event-actions form {
            display: inline;
        }
        .event-actions button {
            background-color: #4CAF50;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }
        .event-actions button:hover {
            background-color: #45a049;
        }
        #addForm {
            display: none;
        }
        #updateForm {
            display: none;
            margin-top: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #fff;
            max-width: 600px;
            margin: 20px auto;
        }
        .button-container {
            text-align: right;
            margin-bottom: 20px;
        }
        .add-button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
    </style>
</head>
<body>
    <!-- Navigation Header -->
    <div class="nav-header">
        <a href="home.html">Home</a>
        <a href="members">Members</a>
        <a href="loans">Loans</a>
        <a href="books">Books</a>
    </div>

    <div class="container">
        <h2>Events</h2>

        <!-- Container for the "Add Event" button aligned to the right -->
        <div class="button-container">
            <button onclick="showAddForm()" class="add-button">Add Event</button>
        </div>

        <!-- Form to add a new event -->
        <div id="addForm" class="form-container">
            <h3>Add Event</h3>
            <form method="post" action="events">
                <input type="hidden" name="action" value="add">
                <label for="eventName">Event Name:</label>
                <input type="text" id="eventName" name="eventName" placeholder="Event Name" required>

                <label for="eventDate">Event Date:</label>
                <input type="date" id="eventDate" name="eventDate" placeholder="Event Date" required>

                <label for="eventLocation">Event Location:</label>
                <input type="text" id="eventLocation" name="eventLocation" placeholder="Event Location" required>

                <button class="add-button" type="submit">Add Event</button>
                <button type="button" onclick="hideForms()">Cancel</button>
            </form>
        </div>

        <!-- Display existing events -->
        <h3>Current Events</h3>
        <ul id="eventList">
            <% 
                @SuppressWarnings("unchecked")
                List<Event> eventList = (List<Event>) request.getAttribute("events");
                if (eventList != null) {
                    for (Event event : eventList) {
                        String eventDateStr = event.getEventDate().toString(); // Convert date to string
                        %>
                        <li class="event-item">
                            <span>
                                Name: <%= event.getEventName() %>, 
                                Date: <%= eventDateStr %>, 
                                Location: <%= event.getEventLocation() %>
                            </span>
                            <span class="event-actions">
                                <form method="post" action="events" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="eventName" value="<%= event.getEventName() %>">
                                    <button type="submit">Delete</button>
                                </form>
                                <button onclick="showUpdateForm('<%= event.getEventName() %>', '<%= eventDateStr %>', '<%= event.getEventLocation() %>')">Update</button>
                            </span>
                        </li>
                        <% 
                    }
                }
            %>
        </ul>

        <!-- Form to update an event -->
        <div id="updateForm" class="form-container">
            <h3>Update Event</h3>
            <form method="post" action="events">
                <input type="hidden" name="action" value="update">
                <input type="hidden" id="oldEventName" name="oldEventName">

                <label for="updateEventName">Event Name:</label>
                <input type="text" id="updateEventName" name="eventName" required>

                <label for="updateEventDate">Event Date:</label>
                <input type="date" id="updateEventDate" name="eventDate" required>

                <label for="updateEventLocation">Event Location:</label>
                <input type="text" id="updateEventLocation" name="eventLocation" required>

                <button type="submit">Update Event</button>
                <button type="button" onclick="hideForms()">Cancel</button>
            </form>
        </div>
    </div>
</body>
</html>
