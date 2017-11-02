package sample.Controllers;

import java.util.*;
import java.text.*;

public class DateDemo {

    public static int period(Date date)
    {
        Date dNow = new Date( );
        long a = (dNow.getTime() - date.getTime()) / 86400000;
        if (a > 0)
        {return ((int) a);}
        else {return 0;}
    }

    public static Date parseDate(String date)
    {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(date);}
        catch (ParseException e)
        {
            return null;
        }
    }
    public static void main(String args[]) {
        Date dNow = new Date();
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy-MM-dd");
        dNow = parseDate("2017-03-09");
        System.out.println("Current Date: " + ft.format(dNow)+ " et de periode "+ period(dNow));
    }
}
/**
 * Created by NS on 13/03/2017.
 */
