package model.beans;

import model.dao.CallDao;
import model.dto.Call;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@ManagedBean
@ViewScoped
public class CallBean implements Serializable {
    private static final Map<Integer, String> ipAddressMap = new HashMap<>();

    private Call assistanceCall = new Call();
    CallDao assistanceCallDao = new CallDao();
    private List<CallBean> assistanceCallList;

    public CallBean() {
    }

    public CallBean(Call assistanceCall) {
        this.assistanceCall = assistanceCall;
    }

    public void importAssistanceCalls() {
        assistanceCallList = assistanceCallDao
                .getAll()
                .stream()
                .map(CallBean::new)
                .sorted((a, b) -> a.assistanceCall.getDatetime().isBefore(b.assistanceCall.getDatetime()) ? 1 : -1)
                .collect(toList());
    }

    public List<String> splitLocation() {
        return Arrays.asList(assistanceCall.getLocation().split(" "));
    }

    public Call getAssistanceCall() {
        return assistanceCall;
    }

    public void setAssistanceCall(Call assistanceCall) {
        this.assistanceCall = assistanceCall;
    }

    public List<CallBean> getAssistanceCallList() {
        return assistanceCallList;
    }

    public void setAssistanceCallList(List<CallBean> assistanceCallList) {
        this.assistanceCallList = assistanceCallList;
    }

    public String createCall() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String lat = params.get("lat");
        String lng = params.get("lng");
        assistanceCall.setLocation(lat + " " + lng);
        assistanceCall.setBlocked(false);
        assistanceCall.setDatetime(LocalDateTime.now());
        assistanceCall.setReportsCounter(0);
        assistanceCallDao.add(assistanceCall);
        return "mainPage.xhtml";
    }

    public String reportCall() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getCookies() != null && Stream.of(request.getCookies()).anyMatch(e -> e.getName().startsWith("LastReport") &&
                e.getValue().equals(String.valueOf(assistanceCall.getId())))) {
            FacesContext.getCurrentInstance().addMessage("form" + assistanceCall.getId() + ":" + "report" + assistanceCall.getId(), new FacesMessage("You already reported this post!"));
            return "";
        }
        Cookie cookie = new Cookie("LastReport" + assistanceCall.getId(), String.valueOf(assistanceCall.getId()));
        cookie.setMaxAge(60 * 60 * 24 * 7);
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addCookie(cookie);

        if (assistanceCallDao.reportCall(assistanceCall.getId()))
            assistanceCall.setReportsCounter(assistanceCall.getReportsCounter() + 1);
        return "failure";
    }

    public boolean getCheckIsReported() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        var cookies = request.getCookies();
        if (cookies == null) return false;
        if (Stream.of(cookies).anyMatch(e -> e.getName().startsWith("LastReport") &&
                e.getValue().equals(String.valueOf(assistanceCall.getId())))) {
            return true;
        }
        return false;
    }

    public String blockCall() {
        CallDao callDao = new CallDao();
        if (!assistanceCall.isBlocked()) {
            assistanceCall.setBlocked(true);
            callDao.update(assistanceCall.getId(), assistanceCall);
        }
        return "/admin_home.xhtml?faces-redirect=true";
    }

    public String unblockCall() {
        CallDao callDao = new CallDao();
        if (assistanceCall.isBlocked()) {
            assistanceCall.setBlocked(false);
            callDao.update(assistanceCall.getId(), assistanceCall);
        }
        return "/admin_home.xhtml?faces-redirect=true";
    }
    public String deleteCall() {
        CallDao callDao = new CallDao();
        callDao.delete(assistanceCall.getId());
        return "/admin_home.xhtml?faces-redirect=true";
    }
}
