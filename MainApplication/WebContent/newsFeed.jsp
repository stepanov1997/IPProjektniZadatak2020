<%@ page import="model.beans.AccountBean" %>
<%@ page import="model.dao.AccountDao" %>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>

<% if (session.getAttribute("accountBean") == null) {
    response.sendRedirect("login.jsp");
    return;
}
%>

<jsp:useBean id="accountBean" scope="session" type="model.beans.AccountBean"/>
<% accountBean.setAccount(new AccountDao().get(accountBean.getAccount().getId())); %>
<html>
<head>
    <title>NEWS FEED</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="styles/newsFeedStyle.css">
    <link href="https://vjs.zencdn.net/7.7.5/video-js.css" rel="stylesheet"/>
    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->
    <script src="https://vjs.zencdn.net/ie8/1.1.2/videojs-ie8.min.js"></script>
    <script src="https://vjs.zencdn.net/7.7.5/video.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false">
    </script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js">
    </script>
    <script src="scripts/google-maps.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="ICON"
          href="https://scontent.fbeg4-1.fna.fbcdn.net/v/t1.0-9/54255431_645793952539379_1611586770158223360_o.jpg?_nc_cat=110&_nc_sid=09cbfe&_nc_ohc=zDb83HnW2FoAX-AuhRZ&_nc_ht=scontent.fbeg4-1.fna&oh=da1701f4c2fa67f6a3bad35766e337e7&oe=5E9CEBBE"
          type="image/jpg"/>

    <script src="scripts/newsFeedScript.js"></script>
    <script>addProfilePicture();</script>
    <script>
        function refresh() {
            var top = document.documentElement.scrollTop;
            document.getElementById("postsDiv").innerHTML = "";
            document.getElementById("postsDiv").remove();
            addPosts();
            setTimeout(function () {
                document.documentElement.scrollTop = top;
            }, 1000);
        }

        addPosts();

        setInterval(refresh, 30000);


    </script>
    <script>addWeatherForcast();</script>

    <meta property="og:url" content="https://www.your-domain.com/your-page.html"/>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="Your Website Title"/>
    <meta property="og:description" content="Your description"/>
    <meta property="og:image" content="https://www.your-domain.com/path/image.jpg"/>
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
                <p><a id="name" class="italicAndBoldFont">Name:</a> ${accountBean.account.name}</p>
                <p><a id="surname" class="italicAndBoldFont">Surname:</a> ${accountBean.account.surname}</p>
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

    <div id="posts" class="midcolumn">
        <div class="tab">
            <button class="tablinks" onclick="showTab(event, 'createPost1')">Post with an attachment</button>
            <button class="tablinks" onclick="showTab(event, 'createPost2')">Post about a potential danger</button>
        </div>
        <div id="createPost1" class="tabcontent">
            <form onsubmit="return createPost1()">
                <div class="leftPost">
                    <label>Type text: </label><br>
                    <textarea id="text" rows=5 cols="50"></textarea>
                </div>
                <div id="attachment" class="insertAttachment">
                    <label id="upload-image" for="file-input">
                        <img id="picture-img" src="https://static.xx.fbcdn.net/rsrc.php/v3/yA/r/6C1aT2Hm3x-.png" alt=""
                             onclick="fileType='picture'">
                        <img id="video-img"
                             src="https://secure.webtoolhub.com/static/resources/icons/set165/e81b28f7.png"
                             onclick="fileType='video'" alt="">
                    </label>
                    <input id="file-input" enctype="multipart/form-data" type="file" hidden="hidden">
                    <img id="yt-link"
                         src="https://icons-for-free.com/iconfiles/png/512/video+youtube+icon-1320192294490006733.png"
                         onclick="addLink(true)" alt="">
                    <img id="link" src="https://icon-library.net/images/website-link-icon/website-link-icon-23.jpg"
                         onclick="addLink(false)" alt=""><br>
                    <button type="submit" onclick="showMore()">SHARE POST</button>
                </div>
            </form>
        </div>

        <div id="createPost2" class="tabcontent">
            <form onsubmit="return createPost2()">
                <div class="leftPost">
                    <label>Type text: </label><br>
                    <textarea id="text2" rows=5 cols="50"></textarea>
                </div>
                <div id="attachment2" class="insertAttachment2">
                    <a>Choose type of potential danger: </a>
                    <select>
                        <% // dodati opcije %>
                        <option name="danger" value="1">A fallen tree on the road.</option>
                        <option name="danger" value="2">A storm forecast.</option>
                        <option name="danger" value="3">A flood</option>
                        <option name="danger" value="4">Fire</option>
                    </select>
                    <br><br>
                    <a>Choose category of potential danger:</a>
                    <select>
                        <option name="category" value="1">HIGH PRIORITY</option>
                        <option name="category" value="2">MEDIUM PRIORITY</option>
                        <option name="category" value="3">LOW PRIORITY</option>
                    </select>
                    <br><br>
                    <input type="checkbox"><a> Is post emergency?</a><br>
                    <!--The div element for the map -->
                    <div id="map"></div>
                    <script async defer
                            src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBYxcxZ3yB7owNaBe5Pr6WbxHGn2WId-4w&callback=initMap">
                    </script>
                    <input id="lat" type="text" onkeydown="changePosition()">
                    <input id="lng" type="text" onkeydown="changePosition()">
                    <button type="submit" onclick="showMore()">SHARE POST</button>
                </div>
            </form>
        </div>

    </div>
    <div id="weatherForcast" class="rightcolumn">
        <div class="card">
            <div id="myCity" class="card"></div>
            <div id="city1" class="card"></div>
            <div id="city2" class="card"></div>
        </div>
    </div>
</div>

<div class="footer">
    <h2>Footer</h2>
</div>
</body>
</html>