package com.rafsan.inventory.model;

import com.rafsan.inventory.dao.AbstractGenericDao;
import com.rafsan.inventory.dao.PurchaseDao;
import com.rafsan.inventory.entity.Purchase;

public class PurchaseModel extends AbstractGenericDao<Purchase> implements PurchaseDao {

    public PurchaseModel() {
        super(Purchase.class);
    }

}
