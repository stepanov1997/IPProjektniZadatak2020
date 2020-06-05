package controller;

import com.google.gson.*;
import model.beans.UserBean;
import model.dao.UserDao;
import model.dto.User;
import util.SHA1;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserController extends HttpServlet implements Serializable {

    static HashMap<Integer, LocalDateTime> onlineUsers = new HashMap<>();

    static {
        UserDao userDao = new UserDao();
        userDao.setAllUsersToOffline();
//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException ignored) {
//                }
//                var userBeans = onlineUsers.entrySet().stream().filter(e -> {
//                    long seconds = Duration.between(e.getValue(), LocalDateTime.now()).getSeconds();
//                    return seconds > 10 || seconds < -10;
//                }).collect(Collectors.toList());
//                for (Map.Entry<Integer, LocalDateTime> userBean : userBeans) {
//                    UserDao userDao = new UserDao();
//                    userDao.logout(Objects.requireNonNull(userDao.get(userBean.getKey())));
//                    onlineUsers.remove(userBean.getKey());
//                }
//            }
//        }).start();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("submit") != null && "user".equals(request.getParameter("controller"))) {
            switch (request.getParameter("action")) {
                case "register": {
                    String name = request.getParameter("name");
                    String surname = request.getParameter("surname");
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    String passwordAgain = request.getParameter("passwordAgain");
                    String email = request.getParameter("email");

                    String result = "";
                    if (!name.matches("^[A-Za-z0-9]{2,}$")) {
                        result += "Name must be large than 2 characters (Supported characters: letters and numbers) <br>";
                    }
                    if (!surname.matches("^[A-Za-z0-9]{2,}$")) {
                        result += "Surname must be between 8 and 16 characters (Supported characters: letters and numbers) <br>";
                    }
                    if (!username.matches("^[A-Za-z0-9#_?.]{8,16}$")) {
                        result += "Username must be between 8 and 16 characters (Supported characters: letters, numbers and ._?) <br>";
                    }
                    if (!password.matches("^[A-Za-z0-9#_?.]{8,16}$")) {
                        result += "Password must be between 8 and 16 characters (Supported characters: letters, numbers and ._?) <br>";
                    }
                    if (!passwordAgain.equals(password)) {
                        result += "Passwords don't match each other <br>";
                    }
                    String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
                    if (!email.matches(emailPattern)) {
                        result += "Email format is not correct (Example: email@gmail.com) <br>";
                    }

                    UserBean userBean = new UserBean();
                    userBean.setUsernameKey(username);

                    if (userBean.existsByUsername()) {
                        result += "Username already exists <br>";
                    }

                    userBean.setEmailKey(email);
                    if (userBean.existsByEmail()) {
                        result += "Email already exists. <br>";
                    }

                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    Gson gson = new Gson();
                    if (!"".equals(result)) {
                        Map<String, Object> inputMap = new HashMap<>();
                        inputMap.put("redirect", false);
                        inputMap.put("message", result);
                        // convert map to JSON String
                        String json = gson.toJson(inputMap);
                        out.print(json);
                        return;
                    }

                    userBean.getUser().setName(name);
                    userBean.getUser().setSurname(surname);
                    userBean.getUser().setUsername(username);
                    userBean.getUser().setPassword(SHA1.encryptPassword(password));
                    userBean.getUser().setEmail(email);
                    request.getSession().setAttribute("userBean", userBean);
                    if (userBean.add()) {
                        Map<String, Object> inputMap = new HashMap<>();
                        inputMap.put("redirect", true);
                        inputMap.put("message", "You are successfully registered.");
                        String json = gson.toJson(inputMap);
                        out.print(json);
                    } else {
                        Map<String, Object> inputMap = new HashMap<>();
                        inputMap.put("redirect", false);
                        inputMap.put("message", "You are unsuccessfully registered.");
                        String json = gson.toJson(inputMap);
                        out.print(json);
                    }
                }
                break;
                case "editProfile": {
                    UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
                    User user = userBean.getUser();
                    user.setCountry(request.getParameter("country"));
                    user.setCountryCode(request.getParameter("countryCode"));
                    user.setRegion(request.getParameter("region"));
                    user.setCity(request.getParameter("city"));
                    user.setNotificationType(Integer.parseInt(request.getParameter("notification")));
                    if (!Boolean.parseBoolean(request.getParameter("picture")))
                        user.setPicture_Id(null);
                    UserDao userDao = new UserDao();
                    Gson gson = new Gson();
                    var out = response.getOutputStream();
                    if (userDao.update(user)) {
                        request.getSession().setAttribute("userBean", userBean);
                        Map<String, Object> inputMap = new HashMap<>();
                        inputMap.put("redirect", true);
                        inputMap.put("message", "You successfully updated your profile.");
                        String json = gson.toJson(inputMap);
                        out.print(json);
                    } else {
                        Map<String, Object> inputMap = new HashMap<>();
                        inputMap.put("redirect", false);
                        inputMap.put("message", "You are unsuccessfully registered.");
                        String json = gson.toJson(inputMap);
                        out.print(json);
                    }
                }
                break;
                case "login": {
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");

                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    Gson gson = new Gson();
                    Map<String, Object> inputMap = new HashMap<>();

                    if (username == null || password == null || username.isBlank() || password.isBlank()) {
                        inputMap.put("redirect", false);
                        inputMap.put("message", "Username or password are not valid.");
                        String json = gson.toJson(inputMap);
                        out.print(json);
                        return;
                    }

                    UserBean userBean = new UserBean();
                    UserDao userDao = new UserDao();
                    User user = userDao.getByUsername(username);

                    boolean isValid = user != null && SHA1.encryptPassword(password).equals(user.getPassword());
                    if (user == null || !isValid || !user.isEnabled()) {
                        inputMap.put("redirect", false);
                        if (user == null) {
                            inputMap.put("message", "Username or password are not valid.");
                        } else if (!isValid) {
                            inputMap.put("message", "Username or password are not valid.");
                        } else if (!user.isEnabled()) {
                            inputMap.put("message", "User does not have access right.");
                        }
                        String json = gson.toJson(inputMap);
                        out.print(json);
                        return;
                    }

                    userDao.login(user);

                    userBean.setUser(user);
                    onlineUsers.put(user.getId(), LocalDateTime.now());

                    request.getSession().setAttribute("userBean", userBean);

                    inputMap.put("redirect", true);
                    inputMap.put("message", username + ", you are successfully logged in.");
                    String json = gson.toJson(inputMap);
                    out.print(json);
                }
                break;
                case "picture": {
                    UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
                    User user = userBean.getUser();
                    Integer pictureId = user.getPicture_Id();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    Gson gson = new Gson();
                    Map<String, Object> map = new HashMap<>();
                    if (pictureId == null) {
                        map.put("exists", false);
                        map.put("countryCode", user.getCountryCode());
                    } else {
                        map.put("exists", true);
                        map.put("id", pictureId);
                    }
                    response.getOutputStream().print(gson.toJson(map));
                }
                break;
                case "logout": {
                    UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
                    User user = userBean.getUser();
                    UserDao userDao = new UserDao();

                    userDao.logout(user);

                    request.getSession().invalidate();
                    response.sendRedirect("login.html");
                }
                break;
                case "online": {
                    UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
                    JsonObject jsonObject = new JsonObject();
                    response.setContentType("application/json");
                    if (userBean == null || userBean.getUser() == null) {
                        request.getSession().invalidate();
                        jsonObject.addProperty("expires", true);
                        response.getOutputStream().print(jsonObject.toString());
                        return;
                    }
                    if (onlineUsers.containsKey(userBean.getUser().getId())) {
                        onlineUsers.replace(userBean.getUser().getId(), LocalDateTime.now());
                        jsonObject.addProperty("expires", false);
                    } else {
                        request.getSession().invalidate();
                        jsonObject.addProperty("expires", true);
                    }
                    response.getOutputStream().print(jsonObject.toString());
                }
                break;
                case "weather": {
                    UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
                    User user = userBean.getUser();
                    response.setContentType("application/json");
                    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                    List<City> citiesCollection = new ArrayList<>();

                    String page = "http://battuta.medunes.net/api/region/" + user.getCountryCode() + "/all/?key=00000000000000000000000000000000";
                    String json = getFromUrl(page);
                    JsonArray regions = JsonParser.parseString(json).getAsJsonArray();
                    for (JsonElement region : regions) {
                        String page2 = "https://geo-battuta.net/api/city/" + user.getCountryCode() + "/search/?region=" + region.getAsJsonObject().get("region").getAsString().replace(" ", "+") + "&key=00000000000000000000000000000000";
                        String json2 = getFromUrl(page2);
                        JsonArray cities = JsonParser.parseString(json2).getAsJsonArray();
                        for (JsonElement cityElem : cities) {
                            var city = new City();
                            JsonObject jsonCity = cityElem.getAsJsonObject();
                            city.city = jsonCity.get("city").getAsString();
                            city.latitude = jsonCity.get("latitude").getAsDouble();
                            city.longitude = jsonCity.get("longitude").getAsDouble();
                            citiesCollection.add(city);
                        }
                    }

                    City city1;
                    if (user.getCity() != null && !user.getCity().isBlank()) {
                        city1 = citiesCollection.stream().filter(e -> e.city.equals(user.getCity())).findFirst().get();
                    } else {
                        Random random = new Random();
                        city1 = citiesCollection.get(random.nextInt(citiesCollection.size() - 1));
                    }
                    citiesCollection.remove(city1);

                    Random random = new Random();
                    City city2 = citiesCollection.get(random.nextInt(citiesCollection.size() - 1));

                    citiesCollection.remove(city2);
                    City city3 = citiesCollection.get(random.nextInt(citiesCollection.size() - 1));

                    List<City> cities = Arrays.asList(city1, city2, city3);

                    JsonArray result = new JsonArray();
                    for (City city : cities) {
                        String weatherLink = "http://api.openweathermap.org/data/2.5/forecast?lat=" + city.latitude + "&lon=" + city.longitude + "&appid=43eacaa5f711d065623e781ac55480aa";
                        String weatherJson = getFromUrl(weatherLink);
                        JsonObject weatherObject = JsonParser.parseString(weatherJson).getAsJsonObject();
                        JsonArray weatherList = weatherObject.get("list").getAsJsonArray();
                        List<JsonObject> weatherFiveDays = IntStream
                                .range(0, weatherList.size())
                                .filter(n -> n % 8 == 0)
                                .mapToObj(weatherList::get)
                                .map(JsonElement::getAsJsonObject)
                                .collect(Collectors.toList());

                        JsonArray jsonArray = gson.toJsonTree(weatherFiveDays).getAsJsonArray();
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("city", city.city);
                        jsonObject.add("weather", jsonArray);
                        result.add(jsonObject);
                    }
                    response.getOutputStream().print(result.toString());
                }
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + request.getParameter("action"));
            }
        }
    }

    public String getFromUrl(String page) {
        StringBuilder sb = new StringBuilder();
        try {
            URL oracle = new URL(page);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine.replace("\\\"", "\""));
            in.close();
        } catch (IOException ex) {
            return null;
        }
        return sb.toString();
    }
}

class City {
    public String city;
    public double latitude;
    public double longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city1 = (City) o;
        return Double.compare(city1.latitude, latitude) == 0 &&
                Double.compare(city1.longitude, longitude) == 0 &&
                Objects.equals(city, city1.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, latitude, longitude);
    }
}
