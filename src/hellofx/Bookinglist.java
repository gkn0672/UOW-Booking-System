package hellofx;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Bookinglist {
    private int id;
    private String Rname;
    private String date; 
    private String time;
    private String code;
    private String timeset;
    private String price;

    //Constructor
    public Bookinglist(int id, String Rname, String date, String time, String code, String price, String timeset)throws ParseException{
        this.id = id;
        this.Rname = Rname;
        this.date = getDateFormat(date);
        this.time = getTimeFormat(time) + " " + timeset;
        this.code = code;
        this.price = price;
        this.timeset = timeset;

    }

    public int getId(){
        return id;
    }

    public String getName(){
        return Rname;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public String getCode(){
        return code;
    }

    public String getTimeset(){
        return timeset;
    }

    public String getPrice(){
        return price;
    }


//Format date e.g: 12 Mar 2022
public String getDateFormat(String date) throws ParseException{
final String OLD_FORMAT = "yyyy-MM-dd";
final String NEW_FORMAT = "dd-MMM-yyyy";
    
String newDateString;
    
SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
Date d = (Date) sdf.parse(date);
sdf.applyPattern(NEW_FORMAT);
newDateString = sdf.format(d);
return newDateString;
}
    
//Format time e.g: 12:09 pm
public String getTimeFormat(String time) throws ParseException{
final String OLD_FORMAT = "hh:mm:ss";
final String NEW_FORMAT = "hh:mm";
    
String newTimeString;
    
SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
Date d = (Date) sdf.parse(time);
sdf.applyPattern(NEW_FORMAT);
newTimeString = sdf.format(d);
return newTimeString;
    }
    
}
