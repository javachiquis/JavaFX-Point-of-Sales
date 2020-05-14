package com.rafsan.inventory.dao;

import com.rafsan.inventory.entity.Invoice;
import com.rafsan.inventory.entity.Sale;
import javafx.collections.ObservableList;

public interface SaleDao extends GenericDao<Sale> {

    ObservableList<Sale> getSaleByProductId(long id);

    ObservableList<Integer> getSalesYearsByMonth(int monthIndex);

    ObservableList<Sale> findProductSalesByMonth(Integer monthIndex, Number productId);
}
