package com.laundry.spring.controller;

import com.laundry.spring.model.Employee;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EmployeeDAO {
    Employee getDummyEmployee() {
       // logger.info("Start getDummyEmployee");
        Employee emp = new Employee();
        emp.setId(9999);
        emp.setName("Dummy");
        emp.setCreatedDate(new Date());
        //empData.put(9999, emp);
        return emp;
    }
}
