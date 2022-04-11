package hellofx;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Admin implements Initializable{
    private String username;
    private String role;
    protected Connection con;

    //Constructor
    public Admin(String username, String role){
        this.username = username;
        this.role = role;
    }

    @FXML Label Welcome;
    @FXML Label Role;
    @FXML Label admin_role;
    @FXML Label admin_username;

    @FXML Button logoutbutton;
    @FXML Button Createroom;
    @FXML Button Createpromo;

    //Welcome menu
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Welcome.setText("Welcome, "+username+" !");
        Role.setText("UOW "+role);
    }

    //Event trigger (log out)
    public void Adlogout(ActionEvent event) throws Exception{
        logout();
    }

    //Event trigger (create room)
    public void Createroom(ActionEvent event) throws Exception{
        createroom();
    }

    //Event trigger (create promo)
    public void Createpromo(ActionEvent event) throws Exception{
        createpromo();
    }

    //Log out
    public void logout() throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_form.fxml"));
        m.createScene(loader, "UOW room booking system", 600, 400, 450, 150);
    }

    //Close popup
    @FXML
    public void cancel(Button name) throws Exception{
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    //Create new room form
    public void createroom() throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Create_room_form.fxml"));
        Add_new_room adr1 = new Add_new_room(this.username, this.role, m);
        loader.setController(adr1);
        m.popup(loader, "Create room", 395, 508, 450, 150);
    }

    //Create new promocode form
    public void createpromo() throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Create_promo_code.fxml"));
        Add_new_promo_code adrpc1 = new Add_new_promo_code(this.username, this.role, m);
        loader.setController(adrpc1);
        m.popup(loader, "Create promocode", 373, 453, 450, 150);
    }
}
