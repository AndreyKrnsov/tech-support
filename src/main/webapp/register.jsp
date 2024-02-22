<!-- registration.jsp -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String errorMessage = (String) request.getAttribute("errorMessage"); %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" type="text/css" href="style/components.css">
    <link rel="stylesheet" type="text/css" href="style/register&login.css">

</head>
<body>
    <h1 class="page-title">Регистрация</h1>
    <% if (errorMessage != null) { %>
        <div class="error-message">
            <%= errorMessage %>
        </div>
    <% } %>
    <form action="register" method="post" class="basic-form-block">
        <input class="basic-input" type="text" id="firstName" name="firstName" placeholder="имя" maxlength="50" required>
        <input class="basic-input" type="text" id="lastName" name="lastName" placeholder="фамилия" maxlength="50"required>
        <input class="basic-input" type="text" id="login" name="login" placeholder="логин" minlength="5" maxlength="30" required>
        <div class="password-block">
            <input class="basic-input" type="password" id="password" name="password" placeholder="пароль" minlength="4" maxlength="20" required><br>
            <input type="checkbox" onclick="showPassword()"><label>Показать пароль</label>
        </div>
        <input class="main-button" type="submit" value="Зарегистрироваться">
    </form>
    <div class="auth-link-container">
        Есть аккаунт?
        <a href="login" class="link">
            Войти
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