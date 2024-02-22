<!-- tasks.jsp -->

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
    List<User> performers = (List<User>) request.getAttribute("performers");
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои задачи</title>
    <link rel="stylesheet" type="text/css" href="style/components.css">
    <link rel="stylesheet" type="text/css" href="style/tasks.css">
</head>
<body>
    <div class="main-container">
        <nav>
            <a href="new_requests" class="default-nav">Новые обращения</a>
            <a class="active-nav">Мои задачи</a>
            <a href="complete" class="default-nav">Завершенные задачи</a>
            <a href="login" class="default-nav login-nav">
                Выйти
            </a>
        </nav>

        <%
            if (tasks != null && tasks.size() != 0) {
        %>

        <table class="default-table">
            <tr>
                <th>Пользователь</th>
                <th>Описание проблемы</th>
                <th>Дата<br>поступления</th>
                <%
                    if (performers != null && performers.size() != 0) {
                %>
                <th>Исполнитель</th>
                <% } %>
                <th>Комментарий</th>
            </tr>

             <%
                  for (Task task : tasks) {

             %>

            <tr>
                <td class="first-column">
                    <div class="first-column-main">
                        <%= task.getInfo().split("/")[0] %>
                    </div>
                    <div class="first-column-secondary">
                        <%= task.getInfo().split("/")[1] %>
                    </div>
                </td>
                <td class="description-column">
                <form action="tasks" method="post" class="description-form">
                    <input type="hidden" name="action" value="description">
                    <input type="hidden" name="task-id" value="<%= task.getTaskId() %>">
                    <textarea onchange="showButton(this)" name="description" cols="35" rows="5"><%= task.getDescription() %></textarea>
                    <input type="submit" class="main-button secondary-button description-button" style="display: none;" value="Сохранить">
                </form>

                </td>
                <td class="date-column">
                    <% ZonedDateTime zdt = ZonedDateTime.ofInstant(task.getCreateDate(), ZoneId.systemDefault()); %>
                    <div class="date-cell">
                        <%= zdt.format(DateTimeFormatter.ofPattern("d MMMM", new Locale("ru", "RU"))) %>
                    </div>
                    <div>
                        <%= zdt.format(DateTimeFormatter.ofPattern("HH:mm")) %>
                    </div>
                </td>
                <%
                    if (performers != null && performers.size() != 0) {
                %>
                <td class="performer-column">
                    <%
                        if (task.getPerformerId() != 0) {
                    %>

                        <div class="performer-block">
                            <div><%= task.getInfo().split("/")[2] %></div>
                            <%
                                if (task.getStatus().toString().equals("IN_PROGRESS")) {
                            %>
                                <div class="status-block waiting-cell">
                                    <span class="status-dot"></span>
                                    <div class="status-text">Выполняет</div>
                                </div>
                            <%
                                } else {
                            %>
                                <div class="status-block complete-cell">
                                    <span class="status-dot"></span>
                                    <div class="status-text">Завершено</div>
                                </div>
                            <%
                                }
                            %>

                        </div>

                    <% } else { %>

                        <form action="tasks" method="post" class="performer-form">
                            <input type="hidden" name="action" value="performer">
                            <input type="hidden" name="task-id" value="<%= task.getTaskId() %>">
                            <select name="performer-id" class="performer-select">
                                <%
                                      for (User performer : performers) {

                                 %>
                                <option value="<%= performer.getUserId() %>"><%= performer.getFirstName() + " " +  performer.getLastName() + " (" + Profile.toText(performer.getProfile()) + ")"%></option>
                                <% } %>
                            </select>
                            <input type="submit" class="main-button secondary-button performer-button" value="Назначить исполнителя">
                        </form>

                    <% } %>
                </td>
                <% } %>
                <form action="tasks" method="post">
                    <input type="hidden" name="action" value="complete">
                    <input type="hidden" name="task-id" value="<%= task.getTaskId() %>">
                    <td class="comment-column">
                        <textarea name="comment" id="" cols="25" rows="5" placeholder="комментарий"><%= task.getComment() %></textarea>
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
                У вас нет взятых задач!
            </p>
        <% } %>
    </div>

    <script>
        function showButton(textareaElement) {
            var button = textareaElement.nextElementSibling;
            button.style.display = "block";
        }
    </script>
</body>
</html>