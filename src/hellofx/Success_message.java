package hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Success_message implements Initializable{
    private String message;

    public Success_message(String message){
        this.message = message;
    }

    @FXML Label Smessage;

    @FXML Button Sokbutton;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Smessage.setText(this.message);
    }

    //Event trigger
    public void SCancel(ActionEvent event) throws Exception{
        scancel(Sokbutton);
    }

    //Close pop up
    @FXML
    public void scancel(Button name) throws Exception{
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }
}
