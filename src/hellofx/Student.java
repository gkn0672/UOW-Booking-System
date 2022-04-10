package hellofx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Student implements Initializable{
    private String username;
    private String role;

    public Student(String username, String role){
        this.username = username;
        this.role = role;
    }

    @FXML Label Susername;
    @FXML Label Srole;
    @FXML Button Lgnoutbutton;

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Susername.setText("Welcome, "+username+" !");
        Srole.setText("UOW "+role);
    }

    public void Stulogout(ActionEvent event) throws IOException{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_form.fxml"));
        m.createScene(loader, "UOW room booking system", 600, 400, 450, 150);
    }
}