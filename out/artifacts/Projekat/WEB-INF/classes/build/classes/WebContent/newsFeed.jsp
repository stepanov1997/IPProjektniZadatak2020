<%@ page import="com.rometools.rome.feed.synd.SyndEntry" %>
<%@ page import="com.rometools.rome.feed.synd.SyndFeed" %>
<%@ page import="com.rometools.rome.io.FeedException" %>
<%@ page import="com.rometools.rome.io.SyndFeedInput" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>

<% if (session.getAttribute("accountBean") == null) {
    response.sendRedirect("login.jsp");
    return;
}
%>

<jsp:useBean id="accountBean" scope="session" type="model.beans.AccountBean"/>
<html>
<head>
    <title>NEWS FEED</title>
    <link rel="stylesheet" type="text/css" href="styles/newsFeedStyle.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="ICON"
          href="https://scontent.fbeg4-1.fna.fbcdn.net/v/t1.0-9/54255431_645793952539379_1611586770158223360_o.jpg?_nc_cat=110&_nc_sid=09cbfe&_nc_ohc=zDb83HnW2FoAX-AuhRZ&_nc_ht=scontent.fbeg4-1.fna&oh=da1701f4c2fa67f6a3bad35766e337e7&oe=5E9CEBBE"
          type="image/jpg"/>
    <script src="scripts/newsFeedScript.js"></script>
    <script>

        var fileType = "";
        var linkType = "";

        function getId(url) {
            var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
            var match = url.match(regExp);

            if (match && match[2].length == 11) {
                return match[2];
            } else {
                return 'error';
            }
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
                    if (result.redirect) {
                        var div = document.createElement("div");

                        var html = "";
                        var ptag = document.createElement("p");
                        ptag.innerText = "Datetime: " + result.dateTime;
                        html += ptag.innerHTML;

                        var ptag = document.createElement("p");
                        ptag.innerText = "Datetime: " + result.dateTime;
                        html += ptag.innerHTML;

                        var profile = new Image();
                        profile.style.width = "50px";
                        profile.style.height = "50px";
                        if (result.Picture_id == null || result.Picture_id === "") {
                            var flagLink = document.getElementById("profilePic").getAttribute("src");
                            profile.setAttribute("src", flagLink);
                        } else {
                            profile.setAttribute("src", "rest?id=" + result.Picture_id);
                        }
                        html += profile.innerHTML;
                        switch (result.contentType) {
                            case 'link':
                                var aTag = document.createElement("a");
                                aTag.href = result.value;
                                html += aTag.innerHTML;
                                break;
                            case 'ytLink':
                                html += `<iframe width="560" height="315" src="//www.youtube.com/embed/" + getId(result.value) + " frameborder="0" allowfullscreen></iframe>`
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
                        ptag.innerText = "Datetime: " + result.dateTime;
                        html += ptag.innerHTML;

                        var div = document.createElement("div");
                        div.innerHTML = html;

                        var createPost = document.getElementById("createPost");
                        createPost.parentNode.insertBefore(div, createPost.nextSibling);
                    } else {
                        alert("greska");
                    }
                }
            };
            // var url = "Controller?controller=newsFeed&action=createPost";
            var url = "NewsFeedController";
            url += "&isVideo=" + fileType;
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

        addProfilePicture();
    </script>
    <meta property="og:url" content="https://www.your-domain.com/your-page.html"/>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="Your Website Title"/>
    <meta property="og:description" content="Your description"/>
    <meta property="og:image" content="https://www.your-domain.com/path/image.jpg"/>
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<%--    <script async="async">--%>
<%--        $(window).load(function () {--%>
<%--            (function (d, s, id) {--%>
<%--                var js, fjs = d.getElementsByTagName(s)[0];--%>
<%--                if (d.getElementById(id)) return;--%>
<%--                js = d.createElement(s);--%>
<%--                js.id = id;--%>
<%--                js.async = true;--%>
<%--                js.src = "https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v3.0";--%>
<%--                fjs.parentNode.insertBefore(js, fjs);--%>
<%--            }(document, 'script', 'facebook-jssdk'));--%>

<%--            /* Twitter */--%>
<%--            !function (d, s, id) {--%>
<%--                var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/.test(d.location) ? 'http' : 'https';--%>
<%--                if (!d.getElementById(id)) {--%>
<%--                    js = d.createElement(s);--%>
<%--                    js.id = id;--%>
<%--                    js.src = p + '://platform.twitter.com/widgets.js';--%>
<%--                    fjs.parentNode.insertBefore(js, fjs);--%>
<%--                }--%>
<%--            }(document, 'script', 'twitter-wjs');--%>
<%--        });--%>

