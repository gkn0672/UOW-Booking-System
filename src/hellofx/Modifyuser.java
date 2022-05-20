package hellofx;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
public class Modifyuser extends UserMng{
    ObservableList<String> Roletype = FXCollections.observableArrayList("Staff", "Student", "User Management");
    private Main m;
    private UserMng u;

    @FXML
    Button cancelbutton;

    @FXML
     TextField fname;

    @FXML
     TextField lname;

    @FXML
     Button modifybutton;

    @FXML
     TextField password;

    @FXML
     ComboBox<String> role;

    @FXML
    TextField username;

     @FXML
    Label modifyusererror;

    //constructor
    public Modifyuser (String username, String role, Main m, UserMng u, User user) throws Exception{
        super(username, role);
        this.m = m;
        this.u = u;
        this.user = user;
    }    
    
    @FXML
    void cancel(ActionEvent event) throws Exception{
        cancel(cancelbutton);
    }

    @FXML
    void modify(ActionEvent event) {
        modifyusererror.setVisible(false);

        try{
            Connection con = m.getC().getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("SELECT `uname` FROM `users` WHERE `uname` = ?");
            ps.setString(1,username.getText());
            ResultSet rs = (ResultSet) ps.executeQuery();

            if(((ResultSet)rs).next()){
                modifyusererror.setText("Username is in use");
                modifyusererror.setVisible(true);
            }
            else{
                ps = con.prepareStatement("UPDATE `users` SET fname = ?, lname = ?, uname = ?, password = ?, role = ? WHERE `uname` = ?");
                ps.setString(1,fname.getText());    
                ps.setString(2,lname.getText());   
                ps.setString(3,username.getText());   
                ps.setString(4,password.getText());   
                ps.setString(5,role.getValue());   
                ps.setString(6,user.getUname()); 

                if(ps.executeUpdate() != 0){
                    cancel(cancelbutton);
                    Main m = new Main();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
                    Success_message sm1 = new Success_message("User Modified!");
                    loader.setController(sm1);
                    m.popup(loader, "Success", 332, 194, 650, 250);  
                    u.updateUser();
                }
                else{
                    System.out.println("User modification failed");
                }     
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        username.setText(user.getUname());
        password.setText(user.getPassword());
        fname.setText(user.getFname());
        lname.setText(user.getLname());
        role.setValue(user.getRole());
        role.setItems(Roletype);

    }
}
