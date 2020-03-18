package controller;

import model.dto.AccountBean;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "accountController")
@ApplicationScoped
public class AccountController extends HttpServlet implements Serializable {
    private List<AccountBean> accounts = new ArrayList<AccountBean>();
    private AccountBean accountBean = new AccountBean();
    private String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("submit")!=null && "account".equals(request.getParameter("controller"))) {
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

                    if (!"".equals(result)) {
                        response.getOutputStream().println(result);
                        return;
                    }
                    AccountBean accountBean = new AccountBean();
                    accountBean.setName(name);
                    accountBean.setSurname(surname);
                    accountBean.setUsername(username);
                    accountBean.setPassword(password);
                    accountBean.setEmail(email);
                    this.accountBean = accountBean;
                    addAccount();

                    response.getOutputStream().println("Uspjesno ste registrovali novog korisnika");
                    return;
                }
                //break;
            }
        }
    }

    //    {
//        accounts = Arrays.asList(new AccountBean(0,"Kristijan", "Stepanov", "kiki", "kiki", "kristijan.stepanov95@gmail.com"),
//                new AccountBean(1,"Milan", "Medic", "medo", "medo", "milan.medic@gmail.com"),
//                new AccountBean(2,"Gorana", "Golubović", "goca", "goca", "gorana.golubovic@gmail.com"),
//                new AccountBean(3,"Milica", "Milakovic", "cimi", "cimi", "milica.milakovic@gmail.com"),
//                new AccountBean(4,"Petar", "Mihajlović", "pero", "pero", "petar.mihajlovic@gmail.com"));
//
//    }

    public String addAccount()
    {
        accounts.add(accountBean);
        for (var account: accounts) {
            System.out.println(account);
        }
        return "Uspjesno dodat nalog";
    }
    public String removeAccount()
    {
        accounts.remove(accountBean);
        return "Uspjesno obrisan nalog";
    }

    public String submited()
    {
        System.out.println("Submited form!!");
        return "Submited form!!";
    }

    public boolean updateAccount()
    {
        AccountBean found = findAccount(accountBean.getId());
        if(found!=null) {
            if (accountBean.getName() != null) accountBean.setName(accountBean.getName());
            if(accountBean.getSurname() !=null) accountBean.setName(accountBean.getSurname());
            if(accountBean.getUsername()!=null) accountBean.setName(accountBean.getUsername());
            if(accountBean.getPassword()!=null) accountBean.setName(accountBean.getPassword());
            if(accountBean.getEmail()!=null) accountBean.setName(accountBean.getEmail());
            return true;
        }
        return false;
    }

    private AccountBean findAccount(int id)
    {
        if(accounts.stream().anyMatch(e -> e.getId() == id))
            return accounts.stream().filter(e -> e.getId() == id).findFirst().get();
        return null;
    }
}
