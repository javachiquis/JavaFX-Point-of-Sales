package com.rafsan.inventory.model;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.dao.AbstractGenericDao;
import com.rafsan.inventory.dao.SaleDao;
import com.rafsan.inventory.entity.Invoice;
import com.rafsan.inventory.entity.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import java.util.List;

public class SalesModel extends AbstractGenericDao<Sale> implements SaleDao {

    public SalesModel() {
        super(Sale.class);
    }

    @Override
    public ObservableList<Sale> getSaleByProductId(long id) {
        ObservableList<Sale> list = FXCollections.observableArrayList();
        try {
            startOperation();
            Query query = getSession().createQuery("from Sale where productId = :productId");
            query.setParameter("productId", id);
            commit();
            List<Sale> sales = query.list();
            sales.stream().forEach(list::add);
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(getSession());
        }
        return list;
    }

    @Override
    public ObservableList<Integer> getSalesYearsByMonth(int monthIndex){
        try {
            startOperation();
            Query query = getSession().createNativeQuery("SELECT DISTINCT(YEAR(datetime)) FROM sales WHERE MONTH(datetime) = :monthIndex");
            query.setParameter("monthIndex", monthIndex);
            commit();
            return FXCollections.observableArrayList(query.getResultList());
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(getSession());
        }

        return null;
    }

    @Override
    public ObservableList<Sale> findProductSalesByMonth(Integer monthIndex, Number productId) {
        try {
            ObservableList<Sale> sales = FXCollections.observableArrayList();
            List<Sale> queryList = null;
            startOperation();
            Query query = getSession().createNamedQuery("salesMonthlyQuery", Sale.class);
            query.setParameter("monthIndex", monthIndex);
            query.setParameter("productId", productId);
            commit();

            queryList = query.list();
            sales.addAll(queryList);

            return sales;
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(getSession());
        }

        return null;
    }

}
