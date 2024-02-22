<!-- registration.jsp -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String errorMessage = (String) request.getAttribute("errorMessage"); %>
<% String successMessage = (String) request.getAttribute("successMessage"); %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" type="text/css" href="style/components.css">
</head>
<body>
    <div class="main-container">
        <nav style=" margin-bottom: 70px;">
            <a class="active-nav">Добавить сотрудника</a>
            <a href="employee" class="default-nav">Все сотрудники</a>
            <a href="login" class="default-nav login-nav">
                Выйти
            </a>
        </nav>
        <% if (errorMessage != null) { %>
            <div class="error-message" style="text-align: start;">
                <%= errorMessage %>
            </div>
        <% } %>
        <% if (successMessage != null) { %>
            <div class="success-message" style="text-align: start;">
                <%= successMessage %>
            </div>
        <% } %>
        <form action="add_user" method="post" class="basic-form-block" style="align-items: flex-start;">
            <input class="basic-input" type="text" id="firstName" name="firstName" placeholder="имя" maxlength="50" required>
            <input class="basic-input" type="text" id="lastName" name="lastName" placeholder="фамилия" maxlength="50"required>
            <input class="basic-input" type="text" id="login" name="login" placeholder="почта" minlength="5" maxlength="30" required>
            <select name="role" id="role" class="basic-select" style="width: 268px">
                <option value="PERFORMER">сотрудник-исполнитель</option>
                <option value="SUPPORT_STAFF">сотрудник поддержки</option>
            </select>
            <select name="profile" id="profile" class="basic-select" style="width: 268px">
                <option value="PROGRAMMER">программист</option>
                <option value="MANAGER">менеджер</option>
                <option value="DATABASE_SPECIALIST">специалист по БД</option>
                <option value="SYSTEM_ADMINISTRATOR">сисадмин</option>
            </select>
            <div class="password-block">
                <input class="basic-input" type="password" id="password" name="password" placeholder="пароль" minlength="4" maxlength="20" required><br>
                <input type="checkbox" onclick="showPassword()"><label>Показать пароль</label>
            </div>
            <input class="main-button" type="submit" value="Зарегистрировать сотрудника" style="width: 250px">
        </form>
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

        document.getElementById('role').addEventListener('change', function() {
            var value = this.value;
            var secondSelect = document.getElementById('profile');

            if (value == 'PERFORMER') {
                secondSelect.style.display = 'block';
            } else {
                secondSelect.style.display = 'none';
            }
        });
    </script>
</body>
</html>