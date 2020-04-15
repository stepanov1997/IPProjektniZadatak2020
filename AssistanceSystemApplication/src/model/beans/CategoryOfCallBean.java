package model.beans;

import model.dao.CategoryDao;
import model.dto.Category;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class CategoryOfCallBean implements Serializable {
    private Category category = new Category();
    private CategoryDao categoryDao = new CategoryDao();
    private List<Category> categories = new ArrayList<>();

    public CategoryOfCallBean() {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void importCategories()
    {
        categories = categoryDao.getAll();
    }
}
