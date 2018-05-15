package com.laundry.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by System-2 on 2/23/2017.
 */
public class CommaSeparatedString {
    public static String generate(List<Integer> list) {
        String s = "";
        int i = 1;
        for (Integer id : list) {
            if (i == 1) {
                s = String.valueOf(id);
            } else {
                s = s + "," + String.valueOf(id);
            }
            i++;

        }
        return s;
    }

    public static List<Integer> split(String s){
        int i;
        List<Integer> integers = new ArrayList<Integer>();
        String a[] = s.trim().split(Pattern.quote(","));
        for (i = 0; i < a.length; i++) {
            integers.add(Integer.parseInt(a[i]));
        }
        return integers;
    }

}
