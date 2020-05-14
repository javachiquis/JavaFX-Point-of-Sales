package com.rafsan.inventory.dao;

import com.rafsan.inventory.entity.Employee;
import javafx.collections.ObservableList;

public interface EmployeeDao extends GenericDao<Employee> {

    Employee getEmployeeByName(String name);
    String getEmployeeType(String username);
    boolean checkPassword(String username,String password);
    boolean checkUser(String username);
}
