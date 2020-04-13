package model.beans;

import model.dao.DangerCategoryDao;
import model.dto.DangerCategory;

import java.util.ArrayList;
import java.util.List;

public class DangerCategoryBean {
    private DangerCategoryDao dangerCategoryDao = new DangerCategoryDao();
    private List<DangerCategory> dangerCategories = new ArrayList<>();

    public DangerCategoryBean() {
    }

    public void importDangerCategories()
    {
        dangerCategories = dangerCategoryDao.getAll();
    }

    public List<DangerCategory> getDangerCategories() {
        return dangerCategories;
    }

    public void setDangerCategories(List<DangerCategory> dangerCategories) {
        this.dangerCategories = dangerCategories;
    }
}
