package hellofx;

public class Promocode {
    private String name;
    private int value;

    public Promocode(String name, int value){
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public int getValue(){
        return value;
    }
}
