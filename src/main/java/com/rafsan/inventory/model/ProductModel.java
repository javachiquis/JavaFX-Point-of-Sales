package com.rafsan.inventory.model;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.dao.AbstractGenericDao;
import com.rafsan.inventory.dao.ProductDao;
import com.rafsan.inventory.entity.Product;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

public class ProductModel extends AbstractGenericDao<Product> implements ProductDao {

    public ProductModel() {
        super(Product.class);
    }

    @Override
    public Product getProductByName(String productName) {
        try {
            startOperation();
            Query query = getSession().createQuery("from Product where productName=:name");
            query.setParameter("name", productName);
            commit();
            Product product = (Product) query.uniqueResult();
            return product;
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(getSession());
        }

        return null;
    }

    @Override
    public void increaseProduct(Product product) {
        saveOrUpdate(product);
    }

    @Override
    public void decreaseProduct(Product product) {
        saveOrUpdate(product);
    }

    @Override
    public ObservableList<String> getProductNames() {
    
        /*session.beginTransaction();
        Criteria criteria = session.createCriteria(Product.class);
        criteria.setProjection(Projections.property("productName"));
        ObservableList<String> list = FXCollections.observableArrayList(criteria.list());
        session.getTransaction().commit();
        closeSession();
        
        return list;*/
        return null;
    }
}
