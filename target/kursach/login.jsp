<!-- login.jsp -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String errorMessage = (String) request.getAttribute("errorMessage"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <link rel="stylesheet" type="text/css" href="style/components.css">
    <link rel="stylesheet" type="text/css" href="style/register&login.css">

</head>
<body>
    <h1 class="page-title">Вход</h1>
    <% if (errorMessage != null) { %>
        <div class="error-message">
            <%= errorMessage %>
        </div>
    <% } %>
    <% if (session != null && session.getAttribute("flashMessage") != null) { %>
        <script>
            alert('<%= session.getAttribute("flashMessage") %>');
        </script>
        <% session.removeAttribute("flashMessage"); %>
    <% } %>
    <form action="login" method="post" class="basic-form-block">
        <input class="basic-input" type="text" id="login" name="login" placeholder="логин" required>
        <div class="password-block">
            <input class="basic-input" type="password" id="password" name="password" placeholder="пароль" required><br>
            <input type="checkbox" onclick="showPassword()"><label>Показать пароль</label>
        </div>
        <input class="main-button" type="submit" value="Войти">
    </form>
    <div class="auth-link-container">
        Нет аккаунта? <br>
        <a href="register" class="link">
            Зарегистрироваться
        </a>
    </div>

    <script>
        function showPassword() {
            var x = document.getElementById("password");
            if (x.type === "password") {
                x.type = "text";
            } else {
                x.type = "password";
            }
        }
    </script>
</body>
</html>