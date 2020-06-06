package model.beans;

import model.dao.CategoryCallDao;
import model.dto.CategoryCall;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class CallCategoryBean implements Serializable {
    private CategoryCall category = new CategoryCall();
    private CategoryCallDao categoryDao = new CategoryCallDao();
    private List<CategoryCall> categories = new ArrayList<>();

    public CallCategoryBean() {
    }

    public CategoryCall getCategory() {
        return category;
    }

    public void setCategory(CategoryCall category) {
        this.category = category;
    }

    public List<CategoryCall> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryCall> categories) {
        this.categories = categories;
    }

    public void importCategories()
    {
        categories = categoryDao.getAll();
    }
}
