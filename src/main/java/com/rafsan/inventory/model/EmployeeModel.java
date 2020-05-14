package com.rafsan.inventory.model;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.dao.AbstractGenericDao;
import com.rafsan.inventory.dao.EmployeeDao;
import com.rafsan.inventory.entity.Employee;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

public class EmployeeModel extends AbstractGenericDao<Employee> implements EmployeeDao {

    public EmployeeModel() {
        super(Employee.class);
    }

    @Override
    public Employee getEmployeeByName(String username){
        Employee employee = new Employee();
        try {
            startOperation();
            Query query = getSession().createQuery("from Employee where userName = :username");
            query.setParameter("username", username);
            commit();
            employee = (Employee) query.getSingleResult();
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(getSession());
        }
        return employee;
    }

    @Override
    public String getEmployeeType(String username){
        Employee employee = new Employee();
        try {
            startOperation();
            Query query = getSession().createQuery("from Employee where userName = :username");
            query.setParameter("username", username);
            commit();
            employee = (Employee) query.getSingleResult();
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(getSession());
        }
        return employee.getType();
    }

    @Override
    public boolean checkUser(String username) {
        try {
            startOperation();
            Query query = getSession().createQuery("from Employee where userName = :username");
            query.setParameter("username", username);
            commit();
            Employee employee = (Employee) query.uniqueResult();
            return employee != null;
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(getSession());
        }

        return false;
    }
    
    @Override
    public boolean checkPassword(String username, String password) {

        try {
            startOperation();
            Query query = getSession().createQuery("from Employee where userName = :username");
            query.setParameter("username", username);
            commit();
            Employee employee = (Employee) query.uniqueResult();
            return employee.getPassword().equals(password);
        } catch (HibernateException ex) {
            handleException(ex);
        } finally {
            HibernateUtil.close(getSession());
        }

        return false;
    }

}
