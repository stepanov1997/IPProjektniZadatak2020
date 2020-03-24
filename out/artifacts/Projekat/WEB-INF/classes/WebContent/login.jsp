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
    <title>LOGIN</title>
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <link rel="ICON" href="https://scontent.fbeg4-1.fna.fbcdn.net/v/t1.0-9/54255431_645793952539379_1611586770158223360_o.jpg?_nc_cat=110&_nc_sid=09cbfe&_nc_ohc=zDb83HnW2FoAX-AuhRZ&_nc_ht=scontent.fbeg4-1.fna&oh=da1701f4c2fa67f6a3bad35766e337e7&oe=5E9CEBBE" type="image/jpg" />
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