package model.beans;

import model.dao.DngCategoryDao;
import model.dto.DngCategory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@SessionScoped
public class DngCategoryBean implements Serializable {
    private DngCategory dngCategory = new DngCategory();
    private DngCategoryDao dangerCategoryDao = new DngCategoryDao();
    private List<DngCategory> dangerCategories = new ArrayList<>();

    public DngCategoryBean() {
    }

    public void importDangerCategories()
    {
        dangerCategories = dangerCategoryDao.getAll();
    }

    public DngCategory getDngCategory() {
        return dngCategory;
    }

    public void setDngCategory(DngCategory dngCategory) {
        this.dngCategory = dngCategory;
    }

    public DngCategoryDao getDangerCategoryDao() {
        return dangerCategoryDao;
    }

    public void setDangerCategoryDao(DngCategoryDao dangerCategoryDao) {
        this.dangerCategoryDao = dangerCategoryDao;
    }

    public List<DngCategory> getDangerCategories() {
        return dangerCategories;
    }

    public void setDangerCategories(List<DngCategory> dangerCategories) {
        this.dangerCategories = dangerCategories;
    }

    public String addCategory() {
        DngCategoryDao dngCategoryDao = new DngCategoryDao();
        if(dngCategoryDao.add(dngCategory)!=null){
            return "/admin_home.xhtml?faces-redirect=true";
        }else {
            return "";
        }
    }
}
