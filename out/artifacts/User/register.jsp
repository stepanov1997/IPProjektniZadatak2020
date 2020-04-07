<%--
  Created by IntelliJ IDEA.
  User: stepa
  Date: 17.3.2020.
  Time: 22:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>REGISTRATION</title>
    <link rel="ICON" href="https://scontent.fbeg4-1.fna.fbcdn.net/v/t1.0-9/54255431_645793952539379_1611586770158223360_o.jpg?_nc_cat=110&_nc_sid=09cbfe&_nc_ohc=zDb83HnW2FoAX-AuhRZ&_nc_ht=scontent.fbeg4-1.fna&oh=da1701f4c2fa67f6a3bad35766e337e7&oe=5E9CEBBE" type="image/jpg" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script>
        function loadDoc() {
            const xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    debugger;
                    const result = JSON.parse(this.responseText);
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
            let url = "Controller?controller=user&action=register";
            url += "&name=" + $('[name$="name"]').val();
            url += "&surname=" + $('[name$="surname"]').val();
            url += "&username=" + $('[name$="username"]').val();
            url += "&password=" + $('[name$="password"]').val();
            url += "&passwordAgain=" + $('[name$="passwordAgain"]').val();
            url += "&email=" + $('[name$="email"]').val();
            url += "&submit=" + $('[name$="submit"]').val();
            console.log(url);
            xhttp.open("POST", url, true);
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
    <p>Name: <label>
        <input type="text" name="name">
    </label></p>
    <p>Surname: <label>
        <input type="text" name="surname">
    </label></p>
    <p>Username: <label>
        <input type="text" name="username">
    </label></p>
    <p>Password: <label>
        <input type="password" name="password">
    </label></p>
    <p>Password: <label>
        <input type="password" name="passwordAgain">
    </label></p>
    <p>E-mail: <label>
        <input type="text" name="email">
    </label></p><br>
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
