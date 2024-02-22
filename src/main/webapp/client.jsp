<!-- client.jsp -->

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
    <title>Поддержка</title>
    <link rel="stylesheet" type="text/css" href="style/components.css">
    <link rel="stylesheet" type="text/css" href="style/client.css">
</head>
<body>
    <div class="main-container">
        <nav>
            <a class="active-nav">Техподдержка</a>
            <a href="login" class="default-nav login-nav">
                Выйти
            </a>
        </nav>
        <h2>Оставьте свой номер <br>телефона и вам перезвонят</h2>
        <form action="client" method="post" class="client-form">
            <input type="hidden" name="action" value="create request">
            <input class="basic-input number-input" pattern="(8|\+7)\d{10}" type="text" name="number" placeholder="номер телефона" required>
            <textarea class="input-textarea" name="description" placeholder="опишите свою проблему здесь" maxlength="1000" required></textarea>
            <input class="main-button" type="submit" value="Отправить заявку">
        </form>
        <h2>Мои текущие обращения</h2>
        <%
                if (requests != null && requests.size() != 0) {
        %>
        <table>
            <tr>
                <th>Дата</th>
                <th>Описание проблемы</th>
                <th>Статус</th>
            </tr>

            <%
                 for (Request userRequest : requests) {

            %>

            <tr>
                <td class="first-column">
                    <% ZonedDateTime zdt = ZonedDateTime.ofInstant(userRequest.getCreateDate(), ZoneId.systemDefault()); %>
                    <div class="first-column-main">
                        <%= zdt.format(DateTimeFormatter.ofPattern("d MMMM", new Locale("ru", "RU"))) %>
                    </div>
                    <div class="first-column-secondary">
                        <%= zdt.format(DateTimeFormatter.ofPattern("HH:mm")) %>
                    </div>
                </td>
                <td class="description-column">
                    <%= userRequest.getDescription() %>
                </td>
                <%
                    if (userRequest.getStatus().toString().equals("COMPLETE")) {
                %>
                    <td class="status-column complete-cell">
                        <div class="status-block">
                            <span class="status-dot"></span>
                            <div class="status-text">Завершено</div>
                        </div>
                    </td>
                <%
                    } else {
                %>
                    <td class="status-column waiting-cell">
                        <div class="status-block">
                            <span class="status-dot"></span>
                            <%
                                if (userRequest.getStatus().toString().equals("IN_PROGRESS")) {
                            %>
                                <div class="status-text">В работе</div>
                            <%
                                } else {
                            %>
                                <div class="status-text">Отправлено в поддержку</div>
                            <%
                                }
                            %>
                        </div>
                    </td>
                <%
                    }
                %>
                <%
                    if (userRequest.getStatus().toString().equals("COMPLETE")) {
                %>

                    <%
                        if (userRequest.getComment() != null && !userRequest.getComment().trim().equals("")) {
                    %>

                        <td class="comment-static-column">
                            <b>Комментарий:</b> <%= userRequest.getComment() %>
                        </td>

                    <%
                        }
                    %>

                    <%
                        if (userRequest.getScore() != 0) {
                    %>

                        <td>
                            <b>Оценка:</b> <%= userRequest.getScore() %>
                        </td>

                    <%
                        } else {
                    %>

                        <td>
                            <form action="client" method="post" class="score-form">
                                <input type="hidden" name="action" value="set score">
                                <input type="hidden" name="request-id" value="<%= userRequest.getRequestId() %>">
                                <select name="score" id="score" class="score-select">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5" selected>5</option>
                                </select>
                                <input type="submit" class="main-button secondary-button" value="Оценить помощь">
                            </form>
                        </td>

                    <%
                        }
                    %>

                <%
                    }
                %>
            </tr>
            <% } %>
        </table>
        <% } else { %>
            <p>
                У вас пока что не было ни одного запроса.
            </p>
        <% } %>
    </div>


</body>
</html>