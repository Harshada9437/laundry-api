package com.laundry.spring.requesthandler;

import com.laundry.spring.DTO.RequestDto;
import com.laundry.spring.bo.LaundryRequestBO;
import com.laundry.spring.bo.LaundryUpdateRequestBO;
import com.laundry.spring.dao.RequestDAO;
import com.laundry.spring.model.ClotheRequest;
import com.laundry.spring.model.ServiceRequest;
import com.laundry.spring.response.GetLaundryResponse;
import com.laundry.spring.response.LaundryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RequestHandler {
    @Autowired
    private RequestDAO requestDAO;

    public int createRequest(LaundryRequestBO laundryRequestBO) throws SQLException {
        return requestDAO.createRequest(laundryRequestBO);

    }

    public Boolean updateRequest(LaundryUpdateRequestBO laundryRequestBO) throws SQLException {
        return requestDAO.updateRequest(laundryRequestBO);
    }

    public GetLaundryResponse requestInfo(int id) throws SQLException {
        GetLaundryResponse getLaundryResponse = buildinfoFromDto(requestDAO.getRequest(id));
            return getLaundryResponse;
        }

    private GetLaundryResponse buildinfoFromDto(List<RequestDto> requestDtos) {
        GetLaundryResponse getLaundryResponse = new GetLaundryResponse();

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

    public List<LaundryResponse> requestList(int id) throws SQLException {
        List<RequestDto> requestDtos = requestDAO.getRequestByStatus(id);
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

    public List<LaundryResponse> requestListByService(int id) throws SQLException {
        List<RequestDto> requestDtos = requestDAO.getRequestByService(id);
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
