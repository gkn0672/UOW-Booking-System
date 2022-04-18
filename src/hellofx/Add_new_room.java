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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class Add_new_room  extends Admin {
    ObservableList<String> Blknumber = FXCollections.observableArrayList("A", "B", "C");
    ObservableList<String> Promo = FXCollections.observableArrayList();
    private Main m;
    private LocalDate mydate;
    private Admin ad;
    private String date;
    private String time;
    public Add_new_room(String username, String role, Main m, Admin ad) throws Exception{
        super(username, role);
        this.m = m;
        this.ad = ad;
        initpromo();
    }

    @FXML Button Confirmcreate;
    @FXML Button Cancelbutton;

    @FXML ComboBox<String> Blknum;  
    @FXML ComboBox<String> Selectpromo;

    @FXML DatePicker Date;

    @FXML LimitedTextField Hour = new LimitedTextField();
    @FXML LimitedTextField Min = new LimitedTextField();

    @FXML TextField Price;
    @FXML TextField Roomnumber;
    @FXML TextField Capacity;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        Blknum.setValue("A");
        Blknum.setItems(Blknumber);
        
        Selectpromo.setItems(Promo);
        Hour.TimerestrictHour();
        Min.TimerestrictMin();
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
            Connection con = m.getC().getConnection();
            PreparedStatement ps;

            ps = con.prepareStatement("SELECT * FROM `room` WHERE `BlkNum` = ? AND `RoomNum` = ?");
            ps.setString(1, Blknum.getValue());
            ps.setString(2, Roomnumber.getText());
            ResultSet rs = (ResultSet) ps.executeQuery();

            if(((ResultSet)rs).next()){
                System.out.println("Already exist");
            }
            else{
                ps = con.prepareStatement("INSERT INTO room (`RoomNum`, `BlkNum`, `Status`, `Price`, `Capacity`, `Date`, `Time`, `Promo_code`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, Roomnumber.getText());
                ps.setString(2, Blknum.getValue());
                ps.setString(3, "Not available");
                ps.setString(4, Price.getText());
                ps.setString(5, Capacity.getText());
                ps.setString(6, date);
                gettime();
                ps.setString(7, time);
                if(Selectpromo.getValue() == null){
                    ps.setString(8, null);
                }
                else{
                    ps.setString(8, Selectpromo.getValue());
                }

                if(ps.executeUpdate() != 0){
                    cancel(Cancelbutton);
                    Main m = new Main();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
                    Success_message sm1 = new Success_message("Room added");
                    loader.setController(sm1);
                    m.popup(loader, "Success", 332, 194, 650, 250);  
                    ad.updatePromo();
                }
                else{
                    System.out.println("Update failed");
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
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
            date = mydate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //Get time format
    public void gettime(){
        int hour = Integer.parseInt(Hour.getText());
        int minute = Integer.parseInt(Min.getText());
        String finalhour;
        String finalmin;

        if (hour < 10){
            finalhour = String.format("0%s", String.valueOf(hour));
        }
        else{
            finalhour = String.format("%s", String.valueOf(hour));
        }

        if(minute < 10){
            finalmin = String.format("0%s", String.valueOf(minute));
        }
        else{
            finalmin = String.format("%s", String.valueOf(minute));
        }

        time = String.format("%s:%s:00", finalhour, finalmin);
    }
}
