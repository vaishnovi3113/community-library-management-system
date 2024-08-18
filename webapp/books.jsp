<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.community.library.servlet.BookServlet.Book" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Books</title>
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
        h2 {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
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
        h3 {
            color: #4CAF50;
            text-align: center;
        }
        .button-group {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            margin-bottom: 20px;
        }
        .button-group button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .button-group button:hover {
            background-color: #45a049;
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
        form label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        form input[type="text"],
        form input[type="date"],
        form input[type="tel"],
        form input[type="search"],
        form input[type="number"] {
            width: calc(100% - 22px);
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        form button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        form button:hover {
            background-color: #45a049;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .action-buttons {
            display: flex;
            gap: 10px;
        }
        .action-buttons form {
            margin: 0;
        }
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.5);
        }
        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 70%;
            max-width: 500px;
        }
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }
        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
        .add-button, .update-button, .search-button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .add-button:hover, .update-button:hover, .search-button:hover {
            background-color: #45a049;
        }
        .update-button {
            padding: 2px 8px; /* Reduced padding for smaller height */
            font-size: 14px; /* Optional: Adjust font size */
        }
    </style>
</head>
<body>
<div class="nav-header">
    <a href="home.html">Home</a>
    <a href="members">Members</a>
    <a href="events">Events</a>
    <a href="loans">Loans</a>
</div>
<div class="container">
    <h2>Books</h2>
    <div class="button-group">
        <button class="add-button" onclick="openAddModal()">Add Book</button>
        <button class="search-button" onclick="openSearchModal()">Search Books</button>
    </div>
    <h3>List of Books</h3>
    <table>
        <thead>
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>ISBN</th>
            <th>Genre</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Book> books = (List<Book>) request.getAttribute("books");
            if (books != null) {
                for (Book book : books) {
        %>
        <tr>
            <td><%= book.getTitle() %></td>
            <td><%= book.getAuthor() %></td>
            <td><%= book.getIsbn() %></td>
            <td><%= book.getGenre() %></td>
            <td class="action-buttons">
                <form method="post" action="books">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="isbn" value="<%= book.getIsbn() %>">
                    <button class="delete-button" type="submit">Delete</button>
                </form>
                <button class="update-button" onclick="openUpdateModal('<%= book.getTitle() %>', '<%= book.getAuthor() %>', '<%= book.getIsbn() %>', '<%= book.getGenre() %>')">Update</button>
            </td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>

<!-- The Update Modal -->
<div id="updateModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeUpdateModal()">&times;</span>
        <h3>Update Book</h3>
        <form method="post" action="books">
            <input type="hidden" name="action" value="update">
            <input type="hidden" id="oldIsbn" name="oldIsbn">
            <label for="modalTitle">Title:</label>
            <input type="text" id="modalTitle" name="title" required>
            <label for="modalAuthor">Author:</label>
            <input type="text" id="modalAuthor" name="author" required>
            <label for="modalIsbn">ISBN:</label>
            <input type="number" id="modalIsbn" name="isbn" required min="0" step="1">
            <label for="modalGenre">Genre:</label>
            <input type="text" id="modalGenre" name="genre" required>
            <button class="update-button" type="submit">Update Book</button>
        </form>
    </div>
</div>

<!-- The Add Modal -->
<div id="addModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeAddModal()">&times;</span>
        <h3>Add a New Book</h3>
        <form method="post" action="books">
            <input type="hidden" name="action" value="add">
            <label for="addTitle">Title:</label>
            <input type="text" id="addTitle" name="title" required>
            <label for="addAuthor">Author:</label>
            <input type="text" id="addAuthor" name="author" required>
            <label for="addIsbn">ISBN:</label>
            <input type="number" id="addIsbn" name="isbn" required min="0" step="1">
            <label for="addGenre">Genre:</label>
            <input type="text" id="addGenre" name="genre" required>
            <button class="add-button" type="submit">Add Book</button>
        </form>
    </div>
</div>

<!-- The Search Modal -->
<div id="searchModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeSearchModal()">&times;</span>
        <h3>Search Books</h3>
        <form method="get" action="books">
            <label for="searchTerm">Search by Title or Author:</label>
            <input type="search" id="searchTerm" name="searchTerm">
            <button type="submit">Search</button>
        </form>
    </div>
</div>

<script>
    // Get the modals
    var updateModal = document.getElementById("updateModal");
    var addModal = document.getElementById("addModal");
    var searchModal = document.getElementById("searchModal");

    // Function to open the update modal and fill it with the book details
    function openUpdateModal(title, author, isbn, genre) {
        document.getElementById("modalTitle").value = title;
        document.getElementById("modalAuthor").value = author;
        document.getElementById("modalIsbn").value = isbn;
        document.getElementById("oldIsbn").value = isbn;
        document.getElementById("modalGenre").value = genre;
        updateModal.style.display = "block";
    }

    // Function to close the update modal
    function closeUpdateModal() {
        updateModal.style.display = "none";
    }

    // Function to open the add modal
    function openAddModal() {
        addModal.style.display = "block";
    }

    // Function to close the add modal
    function closeAddModal() {
        addModal.style.display = "none";
    }

    // Function to open the search modal
    function openSearchModal() {
        searchModal.style.display = "block";
    }

    // Function to close the search modal
    function closeSearchModal() {
        searchModal.style.display = "none";
    }

    // Close modals if the user clicks outside of the modal content
    window.onclick = function(event) {
        if (event.target == updateModal) {
            closeUpdateModal();
        } else if (event.target == addModal) {
            closeAddModal();
        } else if (event.target == searchModal) {
            closeSearchModal();
        }
    }
</script>
</body>
</html>
