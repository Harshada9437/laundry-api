package com.laundry.spring.dao;

import com.laundry.spring.bo.LaundryRequestBO;
import com.laundry.spring.bo.LaundryUpdateRequestBO;
import com.laundry.spring.model.ClotheRequest;
import com.laundry.spring.model.ServiceRequest;
import com.laundry.spring.util.CommaSeparatedString;
import com.laundry.spring.DTO.ClotheDTO;
import com.laundry.spring.DTO.RequestDto;
import com.laundry.spring.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RequestDAO {
    
    @Autowired
    private ConnectionHandler connectionHandler;

    public List<RequestDto> getRequest(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<RequestDto> request = new ArrayList<RequestDto>();
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("select  m.id as reqId, m.*,r.*,u.name,s.label,ct.label as category, c.name_of_cloth from request_master m\n" +
                    "\tjoin request_details r on m.id=r.request_id\n" +
                    "\tleft join clothes c on c.id = r.clothe_id\n" +
                    "\tjoin status s on s.id = m.`status`\n" +
                    "\tjoin category ct on ct.id = r.category_id\n" +
                    "\tjoin user_registration u on u.id=m.user_id\n" +
                    "\twhere m.id=?");
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

    public List<String> getClotheCategory(String ids) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<String> categories = new ArrayList<String>();
        try {

            List<Integer> id = CommaSeparatedString.split(ids);

            connection = connectionHandler.getConnection();

            String query = "select label from category where id in ";

            StringBuilder parameterBuilder = new StringBuilder();
            parameterBuilder.append(" (");
            for (int i = 0; i < id.size(); i++) {
                parameterBuilder.append("?");
                if (id.size() > i + 1) {
                    parameterBuilder.append(",");
                }
            }
            parameterBuilder.append(")");

            statement = connection.prepareStatement(query + parameterBuilder);
            for (int i = 1; i < id.size() + 1; i++) {
                statement.setInt(i, (int) id.get(i - 1));
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                categories.add(resultSet.getString("label"));
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
        return categories;
    }

    public List<ClotheDTO> getCategoryList() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<ClotheDTO> requestDtos = new ArrayList<ClotheDTO>();
        try {

            connection = connectionHandler.getConnection();

            StringBuilder query = new StringBuilder("select * from category");

            statement = connection.prepareStatement(query.toString());

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

    public List<ClotheDTO> getClotheList() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<ClotheDTO> requestDtos = new ArrayList<ClotheDTO>();
        try {

            connection = connectionHandler.getConnection();

            StringBuilder query = new StringBuilder("select * from clothes");

            statement = connection.prepareStatement(query.toString());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ClotheDTO request = new ClotheDTO();
                request.setId(resultSet.getInt("id"));
                request.setName(resultSet.getString("name_of_cloth"));
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

    public List<RequestDto> getRequestByCategory(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<RequestDto> requestDtos = new ArrayList<RequestDto>();
        try {

            connection = connectionHandler.getConnection();

            StringBuilder query = new StringBuilder("select r.id,u.name,c.name_of_cloth,r.no_of_cloth,s.label,m.created_date,m.updated_date from request_master m\n" +
                    "\tjoin request_details r on m.id=r.req_id\n" +
                    "\tjoin clothes c on c.id = r.cloth_id\n" +
                    "\tjoin status s on s.id = m.`status`\n" +
                    "\tjoin user_registration u on u.id=m.user_id\n" +
                    "\twhere r.category_id like ?");

            int parameterIndex=1;
            statement = connection.prepareStatement(query.toString());
            statement.setString(parameterIndex++,"%|"+id+"|%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RequestDto request = new RequestDto();
                request.setId(resultSet.getInt("id"));
                request.setCustomerName(resultSet.getString("name"));
                request.setClotheName(resultSet.getString("name_of_cloth"));
                request.setClotheCount(resultSet.getInt("no_of_cloth"));
                request.setStatus(resultSet.getString("label"));
                request.setCreateDate(DateUtil.getDateStringFromTimeStamp(resultSet.getTimestamp("created_date")));
                request.setUpdateDate(DateUtil.getDateStringFromTimeStamp(resultSet.getTimestamp("updated_date")));
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

    public int createRequest(LaundryRequestBO laundryRequestBO) throws SQLException {
        int id = 0;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            int parameterIndex = 1;
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement("INSERT  INTO request_master (pickup_date,userId) VALUES(?,?) ");


            preparedStatement.setString(parameterIndex++, laundryRequestBO.getPickupDate());

            preparedStatement.setInt(parameterIndex++, laundryRequestBO.getUserId());

            int i = preparedStatement.executeUpdate();

            try {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);

                    for (ServiceRequest request:laundryRequestBO.getRequests()) {
                        if(request.getClothes().size()==0) {
                            createSubRequest(id,request.getService(),null,connection,request.getRemark());
                        }else{
                            for (ClotheRequest clotheRequest:request.getClothes()) {
                                createSubRequest(id,request.getService(),clotheRequest,connection,request.getRemark());
                            }
                        }
                    }
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

    private void createSubRequest(int id,int service,ClotheRequest request,Connection connection,String remark) {
        PreparedStatement statement = null;

        try {
            int parameterIndex = 1;
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement("INSERT INTO request_details(request_id,category_id,clothe_id,count,remark) values(?,?,?,?,?)");

            statement.setInt(parameterIndex++, id);
            statement.setInt(parameterIndex++, service);
            if(request==null){
                statement.setInt(parameterIndex++, 0);
                statement.setInt(parameterIndex++, 0);
            }else {
                statement.setInt(parameterIndex++, request.getId());
                statement.setInt(parameterIndex++, request.getCount());
            }
            statement.setString(parameterIndex++, remark);

            int i = statement.executeUpdate();

            if (i > 0) {
                connection.commit();
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
    }

    public Boolean updateRequest(LaundryUpdateRequestBO laundryRequestBO) throws SQLException {
        Boolean isProcessed = Boolean.FALSE;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            int parameterIndex = 1;
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("UPDATE request_master SET status=?  WHERE id=?");

            statement.setInt(parameterIndex++, laundryRequestBO.getStatus());

            statement.setInt(parameterIndex++, laundryRequestBO.getId());

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

    public List<RequestDto> getRequestByStatus(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<RequestDto> request = new ArrayList<RequestDto>();
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("select  m.id as reqId,m.*,r.*,u.name,s.label,ct.label as category, c.name_of_cloth from request_master m\n" +
                    "\tjoin request_details r on m.id=r.request_id\n" +
                    "\tleft join clothes c on c.id = r.clothe_id\n" +
                    "\tjoin status s on s.id = m.`status`\n" +
                    "\tjoin category ct on ct.id = r.category_id\n" +
                    "\tjoin user_registration u on u.id=m.user_id\n" +
                    "\twhere m.status=?");
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

    public List<RequestDto> getRequestByService(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<RequestDto> request = new ArrayList<RequestDto>();
        try {
            connection = connectionHandler.getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("select  m.id as reqId,m.*,r.*,u.name,s.label,ct.label as category, c.name_of_cloth from request_master m\n" +
                    "\tjoin request_details r on m.id=r.request_id\n" +
                    "\tleft join clothes c on c.id = r.clothe_id\n" +
                    "\tjoin status s on s.id = m.`status`\n" +
                    "\tjoin category ct on ct.id = r.category_id\n" +
                    "\tjoin user_registration u on u.id=m.user_id\n" +
                    "\twhere r.category_id=?");
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
}
