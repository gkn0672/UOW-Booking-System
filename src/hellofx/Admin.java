package hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Admin implements Initializable{
    private String username;
    private String role;

    public Admin(String username, String role){
        this.username = username;
        this.role = role;
    }

    @FXML Label Welcome;
    @FXML Label Role;
    @FXML Button logoutbutton;
    @FXML Button Createbutton;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Welcome.setText("Welcome, "+username+" !");
        Role.setText("UOW "+role);  
    }

    public void Adlogout(ActionEvent event) throws Exception{
        logout();
    }

    public void Createroom(ActionEvent event) throws Exception{
        createroom();
    }

    public void logout() throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_form.fxml"));
        m.createScene(loader, "UOW room booking system", 600, 400, 450, 150);
    }

    public void createroom() throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Create_room_form.fxml"));
        m.popup(loader, "Create room", 395, 508, 450, 150);
    }
}
