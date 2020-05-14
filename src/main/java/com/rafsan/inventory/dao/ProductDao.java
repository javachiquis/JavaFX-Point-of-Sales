package com.rafsan.inventory.dao;

import com.rafsan.inventory.entity.Product;
import javafx.collections.ObservableList;

public interface ProductDao extends GenericDao<Product> {

    Product getProductByName(String productName);

    void increaseProduct(Product product);

    void decreaseProduct(Product product);

    ObservableList<String> getProductNames();
}
