package com.laundry.spring.requesthandler;


import com.laundry.spring.DTO.RequestDto;
import com.laundry.spring.DTO.UserDto;
import com.laundry.spring.DTO.VerifyOtpDTO;
import com.laundry.spring.bo.UserBO;
import com.laundry.spring.dao.UserDAO;
import com.laundry.spring.model.*;
import com.laundry.spring.response.LaundryResponse;
import com.laundry.spring.response.UserResponse;
import com.laundry.spring.util.MD5Encode;
import com.laundry.spring.util.SendSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class UserRequestHandler {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserDto userDto;



    public String verifyOtp(VerifyOtpRequestBO verifyOtpRequestBO) throws SQLException {
        VerifyOtpDTO verifyOtpDTO = new VerifyOtpDTO();

        verifyOtpDTO.setOtp(verifyOtpRequestBO.getOtp());
        verifyOtpDTO.setMobile(verifyOtpRequestBO.getMobile());

        String token = userDAO.verifyOtp(verifyOtpDTO);

        return token;
    }

    public Boolean verifyEmail(String email) throws SQLException {

        Boolean isProcessed;
        try {
            if (!email.trim().equals("")) {
                isProcessed = userDAO.getValidationForEmail(email);
            } else {
                return false;
            }
        } catch (SQLException sq) {
            isProcessed = false;
        }
        return isProcessed;
    }

    public Boolean verifyPhoneNumber(String mobile) throws SQLException {
        Boolean isProcessed;
        isProcessed = userDAO.getValidationForPhoneNumber(mobile);
        return isProcessed;
    }

    private Integer getCount(String sessionId) {
        int counter = 0;
        if (sessionId != null) {
            for (int i = 0; i < sessionId.length(); i++) {
                if (sessionId.charAt(i) == '|') {
                    counter++;
                }
            }
        }
        return counter;
    }

    public LoginResponse createLogin(LoginRequest loginRequest) throws Exception {
        LoginResponse loginResponse = new LoginResponse();
        UserDto userDto=userDAO.getDetail(loginRequest.getUsername());
        String encodedPass= MD5Encode.Encode(loginRequest.getPassword());
        String sessionId = userDAO.getSessionIdForUser(loginRequest.getUsername(), encodedPass);
        int count = getCount(sessionId);
        Boolean isValidUser = loginRequest.getUsername().equals(userDto.getUsername()) && encodedPass.equals
                (userDto.getPassword());
        Long newSessionId = new Date().getTime();
        if(isValidUser){
            if(count<16) {
                if (userDto.getSessionId() == null) {
                    userDAO.updateLogInSessionId(userDto.getId(), "|" + String.valueOf(newSessionId) + "|");
                } else {
                    userDAO.updateLogInSessionId(userDto.getId(), userDto.getSessionId() + "|" +
                            String.valueOf(newSessionId) + "|");
                }
            }else{
                userDAO.removeSessionId(loginRequest.getPassword(), encodedPass);
                userDAO.updateLogInSessionId(userDto.getId(), String.valueOf(newSessionId));
            }
            loginResponse.setId(userDto.getId());
            loginResponse.setSessionId(newSessionId + "@" + loginResponse.getId());
        }
        return loginResponse;
    }

    public Boolean resetPassword(ResetPasswordRequestBO resetPasswordRequestBO) throws Exception {

        String encodedNewPassword = MD5Encode.Encode(resetPasswordRequestBO.getNewPassword());
        resetPasswordRequestBO.setNewPassword(encodedNewPassword);
        Boolean isProcessed = userDAO.resetPassword(resetPasswordRequestBO);

        return isProcessed;
    }


    public int createUser(UserBO userBO) throws Exception{
        int id = userDAO.createUser(buildDtoFromBo(userBO));
        return id;
    }

    public boolean changePassword(ChangePasswordBO changePwdBO) throws Exception {

        String encodedOldPassword = MD5Encode.Encode(changePwdBO.getOldPassword());
        String encodedNewPassword = MD5Encode.Encode(changePwdBO.getNewPassword());
        changePwdBO.setOldPassword(encodedOldPassword);
        changePwdBO.setNewPassword(encodedNewPassword);
        Boolean isProcessed = userDAO.changePassword(changePwdBO);

        return isProcessed;
    }

    private UserDto buildDtoFromBo(UserBO userBO) throws Exception {
        userDto.setComplexId(userBO.getComplexId());
        userDto.setWingId(userBO.getComplexId());
        userDto.setName(userBO.getName());
        userDto.setContactNo(userBO.getContactNo());
        userDto.setEmail(userBO.getEmail());
        userDto.setFlatNo(userBO.getFlatNo());
        userDto.setUsername(userBO.getUsername());
        userDto.setPassword(MD5Encode.Encode(userBO.getPassword()));
        return userDto;
    }

    public int updateUser(UserBO userBO) throws Exception{
        int id = userDAO.updateUser(buildupdateDtoFromBo(userBO));
        return id;
    }

    private UserDto buildupdateDtoFromBo(UserBO userBO) throws Exception {
        userDto.setComplexId(userBO.getComplexId());
        userDto.setWingId(userBO.getComplexId());
        userDto.setName(userBO.getName());
        userDto.setContactNo(userBO.getContactNo());
        userDto.setEmail(userBO.getEmail());
        userDto.setFlatNo(userBO.getFlatNo());
        userDto.setId(userBO.getId());
        userDto.setStatus(userBO.getStatus());
        return userDto;
    }

    public UserResponse userInfo(int id) throws Exception{
        UserResponse user = buildinfoFromDto(userDAO.userInfo(id));
        return user;
    }

    private UserResponse buildinfoFromDto(UserDto userDto) throws Exception {
        UserResponse userResp = new UserResponse();
        userResp.setComplexId(userDto.getComplexId());
        userResp.setCompName(userDto.getCompName());
        userResp.setWingId(userDto.getComplexId());
        userResp.setWing(userDto.getWing());
        userResp.setName(userDto.getName());
        userResp.setContactNo(userDto.getContactNo());
        userResp.setEmail(userDto.getEmail());
        userResp.setFlatNo(userDto.getFlatNo());
        userResp.setId(userDto.getId());
        userResp.setStatus(userDto.getStatus());
        userResp.setCreatedDate(userDto.getCreatedDate());
        return userResp;
    }

    public List<UserDto> userList(int id) throws Exception{
        List<UserDto> user = userDAO.userList(id);
        return user;
    }

    public Boolean logout(int userId, String sessionIdOfUser) throws SQLException {
        String sessionId = userDAO.getSessionIdForUserId(userId);
        Boolean isLoggedOut = userDAO.updateSessionId(sessionId, sessionIdOfUser, userId);
        return isLoggedOut;
    }

    public int generateOtp(UserBO otpRequestBO) throws SQLException {
        int id = userDAO.getCustomerId(otpRequestBO.getContactNo());
        String otp1 = userDAO.getOtp(otpRequestBO.getContactNo());
        if (id == 0) {
            otp1 = verifyOtp1(otp1, otpRequestBO.getContactNo());
            SendSms sendSms = new SendSms();
            sendSms.NewUserSignup(otpRequestBO.getContactNo(), Integer.parseInt(otp1));

        } else {
            otp1 = verifyOtp1(otp1, otpRequestBO.getContactNo());
            SendSms sendSms = new SendSms();
            sendSms.NewUserSignup(otpRequestBO.getContactNo(), Integer.parseInt(otp1));
        }
        return id;
    }

    private String verifyOtp1(String otp1, String mobileNumber) throws SQLException {

        if (otp1 != null && otp1.equals("")) {
            Random rnd = new Random();
            int otp = 100000 + rnd.nextInt(900000);
            userDAO.setOtp(mobileNumber, otp);
            otp1 = otp + "";
        }
        return otp1;
    }

    public List<LaundryResponse> requestList(int id) throws SQLException {
        List<RequestDto> requestDtos = userDAO.requestList(id);
        List<RequestDto> requestData = new ArrayList<RequestDto>();
        List<LaundryResponse> responses = new ArrayList<LaundryResponse>();
        List<Integer> ids = new ArrayList<Integer>();
        int i=0;

        for (RequestDto request:requestDtos){
            if(!ids.contains(request.getId())){
                if(i>0) {
                    responses.add(formatRequest(requestData));
                    requestData = new ArrayList<RequestDto>();
                }
                requestData.add(request);
                ids.add(request.getId());
            }else{
                requestData.add(request);
            }
            i++;
            if (i==requestDtos.size()){
                responses.add(formatRequest(requestData));
            }
        }
        return responses;
    }

    private LaundryResponse formatRequest(List<RequestDto> requestDtos) {
        LaundryResponse getLaundryResponse = new LaundryResponse();

        RequestDto dto= requestDtos.get(0);
        getLaundryResponse.setUserId(dto.getUserId());
        getLaundryResponse.setUserName(dto.getCustomerName());
        getLaundryResponse.setPickupDate(dto.getPickupDate());
        getLaundryResponse.setCreatedDate(dto.getCreateDate());
        getLaundryResponse.setUpdatedDate(dto.getUpdateDate());
        getLaundryResponse.setStatus(dto.getStatus());
        getLaundryResponse.setStatusId(dto.getStatusId());
        List<String> ids = new ArrayList<String>();
        List<ClotheRequest> clothes=null;
        int i=0;
        List<ServiceRequest> serviceRequests = new ArrayList<ServiceRequest>();
        ServiceRequest service=null;
        for (RequestDto requestDto:requestDtos){
            if(!ids.contains(requestDto.getCategory())){
                if(i!=0){
                    service.setClothes(clothes);
                    serviceRequests.add(service);
                }
                service = new ServiceRequest();
                service.setService(requestDto.getCategoryId());
                service.setServiceName(requestDto.getCategory());
                service.setRemark(requestDto.getRemark());
                if(requestDto.getClotheId()>0) {
                    clothes = new ArrayList<ClotheRequest>();
                    ClotheRequest clotheRequest = new ClotheRequest();
                    clotheRequest.setCount(requestDto.getClotheCount());
                    clotheRequest.setId(requestDto.getClotheId());
                    clotheRequest.setName(requestDto.getClotheName());
                    clothes.add(clotheRequest);
                }else{
                    clothes=null;
                }
                ids.add(requestDto.getCategory());
            }else{
                ClotheRequest clotheRequest = new ClotheRequest();
                clotheRequest.setCount(requestDto.getClotheCount());
                clotheRequest.setId(requestDto.getClotheId());
                clotheRequest.setName(requestDto.getClotheName());
                clothes.add(clotheRequest);
            }
            i++;
            if(i==requestDtos.size()){
                service.setClothes(clothes);
                serviceRequests.add(service);
            }
        }
        getLaundryResponse.setRequests(serviceRequests);
        return getLaundryResponse;
    }
}
