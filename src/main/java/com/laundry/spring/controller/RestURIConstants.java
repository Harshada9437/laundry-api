package com.laundry.spring.controller;

/**
 * Created by Sumedh on 10-05-2018.
 */
public class RestURIConstants {
    public static final String GET_OTP = "/rest/user/getOtp";
    public static final String VERIFY_OTP = "/rest/user/verifyOtp";
    public static final String CREATE_USER = "/rest/user/create";
    public static final String UPDATE_USER = "/rest/user/update";
    public static final String INFO_USER = "/rest/user/info/{id}";
    public static final String LIST_USER = "/rest/user/list/{type_id}";
    public static final String LOG_IN_USER = "/rest/user/login";
    public static final String LOG_OUT_USER = "/rest/user/logout";
    public static final String CHANGE_PWD = "/rest/user/changePassword";
    public static final String RESET_PWD = "/rest/user/resetPassword";
    public static final String LIST_REQUEST_BY_USER = "/rest/user/requestList/{id}";
    public  static final String CREATE_REQ  = "/rest/laundryRequest/create";
    public  static final String UPDATE_REQ  = "/rest/laundryRequest/update";
    public  static final String INFO_REQ  = "/rest/laundryRequest/info/{id}";
    public  static final String LIST_STATUS_REQ  = "/rest/laundryRequest/listByStatus/{id}";
    public  static final String LIST_REQ  = "/rest/laundryRequest/list/{service_id}";
}
