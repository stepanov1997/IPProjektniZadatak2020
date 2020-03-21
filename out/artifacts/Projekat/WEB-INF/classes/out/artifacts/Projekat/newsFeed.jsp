<jsp:useBean id="accountBean" scope="session" type="model.beans.AccountBean"/>
<%--
  Created by IntelliJ IDEA.
  User: stepa
  Date: 20.3.2020.
  Time: 20:52
  To change this template use File | Settings | File Templates.
--%>
<%@page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script>
        function addProfilePicture() {
            const xhttp = new XMLHttpRequest();
            const url = 'Controller?controller=account&action=picture';

            xhttp.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    let result = JSON.parse(this.responseText);
                    if(result.exists)
                    {
                        var img = new Image();
                        img.src = "rest?id="+result.id;
                        img.style.height = "100px";
                        img.style.width = "150px";
                        document.getElementById("profilePic").appendChild(img);
                    }
                    else
                    {
                        showImageOfCountry(result.countryCode);
                    }
                }
                else
                {
                    $("#profilePic").hidden = true;
                }
            };
            xhttp.open('GET', url, true);
            xhttp.send();
        }

        function showImageOfCountry(countryCode) {
            const xhttp = new XMLHttpRequest();
            const url = 'https://restcountries.eu/rest/v2/region/europe';
            xhttp.open('GET', url);
            xhttp.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    let result = JSON.parse(this.responseText);
                    var flagLink = result.find(elem => elem.alpha2Code === countryCode).flag;
                    var img = new Image();
                    img.src = flagLink;
                    img.style.height = "100px";
                    img.style.width = "150px";
                    document.getElementById("profilePic").appendChild(img);
                }
            };
            xhttp.send();
        }
    </script>
</head>
<body>
<p>Name: ${accountBean.account.name}</p>
<p>Surname: ${accountBean.account.surname}</p>
<p>Username: ${accountBean.account.username}</p>
<p>Email: ${accountBean.account.email}</p>
<p>Email: ${accountBean.account.country}</p>
<p>Email: ${accountBean.account.region}</p>
<p>Email: ${accountBean.account.city}</p>
<p id="profilePic"></p>

</body>
</html>
