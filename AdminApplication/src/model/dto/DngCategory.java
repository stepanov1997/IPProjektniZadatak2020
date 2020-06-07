package model.dto;

import java.io.Serializable;

public class DngCategory implements Serializable {
    private Integer id;
    private String name;

    public DngCategory() {
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
