package com.rafsan.inventory.model;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.dao.AbstractGenericDao;
import com.rafsan.inventory.dao.CategoryDao;
import com.rafsan.inventory.entity.Category;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class CategoryModel extends AbstractGenericDao<Category> implements CategoryDao {

    public CategoryModel() {
        super(Category.class);
    }

}
