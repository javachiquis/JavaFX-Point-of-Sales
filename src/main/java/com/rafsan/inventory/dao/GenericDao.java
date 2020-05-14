package com.rafsan.inventory.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T> {

    Serializable save(T entity);

    void saveOrUpdate(T entity);

    void delete(T entity);

    List<T> findAll();

    List<T> findAllByDateDesc();

    T findById(Serializable id);

    void clear();

    void flush();

}
