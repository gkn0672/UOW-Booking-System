package hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Add_new_promo_code extends Admin {

    public Add_new_promo_code(String username, String role) {
        super(username, role);
    }

    @FXML Button Confirmpromobutton;
    @FXML Button Cancelpromobutton;

    @Override 
    public void initialize(URL arg0, ResourceBundle arg1) {
    }
    
    //Event trigger (cancel create promo)
    public void Cancelpromo(ActionEvent event) throws Exception{
        cancel(Cancelpromobutton);
    }

    //Event trigger (confirm create promocode)
    public void Confirmpromo(ActionEvent event) throws Exception{
        confirmpromo();
    }

    //Confirm create a promocode (send data to mysql server)
    public void confirmpromo() throws Exception{
        System.out.println("Create sucess!");
    }
}
