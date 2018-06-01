package com.journaldev.spring.dao;

import com.journaldev.spring.model.Employee;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EmployeeDAO {
    public Employee getDummyEmployee() {
        Employee emp = new Employee();
        emp.setId(9999);
        emp.setName("Dummy");
        emp.setCreatedDate(new Date());
        return emp;
    }
}
