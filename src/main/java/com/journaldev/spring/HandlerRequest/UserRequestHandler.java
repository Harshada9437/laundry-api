package com.journaldev.spring.HandlerRequest;

import com.journaldev.spring.DTO.PreRequestDTO;
import com.journaldev.spring.MD5Encode;
import com.journaldev.spring.Response.UserResponse;
import com.journaldev.spring.dao.UsersDAO;
import com.journaldev.spring.model.LoginRequest;
import com.journaldev.spring.BO.LoginResponseBO;
import com.journaldev.spring.DTO.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserRequestHandler {
             @Autowired
             UsersDAO usersDAO;


    public LoginResponseBO login(LoginRequest loginRequest)
            throws Exception {
        LoginResponseDTO loginResponseDTO = usersDAO.getUserDetailsWithName(loginRequest.getUserName());
        String userName = loginRequest.getUserName();
        LoginResponseBO loginResponseBO = new LoginResponseBO();
        String encodedPassword = MD5Encode.Encode(loginRequest.getPassword());
        String sessionId = usersDAO.getSessionIdForUser(userName, encodedPassword);
       // int count = getCount(sessionId);
        Boolean isValidUser = userName.equals(loginResponseDTO.getUserName()) && encodedPassword.equals
                (loginResponseDTO.getPassword());
        if (isValidUser) {
            //if (count < 16) {
                Long newSessionId = new Date().getTime();
                if (loginResponseDTO.getSessionId() == null) {
                    usersDAO.updateLogInSessionId(loginResponseDTO.getId(), "|" + String.valueOf(newSessionId) + "|");
                } else {
                    usersDAO.updateLogInSessionId(loginResponseDTO.getId(), loginResponseDTO.getSessionId() + "|" +
                            String.valueOf(newSessionId) + "|");
                }
                loginResponseBO.setSessionId(newSessionId + "@" + loginResponseDTO.getId());
                loginResponseBO.setStatus(loginResponseDTO.getStatus());


           /* } else {
                usersDAO.removeSessionId(userName, encodedPassword);
                loginResponseDTO.setSessionId("");
                Long newSessionId = new Date().getTime();
                if (loginResponseDTO.getSessionId() == null) {
                    usersDAO.updateLogInSessionId(loginResponseDTO.getId(), String.valueOf(newSessionId));
                } else {
                    usersDAO.updateLogInSessionId(loginResponseDTO.getId(), loginResponseDTO.getSessionId() + "|" +
                            String.valueOf(newSessionId) + "|");
                }
                loginResponseBO.setSessionId(newSessionId + "@" + loginResponseDTO.getId());
                loginResponseBO.setId(loginResponseDTO.getId());
                loginResponseBO.setUserName(loginResponseDTO.getUserName());

            }*/
        }
        return loginResponseBO;
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


    public List<UserResponse> getCustomers(int userId) throws SQLException {
        List<UserResponse> userResponse = buildUsersResponseFromDTO(usersDAO.userList(userId));
        return userResponse;
    }

    private List<UserResponse> buildUsersResponseFromDTO(List<PreRequestDTO> userDTOs) {

        List<UserResponse> customers = new ArrayList<UserResponse>();
        for (PreRequestDTO preRequestDTO : userDTOs ) {
            UserResponse userResponse = new UserResponse();
            userResponse.setUser_id(preRequestDTO.getUser_id());
            userResponse.setName(preRequestDTO.getName());
            userResponse.setName_of_cloth(preRequestDTO.getName_of_cloth());
            userResponse.setNo_of_cloth(preRequestDTO.getNo_of_cloth());
            userResponse.setReq_id(preRequestDTO.getReq_id());
            userResponse.setStatus(preRequestDTO.getStatus());
            customers.add(userResponse);
        }
        return customers;
    }
}
