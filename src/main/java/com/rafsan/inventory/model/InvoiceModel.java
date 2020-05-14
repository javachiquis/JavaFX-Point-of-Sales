package com.rafsan.inventory.model;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.dao.AbstractGenericDao;
import com.rafsan.inventory.dao.InvoiceDao;
import com.rafsan.inventory.entity.Employee;
import com.rafsan.inventory.entity.Invoice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import java.util.List;

public class InvoiceModel extends AbstractGenericDao<Invoice> implements InvoiceDao {

    public InvoiceModel() {
        super(Invoice.class);
    }

    @Override
    public ObservableList<Integer> getInvoiceYearsByMonth(int monthIndex) {
        try {
            startOperation();
            Query query = getSession().createNativeQuery("SELECT DISTINCT(YEAR(datetime)) FROM invoices WHERE MONTH(datetime) = :monthIndex");
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
    public ObservableList<Invoice> findAllInvoicesByMonth(Integer monthIndex) {
        try {
            ObservableList<Invoice> invoices = FXCollections.observableArrayList();
            List<Invoice> queryList = null;
            startOperation();
            //Query<Invoice> query = getSession().createNativeQuery("SELECT * FROM invoices WHERE MONTH(datetime) = :monthIndex");
            Query query = getSession().createNamedQuery("invoiceMonthlyQuery", Invoice.class);
            query.setParameter("monthIndex", monthIndex);
            commit();

            queryList = query.list();
            invoices.addAll(queryList);

            return invoices;
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(getSession());
        }

        return null;
    }

}
