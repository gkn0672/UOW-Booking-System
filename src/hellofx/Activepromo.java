package hellofx;

public class Activepromo {
    private int id;
    private String name;

    public Activepromo(int id, String name){
        this.name = name;
        this.id = id;
    }

    public Activepromo(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }
}
