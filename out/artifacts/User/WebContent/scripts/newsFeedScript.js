const days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

function imAlive()
{
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if(this.status===200 && this.readyState===4){
            const elem = JSON.parse(this.responseText);
            if(elem.expires)
                window.location = "newsFeed.jsp"
        }
    };
    xhttp.open('POST', 'Controller?controller=user&action=online&submit=submit', true);
    xhttp.send();
}

setInterval(imAlive, 5000);

$(window).load(function () {
    imAlive();

    (function (d, s, id) {
        let js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) return;
        js = d.createElement(s);
        js.id = id;
        js.async = true;
        js.src = "https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v3.0";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

    /* Twitter */
    !function (d, s, id) {
        let js, fjs = d.getElementsByTagName(s)[0], p = /^http:/.test(d.location) ? 'http' : 'https';
        if (!d.getElementById(id)) {
            js = d.createElement(s);
            js.id = id;
            js.src = p + '://platform.twitter.com/widgets.js';
            fjs.parentNode.insertBefore(js, fjs);
        }
    }(document, 'script', 'twitter-wjs');
});

function addProfilePicture() {
    const xhttp = new XMLHttpRequest();
    const url = 'Controller?controller=user&action=picture&submit=submit';

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let result = JSON.parse(this.responseText);
            if (result.exists) {
                const img = new Image();
                img.src = "pictures?id=" + result.id;
                document.getElementById("profilePic").appendChild(img);
            } else {
                showImageOfCountry(result.countryCode);
            }
        }
    };
    xhttp.open('POST', url, true);
    xhttp.send();
}

let fileType = "";
let linkType = "";

function getId(url) {
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|&v=)([^#&?]*).*/;
    const match = url.match(regExp);

    if (match && match[2].length === 11) {
        return match[2];
    } else {
        return 'error';
    }
}

function showImageOfCountry(countryCode) {
    const xhttp = new XMLHttpRequest();
    const url = 'https://restcountries.eu/rest/v2/region/europe';
    xhttp.open('GET', url);
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let result = JSON.parse(this.responseText);
            const flagLink = result.find(elem => elem.alpha2Code === countryCode).flag;
            const img = new Image();
            img.src = flagLink;
            document.getElementById("profilePic").appendChild(img);
        }
    };
    try {
        xhttp.send();
    }
    catch (e) {
        document.write("Flag of country is missing.")
    }
}

