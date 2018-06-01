package com.journaldev.spring.controller;

import com.journaldev.spring.BO.LoginResponseBO;
import com.journaldev.spring.HandlerRequest.UserRequestHandler;
import com.journaldev.spring.Response.LoginResponse;
import com.journaldev.spring.Response.MessageResponse;
import com.journaldev.spring.Response.UserListResponse;
import com.journaldev.spring.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    protected LoginRequest loginRequest;
    @Autowired
    private UserRequestHandler userRequestHandler;
    @Autowired
    private LoginResponse loginResponse;
    @Autowired
    private MessageResponse messageResponse;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = EmpRestURIConstants.To_Login, method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResponse login(@RequestBody LoginRequest loginRequest){

        try {
            LoginResponseBO loginResponseBO = userRequestHandler.login(loginRequest);

            if (loginResponseBO.getSessionId() != null && loginResponseBO.getStatus().equals("A")) {
                messageResponse.setMessage(loginResponseBO.getSessionId());
                messageResponse.setMessageType("SUCCESS");
                return messageResponse;

            } else {
                messageResponse.setMessage("Invalid password or inactive user.");
                messageResponse.setMessageType("FAILURE");
                return messageResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageResponse.setMessage("Invalid Log in");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }

    }

    @RequestMapping(value = EmpRestURIConstants.to_see_userList, method = RequestMethod.GET)
    public
    @ResponseBody
    UserListResponse requestList(@PathVariable("id") int userId) {

            UserListResponse userListResponse = new UserListResponse();

        try {
            userListResponse.setCustomers(userRequestHandler.getCustomers(userId));
            userListResponse.setMessage("List of registered customers.");
            userListResponse.setMessageType("Success");
            return userListResponse;


        } catch (Exception e) {
            e.printStackTrace();
            userListResponse.setMessage("something went wrong while processing.");
            userListResponse.setMessageType("FAILURE");
            return userListResponse;
        }
    }


}