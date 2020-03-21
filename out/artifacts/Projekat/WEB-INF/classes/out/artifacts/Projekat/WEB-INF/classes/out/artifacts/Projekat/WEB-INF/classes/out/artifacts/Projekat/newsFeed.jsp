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
    <script>addProfilePicture();</script>
    <meta property="og:url" content="https://www.your-domain.com/your-page.html"/>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="Your Website Title"/>
    <meta property="og:description" content="Your description"/>
    <meta property="og:image" content="https://www.your-domain.com/path/image.jpg"/>
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script>
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

    </script>
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
