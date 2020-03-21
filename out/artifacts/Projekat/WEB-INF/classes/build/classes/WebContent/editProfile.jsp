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

        var id = -1;

        function saveProfilePicture() {
            var fileInput = document.getElementById('file');
            if (fileInput.files.length === 0) {
                return false;
            }
            var file = fileInput.files[0];
            var xhr = new XMLHttpRequest();
            var formData = new FormData;
            formData.append("file", file);
            xhr.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
                    var result = JSON.parse(this.responseText);
                    if (result.success) {
                        id = result.id;
                        var img = new Image();
                        img.onload = function () {
                            document.getElementById("upload").appendChild(img);
                        };
                        img.setAttribute("style", "height:300px;width:400px");
                        img.src = "rest?id=" + result.id;
                        document.getElementById("pic_id").innerText = "Picture_id: " + result.id;
                    }
                }
            };
            var url = "Controller?controller=upload&action=profilePicture&submit=submit";

            xhr.open('POST', url, true);
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
                    var link = result.find(elem => elem.alpha2Code === selectedOpt.value).flag;
                    var img = document.createElement("img");
                    img.setAttribute("src", link);
                    img.setAttribute("height", "300px");
                    img.setAttribute("width", "400px");
                    var slikaPar = document.getElementById("slikaPar");
                    slikaPar.appendChild(img);
                }
            };
            xhttp.send();
        }

        function editProfile() {
            var fileInput = document.getElementById('file');
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
                    var result = JSON.parse(this.responseText);
                    if (result.redirect) {
                        $("#result").html(result.message);
                        setTimeout(function () {
                            window.location = "newsFeed.jsp";
                        }, 2000);
                    } else {
                        $("#result").html(result.message);
                        setTimeout(function () {
                            $("#result").html("");
                        }, 5000);
                    }
                }
            };
            var url = "Controller?controller=account&action=editProfile&submit=submit";
            var selectCountries = document.getElementById("countries");
            var countryValue = selectCountries.options[selectCountries.selectedIndex].value;
            url += "&countries=" + countryValue;
            var selectRegions = document.getElementById("regions");
            if(selectRegions.hidden===false)
            {
                var regionValue = selectRegions.options[selectRegions.selectedIndex].value;
                url += "&regions=" + regionValue;
            }
            var selectCities = document.getElementById("cities");
            if(selectCities.hidden===false) {
                var cityValue = selectCities.options[selectCities.selectedIndex].value;
                url += "&cities=" + cityValue;
            }
            if (fileInput.files.length === 0) {
                url += "&picture=" + false;
            } else {
                url += "&picture=" + true;
            }

            xhr.open('POST', url, true);
            xhr.send();
            return false;
        }
    </script>
</head>
<body>
<p>Name: ${accountBean.account.name}</p>
<p>Surname: ${accountBean.account.surname}</p>
<p>Username: ${accountBean.account.username}</p>
<p>Email: ${accountBean.account.email}</p>
<p id="pic_id"></p>

<form id="fileForm" method="post" onsubmit="return editProfile()">
    <label for="countries">Choose a country:</label>
    <select id="countries" name="countries" onchange="fillRegions()">
    </select>
    <select id="regions" name="regions" hidden="hidden" onchange="fillCities()">
    </select>
    <select id="cities" name="cities" hidden="hidden">
    </select><br>
    <input id="file" type="file" enctype="multipart/form-data" onchange="saveProfilePicture()" name="file" accept="image/*" size="1"/>
    <br/><br/><br/>
    <input type="submit" name="submit" value="Update profile"/>
</form>
<p id="slikaPar"></p>
<hr>
<h2 id="upload"></h2>
<hr>
<h2 id="result"></h2>
</body>
</html>
