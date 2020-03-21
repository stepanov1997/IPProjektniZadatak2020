function addProfilePicture() {
    const xhttp = new XMLHttpRequest();
    const url = 'Controller?controller=account&action=picture&submit=submit';

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let result = JSON.parse(this.responseText);
            if (result.exists) {
                var img = new Image();
                img.src = "rest?id=" + result.id;
                document.getElementById("profilePic").appendChild(img);
            } else {
                showImageOfCountry(result.countryCode);
            }
        }
    };
    xhttp.open('POST', url, true);
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
            document.getElementById("profilePic").appendChild(img);
        }
    };
    xhttp.send();
}