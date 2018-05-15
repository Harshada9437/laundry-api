package com.laundry.spring.dao;

import com.laundry.spring.*;
import com.laundry.spring.DTO.*;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAO {
    public Boolean createUser(UserDto userDto, int outletId) throws SQLException {
    boolean isCreated = false;
    PreparedStatement preparedStatement = null;
    Connection connection = null;
        try

    {
        int parameterIndex = 1;
        connection = new ConnectionHandler().getConnection();
        connection.setAutoCommit(false);
        preparedStatement = connection
                .prepareStatement("INSERT  INTO user_registration (name,contact_no, email , add_id, username, password) VALUES(?,?,?,?,?,?,?) ");

        preparedStatement.setString(parameterIndex++, userDto.getName());

        preparedStatement.setString(parameterIndex++, userDto.getContactNo());

        preparedStatement.setString(parameterIndex++, userDto.getEmail());

        int addId = getAddressId(userDto.getComplexId(),userDto.getWingId(),userDto.getFlatNo(),connection);
        preparedStatement.setInt(parameterIndex++, addId);

        preparedStatement.setString(parameterIndex++, userDto.getUsername());

        preparedStatement.setString(parameterIndex++, userDto.getPassword());

        int i = preparedStatement.executeUpdate();
        if (i > 0) {
            connection.commit();
            isCreated = Boolean.TRUE;
        } else {
            connection.rollback();
        }
    } catch(
    SQLException sqlException)

    {
        sqlException.printStackTrace();
        throw sqlException;
    } finally

    {
        try {
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        return isCreated;
}

    private int getAddressId(int complexId, int wingId, int flatNo,Connection connection) throws SQLException {
        int id=0;
        PreparedStatement statement = null;
        try {

            StringBuffer query = new StringBuffer("select id from address where comp_id =? and wing_id=? and flat_no=?");
            statement = connection.prepareStatement(query.toString());
            statement.setInt(1,complexId);
            statement.setInt(2,wingId);
            statement.setInt(3,flatNo);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id=resultSet.getInt("id");
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

    public CustomerDTO getCustomer(String customerId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        CustomerDTO customerDTO = new CustomerDTO();
        try {
            connection = new ConnectionHandler().getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("select u.id,u.name,u.contact_no,u.email,u.status,u.created_date,c.comp_name,a.flat_no,t.label,w.label as wing from user_registration u\n" +
                    "\tjoin user_type t on t.id=u.type_id\n" +
                    "\tjoin address a on a.id=u.add_id\n" +
                    "\tjoin complex_details c on c.id=a.comp_id\n" +
                    "\tjoin wings w on w.id=a.wing_id\n" +
                    "\twhere u.id=?");
            statement = connection.prepareStatement(query.toString());
            statement.setString(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customerDTO.setId(resultSet.getInt("id"));
                customerDTO.setType(resultSet.getString("label"));
                customerDTO.setWing(resultSet.getString("wing"));
                customerDTO.setName(resultSet.getString("name"));
                customerDTO.setContact_no(resultSet.getString("contact_no"));
                customerDTO.setFlat_no(resultSet.getString("flat_no"));
                customerDTO.setEmail(resultSet.getString("email"));
                customerDTO.setStatus(resultSet.getString("status"));
                customerDTO.setCreated_date(DateUtil.getDateStringFromTimeStamp(resultSet.getTimestamp("created_date")));
                customerDTO.setComp_name(resultSet.getString("comp_name"));
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

    public static String getToken(VerifyOtpDTO verifyOtpDTO) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String token = "";
        try {
            connection = new ConnectionHandler().getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("select token from user_otp where mobile=? and otp=?;");

            int parameterIndex=1;
            statement = connection.prepareStatement(query.toString());
            statement.setString(parameterIndex++,verifyOtpDTO.getMobile());
            statement.setString(parameterIndex++,verifyOtpDTO.getOtp());

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

    public List<CustomerDTO> getAllUserList(int typeId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<CustomerDTO> typeRequest = new ArrayList<CustomerDTO>();
        try {
            connection = new ConnectionHandler().getConnection();

            StringBuilder query = new StringBuilder("select u.id,u.name,u.contact_no,u.email,u.status,u.created_date,c.comp_name,a.flat_no,t.label,w.label as wing from user_registration u \n"+
                    "\tjoin user_type t on t.id=u.type_id \n"+
                    "\tjoin address a on a.id=u.add_id \n"+
                    "\tjoin complex_details c on c.id=a.comp_id \n"+
                    "\tjoin wings w on w.id=a.wing_id \n"+
                    "\twhere u.type_id=2)");

            statement = connection.prepareStatement(query.toString());
            statement.setInt(1,typeId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setId(resultSet.getInt("id"));
                customerDTO.setType(resultSet.getString("label"));
                customerDTO.setWing(resultSet.getString("wing"));
                customerDTO.setName(resultSet.getString("name"));
                customerDTO.setContact_no(resultSet.getString("contact_no"));
                customerDTO.setFlat_no(resultSet.getString("flat_no"));
                customerDTO.setEmail(resultSet.getString("email"));
                customerDTO.setStatus(resultSet.getString("status"));
                customerDTO.setCreated_date(DateUtil.getDateStringFromTimeStamp(resultSet.getTimestamp("created_date")));
                customerDTO.setComp_name(resultSet.getString("comp_name"));
                typeRequest.add(customerDTO);
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
        return typeRequest;
    }


    public int insertOtp(OtpDTO otpDTO) throws SQLException {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        StringBuilder query = new StringBuilder("insert into user_otp ('mobile','otp','token') values (?,?,?)");
        int id = 0;
        try {
            int parameterIndex = 1;
            connection = new ConnectionHandler().getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setString(parameterIndex++,otpDTO.getMobile());
            preparedStatement.setString(parameterIndex++, otpDTO.getOtp());
            preparedStatement.setString(parameterIndex++, otpDTO.getToken());

            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                connection.commit();
            } else {
                connection.rollback();
            }
            try {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                    connection.commit();
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                throw e;
            }
        } catch (SQLException sqlException) {
            connection.rollback();
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

    public static Boolean getValidationForEmail(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        Boolean isProcessed = Boolean.FALSE;
        try {
            connection = new ConnectionHandler().getConnection();

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



    public static Boolean getValidationForPhoneNumber(String mobile) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        Boolean isProcessed = Boolean.FALSE;
        try {

            connection = new ConnectionHandler().getConnection();

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


    public List<String> getFlats(int complexId,int wingId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<String> flats = new ArrayList<String>();
        try {

            connection = new ConnectionHandler().getConnection();

            String query = "select flat_no from address where comp_id=? and wing_id=?";

            statement = connection.prepareStatement(query);

            statement.setInt(1,complexId);
            statement.setInt(2,wingId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                flats.add(resultSet.getString("flat_no"));
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
        return flats;
    }

    public Boolean matchToken(String mobile,String token) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        Boolean isvalid=Boolean.FALSE;
        try {

            connection = new ConnectionHandler().getConnection();

            String query = "select id from user_otp where mobile=? and token=?";

            int parameterIndex=1;
            statement = connection.prepareStatement(query);
            statement.setString(parameterIndex++,mobile);
            statement.setString(parameterIndex++,token);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                isvalid = Boolean.TRUE;
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
        return isvalid;
    }

    public List<ClotheDTO> getWings(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<ClotheDTO> requestDtos = new ArrayList<ClotheDTO>();
        try {

            connection = new ConnectionHandler().getConnection();

            StringBuilder query = new StringBuilder("select w.id,c.comp_name,w.label from address a\n" +
                    "\tjoin complex_details c on a.comp_id=c.id\n" +
                    "\tjoin wings w on w.id=a.wing_id\n" +
                    "\twhere a.comp_id=?");

            statement = connection.prepareStatement(query.toString());
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ClotheDTO request = new ClotheDTO();
                request.setId(resultSet.getInt("id"));
                request.setName(resultSet.getString("label"));
                requestDtos.add(request);
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
        return requestDtos;
    }

}