function createPost1() {
    const formData = new FormData();
    const fileInput = document.getElementById("file-input");
    if (fileInput.files.length !== 0) {
        const file = fileInput.files[0];
        formData.append("file", file);
    }
    const ytLink = document.getElementById("yt-link-input");
    const link = document.getElementById("link-input");
    const text = document.getElementById("text");
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
            const result = JSON.parse(this.responseText);
            if (result.success) {
                let divHtml = "";
                const div = document.createElement("div");

                const ptag = document.createElement("p");
                ptag.innerText = "Datetime: " + result.dateTime.day + "." + result.dateTime.month + "." + result.dateTime.year + ".";
                divHtml += ptag.outerHTML;

                const profile = new Image();
                profile.style.width = "50px";
                profile.style.height = "50px";
                if (result.Picture_id === undefined || result.Picture_id === "") {
                    const img = document.getElementById("profilePic").firstChild;
                    const flagLink = img.getAttribute("src");
                    profile.setAttribute("src", flagLink);
                } else {
                    profile.setAttribute("src", "pictures?id=" + result.Picture_id);
                }
                divHtml += profile.outerHTML;

                let pTag = document.createElement("p");
                pTag.innerHTML = "Text: " + result.text;
                divHtml += pTag.outerHTML;
                let aTag;

                switch (result.contentType) {
                    case 'link':
                        aTag = document.createElement("a");
                        aTag.href = result.value;
                        aTag.innerHTML = "Link";
                        divHtml += aTag.outerHTML;
                        break;
                    case 'ytLink':
                        const iframe = document.createElement("iframe");
                        iframe.style.width = "560px";
                        iframe.style.height = "315px";
                        iframe.src = "//www.youtube.com/embed/" + getId(result.value);
                        iframe.setAttribute("frameborder", "0");
                        iframe.setAttribute("allowfullscreen", "true");
                        divHtml += iframe.outerHTML;
                        break;
                    case 'picture':
                        const pic = new Image();
                        pic.className = "fakeimg";
                        pic.style.height = "300px";
                        pic.style.width = "533px";
                        pic.src = "pictures?id=" + result.value;
                        divHtml += pic.outerHTML;
                        break;
                    case 'video':
                        const video = document.createElement("video");
                        video.innerHTML = "Your browser does not support the video tag.";
                        video.controls = true;
                        video.setAttribute("id", "my-video");
                        video.setAttribute("class", "video-js");
                        video.setAttribute("preload", "auto");
                        video.setAttribute("width", "640px");
                        video.setAttribute("height", "284");
                        video.setAttribute("data-setup", "{}");
                        const src = document.createElement("source");
                        src.setAttribute("src", "videos?id=" + result.value);
                        src.setAttribute("type", "video/mp4");
                        video.innerHTML = src.outerHTML;
                        pTag = document.createElement("p");
                        pTag.setAttribute("class", "vjs-no-js");
                        pTag.innerText = "To view this video please enable JavaScript, and consider upgrading to a web browser that";
                        aTag = document.createElement("a");
                        aTag.setAttribute("href", "https://videojs.com/html5-video-support/");
                        aTag.setAttribute("target", "_blank");
                        aTag.innerText = "supports HTML5 video";
                        pTag.appendChild(aTag);
                        video.appendChild(pTag);
                        divHtml += video.outerHTML;
                        break;
                    default:
                        break;
                }
                div.innerHTML = divHtml;
                const createPost = document.getElementById("createPost2");
                div.setAttribute("class", "card");
                createPost.parentNode.insertBefore(div, createPost.nextSibling);
            } else {
                alert("greska");
            }
        }
    };
    // var url = "Controller?controller=newsFeed&action=createPost";
    let url = "NewsFeedController?";
    url += "content=" + fileType;
    url += "&isYoutubeLink=" + linkType;
    if (ytLink !== null && ytLink.value !== "") {
        url += "&ytLink=" + ytLink.value;
    }
    if (link !== null && link.value !== "") {
        url += "&link=" + link.value;
    }
    url += "&text=" + text.value;
    xhttp.open('post', url, true);
    try {
        if (formData.entries().length !== 0)
            xhttp.send(formData);
        else
            xhttp.send();
    }
    catch (e) {
        document.write("Post cannot be created");
    }
    return false;
}

