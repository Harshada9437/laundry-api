package com.laundry.spring.dao;


import com.laundry.spring.DTO.CustomerDTO;
import com.laundry.spring.DTO.RequestDto;
import com.laundry.spring.DTO.UserDto;
import com.laundry.spring.DTO.VerifyOtpDTO;
import com.laundry.spring.model.ChangePasswordBO;
import com.laundry.spring.model.LoginRequest;
import com.laundry.spring.model.LoginResponse;
import com.laundry.spring.model.ResetPasswordRequestBO;
import com.laundry.spring.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserDAO {

    @Autowired
    private ConnectionHandler connectionHandler;

    public void removeSessionId(String userName, String encodedPassword) throws SQLException {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement("UPDATE user_registration SET session_id ='' WHERE user_name =? and password=?");

            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,encodedPassword);
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

    public Boolean getValidationForEmail(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        Boolean isProcessed = Boolean.FALSE;
        try {
            connection = connectionHandler.getConnection();

            StringBuilder query = new StringBuilder(
                    "SELECT email FROM user_registration where email = ?");

            int parameterIndex=1;
            statement = connection.prepareStatement(query.toString());
            statement.setString(parameterIndex++,email);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                isProcessed = true;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isProcessed;
    }

    public Boolean getValidationForPhoneNumber(String mobile) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        Boolean isProcessed = Boolean.FALSE;
        try {

            connection = connectionHandler.getConnection();

            StringBuilder query = new StringBuilder("SELECT contact_no FROM user_registration where contact_no = ?");

            int parameterIndex=1;
            statement = connection.prepareStatement(query.toString());
            statement.setString(parameterIndex++,mobile);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                isProcessed = true;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isProcessed;
    }


    public Boolean changePassword(ChangePasswordBO changePwdBO) throws SQLException {
        Boolean isProcessed = Boolean.FALSE;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);

            String query = "SELECT password FROM user_registration WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1,changePwdBO.getId());
            ResultSet resultSet = statement.executeQuery();
            String oldDBpassword = null;
            while (resultSet.next()) {
                oldDBpassword = resultSet.getString("password");
            }

            if (oldDBpassword != null && changePwdBO.getOldPassword() != null
                    && oldDBpassword.equals(changePwdBO.getOldPassword())) {
                if (updatePassword(changePwdBO.getNewPassword(),
                        changePwdBO.getId(), connection)) {
                    connection.commit();
                    isProcessed = Boolean.TRUE;
                }
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
        return isProcessed;
    }

    private boolean updatePassword(String newPassword, int userId, Connection connection) throws SQLException {
        boolean isUpdated = false;
        connection.setAutoCommit(false);
        String query = "UPDATE user_registration SET password=? WHERE id=?";
        PreparedStatement preparedStatement = connection
                .prepareStatement(query);
        preparedStatement.setString(1,newPassword);
        preparedStatement.setInt(2,userId);
        int i = preparedStatement.executeUpdate();
        if (i > 0) {
            connection.commit();
            isUpdated = Boolean.TRUE;
        } else {
            connection.rollback();
        }
        return isUpdated;

    }

    public Boolean resetPassword(ResetPasswordRequestBO resetPwdBO) throws SQLException {
        Boolean isProcessed = Boolean.FALSE;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            int parameterIndex = 1;
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("UPDATE user_registration SET password=?  WHERE id=?");

            statement.setString(parameterIndex++, resetPwdBO.getNewPassword());

            statement.setInt(parameterIndex++, resetPwdBO.getId());

            int i = statement.executeUpdate();
            if (i > 0) {
                connection.commit();
                isProcessed = Boolean.TRUE;
            } else {
                connection.rollback();
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
        return isProcessed;
    }

    public List<RequestDto> requestList(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<RequestDto> request = new ArrayList<RequestDto>();
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("select m.id as reqId, m.*,r.*,u.name,s.label,ct.label as category, c.name_of_cloth from request_master m\n" +
                    "\tjoin request_details r on m.id=r.request_id\n" +
                    "\tleft join clothes c on c.id = r.clothe_id\n" +
                    "\tjoin status s on s.id = m.`status`\n" +
                    "\tjoin category ct on ct.id = r.category_id\n" +
                    "\tjoin user_registration u on u.id=m.user_id\n" +
                    "\twhere m.user_id=?");
            statement = connection.prepareStatement(query.toString());
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                RequestDto requestDto= new RequestDto();
                requestDto.setId(resultSet.getInt("reqId"));
                requestDto.setUserId(resultSet.getInt("user_id"));
                requestDto.setClotheName(resultSet.getString("name_of_cloth"));
                requestDto.setClotheId(resultSet.getInt("clothe_id"));
                requestDto.setStatus(resultSet.getString("label"));
                requestDto.setStatusId(resultSet.getInt("status"));
                requestDto.setCustomerName(resultSet.getString("name"));
                requestDto.setClotheCount(resultSet.getInt("count"));
                requestDto.setCreateDate(DateUtil.getDateStringFromTimeStamp(resultSet.getTimestamp("created_date")));
                requestDto.setUpdateDate(DateUtil.getDateStringFromTimeStamp(resultSet.getTimestamp("updated_date")));
                requestDto.setPickupDate(resultSet.getString("pickup_date"));
                requestDto.setCategory(resultSet.getString("category"));
                requestDto.setCategoryId(resultSet.getInt("category_id"));
                request.add(requestDto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return request;
    }

    public int getCustomerId(String mobile) throws SQLException{
        Connection connection = null;
        PreparedStatement statement = null;
        int id=0;
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("select id from user_registration where  contact_no=?");

            int parameterIndex=1;
            statement = connection.prepareStatement(query.toString());
            statement.setString(parameterIndex++,mobile);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id=resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public String verifyOtp(VerifyOtpDTO verifyOtpDTO) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        String where = "", token = "";
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);
            statement=connection.createStatement();

            if (!verifyOtpDTO.getOtp().equals("0")) {
                where = " and otp=" + verifyOtpDTO.getOtp();
            }

            StringBuilder query = new StringBuilder("SELECT isExpired,otp,token FROM user_otp where mobile =\"").append(verifyOtpDTO.getMobile()).append("\"").append(where);
            ResultSet resultSet = statement.executeQuery(query.toString());
            while (resultSet.next()) {
                if (resultSet.getString("isExpired").equals("NO")) {
                    token = resultSet.getString("token");
                    expireOtp(verifyOtpDTO.getOtp(), verifyOtpDTO.getMobile(), connection);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    public Boolean setOtp(String id, int otp) {
        Connection connection = null;
        PreparedStatement statement = null;
        Boolean isCreate = Boolean.FALSE;

        try {
            int parameterIndex = 1;
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement("INSERT INTO user_otp(mobile,otp,isExpired,token) values(?,?,?,?)");

            statement.setString(parameterIndex++, id);
            statement.setInt(parameterIndex++, otp);
            statement.setString(parameterIndex++, "NO");
            String token = UUID.randomUUID().toString();
            statement.setString(parameterIndex++, token);


            int i = statement.executeUpdate();

            if (i > 0) {
                connection.commit();
                isCreate = Boolean.TRUE;


            } else {
                connection.rollback();
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isCreate;
    }

    private Boolean expireOtp(String otp, String userId, Connection connection) throws SQLException {
        String where = "";
        Boolean isCreate = Boolean.FALSE;
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE user_otp SET isExpired=\"" + "YES" + "\" WHERE otp='" + otp + "' and mobile=\""
                            + userId + "\"" + where);


            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                connection.commit();
                isCreate = Boolean.TRUE;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw sqlException;
        }
        return isCreate;
    }

    public String getToken(String mobile) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String token = "";
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("SELECT token FROM user_otp where mobile=? and id=(select max(id) from  user_otp where mobile=?)");

            int parameterIndex=1;
            statement = connection.prepareStatement(query.toString());
            statement.setString(parameterIndex++,mobile);
            statement.setString(parameterIndex++,mobile);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                token = resultSet.getString("token");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    public String getOtp(String mob) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String token = "";
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("SELECT otp FROM user_otp where mobile=? and isExpired='NO'");

            statement = connection.prepareStatement(query.toString());
            statement.setString(1,mob);
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                token = resultSet.getString("otp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    public Boolean updateLogInSessionId(int userId, String sessionId)
            throws SQLException {
        boolean isUpdated = false;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = connectionHandler.getConnection();
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


    public UserDto getDetail(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        UserDto loginResponseDTO = new UserDto();
        try {
            connection = connectionHandler.getConnection();

            StringBuilder query = new StringBuilder("SELECT * FROM user_registration u where u.username =?");
            statement = connection.prepareStatement(query.toString());
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                loginResponseDTO.setId(resultSet.getInt("id"));
                loginResponseDTO.setUsername(resultSet.getString("username"));
                loginResponseDTO.setPassword(resultSet.getString("password"));
                loginResponseDTO.setSessionId(resultSet.getString("session_id"));
                loginResponseDTO.setStatus(resultSet.getString("status"));
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
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

    public Boolean updateSessionId(String sessionId, int userId) {
        boolean isUpdated = false;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);
            StringBuffer query = new StringBuffer(
                    "UPDATE user_registration SET session_id = ? WHERE id = ?");
            preparedStatement = connection.prepareStatement(query.toString());

            preparedStatement.setString(1, sessionId);
            preparedStatement.setInt(2, userId);

            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                connection.commit();
                isUpdated = Boolean.TRUE;
            } else {
                connection.rollback();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
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

    public int createUser(UserDto userDto) throws SQLException {
        int id = 0;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            int parameterIndex = 1;
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement("INSERT  INTO user_registration (name,contact_no, email , add_id, username, password) VALUES(?,?,?,?,?,?) ");

            preparedStatement.setString(parameterIndex++, userDto.getName());

            preparedStatement.setString(parameterIndex++, userDto.getContactNo());

            preparedStatement.setString(parameterIndex++, userDto.getEmail());

            int addId = getAddressId(userDto.getComplexId(), userDto.getWingId(), userDto.getFlatNo(), connection);
            preparedStatement.setInt(parameterIndex++, addId);

            preparedStatement.setString(parameterIndex++, userDto.getUsername());

            preparedStatement.setString(parameterIndex++, userDto.getPassword());

            int i = preparedStatement.executeUpdate();

            try {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                    connection.commit();
                } else {
                    connection.rollback();
                    throw new SQLException("Creating feedback failed, no ID obtained.");
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException sqlException)
        {
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
        return id;
    }

    public int updateUser(UserDto userDto) throws SQLException {
        int id = 0;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            int parameterIndex = 1;
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement("UPDATE user_registration SET name=?,contact_no=?, email=? , add_id=?, status=? WHERE  id=?");

            preparedStatement.setString(parameterIndex++, userDto.getName());

            preparedStatement.setString(parameterIndex++, userDto.getContactNo());

            preparedStatement.setString(parameterIndex++, userDto.getEmail());

            int addId = getAddressId(userDto.getComplexId(), userDto.getWingId(), userDto.getFlatNo(), connection);
            preparedStatement.setInt(parameterIndex++, addId);

            preparedStatement.setString(parameterIndex++, userDto.getStatus());

            preparedStatement.setInt(parameterIndex++, userDto.getId());

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
        return id;
    }

    private int getAddressId(int complexId, int wingId, String flatNo, Connection connection) throws SQLException {
        int id = 0;
        PreparedStatement statement = null;
        try {

            StringBuffer query = new StringBuffer("select id from address where comp_id =? and wing_id=? and flat_no=?");
            statement = connection.prepareStatement(query.toString());
            statement.setInt(1, complexId);
            statement.setInt(2, wingId);
            statement.setString(3, flatNo);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw sqlException;
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public UserDto userInfo(int customerId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        UserDto customerDTO = new UserDto();
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("select a.wing_id,a.comp_id,u.type_id,u.id,u.name,u.contact_no,u.email,u.status,u.created_date,c.comp_name,a.flat_no,t.label,w.label as wing from user_registration u\n" +
                    "\tjoin user_type t on t.id=u.type_id\n" +
                    "\tjoin address a on a.id=u.add_id\n" +
                    "\tjoin complex_details c on c.id=a.comp_id\n" +
                    "\tjoin wings w on w.id=a.wing_id\n" +
                    "\twhere u.id=?");
            statement = connection.prepareStatement(query.toString());
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customerDTO.setId(resultSet.getInt("id"));
                customerDTO.setType(resultSet.getString("label"));
                customerDTO.setTypeId(resultSet.getInt("type_id"));
                customerDTO.setWing(resultSet.getString("wing"));
                customerDTO.setWingId(resultSet.getInt("wing_id"));
                customerDTO.setName(resultSet.getString("name"));
                customerDTO.setContactNo(resultSet.getString("contact_no"));
                customerDTO.setFlatNo(resultSet.getString("flat_no"));
                customerDTO.setEmail(resultSet.getString("email"));
                customerDTO.setStatus(resultSet.getString("status"));
                customerDTO.setCreatedDate(DateUtil.getDateStringFromTimeStamp(resultSet.getTimestamp("created_date")));
                customerDTO.setCompName(resultSet.getString("comp_name"));
                customerDTO.setComplexId(resultSet.getInt("comp_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customerDTO;
    }

    public Boolean updateSessionId(String sessionIdL, String sessionId, int userId)
            throws SQLException {
        boolean isUpdated = false;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);
            StringBuffer query = new StringBuffer(
                    "UPDATE user_registration SET session_id=replace(\'" + sessionIdL + "\',\'|" + sessionId
                            + "|\','') WHERE id = " + userId);
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

    public String getSessionIdForUserId(int userId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String sessionId = "";
        try {
            connection = connectionHandler.getConnection();

            StringBuilder query = new StringBuilder("SELECT session_id FROM user_registration where id =?");
            statement = connection.prepareStatement(query.toString());
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                sessionId = resultSet.getString("session_id");
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


    public List<UserDto> userList(int customerId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<UserDto> users = new ArrayList<UserDto>();
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("select a.wing_id,a.comp_id,u.type_id,u.id,u.name,u.contact_no,u.email,u.status,u.created_date,c.comp_name,a.flat_no,t.label,w.label as wing from user_registration u\n" +
                    "\tjoin user_type t on t.id=u.type_id\n" +
                    "\tjoin address a on a.id=u.add_id\n" +
                    "\tjoin complex_details c on c.id=a.comp_id\n" +
                    "\tjoin wings w on w.id=a.wing_id\n" +
                    "\twhere u.type_id=?");
            statement = connection.prepareStatement(query.toString());
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserDto customerDTO = new UserDto();
                customerDTO.setId(resultSet.getInt("id"));
                customerDTO.setType(resultSet.getString("label"));
                customerDTO.setTypeId(resultSet.getInt("type_id"));
                customerDTO.setWing(resultSet.getString("wing"));
                customerDTO.setWingId(resultSet.getInt("wing_id"));
                customerDTO.setName(resultSet.getString("name"));
                customerDTO.setContactNo(resultSet.getString("contact_no"));
                customerDTO.setFlatNo(resultSet.getString("flat_no"));
                customerDTO.setEmail(resultSet.getString("email"));
                customerDTO.setStatus(resultSet.getString("status"));
                customerDTO.setCreatedDate(DateUtil.getDateStringFromTimeStamp(resultSet.getTimestamp("created_date")));
                customerDTO.setCompName(resultSet.getString("comp_name"));
                customerDTO.setComplexId(resultSet.getInt("comp_id"));
                users.add(customerDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public String getSessionIdForUser(String userName, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String sessionId = null;
        try {
            connection = connectionHandler.getConnection();

            StringBuilder query = new StringBuilder(
                    "SELECT session_id FROM user_registration where user_name =? and password=?");
            statement = connection.prepareStatement(query.toString());
            statement.setString(1,userName);
            statement.setString(2,password);
            ResultSet resultSet = statement.executeQuery();
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

}
