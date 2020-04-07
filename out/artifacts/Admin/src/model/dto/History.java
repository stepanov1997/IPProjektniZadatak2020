package model.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class History {
    private List<Map.Entry<LocalDateTime, Integer>> history;
    private String xAxisName;
    private String yAxisName;

    public List<Map.Entry<LocalDateTime, Integer>> getHistory() {
        return history;
    }

    public void setHistory(List<Map.Entry<LocalDateTime, Integer>> history) {
        this.history = history;
    }

    public String getxAxisName() {
        return xAxisName;
    }

    public void setxAxisName(String xAxisName) {
        this.xAxisName = xAxisName;
    }

    public String getyAxisName() {
        return yAxisName;
    }

    public void setyAxisName(String yAxisName) {
        this.yAxisName = yAxisName;
    }

    public History(List<Map.Entry<LocalDateTime, Integer>> history, String xAxisName, String yAxisName) {
        this.history = history;
        this.xAxisName = xAxisName;
        this.yAxisName = yAxisName;
    }
}
