package hellofx;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Resultset;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class Add_new_room  extends Admin {
    ObservableList<String> Blknumber = FXCollections.observableArrayList("A", "B", "C");
    ObservableList<String> Promo = FXCollections.observableArrayList();
    private Main m;
    private LocalDate mydate;
    public Add_new_room(String username, String role, Main m) throws Exception{
        super(username, role);
        this.m = m;
        initpromo();
    }

    @FXML Button Confirmcreate;

    @FXML Button Cancelbutton;

    @FXML ComboBox<String> Blknum;  

    @FXML DatePicker Date;

    @FXML TextField Hour;

    @FXML TextField Min;

    @FXML TextField Price;

    @FXML TextField Roomnumber;

    @FXML ComboBox<String> Selectpromo;

    @FXML TextField Capacity;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        Blknum.setValue("A");
        Blknum.setItems(Blknumber);
        
        Selectpromo.setItems(Promo);
    }

    //Event trigger
    public void Cancelcreate(ActionEvent event) throws Exception{
        cancel(Cancelbutton);
    }

    //Event trigger (confirm create room)
    public void Confirmcreateroom(ActionEvent event) throws Exception{
        confirmcreate();
    }
    
    //Event trigger (Select date)
    public void getDate(ActionEvent event) throws Exception{
        getdateformat();
    }

    public void confirmcreate() throws Exception{
        try{

        }
        catch(Exception e){
            
        }
    }

    //Confirm create a room (Send data to mysql server)
    public void initpromo() throws Exception{
        Connection con = m.getC().getConnection();
        PreparedStatement ps;
        ps = con.prepareStatement("SELECT `Code_name` FROM `promotioncode`");
        Resultset rs = (Resultset) ps.executeQuery();
        while(((ResultSet) rs).next()){
            Promo.add(((ResultSet) rs).getString(1));
        }
    }

    //Get date format
    public void getdateformat() throws Exception{
        try{
            mydate = Date.getValue();
            String date = mydate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            System.out.println(date);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
