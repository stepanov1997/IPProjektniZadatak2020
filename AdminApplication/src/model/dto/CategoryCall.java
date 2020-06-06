package model.dto;

import java.io.Serializable;

public class CategoryCall implements Serializable {
    private Integer id;
    private String name;

    public CategoryCall() {
    }

    public CategoryCall(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
