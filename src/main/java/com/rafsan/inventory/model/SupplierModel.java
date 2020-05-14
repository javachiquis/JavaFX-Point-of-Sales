package com.rafsan.inventory.model;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.dao.AbstractGenericDao;
import com.rafsan.inventory.dao.SupplierDao;
import com.rafsan.inventory.entity.Category;
import com.rafsan.inventory.entity.Supplier;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class SupplierModel extends AbstractGenericDao<Supplier> implements SupplierDao {

    public SupplierModel() {
        super(Supplier.class);
    }

}
