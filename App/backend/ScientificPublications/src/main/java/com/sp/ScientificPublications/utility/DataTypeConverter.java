package com.sp.ScientificPublications.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTypeConverter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parseDate(String dateValue) {

        try {
            return sdf.parse(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String printDate(Date date) {

        if(date != null) {
            return sdf.format(date);
        }
        return null;

    }

}
