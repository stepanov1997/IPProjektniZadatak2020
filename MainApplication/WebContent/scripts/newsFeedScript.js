const days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

function imAlive() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.status === 200 && this.readyState === 4) {
            const elem = JSON.parse(this.responseText);
            if (elem.expires)
                window.location = "newsFeed.jsp"
        }
    };
    xhttp.open('POST', 'Controller?controller=user&action=online&submit=submit', true);
    xhttp.send();
}

setInterval(imAlive, 5000);

function refresh() {
    const top = document.documentElement.scrollTop;
    addPosts();
    addNotifications();
    setTimeout(function () {
        document.documentElement.scrollTop = top;
    }, 1000);
}

function deleteNotification(post_id) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            if (JSON.parse(this.responseText).success) {
                notificationsArray = notificationsArray.filter(notification => notification.post_id !== post_id);
                if (notificationsArray.length === 0) {
                    document.getElementById('notificationDiv').innerText = "You don't have notifications.";
                }
                let notification = document.getElementById(`notification${post_id}`);
                notification.parentNode.removeChild(notification);
            }
        }
    }
    xhttp.open('POST', `Controller?controller=user&action=deleteNotification&submit=submit&post_id=${post_id}`, true);
    xhttp.send();
}

let notificationsArray = [];

function addNotifications() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
            let notifications = JSON.parse(this.responseText);
            let htmlElement = document.getElementById('notificationDiv');
            if (notificationsArray.length === 0) htmlElement.innerHTML = "";
            let difference = arr_diff(notificationsArray, notifications);
            difference.forEach((notification) => {
                notificationsArray.push(notification);
                const text = notification.text;
                const id = notification.id;
                const post_id = notification.post_id;
                let notificationDiv = document.createElement('div');
                notificationDiv.id = `notification${post_id}`;
                let pTag = document.createElement("p");
                pTag.innerHTML = `${text} <br><a id="a${post_id}" href='#post${post_id}' onclick="deleteNotification(${post_id});return true;">Focus post</a><br>`;
                notificationDiv.innerHTML = pTag.outerHTML;
                htmlElement.insertBefore(notificationDiv, htmlElement.firstChild);
            })
            if (notificationsArray.length === 0) {
                htmlElement.innerText = "You don't have notifications.";
            }
        }
    }
    xhttp.open('POST', 'Controller?controller=user&submit=submit&action=notification', true);
    xhttp.send();
}

$(window).load(function () {
    imAlive();

    /* Facebook */
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
let mapAttributes = [];


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
    } catch (e) {
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
                refresh();
            } else {
                alert("greska");
            }
        }
    };
    // var url = "Controller?controller=newsFeed&action=createPost";
    let url = "NewsFeedController?";
    url += "withAttachment=" + true;
    url += "&content=" + fileType;
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
    } catch (e) {
        document.write("Post cannot be created");
    }
    return false;
}

function createPost2() {
    const text2 = document.getElementById("text2");
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
            const result = JSON.parse(this.responseText);
            if (result.success) {
                refresh();
            } else {
                alert("greska");
            }
        }
    };
    let lat = document.getElementById("lat");
    let lng = document.getElementById("lng");
    let isEmergency = document.getElementById("isEmergency");
    let selectCategory = document.getElementById("selectCategory");
    let category = selectCategory.options[selectCategory.selectedIndex].value;
    // var url = "Controller?controller=newsFeed&action=createPost";
    let url = "NewsFeedController?";
    url += "withAttachment=" + false;
    if(document.getElementById("enableMap").checked)
        url += "&location=" + lat.value + "+" + lng.value;
    else
        url += "&location=+";
    url += "&isEmergency=" + isEmergency.checked;
    url += "&category=" + category;
    url += "&text2=" + text2.value;
    xhttp.open('post', url, true);
    try {
        xhttp.send();
    } catch (e) {
        window.alert("Post cannot be created");
    }
    return false;
}

