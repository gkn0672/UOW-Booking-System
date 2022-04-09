package hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Welcome.setText(username);
        Role.setText(role);
        
    }
}
