package com.journaldev.spring.dao;

import com.journaldev.spring.DTO.PreRequestDTO;
import com.journaldev.spring.UserNotFoundException;
import com.journaldev.spring.DTO.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UsersDAO {
    @Autowired
    ConnectionHandler connectionHandler;

    public int createUser(LoginResponseDTO userDto) throws Exception {
        int id = 0;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            int parameterIndex = 1;
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement("INSERT  INTO user_registration (name ,email, username, password) VALUES(?,?,?,?) ");

            preparedStatement.setString(parameterIndex++, userDto.getName());

            preparedStatement.setString(parameterIndex++, userDto.getEmail());

            preparedStatement.setString(parameterIndex++, userDto.getUserName());

            preparedStatement.setString(parameterIndex++, userDto.getPassword());

            int i = preparedStatement.executeUpdate();

            try {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                    connection.commit();
                } else {
                    connection.rollback();
                    throw new Exception("Creating feedback failed, no ID obtained.");
                }
            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (Exception sqlException)
        {
            sqlException.printStackTrace();
            throw sqlException;
        } finally {
            try {
                connection.close();
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return id;
    }
    public String getSessionIdForUser(String userName, String password) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        String sessionId = null;
        try {
            connection = new ConnectionHandler().getConnection();
            statement = connection.createStatement();
            StringBuilder query = new StringBuilder(
                    "SELECT session_id FROM user_registration where user_name =\"" + userName + "\" and password=\"" + password + "\"");
            ResultSet resultSet = statement.executeQuery(query.toString());
            while (resultSet.next()) {
                sessionId = resultSet.getString(1);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw sqlException;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sessionId;
    }

    public Boolean updateLogInSessionId(int userId, String sessionId)
            throws SQLException {
        boolean isUpdated = false;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = new ConnectionHandler().getConnection();
            connection.setAutoCommit(false);
            StringBuffer query = new StringBuffer(
                    "UPDATE user_registration SET session_id = '").append(sessionId)
                    .append("' WHERE id = ").append(userId);
            preparedStatement = connection.prepareStatement(query.toString());

            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                connection.commit();
                isUpdated = Boolean.TRUE;
            } else {
                connection.rollback();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw sqlException;
        } finally {
            try {
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isUpdated;
    }

    public void removeSessionId(String userName, String encodedPassword) throws SQLException {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = new ConnectionHandler().getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement("UPDATE user_registration SET session_id =\"" + "\"" + " WHERE user_name =\"" + userName + "\"" + " and password=\"" + encodedPassword + "\"");

            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw sqlException;
        } finally {
            try {
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public LoginResponseDTO getUserDetailsWithName(String name) throws UserNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        try {
            connection = new ConnectionHandler().getConnection();

            StringBuilder query = new StringBuilder(
                    "SELECT * from user_registration u where u.user_name =?");
            statement=connection.prepareStatement(query.toString());
            statement.setString(1,name);

            ResultSet resultSet = statement.executeQuery(query.toString());
            int rowCount = 0;
            while (resultSet.next()) {
                loginResponseDTO.setUserName(resultSet.getString("user_name"));
                loginResponseDTO.setId(resultSet.getInt("id"));
                loginResponseDTO.setEmail(resultSet.getString("email"));
                loginResponseDTO.setName(resultSet.getString("name"));
                loginResponseDTO.setStatus(resultSet.getString("status"));
                loginResponseDTO.setPassword(resultSet.getString("password"));
                loginResponseDTO.setSessionId(resultSet.getString("session_id"));
                rowCount++;
            }
            if (rowCount == 0) {
                throw new UserNotFoundException("User name invalid");
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw sqlException;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return loginResponseDTO;
    }

    public List<PreRequestDTO> userList(int reqId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<PreRequestDTO> pRequest = new ArrayList<PreRequestDTO>();
        try {
            connection = connectionHandler.getConnection();

            StringBuilder query = new StringBuilder("select u.name,s.lable as status,m.created_date,d.no_of_cloth,c.name_of_cloth,d.req_id,m.user_id from request_master m \n"+
                    "\t join request_details d on m.id=d.req_id \n"+
                    "\t join clothes c on c.id = d.cloth_id \n"+
                    "\t join status s on s.id = m.status_id \n"+
                    "\t join user_registration u on u.id=m.user_id \n"+
                    "\t where m.user_id=? \n"+
                    "\t group by d.req_id");

            statement = connection.prepareStatement(query.toString());
            statement.setInt(1,reqId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PreRequestDTO preRequestDTO = new PreRequestDTO();
                preRequestDTO.setUser_id(resultSet.getInt("user_id"));
                preRequestDTO.setName(resultSet.getString("name"));
                preRequestDTO.setCreated_date(resultSet.getString("created_date"));
                preRequestDTO.setReq_id(resultSet.getInt("req_id"));
                preRequestDTO.setStatus(resultSet.getString("status"));
                preRequestDTO.setNo_of_cloth(resultSet.getInt("no_of_cloth"));
                preRequestDTO.setName_of_cloth(resultSet.getString("name_of_cloth"));

                pRequest.add(preRequestDTO);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw sqlException;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pRequest;
    }

}
