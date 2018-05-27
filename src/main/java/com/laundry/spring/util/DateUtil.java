package com.laundry.spring.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    // List of all date formats that we want to parse.
    // Add your own format here.
    private static List<SimpleDateFormat>
            dateFormats = new ArrayList<SimpleDateFormat>() {
        private static final long serialVersionUID = 1L;
        {
            add(new SimpleDateFormat("M/dd/yyyy"));
            add(new SimpleDateFormat("dd.M.yyyy"));
            add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
            add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
            add(new SimpleDateFormat("dd.MMM.yyyy"));
            add(new SimpleDateFormat("dd-MMM-yyyy"));
        }
    };

    /**
     * Convert String with various formats into java.util.Date
     *
     * @param input
     *            Date as a string
     * @return java.util.Date object if input string is parsed
     *          successfully else returns null
     */
    public static Date convertToDate(String input) {
        Date date = null;
        if(null == input) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                date = format.parse(input);
            } catch (ParseException e) {
                //Shhh.. try other formats
            }
            if (date != null) {
                break;
            }
        }

        return date;
    }
    public static Timestamp getTimeStampFromString(String dateStr) {
        return Timestamp.valueOf(dateStr);
    }

    public static String getDateStringFromTimeStamp(Timestamp timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
    }

    public static String getCurrentServerTime() {
        SimpleDateFormat sdfAmerica = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdfAmerica.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return sdfAmerica.format(new Date());
    }

    public static String getCurrentServerTimeByRemoteTimestamp(Timestamp timestamp) {
        SimpleDateFormat sdfAmerica = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdfAmerica.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return sdfAmerica.format(timestamp);
    }

    public static String getCurrentServerDate() {
        SimpleDateFormat sdfAmerica = new SimpleDateFormat("yyyy-MM-dd");
        sdfAmerica.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return sdfAmerica.format(new Date());
    }

    public static String getTimestampForReport(Timestamp timestamp){
        return new SimpleDateFormat("HH:mm:ss").format(timestamp);
    }

    public static int compareTimestampWithCurrentDate(Timestamp timestamp){
        DateTime dt = new DateTime(timestamp,
                DateTimeZone.forID("Asia/Kolkata"));
        DateTime currentDate = new DateTime(new Date(),
                DateTimeZone.forID("Asia/Kolkata"));
        DateTimeComparator comparator = DateTimeComparator.getInstance();
        return comparator.compare(dt,currentDate);
    }

    public static String format(Timestamp value, String format) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormatter.format(value.getTime());
    }
}