function addPost(elem) {
    let html = "";
    const nameSurname = document.createElement("h2");
    nameSurname.innerText = elem.nameSurname;
    html += nameSurname.outerHTML;

    let img = new Image();
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

    let wrapper = document.createElement("div");
    wrapper.width = "100%";
    wrapper.className = "mediaWrapper";
    if (elem.withAttachment) {
        switch (elem.contentType) {
            case 'link':
                aTag = document.createElement("a");
                aTag.href = elem.value;
                aTag.innerHTML = "Link";
                wrapper.innerHTML += aTag.outerHTML;
                break;
            case 'ytLink':
                const iframe = document.createElement("iframe");
                iframe.className = "media";
                iframe.src = "//www.youtube.com/embed/" + getId(elem.value);
                iframe.setAttribute("frameborder", "0");
                iframe.style.height = "400px";
                iframe.setAttribute("allowfullscreen", "true");
                wrapper.innerHTML += iframe.outerHTML;
                break;
            case 'picture':
                const pic = new Image();
                pic.className = "fakeimg";
                pic.className = "media";
                pic.style.height = "300px";
                pic.style.width = "75%";
                pic.src = "pictures?id=" + elem.value;
                wrapper.innerHTML += pic.outerHTML;
                break;
            case 'video':
                const video = document.createElement("video");
                video.innerHTML = "Your browser does not support the video tag.";
                video.controls = true;
                video.className = "media";
                video.setAttribute("id", "my-video");
                video.setAttribute("class", "video-js");
                video.style.width = "100%";
                video.setAttribute("preload", "auto");
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
                wrapper.innerHTML += video.outerHTML;
                break;
            default:
                break;
        }
        html += wrapper.outerHTML;
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
    } else {

        if (!(elem.location === undefined || elem.location === null || elem.location.length <= 0)) {
            var latt = elem.location.split(' ')[0];
            var lngg = elem.location.split(' ')[1];

            if (latt !== "" && lngg !== "") {
                const mapDiv = document.createElement("div");
                mapDiv.id = "map" + elem.id;
                mapDiv.style = "width: 60%; height: 300px; margin: auto;";
                html += mapDiv.outerHTML;
                mapAttributes.push({latt: latt, lngg: lngg, id: elem.id});
            }
        }

        const categoryDiv = document.createElement('div');
        categoryDiv.innerHTML = "<br><br>Category of potential danger : " + elem.category;
        html += categoryDiv.outerHTML;

        const isEmerDiv = document.createElement('div');
        categoryDiv.innerHTML = "<b>" + (elem.isEmergency ? "This danger is emergency!" : "") + "</b>";
        html += categoryDiv.outerHTML;
    }

    return html;
}

function createComment(comment) {
    let commentHtml = "";

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

    const name = document.createElement("a");
    name.innerHTML = "<br><b>" + comment.nameSurname+"</b>";
    commentHtml += name.outerHTML;

    if (comment.Picture_id !== undefined && comment.Picture_id !== "" && comment.Picture_id !== null) {
        let picWrapper = document.createElement("div");
        picWrapper.className = "mediaDiv";
        const commentPicture = new Image();
        commentPicture.className = "media";
        commentPicture.src = "pictures?id=" + comment.Picture_id;
        picWrapper.innerHTML = commentPicture.outerHTML;
        commentHtml += picWrapper.outerHTML;
    }

    const commentText = document.createElement("h5");
    commentText.innerHTML = comment.comment;
    commentHtml += commentText.outerHTML;

    const date = document.createElement("a");
    date.innerHTML = comment.datetime;
    commentHtml += date.outerHTML;

    return commentHtml;
}

function createCommentInput(id) {
    const commentInput = document.createElement("form");
    commentInput.className = "addCommentForm";
    let commentFormHtml = "";

    const commentTextWrapper = document.createElement("div");
    commentTextWrapper.className = "addCommentTextWrapper"
    const commentText = document.createElement("textarea");
    commentText.className = "addCommentText creds";
    commentText.placeholder = "Enter comment";
    commentText.style.textAlign = "left"
    commentText.id = "inputComment" + id;
    commentTextWrapper.innerHTML += commentText.outerHTML;
    commentFormHtml += commentTextWrapper.outerHTML;

    const inputPic = document.createElement("input");
    inputPic.id = "file" + id;
    inputPic.style.display = "none";
    inputPic.type = "file";
    inputPic.formEnctype = "multipart/form-data";
    commentFormHtml += inputPic.outerHTML;

    const div = document.createElement("div");
    div.className = "insertAttachment"
    const commPic = new Image(15, 15);
    commPic.className = "addCommentImg";
    commPic.id = "img";
    commPic.src = "https://static.xx.fbcdn.net/rsrc.php/v3/yA/r/6C1aT2Hm3x-.png";
    commPic.setAttribute("onclick", "document.getElementById('" + inputPic.id + "').click()");
    div.innerHTML = commPic.outerHTML;
    commentFormHtml += div.outerHTML;

    commentInput.innerHTML = commentFormHtml;

    const buttonWrapper = document.createElement('div');
    buttonWrapper.className = "addCommentButtonWrapper";
    const sendButton = document.createElement("button");
    sendButton.id = "button" + id;
    sendButton.className = "addCommentButton submit-button"
    sendButton.type = "button";
    sendButton.innerHTML = "Send comment";
    sendButton.setAttribute("onclick", "addComment(" + id + ");");

    buttonWrapper.innerHTML = sendButton.outerHTML;
    commentInput.appendChild(buttonWrapper);

    return commentInput.outerHTML;
}

