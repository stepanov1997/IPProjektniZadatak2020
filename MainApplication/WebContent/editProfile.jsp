<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" %>

<%  if(session.getAttribute("userBean") == null)
{   %>
<h2>ACCESS DENIED</h2>
<h3>Only logged in or registered user has access.</h3>
<%      return;
}   %>

<jsp:useBean id="userBean" scope="session" type="model.beans.UserBean"/>
<html>
<head>
    <title>EDIT PROFILE</title>
    <link rel="ICON" href="https://scontent.fbeg4-1.fna.fbcdn.net/v/t1.0-9/54255431_645793952539379_1611586770158223360_o.jpg?_nc_cat=110&_nc_sid=09cbfe&_nc_ohc=zDb83HnW2FoAX-AuhRZ&_nc_ht=scontent.fbeg4-1.fna&oh=da1701f4c2fa67f6a3bad35766e337e7&oe=5E9CEBBE" type="image/jpg" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
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
            const url = 'http://battuta.medunes.net/api/region/' + selectedOpt.value + '/all/?key=e5b09e49fe0202afb9f113fff493b701&callback=cb';

            function JsonpHttpRequest(url, callback) {
                const e = document.createElement('script');
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
            const url = "https://geo-battuta.net/api/city/" + selectedCountry.value.toString().replace(" ", "+") + "/search/?region=" + selectedRegion.value.toString().replace(" ", "+") + "&key=e5b09e49fe0202afb9f113fff493b701&callback=cb";

            function JsonpHttpRequest(url, callback) {
                const e = document.createElement('script');
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

        let id = -1;

        function saveProfilePicture() {
            const fileInput = document.getElementById('file');
            if (fileInput.files.length === 0) {
                return false;
            }
            const file = fileInput.files[0];
            const xhr = new XMLHttpRequest();
            const formData = new FormData;
            formData.append("file", file);
            xhr.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
                    const result = JSON.parse(this.responseText);
                    if (result.success) {
                        id = result.id;
                        const img = new Image();
                        img.onload = function () {
                            document.getElementById("upload").appendChild(img);
                        };
                        img.setAttribute("style", "height:300px;width:400px");
                        img.src = "pictures?id=" + result.id;
                        document.getElementById("pic_id").innerText = "Picture_id: " + result.id;
                    }
                }
            };
            const url = "Controller?controller=upload&action=profilePicture&submit=submit";

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
                    const link = result.find(elem => elem.alpha2Code === selectedOpt.value).flag;
                    const img = document.createElement("img");
                    img.setAttribute("src", link);
                    img.setAttribute("height", "300px");
                    img.setAttribute("width", "400px");
                    const slikaPar = document.getElementById("slikaPar");
                    slikaPar.appendChild(img);
                }
            };
            xhttp.send();
        }

        function editProfile() {
            const fileInput = document.getElementById('file');
            const xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
                    const result = JSON.parse(this.responseText);
                    $("#result").html(result.message);
                    if (result.redirect) {
                        setTimeout(function () {
                            window.location = "login.jsp";
                        }, 2000);
                    } else {
                        setTimeout(function () {
                            $("#result").html("");
                        }, 5000);
                    }
                }
            };
            let url = "Controller?controller=user&action=editProfile&submit=submit";
            const selectCountries = document.getElementById("countries");
            const country = selectCountries.options[selectCountries.selectedIndex];
            url += "&country=" + country.innerHTML;
            url += "&countryCode=" + country.value;
            const selectRegions = document.getElementById("regions");
            if(selectRegions.hidden===false)
            {
                const regionValue = selectRegions.options[selectRegions.selectedIndex].value;
                url += "&region=" + regionValue;
            }
            const selectCities = document.getElementById("cities");
            if(selectCities.hidden===false) {
                const cityValue = selectCities.options[selectCities.selectedIndex].value;
                url += "&city=" + cityValue;
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

<p>Name: ${userBean.user.name}</p>
<p>Surname: ${userBean.user.surname}</p>
<p>Username: ${userBean.user.username}</p>
<p>Email: ${userBean.user.email}</p>
<p id="pic_id"></p>

<form id="fileForm" method="post" onsubmit="return editProfile()">
    <label for="countries">Choose a country:</label>
    <select id="countries" name="countries" onchange="fillRegions()">
    </select>
    <label for="regions"></label><select id="regions" name="regions" hidden="hidden" onchange="fillCities()">
    </select>
    <label for="cities"></label><select id="cities" name="cities" hidden="hidden">
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