function addPosts() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
            const result = JSON.parse(this.responseText);
            let html = "";
            const outterDiv = document.createElement("div");
            outterDiv.id = "postsDiv";
            result.forEach(function (elem) {
                let date;
                let img;
                html = "";
                const div = document.createElement("div");
                div.style.alignContent = "center";
                div.setAttribute("class", "card");
                if (elem.isRss) {
                    const title = document.createElement("h2");
                    title.innerText = elem.title;
                    html += title.outerHTML;

                    date = document.createElement("h5");
                    date.innerText = "Date: " + elem.date;
                    html += date.outerHTML;

                    const description = document.createElement("p");
                    description.innerText = elem.description;
                    html += description.outerHTML;

                    const link = document.createElement("a");
                    link.href = elem.link;
                    html += link.outerHTML;
                } else {
                    div.id = elem.id;
                    const nameSurname = document.createElement("h2");
                    nameSurname.innerText = elem.nameSurname;
                    html += nameSurname.outerHTML;

                    img = new Image();
                    img.className = "fakeimg";
                    img.style.height = "50px";
                    img.style.width = "50px";
                    if (elem.Picture_id === undefined || elem.Picture_id === "" || elem.Picture_id === null) {
                        let resultJSON = JSON.parse(
                            $.ajax({
                                type: "GET",
                                url: 'https://restcountries.eu/rest/v2/region/europe',
                                async: false
                            }).responseText);
                        img.src = resultJSON.find(country => country.alpha2Code === elem.countryCode).flag;
                    } else {
                        img.src = "pictures?id=" + elem.Picture_id;
                    }
                    html += img.outerHTML;

                    let pTag = document.createElement("p");
                    pTag.innerHTML = "Text: " + elem.text;
                    html += pTag.outerHTML;
                    let aTag;

                    switch (elem.contentType) {
                        case 'link':
                            aTag = document.createElement("a");
                            aTag.href = elem.value;
                            aTag.innerHTML = "Link";
                            html += aTag.outerHTML;
                            break;
                        case 'ytLink':
                            const iframe = document.createElement("iframe");
                            iframe.style.alignSelf = "center";
                            iframe.style.width = "75%";
                            iframe.style.height = "300px";
                            iframe.src = "//www.youtube.com/embed/" + getId(elem.value);
                            iframe.setAttribute("frameborder", "0");
                            iframe.setAttribute("allowfullscreen", "true");
                            html += iframe.outerHTML;
                            break;
                        case 'picture':
                            const pic = new Image();
                            pic.className = "fakeimg";
                            pic.style.height = "300px";
                            pic.style.width = "533px";
                            pic.src = "pictures?id=" + elem.value;
                            html += pic.outerHTML;
                            break;
                        case 'video':
                            const video = document.createElement("video");
                            video.innerHTML = "Your browser does not support the video tag.";
                            video.controls = true;
                            video.setAttribute("id", "my-video");
                            video.setAttribute("class", "video-js");
                            video.setAttribute("preload", "auto");
                            video.setAttribute("width", "640px");
                            video.setAttribute("height", "284");
                            video.setAttribute("data-setup", "{}");
                            const src = document.createElement("source");
                            src.setAttribute("src", "videos?id=" + elem.value);
                            src.setAttribute("type", "video/mp4");
                            video.innerHTML = src.outerHTML;
                            pTag = document.createElement("p");
                            pTag.setAttribute("class", "vjs-no-js");
                            pTag.innerText = "To view this video please enable JavaScript, and consider upgrading to a web browser that";
                            aTag = document.createElement("a");
                            aTag.setAttribute("href", "https://videojs.com/html5-video-support/");
                            aTag.setAttribute("target", "_blank");
                            aTag.innerText = "supports HTML5 video";
                            pTag.appendChild(aTag);
                            video.appendChild(pTag);
                            html += video.outerHTML;
                            break;
                        default:
                            break;
                    }
                    date = document.createElement("h5");
                    date.innerText = "Date: " + elem.date;
                    html += date.outerHTML;

                    if (elem.contentType === 'link' || elem.contentType === 'ytLink') {
                        const tweet = document.createElement("a");
                        tweet.href = "https://twitter.com/share";
                        tweet.setAttribute("class", "twitter-share-button");
                        tweet.setAttribute("data-url", elem.link);
                        tweet.setAttribute("data-hashtags", elem.text);
                        tweet.innerText = "Tweet";
                        html += tweet.outerHTML;

                        const fbshare = document.createElement("div");
                        fbshare.onclick = function () {
                            $('meta[property="og:description"]').attr('content', elem.text);
                            const metaUrl = $('meta[property="og:url"]');
                            switch (elem.contentType) {
                                case 'ytLink':
                                case 'link':
                                    metaUrl.attr('content', elem.value);
                                    break;
                                case 'picture':
                                    $('meta[property="og:image"]').attr('content', "pictures?id=" + elem.value);
                                    break;
                                case 'video':
                                    $('meta[property="og:video"]').attr('content', "pictures?id=" + elem.value);
                                    break;
                                default:
                                    break;
                            }
                        };
                        fbshare.setAttribute("class", "fb-share-button");
                        switch (elem.contentType) {
                            case 'link':
                            case 'ytLink':
                                fbshare.setAttribute("data-href", elem.value);
                                break;
                            case 'picture':
                                fbshare.setAttribute("data-href", "pictures?id=" + elem.value);
                                break;
                            case 'video':
                                fbshare.setAttribute("data-href", "pictures?id=" + elem.value);
                                break;
                            default:
                                break;
                        }
                        fbshare.setAttribute("data-layout", "button_count");
                        html += fbshare.outerHTML;
                    }

                    const comments = document.createElement("div");
                    comments.className = "card";
                    comments.style.background = "gray";
                    elem.comments.forEach(function (comment) {
                        let commentDiv = document.createElement("div");
                        commentDiv.className = "card";
                        let commentHtml = "";
                        const name = document.createElement("a");
                        name.innerHTML = comment.nameSurname;
                        commentHtml += name.outerHTML;

                        const pic = new Image(50, 50);
                        pic.className = "fakeimg";
                        if (comment.ProfilePic_id === undefined || comment.ProfilePic_id === "" || comment.ProfilePic_id === null) {
                            let result = JSON.parse(
                                $.ajax({
                                    type: "GET",
                                    url: 'https://restcountries.eu/rest/v2/region/europe',
                                    async: false
                                }).responseText);
                            pic.src = result.find(country => country.alpha2Code === comment.countryCode).flag;
                        } else {
                            pic.src = "pictures?id=" + comment.ProfilePic_id;
                        }
                        commentHtml += pic.outerHTML;

                        if (comment.Picture_id !== undefined && comment.Picture_id !== "" && comment.Picture_id !== null) {
                            const commentPicture = new Image(160, 90);
                            commentPicture.src = "pictures?id=" + comment.Picture_id;
                            commentHtml += commentPicture.outerHTML;
                        }

                        const commentText = document.createElement("h5");
                        commentText.innerHTML = comment.comment;
                        commentHtml += commentText.outerHTML;

                        const date = document.createElement("a");
                        date.innerHTML = comment.datetime;
                        commentHtml += date.outerHTML;

                        commentDiv.innerHTML = commentHtml;
                        comments.appendChild(commentDiv);
                    });

                    let commentDiv = document.createElement("div");
                    commentDiv.id = "commentDiv" + elem.id;
                    commentDiv.className = "card";

                    const commentInput = document.createElement("form");
                    let commentFormHtml = "";
                    commentInput.className = "card";

                    const text = document.createElement("p");
                    text.innerHTML = "Type comment: ";
                    commentFormHtml += text.outerHTML;
                    const commentText = document.createElement("input");
                    commentText.id = "inputComment" + elem.id;
                    commentText.type = "text";
                    commentFormHtml += commentText.outerHTML;

                    const inputPic = document.createElement("input");
                    inputPic.id = "file" + elem.id;
                    inputPic.style.display = "none";
                    inputPic.type = "file";
                    inputPic.formEnctype = "multipart/form-data";
                    commentFormHtml += inputPic.outerHTML;

                    const commPic = new Image(50, 50);
                    commPic.id = "img";
                    commPic.src = "https://static.xx.fbcdn.net/rsrc.php/v3/yA/r/6C1aT2Hm3x-.png";
                    commPic.setAttribute("onclick", "document.getElementById('" + inputPic.id + "').click()");
                    commentFormHtml += commPic.outerHTML;

                    commentInput.innerHTML = commentFormHtml;

                    const sendButton = document.createElement("button");
                    sendButton.id = "button" + elem.id;
                    sendButton.type = "button";
                    sendButton.innerHTML = "Send comment";
                    sendButton.setAttribute("onclick", "addComment(" + elem.id + ");");

                    commentInput.appendChild(sendButton);

                    commentDiv.innerHTML = commentInput.outerHTML;

                    comments.appendChild(commentDiv);

                    html += comments.outerHTML;
                }
                div.innerHTML = html;
                div.id = elem.id;
                outterDiv.appendChild(div);
            });
            const createPost = document.getElementById("createPost2");
            createPost.parentNode.insertBefore(outterDiv, createPost.nextSibling);
        }
    };
    const url = "posts";
    xhttp.open('POST', url, true);
    try {
        xhttp.send();
    } catch (e) {
        document.write("Posts cannot be added");
    }

}

