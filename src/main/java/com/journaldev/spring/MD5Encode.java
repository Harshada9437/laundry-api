package com.journaldev.spring;

import java.security.MessageDigest;

/**
 * Created by System1 on 9/29/2016.
 */
public class MD5Encode {
        public static String Encode(String password)throws Exception
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        }
}
