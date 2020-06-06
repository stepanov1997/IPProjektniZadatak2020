<%@ page import="model.beans.UserBean" %>
<%@ page import="model.dao.UserDao" %>
<%@ page import="model.dto.DangerCategory" %>
<%@ page import="model.beans.DangerCategoryBean" %>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" %>

<%
    UserBean acc = (UserBean) session.getAttribute("userBean");
    if (acc == null || acc.getUser() == null || !acc.getUser().isEnabled() || !acc.getUser().isOnline()) {
        session.invalidate();
        response.sendRedirect("login.html");
        return;
    }
%>

<jsp:useBean id="userBean" scope="session" type="model.beans.UserBean"/>
<% userBean.setUser(new UserDao().get(userBean.getUser().getId())); %>
<html>
<head>
    <title>NEWS FEED</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="styles/newsFeedStyle.css">
    <link href="https://vjs.zencdn.net/7.7.5/video-js.css" rel="stylesheet"/>
    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->
    <script src="https://vjs.zencdn.net/ie8/1.1.2/videojs-ie8.min.js"></script>
    <script src="https://vjs.zencdn.net/7.7.5/video.js"></script>
    <script src='https://api.mapbox.com/mapbox-gl-js/v1.8.1/mapbox-gl.js'></script>
    <link href='https://api.mapbox.com/mapbox-gl-js/v1.8.1/mapbox-gl.css' rel='stylesheet'/>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css"
          integrity="sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ=="
          crossorigin=""/>
    <script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"
            integrity="sha512-gZwIG9x3wUXg2hdXF6+rVkLF/0Vi9U8D2Ntg4Ga5I5BZpVkVxlJWbSQtXPSiUTtC0TjtGOmxa1AJPuV0CPthew=="
            crossorigin=""></script>
    <%--    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false">--%>
    <%--    </script>--%>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js">
    </script>
    <%--    <script src="scripts/google-maps.js"></script>--%>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="ICON"
          href="https://scontent.fbeg4-1.fna.fbcdn.net/v/t1.0-9/54255431_645793952539379_1611586770158223360_o.jpg?_nc_cat=110&_nc_sid=09cbfe&_nc_ohc=zDb83HnW2FoAX-AuhRZ&_nc_ht=scontent.fbeg4-1.fna&oh=da1701f4c2fa67f6a3bad35766e337e7&oe=5E9CEBBE"
          type="image/jpg"/>

    <script src="scripts/newsFeedScript.js"></script>
    <script>addProfilePicture();</script>
    <script>
        function refresh() {
            const top = document.documentElement.scrollTop;
            addPosts();
            addNotifications();
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
        <form method="post" action="Controller?controller=user&action=logout">
            <button type="submit" name="submit">Log out</button>
        </form>
    </div>
</div>

<div class="row">
    <div class="leftcolumn">
        <div class="card">
            <h2>About Me</h2>
            <div class="fakeimg">
                <p><a id="name" class="italicAndBoldFont">Name:</a> ${userBean.user.name}</p>
                <p><a id="surname" class="italicAndBoldFont">Surname:</a> ${userBean.user.surname}</p>
                <p><a class="italicAndBoldFont">Username:</a> ${userBean.user.username}</p>
                <p><a class="italicAndBoldFont">Email:</a> ${userBean.user.email}</p>
                <p><a class="italicAndBoldFont">Country:</a> ${userBean.user.country}</p>
                <% if (userBean.getUser().getRegion() != null) { %>
                <p><a class="italicAndBoldFont">Region:</a> ${userBean.user.region}</p>
                <% } %>
                <% if (userBean.getUser().getCity() != null) { %>
                <p><a class="italicAndBoldFont">City:</a> ${userBean.user.city}</p>
                <% } %>
                <p><a class="italicAndBoldFont">Number of logins:</a> ${userBean.user.loginCounter}</p>
                <a class="fakeimg" id="profilePic"></a>
            </div>
        </div>
        <div class="card">
            <h3>Notifications:</h3>
            <div id="notificationDiv">
            </div>
        </div>
<%--        <div class="card">--%>
<%--            <h3>Follow Me</h3>--%>
<%--            <p>Some text..</p>--%>
<%--        </div>--%>
    </div>

    <div id="posts" class="midcolumn">
        <div id="createDiv">
            <div class="tab">
                <button id="tab1" class="tablinks" onclick="showTab('tab1', 'createPost1')">Post with an attachment
                </button>
                <button id="tab2" class="tablinks" onclick="showTab('tab2', 'createPost2')">Post about a potential
                    danger
                </button>
            </div>
            <div id="createPost1" class="tabcontent">
                <form onsubmit="return createPost1()">
                    <div class="leftPost">
                        <label>Type text: </label><br>
                        <label for="text"></label><textarea id="text" rows=5 cols="50"></textarea>
                    </div>
                    <div id="attachment" class="insertAttachment">
                        <div class="buttons">
                            <label id="upload-image" for="file-input">
                                <img id="picture-img" src="https://static.xx.fbcdn.net/rsrc.php/v3/yA/r/6C1aT2Hm3x-.png"
                                     alt=""
                                     onclick="fileType='picture'">
                                <img id="video-img"
                                    src="https://secure.webtoolhub.com/static/resources/icons/set165/e81b28f7.png"
                                    onclick="fileType='video'" alt="">
                            </label>
                            <input id="file-input" enctype="multipart/form-data" type="file" hidden="hidden">

                            <img id="yt-link"
                                 src="https://icons-for-free.com/iconfiles/png/512/video+youtube+icon-1320192294490006733.png"
                                 onclick="addLink(true)" alt="">
                            <img id="link"
                                 src="https://cdn2.iconfinder.com/data/icons/pittogrammi/142/95-512.png"
                                 onclick="addLink(false)" alt=""><br>
                        </div>
                        <div class="button-wrapper">
                            <button type="submit">SHARE POST</button>
                        </div>
                    </div>
                </form>
            </div>

            <div id="createPost2" class="tabcontent">
                <form onsubmit="return createPost2()" class="postType2">
                    <a style="margin: auto">Choose category of potential danger:</a>
                    <label>
                        <select id="selectCategory">
                            <% DangerCategoryBean dangerCategoryBean = new DangerCategoryBean();
                                dangerCategoryBean.importDangerCategories();
                                for (DangerCategory dangerCategory : dangerCategoryBean.getDangerCategories()) { %>
                            <option name="category" value="<%=dangerCategory.getId()%>"><%=dangerCategory.getName()%>
                            </option>
                            <% } %>
                        </select>
                    </label>
                    <br><br>
                    <div class="leftPost">
                        <label>Type text: </label><br>
                        <label for="text2"></label><textarea id="text2" rows=5 cols="50"></textarea>
                    </div>
                    <div id="attachment2">
                        <br><br>
                        <label style="margin: auto">
                            <input id="isEmergency" type="checkbox">
                        </label><a> Is post emergency?</a><br><br>
                        <!--The div element for the map -->
                        <div id="map" style="width: 75%; height: 300px; margin: auto;"></div>
                        <script>
                            mapboxgl.accessToken = 'pk.eyJ1Ijoia2lraWtpa2kxOTkyIiwiYSI6ImNrOHoza2ZqejBhbGQzZGxjeGIxNWM0YnoifQ.3FoukhI7DUYFqV4W63mi6w';
                            var map = new mapboxgl.Map({
                                container: 'map', // container id
                                style: 'mapbox://styles/mapbox/streets-v11',
                                center: [17.1866667, 44.76638889], // starting position
                                zoom: 15 // starting zoom
                            });
                            var marker = new mapboxgl.Marker({
                                draggable: true
                            })
                                .setLngLat([17.1866667, 44.76638889])
                                .addTo(map);

                            function onDragEnd() {
                                var lngLat = marker.getLngLat();
                                document.getElementById('lat').value = lngLat.lat;
                                document.getElementById('lng').value = lngLat.lng;
                            }

                            marker.on('drag', onDragEnd);

                            // Add zoom and rotation controls to the map.
                            map.addControl(new mapboxgl.NavigationControl());
                        </script>
                        <label for="lat"></label><input style="display: none" id="lat" type="text"
                                                        onkeydown="changePosition1()">
                        <label for="lng"></label><input style="display: none" id="lng" type="text"
                                                        onkeydown="changePosition1()"><br><br>
                        <button style="margin: auto" type="submit">Create post</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div id="weatherForcast" class="rightcolumn">
        <div id="myCity" class="card"></div>
        <div id="city1" class="card"></div>
        <div id="city2" class="card"></div>
    </div>
</div>

<div class="footer">
    <h2>Footer</h2>
</div>
</body>
</html>
