package com.rafsan.inventory.dao;

import com.rafsan.inventory.entity.Invoice;
import javafx.collections.ObservableList;

public interface InvoiceDao extends GenericDao<Invoice> {
    ObservableList<Integer> getInvoiceYearsByMonth(int monthIndex);

    ObservableList<Invoice> findAllInvoicesByMonth(Integer month);
}
