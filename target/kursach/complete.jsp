<!-- new_requests.jsp -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.time.ZonedDateTime" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.List" %>
<%@ page import="techSupport.dto.*" %>
<%@ page import="java.util.Objects" %>
<%
   List<Task> tasks = (List<Task>) request.getAttribute("tasks");
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
            <a href="new_requests" class="default-nav">Новые обращения</a>
            <a href="tasks" class="default-nav">Мои задачи</a>
            <a class="active-nav">Завершенные задачи</a>
            <a href="login" class="default-nav login-nav">
                Выйти
            </a>
        </nav>

        <%
            if (tasks != null && tasks.size() != 0) {
        %>
        <table class="default-table">
            <tr>
                <th>Логин</th>
                <th>Описание проблемы</th>
                <th>Дата поступления<br>заявки</th>
                <th>Оценка<br>пользователя</th>
            </tr>

            <%
                 for (Task task : tasks) {

            %>

            <tr>
                <td class="first-column">
                    <div class="first-column-main">
                       <%= task.getInfo().split("/")[2] %>
                    </div>
                </td>
                <td class="description-column">
                    <%= task.getDescription() %>
                </td>
                <td  style="padding-right: 100px;">
                    <% ZonedDateTime zdt = ZonedDateTime.ofInstant(task.getCreateDate(), ZoneId.systemDefault()); %>
                    <div class="date-cell">
                        <%= zdt.format(DateTimeFormatter.ofPattern("d MMMM", new Locale("ru", "RU"))) %>
                    </div>
                    <div>
                        <%= zdt.format(DateTimeFormatter.ofPattern("HH:mm")) %>
                    </div>
                </td>
                <td>
                    <% if (task.getInfo().split("/")[0].equals("0")) { %>
                        Не оценено
                    <% } else { %>
                        <%= task.getInfo().split("/")[0] %>
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
                У вас пока что нет ни одной выполненой задачи.
            </p>
        <% } %>
    </div>


</body>
</html>