<%--    </script>--%>
</head>
<body>
<div class="header">
    <h2 class="leftHeader">NEWS FEED</h2>
    <div class="rightHeader">
        <form method="post" action="Controller?controller=account&action=logout">
            <button type="submit" name="submit">Log out</button>
        </form>
    </div>
</div>

<div class="row">
    <div class="leftcolumn">
        <div class="card">
            <h2>About Me</h2>
            <div class="fakeimg">
                <p><a class="italicAndBoldFont">Name:</a> ${accountBean.account.name}</p>
                <p><a class="italicAndBoldFont">Surname:</a> ${accountBean.account.surname}</p>
                <p><a class="italicAndBoldFont">Username:</a> ${accountBean.account.username}</p>
                <p><a class="italicAndBoldFont">Email:</a> ${accountBean.account.email}</p>
                <p><a class="italicAndBoldFont">Country:</a> ${accountBean.account.country}</p>
                <% if (accountBean.getAccount().getRegion() != null) { %>
                <p><a class="italicAndBoldFont">Region:</a> ${accountBean.account.region}</p>
                <% } %>
                <% if (accountBean.getAccount().getCity() != null) { %>
                <p><a class="italicAndBoldFont">City:</a> ${accountBean.account.city}</p>
                <% } %>
                <p><a class="italicAndBoldFont">Number of logins:</a> ${accountBean.account.loginCounter}</p>
                <a class="fakeimg" id="profilePic"></a>
            </div>
        </div>
        <div class="card">
            <h3>Popular Post</h3>
            <div class="fakeimg">Image</div>
            <br>
            <div class="fakeimg">Image</div>
            <br>
            <div class="fakeimg">Image</div>
        </div>
        <div class="card">
            <h3>Follow Me</h3>
            <p>Some text..</p>
        </div>
    </div>

    <div id="posts" class="rightcolumn">
        <form id="createPost" onsubmit="return createPost()">
            <div class="leftPost">
                <label>Type text: </label><br>
                <textarea id="text" rows=5 cols="50"></textarea>
            </div>
            <div id="attachment" class="insertAttachment">
                <label id="upload-image" for="file-input">
                    <img id="picture-img" src="https://static.xx.fbcdn.net/rsrc.php/v3/yA/r/6C1aT2Hm3x-.png" alt=""
                         onclick="fileType='picture'">
                    <img id="video-img" src="https://secure.webtoolhub.com/static/resources/icons/set165/e81b28f7.png"
                         onclick="fileType='video'" alt="">
                </label>
                <input id="file-input" enctype="multipart/form-data" type="file">
                <img id="yt-link"
                     src="https://icons-for-free.com/iconfiles/png/512/video+youtube+icon-1320192294490006733.png"
                     onclick="addLink(true)" alt="">
                <img id="link" src="https://icon-library.net/images/website-link-icon/website-link-icon-23.jpg"
                     onclick="addLink(false)" alt=""><br>
                <button type="submit">SHARE POST</button>
            </div>
        </form>
        <%
            URL feedUrl = new URL("https://europa.eu/newsroom/calendar.xml_en?field_nr_events_by_topic_tid=151");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = null;
            try {
                feed = input.build(new InputStreamReader(feedUrl.openStream()));
            } catch (FeedException e) {
                e.printStackTrace();
            }
            Locale locale = Locale.getDefault();
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
            ;
            if (feed != null) {
                for (SyndEntry rssFeed : feed.getEntries().stream().sorted((a, b) -> b.getPublishedDate().compareTo(a.getPublishedDate())).collect(Collectors.toList())) {
        %>
        <div class="card">
            <h2><%=rssFeed.getTitle()%>
            </h2>
            <h5><%=dateFormat.format(rssFeed.getPublishedDate())%>
            </h5>
            <p><%=rssFeed.getDescription().getValue()%>
            </p>
            <p><a href="<%=rssFeed.getLink()%>">Link</a></p>
            <a href="https://twitter.com/share" class="twitter-share-button" data-url="<%=rssFeed.getLink()%>"
               data-hashtags="TextSearcher">Tweet</a>
            <div class="fb-share-button"
                 data-href="<%=rssFeed.getLink()%>"
                 data-layout="button_count">
            </div>
        </div>
        <% }
        }
        %>
    </div>

</div>

<div class="footer">
    <h2>Footer</h2>
</div>
</body>
</html>
