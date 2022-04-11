package hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class Add_new_room  extends Admin {
    ObservableList<String> Blknumber = FXCollections.observableArrayList("A", "B", "C");
    private Main m;
    public Add_new_room(String username, String role, Main m){
        super(username, role);
        this.m = m;
    }

    @FXML Button Confirmcreate;
    @FXML Button Cancelbutton;
    @FXML ComboBox<String> Blknum;  
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Blknum.setValue("A");
        Blknum.setItems(Blknumber);
    }

    //Event trigger
    public void Cancelcreate(ActionEvent event) throws Exception{
        cancel(Cancelbutton);
    }

    //Event trigger (confirm create room)
    public void Confirmcreateroom(ActionEvent event) throws Exception{
        confirmcreate();
    }

    //Confirm create a room (Send data to mysql server)
    public void confirmcreate() throws Exception{
        System.out.println("Create success!");
    }
}
