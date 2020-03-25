$(window).load(function () {
    (function (d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) return;
        js = d.createElement(s);
        js.id = id;
        js.async = true;
        js.src = "https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v3.0";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

    /* Twitter */
    !function (d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/.test(d.location) ? 'http' : 'https';
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

var fileType = "";
var linkType = "";

function getId(url) {
    var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    var match = url.match(regExp);

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
            var flagLink = result.find(elem => elem.alpha2Code === countryCode).flag;
            var img = new Image();
            img.src = flagLink;
            document.getElementById("profilePic").appendChild(img);
        }
    };
    xhttp.send();
}

function createPost() {
    var formData = new FormData();
    var fileInput = document.getElementById("file-input");
    if (fileInput.files.length !== 0) {
        var file = fileInput.files[0];
        formData.append("file", file);
    }
    var ytLink = document.getElementById("yt-link-input");
    var link = document.getElementById("link-input");
    var text = document.getElementById("text");
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
            var result = JSON.parse(this.responseText);
            if (result.success) {
                var divHtml = "";
                var div = document.createElement("div");

                var ptag = document.createElement("p");
                ptag.innerText = "Datetime: " + result.dateTime.day + "." + result.dateTime.month + "." + result.dateTime.year + ".";
                divHtml += ptag.outerHTML;

                var profile = new Image();
                profile.style.width = "50px";
                profile.style.height = "50px";
                if (result.Picture_id === undefined || result.Picture_id === "") {
                    var img = document.getElementById("profilePic").firstChild;
                    var flagLink = img.getAttribute("src");
                    profile.setAttribute("src", flagLink);
                } else {
                    profile.setAttribute("src", "rest?id=" + result.Picture_id);
                }
                divHtml += profile.outerHTML;
                switch (result.contentType) {
                    case 'textOnly':
                        var pTag = document.createElement("p");
                        pTag.innerHTML = "Text: " + result.text;
                        divHtml += pTag.outerHTML;
                        break;
                    case 'link':
                        var aTag = document.createElement("a");
                        aTag.href = result.value;
                        aTag.innerHTML = "Link";
                        divHtml += aTag.outerHTML;
                        break;
                    case 'ytLink':
                        var iframe = document.createElement("iframe");
                        iframe.style.width = "560px";
                        iframe.style.height = "315px";
                        iframe.src = "//www.youtube.com/embed/" + getId(result.value);
                        iframe.setAttribute("frameborder", "0");
                        iframe.setAttribute("allowfullscreen", true);
                        divHtml += iframe.outerHTML;
                        break;
                    case 'picture':
                        alert("slika ne radi");
                        break;
                    case 'video':
                        alert("video ne radi");
                        break;
                    default:
                        break;
                }
                div.innerHTML = divHtml;
                var createPost = document.getElementById("createPost");
                div.setAttribute("class", "card");
                createPost.parentNode.insertBefore(div, createPost.nextSibling);
            } else {
                alert("greska");
            }
        }
    };
    // var url = "Controller?controller=newsFeed&action=createPost";
    var url = "NewsFeedController?";
    url += "isVideo=" + fileType;
    url += "&isYoutubeLink=" + linkType;
    if (ytLink !== null && ytLink.value !== "") {
        url += "&ytLink=" + ytLink.value;
    }
    if (link !== null && link.value !== "") {
        url += "&link=" + link.value;
    }
    url += "&text=" + text.value;
    xhttp.open('post', url, true);

    if (formData.entries().length !== 0)
        xhttp.send(formData);
    else
        xhttp.send();
    return false;
}

