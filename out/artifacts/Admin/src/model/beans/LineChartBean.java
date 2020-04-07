package model.beans;

import model.dao.AdminUserDao;
import model.dto.History;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;

@ManagedBean
@ViewScoped
public class LineChartBean implements Serializable {
    AdminUserDao userDao = new AdminUserDao();
    private LineChartModel lineModel;

    @PostConstruct
    public void init() {
        lineModel = new LineChartModel();
        LineChartSeries s = new LineChartSeries();
        lineModel.setStacked(true);
        s.setLabel("Logins/Hour");

        History history = userDao.get24hoursHistory();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        history.getHistory().forEach(entry -> {
            s.set(formatter.format(entry.getKey()), entry.getValue());
        });

        lineModel.addSeries(s);
        lineModel.setLegendPosition("e");
        Axis y = lineModel.getAxis(AxisType.Y);
        y.setTickFormat("%d");
        y.setTickInterval("1");
        y.setMin(history.getHistory().stream().min(Comparator.comparingInt(Map.Entry::getValue)).get().getValue()-1);
        y.setMax(history.getHistory().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get().getValue()+1);
        y.setLabel(history.getyAxisName());

        DateAxis dateAxis = new DateAxis();
        dateAxis.setMin(formatter.format(LocalDateTime.now().minusHours(24)));
        dateAxis.setMax(formatter.format(LocalDateTime.now()));
        dateAxis.setTickAngle(-45);
        dateAxis.setTickInterval("1 hour");
        //dateAxis.setTickFormat("%H:%#M:%S");
        dateAxis.setTickFormat("%Hh");
        dateAxis.setLabel(history.getxAxisName());
        lineModel.getAxes().put(AxisType.X, dateAxis);
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }
}