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
        function loadDoc() {
            const xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    debugger;
                    var result = JSON.parse(this.responseText);
                    if(result.redirect)
                    {
                        $("#result").html(result.message);
                        setTimeout(function () {
                            window.location = "editProfile.jsp";
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
            debugger;
            let url = "Controller?controller=account&action=register";
            url += "&name=" + $('[name$="name"]').val();
            url += "&surname=" + $('[name$="surname"]').val();
            url += "&username=" + $('[name$="username"]').val();
            url += "&password=" + $('[name$="password"]').val();
            url += "&passwordAgain=" + $('[name$="passwordAgain"]').val();
            url += "&email=" + $('[name$="email"]').val();
            url += "&submit=" + $('[name$="submit"]').val();
            console.log(url);
            xhttp.open("POST", url.toString(), true);
            xhttp.send();
        }

    </script>
</head>
<body>
<h1>REGISTRATION</h1>
<h4>Fill data</h4>
<hr>
<br>
<form>
    <p>Name: <input type="text" name="name"></p>
    <p>Surname: <input type="text" name="surname"></p>
    <p>Username: <input type="text" name="username"></p>
    <p>Password: <input type="password" name="password"></p>
    <p>Password: <input type="password" name="passwordAgain"></p>
    <p>E-mail: <input type="text" name="email"></p><br>
    <p>
        <button type="button" onclick="loadDoc()" name="submit">Register</button>
    </p>
</form>
<hr>
<h2 id="result"></h2>
<hr>
<br>
</body>
</html>
