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
   List<Request> requests = (List<Request>) request.getAttribute("requests");
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
            <a class="active-nav">Новые обращения</a>
            <a href="tasks" class="default-nav">Мои задачи</a>
            <a href="complete" class="default-nav">Завершенные задачи</a>
            <a href="login" class="default-nav login-nav">
                Выйти
            </a>
        </nav>

        <%
            if (requests != null && requests.size() != 0) {
        %>
        <table class="default-table">
            <tr>
                <th>Логин</th>
                <th>Описание проблемы</th>
                <th>Дата поступления<br>заявки</th>
            </tr>

            <%
                 for (Request userRequest : requests) {

            %>

            <tr>
                <td class="first-column">
                    <div class="first-column-main">
                       <%= userRequest.getInfo() %>
                    </div>
                </td>
                <td class="description-column">
                    <%= userRequest.getDescription() %>
                </td>
                <td>
                    <% ZonedDateTime zdt = ZonedDateTime.ofInstant(userRequest.getCreateDate(), ZoneId.systemDefault()); %>
                    <div class="date-cell">
                        <%= zdt.format(DateTimeFormatter.ofPattern("d MMMM", new Locale("ru", "RU"))) %>
                    </div>
                    <div>
                        <%= zdt.format(DateTimeFormatter.ofPattern("HH:mm")) %>
                    </div>
                </td>
                <td style="padding-left: 30px;">
                    <form action="new_requests" method="post">
                        <input type="hidden" name="request-id" value="<%= userRequest.getRequestId() %>">
                        <input type="submit" class="main-button secondary-button" value="Взять задачу">
                    </form>

                </td>
            </tr>
           <% } %>
        </table>
        <% } else { %>
            <p>
                Все запросы в работе!
            </p>
        <% } %>
    </div>


</body>
</html>