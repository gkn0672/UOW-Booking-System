package hellofx;

public class Room {
    private String name;
    private String date;
    private String time;
    private String status;
    private String promo;
    private double price;
    private int capacity;


    public Room(String name, String date, String time, String status, String promo, double price, int capacity){
        this.name = name;
        this.date = date;
        this.time = time;
        this.status = status;
        this.promo = promo;
        this.price = price;
        this.capacity = capacity;
    }

    public String getName(){
        return name;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public String getPromo(){
        return promo;
    }

    public String getStatus(){
        return status;
    }
    
    public double getPrice(){
        return price;
    }

    public int getCapacity(){
        return capacity;
    }
}