package com.laundry.spring.controller;


import com.laundry.spring.bo.UserBO;
import com.laundry.spring.dao.UserDAO;
import com.laundry.spring.model.*;
import com.laundry.spring.requesthandler.UserRequestHandler;
import com.laundry.spring.response.LaundryResponseList;
import com.laundry.spring.response.UserResponseList;
import com.laundry.spring.response.util.MessageResponse;
import com.laundry.spring.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
public class UserController {
    @Autowired
    private UserRequestHandler userRequestHandler;
    @Autowired
    private MessageResponse messageResponse;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = RestURIConstants.LOG_IN_USER, method = RequestMethod.POST)
    public
    @ResponseBody
     MessageResponse login(@RequestBody LoginRequest loginRequest) {
        try{
            LoginResponse loginResponse=userRequestHandler.createLogin(loginRequest);
            if (loginResponse.getSessionId() != null && loginResponse.getStatus().equals("A")) {
                messageResponse.setMessage(loginResponse.getSessionId());
                messageResponse.setMessageType("SUCCESS");
                return messageResponse;
            }else {
                messageResponse.setMessage("Invalid credentials or Inactive user.");
                messageResponse.setMessageType("FAILURE");
                return messageResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageResponse.setMessage("Something went wrong.");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }

    }

    @RequestMapping(value = RestURIConstants.VERIFY_OTP, method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResponse verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest) {

        VerifyOtpRequestBO verifyOtpRequestBO = new VerifyOtpRequestBO();

        verifyOtpRequestBO.setMobile(verifyOtpRequest.getMobile());
        verifyOtpRequestBO.setOtp(verifyOtpRequest.getOtp());

        try {

                String token = userRequestHandler.verifyOtp(verifyOtpRequestBO);
                if (token.equals("")) {
                    messageResponse.setMessage("Invalid otp.");
                    messageResponse.setMessageType("FAILURE");
                    return messageResponse;
                } else {
                    messageResponse.setMessage(token);
                    messageResponse.setMessageType("SUCCESS");
                    return messageResponse;
                }
        } catch (Exception e) {
            e.printStackTrace();
            messageResponse.setMessage("Something went wrong.");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }
    }

    @RequestMapping(value = RestURIConstants.GET_OTP, method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResponse gteOtp(@RequestBody User user) {

        UserBO userBO = new UserBO();

        try {
            userBO.setComplexId(user.getComplexId());
            userBO.setWingId(user.getComplexId());
            userBO.setName(user.getName());
            userBO.setContactNo(user.getContactNo());
            userBO.setEmail(user.getEmail());
            userBO.setFlatNo(user.getFlatNo());
            userBO.setUsername(user.getUsername());
            userBO.setPassword(user.getPassword());

            if (!userRequestHandler.verifyEmail(userBO.getEmail())) {
                if (!userRequestHandler.verifyPhoneNumber(userBO.getContactNo())) {

                    int id = userRequestHandler.generateOtp(userBO);
                    messageResponse.setMessage("Otp sent to you on your registered mobile number.");
                    messageResponse.setMessageType("SUCCESS");
                    return messageResponse;
                } else {
                    messageResponse.setMessage( "Mobile number already exists.");
                    messageResponse.setMessageType("FAILURE");
                    return messageResponse;
                }
            } else {
                messageResponse.setMessage("Email address already exists.");
                messageResponse.setMessageType("FAILURE");
                return messageResponse;
            }
        } catch (Exception e) {
            messageResponse.setMessage("Something went wrong.");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }
    }


    @RequestMapping(value = RestURIConstants.CREATE_USER, method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResponse createUser(@RequestBody User user) {
        logger.info("Start create user.");
        UserBO userBO = new UserBO();
        try {
            userBO.setComplexId(user.getComplexId());
            userBO.setWingId(user.getComplexId());
            userBO.setName(user.getName());
            userBO.setContactNo(user.getContactNo());
            userBO.setEmail(user.getEmail());
            userBO.setFlatNo(user.getFlatNo());
            userBO.setUsername(user.getUsername());
            userBO.setPassword(user.getPassword());
            userBO.setToken(user.getToken());
            UserDAO userDAO = new UserDAO();
            String token= userDAO.getToken(userBO.getContactNo());
            if(token.equals(userBO.getToken())) {
                int id = userRequestHandler.createUser(userBO);
                messageResponse.setMessage(String.valueOf(id));
                messageResponse.setMessageType("SUCCESS");
                return messageResponse;
            }else{
                messageResponse.setMessage("Invalid user");
                messageResponse.setMessageType("FAILURE");
                return messageResponse;
            }
        } catch (Exception e) {
            messageResponse.setMessage("Something went wrong.");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }
    }

    @RequestMapping(value = RestURIConstants.UPDATE_USER, method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResponse updateUser(@RequestBody User user) {
        logger.info("Start create user.");
        UserBO userBO = new UserBO();
        try {
            userBO.setComplexId(user.getComplexId());
            userBO.setWingId(user.getComplexId());
            userBO.setName(user.getName());
            userBO.setContactNo(user.getContactNo());
            userBO.setEmail(user.getEmail());
            userBO.setFlatNo(user.getFlatNo());
            userBO.setId(user.getId());
            userBO.setStatus(user.getStatus());

            userRequestHandler.updateUser(userBO);
            messageResponse.setMessage("User updated successfully.");
            messageResponse.setMessageType("SUCCESS");
            return messageResponse;
        } catch (Exception e) {
            messageResponse.setMessage("Something went wrong.");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }
    }

    @RequestMapping(value = RestURIConstants.INFO_USER, method = RequestMethod.GET)
    public
    @ResponseBody
    UserResponse userInfo(@PathVariable("id") int id) {
        logger.info("Start user info.");
        UserResponse userResp = new UserResponse();
        try {
            userResp = userRequestHandler.userInfo(id);
            userResp.setMessage("User info.");
            userResp.setMessageType("SUCCESS");
            return userResp;
        } catch (Exception e) {
            userResp.setMessage("Something went wrong.");
            userResp.setMessageType("FAILURE");
            return userResp;
        }
    }

    @RequestMapping(value = RestURIConstants.LIST_USER, method = RequestMethod.GET)
    public
    @ResponseBody
    UserResponseList userList(@PathVariable("type_id") int id) {
        logger.info("Start user list.");
        UserResponseList userResp = new UserResponseList();
        try {

            userResp.setUsers(userRequestHandler.userList(id));
            userResp.setMessage("User list.");
            userResp.setMessageType("SUCCESS");
            return userResp;
        } catch (Exception e) {
            e.printStackTrace();
            userResp.setMessage("Something went wrong.");
            userResp.setMessageType("FAILURE");
            return userResp;
        }
    }

    @RequestMapping(value = RestURIConstants.LIST_REQUEST_BY_USER, method = RequestMethod.GET)
    public
    @ResponseBody
    LaundryResponseList requestList(@PathVariable("id") int id) {
        logger.info("Start request list.");
        LaundryResponseList laundryResponseList = new LaundryResponseList();
        try {

            laundryResponseList.setRequests(userRequestHandler.requestList(id));
            laundryResponseList.setMessage("User list.");
            laundryResponseList.setMessageType("SUCCESS");
            return laundryResponseList;
        } catch (Exception e) {
            e.printStackTrace();
            laundryResponseList.setMessage("Something went wrong.");
            laundryResponseList.setMessageType("FAILURE");
            return laundryResponseList;
        }
    }

    @RequestMapping(value = RestURIConstants.LOG_OUT_USER, method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResponse logout(@RequestHeader("sessionId") String sessionId) {
        try {
            String[] sessionIdParts = sessionId.split("@");
            Boolean isLoggedOut = userRequestHandler.logout(Integer.parseInt(sessionIdParts[1]), sessionIdParts
                    [0]);
            if (isLoggedOut) {
                messageResponse.setMessage("Successfully logged out.");
                messageResponse.setMessageType("SUCCESS");
                return messageResponse;
            } else {
                messageResponse.setMessage("Unable to log out the current user.");
                messageResponse.setMessageType("FAILURE");
                return messageResponse;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageResponse.setMessage("Something went wrong.");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }
    }

    @RequestMapping(value = RestURIConstants.CHANGE_PWD, method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResponse changePassword(@RequestBody ChangePasswordRequest changePwdReq) {
        ChangePasswordBO changePwdBO = new ChangePasswordBO(
                changePwdReq.getUserId(), changePwdReq.getOldPassword(),
                changePwdReq.getNewPassword());
        try {
            if (userRequestHandler.changePassword(changePwdBO)) {
                messageResponse.setMessage("Password has been changed.");
                messageResponse.setMessageType("SUCCESS");
                return messageResponse;
            } else {
                messageResponse.setMessage("Unable to change the password.");
                messageResponse.setMessageType("FAILURE");
                return messageResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageResponse.setMessage("Something went wrong.");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }
    }

    @RequestMapping(value = RestURIConstants.RESET_PWD, method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResponse resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            ResetPasswordRequestBO resetPasswordRequestBO = new ResetPasswordRequestBO(
                    resetPasswordRequest.getId(),
                    resetPasswordRequest.getNewPassword());
            Boolean isProceessed = userRequestHandler.resetPassword(resetPasswordRequestBO);
            if (isProceessed) {
                messageResponse.setMessage("Password has been reset.");
                messageResponse.setMessageType("SUCCESS");
                return messageResponse;
            } else {
                messageResponse.setMessage("Unable to reset password.");
                messageResponse.setMessageType("FAILURE");
                return messageResponse;
            }
        } catch (Exception e) {
            MessageResponse messageResponse = new MessageResponse();
            e.printStackTrace();
            messageResponse.setMessage("Something went wrong.");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }
    }
}
