<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome page</title>
    <link rel="stylesheet" type="text/css" href="resources/style.css"></link>
    <script>
        function img_click(url) {
            if(window.innerWidth<800 || window.screen.width<800)
            {
                document.getElementById(url).style.border = "solid red 4px";
                setTimeout(null, 1000);
            }
            window.location = url;
        }
    </script>
</head>
<body>
<div class="header"><h1>WELCOME TO THE 'IP2020 SYSTEM'</h1></div>
<div class="centered">
    <div class="row">
        <div class="leftcolumn" style="text-align: center">
            <img id="admin" onclick="img_click('admin')" style="cursor:hand"
                 src="https://cdn0.iconfinder.com/data/icons/user-icon-profile-businessman-finance-vector-illus/100/06-1User-512.png">
            <h2 style="padding-top: 10px">ADMINISTRATOR APPLICATION</h2>
        </div>
        <div class="midcolumn" style="text-align: center">
            <img id="assistance_system" onclick="img_click('assistance_system')" style="cursor:hand"
                 src="https://www.pngitem.com/pimgs/m/145-1450570_info-support-information-phone-call-help-svg-png.png">
            <h2 style="padding-top: 10px">ASSISTANCE SYSTEM APPLICATION</h2>
        </div>
        <div class="rightcolumn" style="text-align: center">
            <img id="user" onclick="img_click('user')" style="cursor:hand"
                 src="https://cdn1.iconfinder.com/data/icons/business-users/512/circle-512.png">
            <h2 style="padding-top: 10px">USER APPLICATION</h2>
        </div>
    </div>
</div>
<div class="footer">Internet programming - 2020</div>
</body>
</html>
