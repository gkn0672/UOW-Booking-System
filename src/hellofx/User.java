package hellofx;
import java.util.Date;

public class User {
    private String fname;
    private String lname;
    private String uname;
    private String password;
    private String role;
    private int suspend;
    private String login;
    private String logout;

    //constructor
    public User(String uname, String password, String fname, String lname, String role){
        this.uname = uname;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.role = role;
    }

    //other constructor
    public User(String uname, String password, String fname, String lname, String role, int suspend, String login, String logout){
        this.uname = uname;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.role = role;
        this.suspend = suspend;
        this.login = login;
        this.logout = logout;
    }

    public String getUname()
    {
        return uname;
    }

    public String getPassword()
    {
        return password;
    }

    public String getFname()
    {
        return fname;
    }

    public String getLname()
    {
        return lname;
    }

    public String getRole()
    {
        return role;
    }

    public String getLogin()
    {
        return login;
    }

    public String getLogout()
    {
        return logout;
    }
}
