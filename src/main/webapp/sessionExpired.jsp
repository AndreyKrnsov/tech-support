<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Сессия Истекла</title>
    <link rel="stylesheet" type="text/css" href="style/components.css">
    <script type="text/javascript">
        function notifyAndRedirect() {
            alert("Ваша сессия истекла. Пожалуйста, войдите в систему снова.");
            window.location.href = 'login';
        }
    </script>
</head>
<body onload="notifyAndRedirect();">
    <div class="main-container">
        <h1>Перенаправление...</h1>
        <p>Если перенаправление не произошло, нажмите <a href="login" class="link">здесь</a> для входа.</p>
    </div>

</body>
</html>