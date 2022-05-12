package hellofx;

public class Booking {
    private int id;
    private String username;
    private String name;
    private String price;

    public Booking(int id, String username, String name, String price){
        this.id = id;
        this.username = username;
        this.name = name;
        this.price = String.format("$%s", price);
    }

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getName(){
        return name;
    }

    public String getPrice(){
        return price;
    }
    
}
