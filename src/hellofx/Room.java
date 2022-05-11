package hellofx;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//Object class
public class Room {
    private int id;
    private String name;
    private String date;
    private String time;
    private String status;
    private String price;
    private String timeset;
    private int capacity;

    //Constructor
    public Room(int id, String name, String date, String time, String status, String price, int capacity, String timeset) throws ParseException{
        this.id = id;
        this.name = name;
        this.date = getDateFormat(date);

        //Time format e.g: 12:30 PM
        this.time = getTimeFormat(time) + " " + timeset;

        this.status = status;

        //Format price e.g: $5.00
        this.price = String.format("$%s", price);
        this.capacity = capacity;

        this.timeset = timeset;
    }

    //get room id
    public int getId(){
        return id;
    }
    
    //get room name
    public String getName(){
        return name;
    }

    //get timeset name
    public String getTimset(){
        return timeset;
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

    //Get date
    public String getDate(){
        return date;
    }

    //Get time
    public String getTime(){
        return time;
    }   

    //Get room status
    public String getStatus(){
        return status;
    }
    
    //Get price
    public String getPrice(){
        return price;
    }

    //Get room capacity
    public int getCapacity(){
        return capacity;
    }

    public static Object getSelectionModel() {
        return null;
    }
}