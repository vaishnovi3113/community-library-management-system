<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.community.library.servlet.LoanServlet.Loan" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Loans</title>
    <script>
        function showUpdateForm(memberId, bookId, loanDate, returnDate) {
            document.getElementById('updateForm').style.display = 'block';
            document.getElementById('updateMemberId').value = memberId;
            document.getElementById('updateBookId').value = bookId;
            document.getElementById('updateLoanDate').value = loanDate;
            document.getElementById('updateReturnDate').value = returnDate;
            document.getElementById('oldMemberId').value = memberId;
            document.getElementById('oldBookId').value = bookId;
        }

        function toggleAddLoanForm() {
            var form = document.getElementById('addLoanForm');
            form.style.display = (form.style.display === 'none' || form.style.display === '') ? 'block' : 'none';
        }
    </script>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
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
        form {
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
            margin: 10px 0 5px;
            font-weight: bold;
        }
        form input[type="text"],
        form input[type="date"],
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
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        form button:hover {
            background-color: #45a049;
        }
        .loan-item {
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
        .loan-item span {
            display: block;
        }
        .loan-actions {
            display: flex;
            gap: 10px;
        }
        .loan-actions form {
            display: inline;
        }
        .loan-actions button {
            background-color: #4CAF50;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }
        .loan-actions button:hover {
            background-color: #45a049;
        }
        #addLoanForm, #updateForm {
            display: none;
            margin-top: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #fff;
            max-width: 600px;
            margin: 20px auto;
        }
        #updateForm h3, #addLoanForm h3 {
            color: #4CAF50;
            text-align: center;
        }
        #toggleAddLoanFormButton {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin: 20px 0;
        }
        #toggleAddLoanFormButton:hover {
            background-color: #45a049;
        }
        .button-container {
            text-align: right;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <!-- Navigation Header -->
    <div class="nav-header">
        <a href="home.html">Home</a>
        <a href="members">Members</a>
        <a href="events">Events</a>
        <a href="books">Books</a>
    </div>

    <div class="container">
        <h2>Loans</h2>

        <!-- Container for the "Add Loan" button aligned to the right -->
        <div class="button-container">
            <button id="toggleAddLoanFormButton" onclick="toggleAddLoanForm()">Add Loan</button>
        </div>

        <!-- Form to add a new loan -->
        <div id="addLoanForm">
            <form method="post" action="loans">
                <input type="hidden" name="action" value="add">
                <label for="memberId">Member ID:</label>
                <input type="number" id="memberId" name="memberId" placeholder="Member ID" required>

                <label for="bookId">Book ID:</label>
                <input type="number" id="bookId" name="bookId" placeholder="Book ID" required>

                <label for="loanDate">Loan Date:</label>
                <input type="date" id="loanDate" name="loanDate" required>

                <label for="returnDate">Return Date:</label>
                <input type="date" id="returnDate" name="returnDate" required>

                <button type="submit">Add Loan</button>
            </form>
        </div>

        <!-- Display existing loans -->
        <h3>Current Loans</h3>
        <ul id="loanList">
            <% 
                @SuppressWarnings("unchecked")
                List<Loan> loanList = (List<Loan>) request.getAttribute("loans");
                if (loanList != null) {
                    for (Loan loan : loanList) {
                        String loanDateStr = loan.getLoanDate().toString(); // Convert date to string
                        String returnDateStr = loan.getReturnDate().toString(); // Convert date to string
                        %>
                        <li class="loan-item">
                            <span>
                                Member ID: <%= loan.getMemberId() %>, 
                                Book ID: <%= loan.getBookId() %>, 
                                Loan Date: <%= loanDateStr %>, 
                                Return Date: <%= returnDateStr %>
                            </span>
                            <span class="loan-actions">
                                <form method="post" action="loans">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="memberId" value="<%= loan.getMemberId() %>">
                                    <input type="hidden" name="bookId" value="<%= loan.getBookId() %>">
                                    <button type="submit">Delete</button>
                                </form>
                                <button onclick="showUpdateForm('<%= loan.getMemberId() %>', <%= loan.getBookId() %>, '<%= loanDateStr %>', '<%= returnDateStr %>')">Update</button>
                            </span>
                        </li>
                        <% 
                    }
                }
            %>
        </ul>

        <!-- Form to update a loan -->
        <div id="updateForm">
            <h3>Update Loan</h3>
            <form method="post" action="loans">
                <input type="hidden" name="action" value="update">
                <input type="hidden" id="oldMemberId" name="oldMemberId">
                <input type="hidden" id="oldBookId" name="oldBookId">

                <label for="updateMemberId">Member ID:</label>
                <input type="number" id="updateMemberId" name="memberId" required>

                <label for="updateBookId">Book ID:</label>
                <input type="number" id="updateBookId" name="bookId" required>

                <label for="updateLoanDate">Loan Date:</label>
                <input type="date" id="updateLoanDate" name="loanDate" required>

                <label for="updateReturnDate">Return Date:</label>
                <input type="date" id="updateReturnDate" name="returnDate" required>

                <button type="submit">Update Loan</button>
            </form>
        </div>
    </div>
</body>
</html>
