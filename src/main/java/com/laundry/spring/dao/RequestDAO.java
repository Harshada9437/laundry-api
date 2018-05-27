package com.laundry.spring.dao;

import com.laundry.spring.util.CommaSeparatedString;
import com.laundry.spring.DTO.ClotheDTO;
import com.laundry.spring.DTO.RequestDto;
import com.laundry.spring.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {
    public RequestDto getRequest(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        RequestDto requestDto = new RequestDto();
        try {
            connection = new ConnectionHandler().getConnection();
            connection.setAutoCommit(false);

            StringBuilder query = new StringBuilder("select m.id,u.name,c.name_of_cloth,r.no_of_cloth,s.label,m.created_date,m.updated_date,r.category_id from request_master m\n" +
                    "\tjoin request_details r on m.id=r.req_id\n" +
                    "\tjoin clothes c on c.id = r.cloth_id\n" +
                    "\tjoin status s on s.id = m.`status`\n" +
                    "\tjoin user_registration u on u.id=m.user_id\n" +
                    "\twhere m.id=?");
            statement = connection.prepareStatement(query.toString());
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requestDto.setId(resultSet.getInt("id"));
                requestDto.setClotheName(resultSet.getString("name_of_cloth"));
                requestDto.setStatus(resultSet.getString("label"));
                requestDto.setCustomerName(resultSet.getString("name"));
                requestDto.setClotheCount(resultSet.getInt("no_of_cloth"));
                requestDto.setCreateDate(DateUtil.getDateStringFromTimeStamp(resultSet.getTimestamp("created_date")));
                requestDto.setUpdateDate(DateUtil.getDateStringFromTimeStamp(resultSet.getTimestamp("updated_date")));
                requestDto.setCategory(getClotheCategory(resultSet.getString("category_id")));
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
        return requestDto;
    }

    public List<String> getClotheCategory(String ids) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<String> categories = new ArrayList<String>();
        try {

            List<Integer> id = CommaSeparatedString.split(ids);

            connection = new ConnectionHandler().getConnection();

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

            connection = new ConnectionHandler().getConnection();

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

            connection = new ConnectionHandler().getConnection();

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

            connection = new ConnectionHandler().getConnection();

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

}
