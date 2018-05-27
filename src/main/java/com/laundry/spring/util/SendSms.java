package com.laundry.spring.util;

/**
 * Created by System-2 on 1/23/2017.
 */

import com.laundry.spring.config.ConfigProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SendSms {

    private String route = "4";

    //Prepare Url
    private URLConnection myURLConnection = null;
    private URL myURL = null;
    private BufferedReader reader = null;

    //Send SMS API
    private String mainUrl = "https://control.msg91.com/api/sendhttp.php?";

    //Prepare parameter string
    private final StringBuilder sbPostData = new StringBuilder(mainUrl);

    public Boolean sendSMS(String mobiles, String message) {
        if (mobiles == null || mobiles.equals("")) {
            return false;
        }
        Boolean isProcessed = Boolean.FALSE;

        //encoding message
        String encoded_message = URLEncoder.encode(message);
        encoded_message = URLEncoder.encode(encoded_message);

        sbPostData.append("authkey=" + ConfigProperties.api_key);
        sbPostData.append("&mobiles=" + mobiles);
        sbPostData.append("&message=" + encoded_message);
        sbPostData.append("&route=" + route);
        sbPostData.append("&sender=" + ConfigProperties.short_code);
        sbPostData.append("&campaign=" + ConfigProperties.campaign);
        try {
            //final string
            mainUrl = sbPostData.toString();
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();

            myURLConnection.connect();

            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            String response;
            while ((response = reader.readLine()) != null) {
            }
            reader.close();

            isProcessed = Boolean.TRUE;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isProcessed;
    }


    public Boolean NewUserSignup(String mobiles, int otp) {
        String message = "Your verification code is "+otp+".";
        return sendSMS(mobiles, message);
    }

}