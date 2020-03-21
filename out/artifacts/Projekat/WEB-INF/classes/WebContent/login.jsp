<%--
  Created by IntelliJ IDEA.
  User: stepa
  Date: 17.3.2020.
  Time: 22:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Account registration</title>
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script>
        function login() {
            const xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    var result = JSON.parse(this.responseText);
                    if(result.redirect)
                    {
                        $("#result").html(result.message);
                        setTimeout(function () {
                            window.location = "newsFeed.jsp";
                        }, 2000);
                    }
                    else
                    {
                        $("#result").html(result.message);
                        setTimeout(function () {
                            $("#result").html("");
                        }, 5000);
                    }
                }
            };
            let url = "Controller?controller=account&action=login";
            url += "&username=" + $('[name$="username"]').val();
            url += "&password=" + $('[name$="password"]').val();
            url += "&submit=" + $('[name$="submit"]').val();
            xhttp.open("POST", url, true);
            xhttp.send();
            return false;
        }
    </script>
</head>
<body>
<h1>LOGIN</h1>
<h4>Fill data</h4>
<hr>
<br>
<form onsubmit="return login()" method="post">
    <p>Username: <input type="text" name="username"></p>
    <p>Password: <input type="password" name="password"></p><br><br>
    <p><a href="register.jsp">Register</a></p><br>
    <p><button type="submit" name="submit">Log in</button></p>
</form>
<hr>
<h2 id="result"></h2>
<hr>
</body>
</html>