function addComment(id) {
    const xhttp = new XMLHttpRequest();
    const text = document.getElementById("inputComment" + id).value;
    const div = document.getElementById("commentDiv" + id);
    const inputPic = document.getElementById("file" + id);
    let formData;
    if (inputPic.files.length !== 0) {
        formData = new FormData();
        const file = inputPic.files[0];
        formData.append("file", file);
    }
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
            const commentObj = JSON.parse(this.responseText);

            let cmnt = document.createElement("div");
            cmnt.className = "card";
            let cmntHtml = "";
            const nameCmnt = document.createElement("a");
            nameCmnt.innerHTML = commentObj.nameSurname;
            cmntHtml += nameCmnt.outerHTML;

            const picCmnt = new Image(50, 50);
            picCmnt.className = "fakeimg";
            if (commentObj.ProfilePic_id === undefined || commentObj.ProfilePic_id === "" || commentObj.ProfilePic_id === null) {
                let result = JSON.parse(
                    $.ajax({
                        type: "GET",
                        url: 'https://restcountries.eu/rest/v2/region/europe',
                        async: false
                    }).responseText);
                picCmnt.src = result.find(country => country.alpha2Code === commentObj.countryCode).flag;
            } else {
                picCmnt.src = "pictures?id=" + commentObj.ProfilePic_id;
            }
            cmntHtml += picCmnt.outerHTML;

            if (commentObj.Picture_id !== undefined && commentObj.Picture_id !== "" && commentObj.Picture_id !== null) {
                const commentPicture = new Image(160, 90);
                commentPicture.src = "pictures?id=" + commentObj.Picture_id;
                cmntHtml += commentPicture.outerHTML;
            }

            const commentText = document.createElement("h5");
            commentText.innerHTML = commentObj.comment;
            cmntHtml += commentText.outerHTML;

            const date = document.createElement("a");
            date.innerHTML = commentObj.datetime;
            cmntHtml += date.outerHTML;

            cmnt.innerHTML = cmntHtml;
            div.parentNode.insertBefore(cmnt, div);
        }
    };
    let url = "/comment?Post_Id=" + id + "&text=" + text;
    if (inputPic.files.length !== 0) {
        url += "&withImage=true";
    } else {
        url += "&withImage=false";
    }
    xhttp.open('POST', url, true);
    try {
        if (inputPic.files.length !== 0) {
            xhttp.send(formData);
        } else {
            xhttp.send();
        }
    } catch (e) {
        document.write("Comment cannot be added");
    }

    return false;
}

