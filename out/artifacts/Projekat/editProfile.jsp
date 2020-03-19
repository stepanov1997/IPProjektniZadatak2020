<jsp:useBean id="accountBean" scope="session" type="model.beans.AccountBean"/>
<%--
  Created by IntelliJ IDEA.
  User: stepa
  Date: 18.3.2020.
  Time: 14:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile info</title>
    <script>
        function fillContries() {
            const xhttp = new XMLHttpRequest();
            const url = 'https://restcountries.eu/rest/v2/region/europe';
            xhttp.open('GET', url);
            xhttp.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    let result = JSON.parse(this.responseText);
                    let select = document.getElementById("countries");
                    result.forEach(elem => {
                        const option = document.createElement("option");
                        option.value = elem.alpha2Code;
                        option.innerHTML = elem.name;
                        select.appendChild(option);
                    });
                    fillRegions();
                }
            };
            xhttp.send();
        }

        fillContries();

        function fillRegions() {
            showImageOfCountry();
            const selectCountries = document.getElementById("countries");
            const selectedOpt = selectCountries.options[selectCountries.selectedIndex];
            const url = 'http://battuta.medunes.net/api/region/' + selectedOpt.value + '/all/?key=a3d51706563163f27cbe078b482e25ee&callback=cb';

            function JsonpHttpRequest(url, callback) {
                var e = document.createElement('script');
                e.src = url;
                document.body.appendChild(e);
                window[callback] = (regions) => {
                    console.log("callback");
                    let selectRegions = document.getElementById("regions");
                    selectRegions.innerHTML = "";
                    regions.forEach(elem => {
                        const option = document.createElement("option");
                        option.value = elem.region;
                        option.innerHTML = elem.region;
                        selectRegions.appendChild(option);
                    });
                    let selectCities = document.getElementById("cities");
                    if (selectRegions.innerHTML === "") {
                        selectRegions.hidden = true;
                        selectCities.hidden = true;
                    } else {
                        selectRegions.hidden = false;
                        selectCities.hidden = false;
                        fillCities();
                    }
                }
            }

            JsonpHttpRequest(url, "cb");
        }

        function fillCities() {
            const selectCountries = document.getElementById("countries");
            const selectRegions = document.getElementById("regions");
            const selectedCountry = selectCountries.options[selectCountries.selectedIndex];
            const selectedRegion = selectRegions.options[selectRegions.selectedIndex];
            const url = "https://geo-battuta.net/api/city/" + selectedCountry.value.toString().replace(" ", "+") + "/search/?region=" + selectedRegion.value.toString().replace(" ", "+") + "&key=a3d51706563163f27cbe078b482e25ee&callback=cb";

            function JsonpHttpRequest(url, callback) {
                var e = document.createElement('script');
                e.src = url;
                document.body.appendChild(e);
                window[callback] = (regions) => {
                    let selectCities = document.getElementById("cities");
                    selectCities.innerHTML = "";
                    regions.forEach(elem => {
                        const option = document.createElement("option");
                        option.value = elem.city;
                        option.innerHTML = elem.city;
                        selectCities.appendChild(option);
                    });
                    selectCities.hidden = selectCities.innerHTML === "";
                }
            }

            JsonpHttpRequest(url, "cb");
        }

        function uploadFileAjax() {
            var fileInput = document.getElementById('file');
            var file = fileInput.files[0];
            var formData = new FormData();
            formData.append('file', file);
            var xhr = new XMLHttpRequest();
            // xhr.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=utf-8");
            xhr.onreadystatechange = function() {
                document.getElementById("upload").innerHTML = this.responseText;
                // var slikaPar = document.getElementById("slikaPar");
                // var img = document.createElement("img");
                // img.setAttribute("src", fileInput.value);
                // img.setAttribute("height", "300px");
                // img.setAttribute("width", "400px");
                // slikaPar.innerHTML="";
                // slikaPar.appendChild(img);
            };
            xhr.open('POST', "Controller?controller=upload&action=profilePicture&submit=submit", true);
            xhr.send(formData);
            return false;
        }

        function showImageOfCountry() {
            const selectCountries = document.getElementById("countries");
            const selectedOpt = selectCountries.options[selectCountries.selectedIndex];
            const xhttp = new XMLHttpRequest();
            const url = 'https://restcountries.eu/rest/v2/region/europe';
            xhttp.open('GET', url);
            xhttp.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    let result = JSON.parse(this.responseText);
                    let select = document.getElementById("countries");
                    var link = result.find(elem => elem.alpha2Code===selectedOpt.value).flag;
                    var img = document.createElement("img");
                    img.setAttribute("src", link);
                    img.setAttribute("height", "300px");
                    img.setAttribute("width", "400px");
                    var slikaPar = document.getElementById("slikaPar");
                    slikaPar.innerHTML="";
                    slikaPar.appendChild(img);
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

<label for="countries">Choose a country:</label>
<select id="countries" onchange="fillRegions()">
</select>
<select id="regions" hidden="hidden" onchange="fillCities()">
</select>
<select id="cities" hidden="hidden">
</select><br>
<%--Controller?controller=upload?action=profilePicture--%>
<form id="fileForm" onsubmit="return uploadFileAjax()" method="post"
      enctype="multipart/form-data">
    <input id="file" type="file" name="file" size="1"/>
    <br/>
    <input type="submit" name="submit" value="Upload File"/>
</form>
<p id="slikaPar"></p>
<hr>
    <h2 id="upload"></h2>
<hr>
</body>
</html>
