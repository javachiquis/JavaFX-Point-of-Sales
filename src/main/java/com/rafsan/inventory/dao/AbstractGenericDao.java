package com.rafsan.inventory.dao;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.exceptions.DataAccessLayerException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractGenericDao<T> implements GenericDao<T> {

    private final Class<T> entityClass;

    private SessionFactory sessionFactory;

    private Session session;
    private Transaction transaction;


    protected AbstractGenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
        HibernateUtil.buildIfNeeded();
    }

    /*@Override
    public T findById(final Serializable id) {
        T entity = null;
        try {
            startOperation();
            //entity = (T) session.load(entityClass, id);
            entity = (T) session.byId(entityClass).getReference(id);
            transaction.commit();
            return entity;
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(session);
        }
        return entity;
    }*/

    @Override
    public T findById(final Serializable id) {
        T entity = null;
        try {
            startOperation();
            Query query = session.createQuery("from " + entityClass.getName() + " where id = :id");
            query.setParameter("id", id);
            entity = (T) query.getSingleResult();
            transaction.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateUtil.close(session);
        }
        return entity;
    }

    @Override
    public Serializable save(T entity) {
        Serializable identifier = null;
        try {
            startOperation();
            identifier = session.save(entity);
            transaction.commit();
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(session);
        }
        return identifier;
    }

    @Override
    public void saveOrUpdate(T entity) {
        try {
            startOperation();
            session.saveOrUpdate(entity);
            transaction.commit();
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(session);
        }
    }

    @Override
    public void delete(T entity) {
        try {
            startOperation();
            session.delete(entity);
            transaction.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateUtil.close(session);
        }
    }

    @Override
    public List<T> findAll() {
        List<T> objects = null;
        try {
            startOperation();
            Query query = session.createQuery("from " + entityClass.getName());
            objects = query.list();
            transaction.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateUtil.close(session);
        }
        return objects;
    }

    @Override
    public List<T> findAllByDateDesc() {
        List<T> objects = null;
        try {
            startOperation();
            Query query = session.createQuery("from " + entityClass.getName() + " order by datetime desc");
            objects = query.list();
            transaction.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateUtil.close(session);
        }
        return objects;
    }

    @Override
    public void clear() {
        session.clear();

    }

    @Override
    public void flush() {
        session.flush();

    }

    protected void handleException(HibernateException e) throws DataAccessLayerException {
        HibernateUtil.rollback(transaction);
        throw new DataAccessLayerException(e);
    }

    protected void startOperation() throws HibernateException {
        session = HibernateUtil.openSession();
        transaction = session.beginTransaction();
    }

    public Session getSession() {
        return session;
    }

    public void commit(){
        transaction.commit();
    }
}
