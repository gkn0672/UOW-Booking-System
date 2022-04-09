package hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Susername.setText(username);
        Srole.setText(role);
        
    }
}