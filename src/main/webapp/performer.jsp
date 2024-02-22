<!-- performer.jsp -->

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
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Новые заявки</title>
    <link rel="stylesheet" type="text/css" href="style/components.css">
    <link rel="stylesheet" type="text/css" href="style/new_requests.css">
</head>
<body>
    <div class="main-container">
        <nav>
            <a class="active-nav">Текущие задачи</a>
            <a href="login" class="default-nav login-nav">
                Выйти
            </a>
        </nav>

        <%
            if (tasks != null && tasks.size() != 0) {
        %>

        <table class="default-table">
            <tr>
                <th>Работник<br>поддержки</th>
                <th>Описание проблемы</th>
                <th>Дата поступления<br>задачи</th>
                <th>Комментарий</th>
            </tr>

            <%
                 for (Task task : tasks) {
            %>

            <tr>
                <td class="first-column" style="padding-right: 30px;">
                    <div class="first-column-main">
                        <%= task.getInfo().split("/")[0] %>
                        <br>
                        <%= task.getInfo().split("/")[1] %>
                    </div>
                    <div class="first-column-secondary">
                        <%= task.getInfo().split("/")[2] %>
                    </div>

                </td>
                <td class="description-column">
                    <%= task.getDescription() %>
                </td>
                <td style="padding-right: 90px;">
                    <% ZonedDateTime zdt = ZonedDateTime.ofInstant(task.getCreateDate(), ZoneId.systemDefault()); %>
                    <div class="date-cell">
                        <%= zdt.format(DateTimeFormatter.ofPattern("d MMMM", new Locale("ru", "RU"))) %>
                    </div>
                    <div>
                        <%= zdt.format(DateTimeFormatter.ofPattern("HH:mm")) %>
                    </div>
                </td>
                <form action="performer" method="post">
                    <input type="hidden" name="task-id" value="<%= task.getTaskId() %>">
                    <td class="comment-column">
                        <textarea name="comment" id="" cols="25" rows="5" placeholder="комментарий"></textarea>
                    </td>
                    <td style="padding-left: 30px;">
                        <input type="submit" class="main-button secondary-button" value="Завершить задачу">
                    </td>
                </form>
            </tr>
        <% } %>
        </table>

        <% } else { %>
            <p>
                У вас нет задач!
            </p>
        <% } %>
    </div>


</body>
</html>