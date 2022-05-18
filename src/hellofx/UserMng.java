package hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class UserMng implements Initializable {
    private String username;
    private String role;

    public UserMng(String username, String role){
        this.username = username;
        this.role = role;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }
    
}