let ytFlag = false;
let nonYtFlag = false;

function addLink(isYTLink) {
    let link;
    let aTag;
    let linkBox;
    let pTag;
    let text;
    let elem;
    if (isYTLink) {
        if (ytFlag) {
            // brisanje yt linka
            linkType = "";
            document.getElementById("yt-link").style.background = "white";
            elem = document.getElementById('plink');
            elem.setAttribute("class", "removeChild");
            elem.style.opacity = '0';
            setTimeout(function () {
                elem.parentNode.removeChild(elem);
            }, 400);
            ytFlag = false;
        } else {
            linkType = "youtube";
            ytFlag = true;
            if (nonYtFlag) {
                addLink(false);
            }
            // dodavanje yt linka
            document.getElementById("yt-link").style.background = "gray";
            text = document.getElementById("text");
            pTag = document.createElement("p");
            pTag.setAttribute("class", "appendChild");
            pTag.setAttribute("id", "plink");
            linkBox = document.createElement("input");
            linkBox.setAttribute("id", "yt-link-input");
            linkBox.setAttribute("type", "text");
            aTag = document.createElement("a");
            aTag.innerText = "Youtube link: ";
            pTag.appendChild(aTag);
            pTag.appendChild(linkBox);
            text.parentNode.insertBefore(pTag, text.nextSibling);
        }
    } else {
        if (nonYtFlag) {
            // remove link
            linkType = "";
            link = document.getElementById("link");
            link.style.background = "white";
            elem = document.getElementById('plink');
            elem.setAttribute("class", "removeChild");
            elem.style.opacity = '0';
            setTimeout(function () {
                elem.parentNode.removeChild(elem);
            }, 400);
            nonYtFlag = false;
        } else {
            // add link
            linkType = "classic";
            nonYtFlag = true;
            if (ytFlag) {
                addLink(true);
            }
            link = document.getElementById("link");
            link.style.background = "gray";
            text = document.getElementById("text");
            pTag = document.createElement("p");
            pTag.setAttribute("class", "appendChild");
            pTag.setAttribute("id", "plink");
            linkBox = document.createElement("input");
            linkBox.setAttribute("id", "link-input");
            linkBox.setAttribute("type", "text");
            aTag = document.createElement("a");
            aTag.innerText = "Link: ";
            pTag.appendChild(aTag);
            pTag.appendChild(linkBox);
            text.parentNode.insertBefore(pTag, text.nextSibling);
        }
    }
}

