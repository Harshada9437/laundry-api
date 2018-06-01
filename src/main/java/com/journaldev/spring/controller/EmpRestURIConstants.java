package com.journaldev.spring.controller;

/**
 * Created by Sumedh on 10-05-2018.
 */
public interface EmpRestURIConstants {
    String DUMMY_EMP = "/rest/emp/dummy";
    String GET_EMP = "/rest/emp/{id}";
    String GET_ALL_EMP = "/rest/emps";
    String CREATE_EMP = "/rest/emp/create";
    String DELETE_EMP = "/rest/emp/delete/{id}";
    String To_Login = "/rest/user/login";
    String to_see_userList ="/rest/user/list/{id}";

}
