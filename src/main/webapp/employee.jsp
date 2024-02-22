<!-- new_requests.jsp -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="techSupport.dto.*" %>
<%@ page import="java.util.Objects" %>
<%
    List<User> employees = (List<User>) request.getAttribute("employees");
    String countTask = (String) request.getAttribute("countTask");
    String avgScore = (String) request.getAttribute("avgScore");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Новые заявки</title>
    <link rel="stylesheet" type="text/css" href="style/components.css">
</head>
<body>
    <div class="main-container">
        <nav>
            <a href="add_user" class="default-nav">Добавить сотрудника</a>
            <a class="active-nav">Все сотрудники поддержки</a>
            <a href="login" class="default-nav login-nav">
                Выйти
            </a>
        </nav>

        <%
            if (employees != null && employees.size() != 0) {
        %>
        <table class="default-table">
            <tr>
                <th>Имя</th>
                <th>Почта</th>
                <th>Выполнено<br>задач</th>
                <th>Средняя<br>оценка</th>
            </tr>

            <%
                 for (User employee : employees) {

            %>

            <tr>
                <td class="first-column">
                    <div class="first-column-main">
                         <%= employee.getFirstName() %> <br> <%= employee.getLastName() %>
                    </div>
                </td>
                <td style="padding-right: 140px;">
                    <%= employee.getLogin() %>
                </td>
                <td style="padding-right: 170px;">
                    <%= employee.getInfo().split("/")[0] %>
                </td>
                <td>
                    <% if (employee.getInfo().split("/")[1].equals("0.0")) { %>
                        Нет оценок
                    <% } else { %>
                        <%= employee.getInfo().split("/")[1] %>
                    <% } %>
                </td>
            </tr>
           <% } %>
        </table>
        <div style="display: flex; flex-direction: row; align-items: center; column-gap: 15px; margin-top: 10px;">
            <h3>Всего выполено задач: </h3> <h1><%= countTask %></h1>
        </div>
        <%
             if (!avgScore.equals("0.0")) {

        %>
        <div style="display: flex; flex-direction: row; align-items: center; column-gap: 15px; margin-top: -20px;">
            <h3>Средняя оценка клиентов: </h3> <h1><%= avgScore %></h1>
        </div>
        <% } %>
        <% } else { %>
            <p>
                У вас нет ни одного сотрудника поддержки.
            </p>
        <% } %>
    </div>


</body>
</html>