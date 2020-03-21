package controller;

import com.google.gson.Gson;
import model.beans.AccountBean;
import model.dao.AccountDao;
import model.dto.Account;
import util.SHA1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AccountController extends HttpServlet implements Serializable {
    private String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("submit") != null && "account".equals(request.getParameter("controller"))) {
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
                    if (!email.matches(emailPattern)) {
                        result += "Email format is not correct (Example: email@gmail.com) <br>";
                    }

                    AccountBean accountBean = new AccountBean();
                    accountBean.setUsernameKey(username);

                    if (accountBean.existsByUsername()) {
                        result += "Username already exists <br>";
                    }

                    accountBean.setEmailKey(email);
                    if (accountBean.existsByEmail()) {
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

                    accountBean.getAccount().setName(name);
                    accountBean.getAccount().setSurname(surname);
                    accountBean.getAccount().setUsername(username);
                    accountBean.getAccount().setPassword(SHA1.encryptPassword(password));
                    accountBean.getAccount().setEmail(email);
                    request.getSession().setAttribute("accountBean", accountBean);
                    if (accountBean.add()) {
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
                    AccountBean accountBean = (AccountBean) request.getSession().getAttribute("accountBean");
                    Account account = accountBean.getAccount();
                    account.setCountry(request.getParameter("country"));
                    account.setCountryCode(request.getParameter("countryCode"));
                    account.setRegion(request.getParameter("region"));
                    account.setCity(request.getParameter("city"));
                    if (!Boolean.parseBoolean(request.getParameter("picture")))
                        account.setPicture_Id(null);
                    AccountDao accountDao = new AccountDao();
                    Gson gson = new Gson();
                    var out = response.getOutputStream();
                    if (accountDao.update(account)) {
                        request.getSession().setAttribute("accountBean", accountBean);
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

                    AccountBean accountBean = new AccountBean();
                    AccountDao accountDao = new AccountDao();
                    Account account = accountDao.getByUsername(username);

                    if (account == null || !SHA1.encryptPassword(password).equals(account.getPassword())) {
                        inputMap.put("redirect", false);
                        inputMap.put("message", "Username or password are not valid.");
                        String json = gson.toJson(inputMap);
                        out.print(json);
                        return;
                    }

                    account.setLoginCounter(account.getLoginCounter()+1);
                    accountDao.update(account);

                    accountBean.setAccount(account);
                    request.getSession().setAttribute("accountBean", accountBean);

                    inputMap.put("redirect", true);
                    inputMap.put("message", username + ", you are successfully logged in.");
                    String json = gson.toJson(inputMap);
                    out.print(json);
                }
                break;
                case "picture":
                {
                    AccountBean accountBean = (AccountBean)request.getSession().getAttribute("accountBean");
                    Account account = accountBean.getAccount();
                    Integer pictureId = account.getPicture_Id();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    Gson gson = new Gson();
                    Map<String, Object> map = new HashMap<>();
                    if(pictureId==null)
                    {
                        map.put("exists", false);
                        map.put("countryCode", account.getCountryCode());
                    }
                    else {
                        map.put("exists", true);
                        map.put("id", pictureId);
                    }
                    response.getOutputStream().print(gson.toJson(map));
                }
                break;
                case "logout":
                {
                    request.getSession().invalidate();
                    response.sendRedirect("login.jsp");
                }
                break;
            }
        }
    }
}