function addPosts() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200 && this.responseText !== "") {
            let elem;
            var result = JSON.parse(this.responseText);
            var html = "";
            var outterDiv = document.createElement("div");
            result.forEach(function (elem) {
                    let img;
                html = "";
                    var div = document.createElement("div");
                    div.setAttribute("class", "card");
                    if (elem.isRss) {
                        const title = document.createElement("h2");
                        title.innerText = elem.title;
                        html += title.outerHTML;

                        var date = document.createElement("h5");
                        date.innerText = "Date: " + elem.date;
                        html += date.outerHTML;

                        var description = document.createElement("p");
                        description.innerText = elem.description;
                        html += description.outerHTML;

                        var link = document.createElement("a");
                        link.href = elem.link;
                        html += link.outerHTML;
                    } else {
                        var nameSurname = document.createElement("h2");
                        nameSurname.innerText = elem.nameSurname;
                        html += nameSurname.outerHTML;

                        img = new Image();
                        img.className = "fakeimg";
                        img.style.height = "50px";
                        img.style.width = "50px";
                        if (result.Picture_id === undefined || result.Picture_id === "") {
                            let result = JSON.parse(
                                $.ajax({
                                    type: "GET",
                                    url: 'https://restcountries.eu/rest/v2/region/europe',
                                    async: false
                                }).responseText);
                            var flagLink = result.find(country => country.alpha2Code === elem.countryCode).flag;
                            img.src = flagLink;
                        } else {
                            img.src = "rest?id=" + result.Picture_id;
                        }
                        html += img.outerHTML;

                        var pTag = document.createElement("p");
                        pTag.innerHTML = "Text: " + elem.text;
                        html += pTag.outerHTML;

                        switch (elem.contentType) {
                            case 'link':
                                var aTag = document.createElement("a");
                                aTag.href = elem.value;
                                aTag.innerHTML = "Link";
                                html += aTag.outerHTML;
                                break;
                            case 'ytLink':
                                var iframe = document.createElement("iframe");
                                iframe.style.width = "560px";
                                iframe.style.height = "315px";
                                iframe.src = "//www.youtube.com/embed/" + getId(elem.value);
                                iframe.setAttribute("frameborder", "0");
                                iframe.setAttribute("allowfullscreen", true);
                                html += iframe.outerHTML;
                                break;
                            case 'picture':
                                var pic = new Image();
                                pic.className = "fakeimg";
                                pic.style.height = "300px";
                                pic.style.width = "533px";
                                pic.src = "rest?id=" + elem.value;
                                html += pic.outerHTML;
                                break;
                            case 'video':
                                var video = document.createElement("video");
                                video.controls = true;
                                video.setAttribute("id", "my-video");
                                video.setAttribute("class", "video-js");
                                video.setAttribute("preload", "auto");
                                video.setAttribute("width", "640px");
                                video.setAttribute("height", "284");
                                video.setAttribute("poster", "MY_VIDEO_POSTER.jpg");
                                video.setAttribute("data-setup", "{}");
                                var src = document.createElement("source");
                                src.setAttribute("src", "rest?id=" + elem.value);
                                var pTag = document.createElement("p");
                                pTag.setAttribute("class","vjs-no-js");
                                pTag.innerText = "To view this video please enable JavaScript, and consider upgrading to a web browser that";
                                var aTag = document.createElement("a");
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
                        var date = document.createElement("h5");
                        date.innerText = "Date: " + elem.date;
                        html += date.outerHTML;

                        if(elem.contentType==='link' || elem.contentType==='ytLink') {
                            var tweet = document.createElement("a");
                            tweet.href = "https://twitter.com/share";
                            tweet.setAttribute("class", "twitter-share-button");
                            tweet.setAttribute("data-url", elem.link);
                            tweet.setAttribute("data-hashtags", elem.text);
                            tweet.innerText = "Tweet";
                            html += tweet.outerHTML;

                            var fbshare = document.createElement("div");
                            fbshare.onclick = function () {
                                $('meta[property="og:description"]').attr('content', elem.text);
                                switch (elem.contentType) {
                                    case 'link':
                                        $('meta[property="og:url"]').attr('content', elem.value);
                                        break;
                                    case 'ytLink':
                                        $('meta[property="og:url"]').attr('content', elem.value);
                                        break;
                                    case 'picture':
                                        $('meta[property="og:image"]').attr('content', "rest?id=" + elem.value);
                                        break;
                                    case 'video':
                                        $('meta[property="og:video"]').attr('content', "rest?id=" + elem.value);
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
                                    fbshare.setAttribute("data-href", "rest?id=" + elem.value);
                                    break;
                                case 'video':
                                    fbshare.setAttribute("data-href", "rest?id=" + elem.value);
                                    break;
                                default:
                                    break;
                            }
                            fbshare.setAttribute("data-layout", "button_count");
                            html += fbshare.outerHTML;
                        }
                    }
                    div.innerHTML = html;
                    div.id = elem.id;
                    outterDiv.appendChild(div);
                }
            )
            var createPost = document.getElementById("createPost");
            createPost.parentNode.insertBefore(outterDiv, createPost.nextSibling);
        }
    }
    ;
    var url = "posts";
    xhttp.open('POST', url, true);
    xhttp.send();
}

var ytFlag = false;
var nonYtFlag = false;

function addLink(isYTLink) {
    if (isYTLink) {
        if (ytFlag) {
            // brisanje yt linka
            linkType = "";
            document.getElementById("yt-link").style.background = "white";
            var elem = document.getElementById('plink');
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
            var text = document.getElementById("text");
            var pTag = document.createElement("p");
            pTag.setAttribute("class", "appendChild");
            pTag.setAttribute("id", "plink");
            var linkBox = document.createElement("input");
            linkBox.setAttribute("id", "yt-link-input");
            linkBox.setAttribute("type", "text");
            var aTag = document.createElement("a");
            aTag.innerText = "Youtube link: ";
            pTag.appendChild(aTag);
            pTag.appendChild(linkBox);
            text.parentNode.insertBefore(pTag, text.nextSibling);
        }
    } else {
        if (nonYtFlag) {
            // remove link
            linkType = "";
            var link = document.getElementById("link");
            link.style.background = "white";
            var elem = document.getElementById('plink');
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
            var link = document.getElementById("link");
            link.style.background = "gray";
            var text = document.getElementById("text");
            var pTag = document.createElement("p");
            pTag.setAttribute("class", "appendChild");
            pTag.setAttribute("id", "plink");
            var linkBox = document.createElement("input");
            linkBox.setAttribute("id", "link-input");
            linkBox.setAttribute("type", "text");
            var aTag = document.createElement("a");
            aTag.innerText = "Link: ";
            pTag.appendChild(aTag);
            pTag.appendChild(linkBox);
            text.parentNode.insertBefore(pTag, text.nextSibling);
        }
    }
}