const arr_diff = (a1, a2) => a2.filter(elem => !arr_contains(a1, elem));

function arr_contains(array, elem) {
    for (let i = 0; i < array.length; i++) {
        if (JSON.stringify(array[i]) === JSON.stringify(elem))
            return true;
    }
    return false;
}

let posts = [];

function addPosts() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
            const result = JSON.parse(this.responseText);
            let html = "";
            let outterDiv = null;
            outterDiv = document.getElementById("postsDiv");
            if (outterDiv == null || outterDiv === undefined || outterDiv === "") {
                outterDiv = document.createElement("div");
                outterDiv.id = "postsDiv";
            }
            let difference = arr_diff(posts, result).reverse();
            difference.forEach(post => {
                let oldPost = document.getElementById("post" + post.id);
                if (oldPost !== null && oldPost !== undefined && oldPost !== "")
                    oldPost.parentNode.removeChild(oldPost);
            })
            posts = JSON.parse(JSON.stringify(result));
            difference.forEach(function (elem) {
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
                    div.id = "post" + elem.id;
                    html += addPost(elem);

                    const comments = document.createElement("div");
                    comments.className = "card";
                    comments.style.background = "#dddddd";
                    elem.comments.forEach(function (comment) {
                        let commentDiv = document.createElement("div");
                        commentDiv.className = "card";
                        commentDiv.innerHTML = createComment(comment);
                        comments.appendChild(commentDiv);
                    });

                    let commentDiv = document.createElement("div");
                    commentDiv.id = "commentDiv" + elem.id;
                    commentDiv.className = "card";

                    commentDiv.innerHTML = createCommentInput(elem.id);

                    comments.appendChild(commentDiv);

                    html += comments.outerHTML;
                }
                div.innerHTML = html;
                outterDiv.insertBefore(div, outterDiv.firstChild);
            });
            const createPost = document.getElementById("posts");
            createPost.appendChild(outterDiv);

            mapAttributes.forEach(e => {
                initMap(parseFloat(e.latt), parseFloat(e.lngg), parseInt(e.id));
            });
            mapAttributes = [];
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
            const comment = JSON.parse(this.responseText);
            var cmnt = document.createElement("div");
            cmnt.className = "card";
            cmnt.innerHTML = createComment(comment);
            div.parentNode.insertBefore(cmnt, div);
            document.getElementById('inputComment'+id).innerText="";
            document.getElementById('file'+id).innerHTML="";
        }
    };
    let url = "comment?Post_Id=" + id + "&text=" + text;
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
    } catch (e) {
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

function initMap(latt, lngg, mapId) {
    mapboxgl.accessToken = 'pk.eyJ1Ijoia2lraWtpa2kxOTkyIiwiYSI6ImNrOHoza2ZqejBhbGQzZGxjeGIxNWM0YnoifQ.3FoukhI7DUYFqV4W63mi6w';
    var map = new mapboxgl.Map({
        container: 'map' + mapId, // container id
        style: 'mapbox://styles/mapbox/streets-v11',
        center: [lngg, latt], // starting position
        zoom: 15 // starting zoom
    });
    var marker = new mapboxgl.Marker({
        draggable: false
    })
        .setLngLat([lngg, latt])
        .addTo(map);

    map.addControl(new mapboxgl.NavigationControl());

    //map.flyTo({center:[lngg, latt]});
}