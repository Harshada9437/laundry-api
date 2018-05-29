package com.laundry.spring.controller;

import com.laundry.spring.bo.LaundryRequestBO;
import com.laundry.spring.bo.LaundryUpdateRequestBO;
import com.laundry.spring.model.LaundryRequest;
import com.laundry.spring.model.LaundryUpdateRequest;
import com.laundry.spring.requesthandler.RequestHandler;
import com.laundry.spring.response.GetLaundryResponse;
import com.laundry.spring.response.LaundryResponseList;
import com.laundry.spring.response.util.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RequestController {

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private RequestHandler requestHandler;
    @Autowired
    private MessageResponse messageResponse;

    @RequestMapping(value = RestURIConstants.CREATE_REQ, method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResponse createRequest(@RequestBody LaundryRequest laundryRequest) {
        logger.info("Start create request.");
        LaundryRequestBO laundryRequestBO = new LaundryRequestBO();
        try {
                laundryRequestBO.setUserId(laundryRequest.getUserId());
                laundryRequestBO.setPickupDate(laundryRequest.getPickupDate());
                laundryRequestBO.setRequests(laundryRequest.getRequests());

                int id = requestHandler.createRequest(laundryRequestBO);
            if(id==0) {
                messageResponse.setMessage(String.valueOf(id));
                messageResponse.setMessageType("SUCCESS");
                return messageResponse;
            }else{
                messageResponse.setMessage("Request creation failed.");
                messageResponse.setMessageType("FAILURE");
                return messageResponse;
            }
        } catch (Exception e) {
            messageResponse.setMessage("Something went wrong.");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }
    }

    @RequestMapping(value = RestURIConstants.UPDATE_REQ, method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResponse updateRequest(@RequestBody LaundryUpdateRequest laundryRequest) {
        logger.info("Start update request.");
        LaundryUpdateRequestBO laundryRequestBO = new LaundryUpdateRequestBO();
        try {
            laundryRequestBO.setId(laundryRequest.getId());
            laundryRequestBO.setStatus(laundryRequest.getStatus());

            Boolean isUpdated = requestHandler.updateRequest(laundryRequestBO);
            if(isUpdated) {
                messageResponse.setMessage("Status updated.");
                messageResponse.setMessageType("SUCCESS");
                return messageResponse;
            }else{
                messageResponse.setMessage("Request creation failed.");
                messageResponse.setMessageType("FAILURE");
                return messageResponse;
            }
        } catch (Exception e) {
            messageResponse.setMessage("Something went wrong.");
            messageResponse.setMessageType("FAILURE");
            return messageResponse;
        }
    }

    @RequestMapping(value = RestURIConstants.INFO_REQ, method = RequestMethod.GET)
    public
    @ResponseBody
    GetLaundryResponse userInfo(@PathVariable("id") int id) {
        logger.info("Start request details.");
        GetLaundryResponse userResp = new GetLaundryResponse();
        try {
            userResp = requestHandler.requestInfo(id);
            userResp.setMessage("User info.");
            userResp.setMessageType("SUCCESS");
            return userResp;
        } catch (Exception e) {
            userResp.setMessage("Something went wrong.");
            userResp.setMessageType("FAILURE");
            return userResp;
        }
    }

    @RequestMapping(value = RestURIConstants.LIST_STATUS_REQ, method = RequestMethod.GET)
    public
    @ResponseBody
    LaundryResponseList requestList(@PathVariable("id") int id) {
        logger.info("Start request list by status.");
        LaundryResponseList laundryResponseList = new LaundryResponseList();
        try {

            laundryResponseList.setRequests(requestHandler.requestList(id));
            laundryResponseList.setMessage("Request list.");
            laundryResponseList.setMessageType("SUCCESS");
            return laundryResponseList;
        } catch (Exception e) {
            e.printStackTrace();
            laundryResponseList.setMessage("Something went wrong.");
            laundryResponseList.setMessageType("FAILURE");
            return laundryResponseList;
        }
    }

    @RequestMapping(value = RestURIConstants.LIST_REQ, method = RequestMethod.GET)
    public
    @ResponseBody
    LaundryResponseList requestListByService(@PathVariable("service_id") int id) {
        logger.info("Start request list by service.");
        LaundryResponseList laundryResponseList = new LaundryResponseList();
        try {
            laundryResponseList.setRequests(requestHandler.requestListByService(id));
            laundryResponseList.setMessage("Request list.");
            laundryResponseList.setMessageType("SUCCESS");
            return laundryResponseList;
        } catch (Exception e) {
            e.printStackTrace();
            laundryResponseList.setMessage("Something went wrong.");
            laundryResponseList.setMessageType("FAILURE");
            return laundryResponseList;
        }
    }

}
