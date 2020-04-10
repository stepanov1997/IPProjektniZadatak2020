package model.beans;

import model.dao.AssistanceCallDao;
import model.dto.AssistanceCall;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class AssistanceCallBean implements Serializable {
    private AssistanceCall assistanceCall;
    private List<AssistanceCallBean> assistanceCallList;

    public AssistanceCallBean() {
    }

    public AssistanceCallBean(AssistanceCall assistanceCall) {
        this.assistanceCall = assistanceCall;
    }

    public void importAssistanceCalls()
    {
        AssistanceCallDao assistanceCallDao = new AssistanceCallDao();
        List<AssistanceCallBean> assistanceCallBeans = assistanceCallDao
                .getAll()
                .stream()
                .filter(e -> !e.isBlocked())
                .map(AssistanceCallBean::new)
                .collect(Collectors.toList());
        assistanceCallList = assistanceCallBeans;
    }

    public List<String> splitLocation()
    {
        return Arrays.asList(assistanceCall.getLocation().split(" "));
    }

    public AssistanceCall getAssistanceCall() {
        return assistanceCall;
    }

    public void setAssistanceCall(AssistanceCall assistanceCall) {
        this.assistanceCall = assistanceCall;
    }

    public List<AssistanceCallBean> getAssistanceCallList() {
        return assistanceCallList;
    }

    public void setAssistanceCallList(List<AssistanceCallBean> assistanceCallList) {
        this.assistanceCallList = assistanceCallList;
    }
}