function addWeatherForcast() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.status === 200 && this.readyState === 4) {
            const result = JSON.parse(this.responseText);

            displayWeatherDataForCity(result[0], "myCity");
            displayWeatherDataForCity(result[1], "city1");
            displayWeatherDataForCity(result[2], "city2");
        }
    };
    const url = "Controller?controller=user&submit=submit&action=weather";
    xhttp.open('POST', url, true);
    try {
        xhttp.send();
    }
    catch (e) {
        document.write("The weather forecast is not accessible right now.")
    }
}

function displayWeatherDataForCity(cityObj, id) {
    const div = document.getElementById(id);
    let html = "";

    const city = cityObj.city;
    const h1 = document.createElement("h1");
    h1.innerText = city;
    html += h1.outerHTML + "<br>";

    const div2 = document.createElement("div");
    div2.className = "weathers";
    let weatherDivHtml = "";
    let index = 0;

    const weathers = cityObj.weather;
    weathers.forEach(weather => {
        const innerDiv = document.createElement("div");
        innerDiv.className = "weatherDay";

        const p1 = document.createElement("p");
        p1.innerHTML = "<b>" + (weather.main.temp - 273.15).toFixed(0) + " °C</b>";
        innerDiv.appendChild(p1);

        const icon = document.createElement("img");
        icon.src = "http://openweathermap.org/img/wn/" + weather.weather[0].icon + "@2x.png";
        if (index > 0) {
            icon.style.width = "70px";
            icon.style.height = "70px";
        } else {
            icon.style.width = "100px";
            icon.style.height = "100px";
        }
        index++;

        innerDiv.appendChild(icon);

        const p2 = document.createElement("p");
        p2.innerText = weather.weather[0].description;
        innerDiv.appendChild(p2);

        const p3 = document.createElement("p");
        const date = new Date(Date.parse(weather.dt_txt.substr(0, 10)));
        let day;
        const today = new Date().getDay();
        const tomorrow = (today + 1) % 7;
        const afterTomorrow = (today + 2) % 7;
        switch (date.getDay()) {
            case today:
                day = "Today";
                break;
            case tomorrow:
                day = "Tomorrow";
                break;
            case afterTomorrow:
                day = "Day after tomorrow";
                break;
            default:
                day = days[date.getDay()];
                break;
        }
        p3.innerHTML = date.getDate() + ". " + (date.getMonth() + 1) + ". " + date.getFullYear() + ". (" + day + ")";
        innerDiv.appendChild(p3);
        weatherDivHtml += innerDiv.outerHTML;
    });
    div2.innerHTML = weatherDivHtml;
    html += div2.outerHTML;

    div.innerHTML = html;
}

function showTab(id, cityName) {
    let i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(cityName).style.display = "block";
    document.getElementById(id).className += " active";

}