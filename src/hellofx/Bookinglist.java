package hellofx;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Bookinglist {
    private int Rid;
    private String Rname;
    private String date; 
    private String time;
    private String DCode;
    private String timeset;
    private String price;

    //Constructor
    public Bookinglist(int Rid, String Rname, String date, String time, String DCode, String price, String timeset)throws ParseException{
        this.Rid = Rid;
        this.Rname = Rname;
        this.date = getDateFormat(date);
        this.time = getTimeFormat(time) + " " + timeset;
        this.DCode = DCode;
        this.price = price;
        this.timeset = timeset;

    }

    public int getId(){
        return Rid;
    }

    public String getRoomname(){
        return Rname;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public String getDiscountcode(){
        return DCode;
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
