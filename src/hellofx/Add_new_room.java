package hellofx;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Add_new_room  extends Admin {
    ObservableList<String> Blknumber = FXCollections.observableArrayList("A", "B", "C");
    ObservableList<String> RFloor = FXCollections.observableArrayList("1", "2", "3", "4", "5");
    private Main m;
    private LocalDate mydate;
    private Admin ad;
    private String date;
    private String time;

    //Constructor
    public Add_new_room(String username, String role, Main m, Admin ad) throws Exception{
        super(username, role);
        this.m = m;
        this.ad = ad;
    }

    @FXML Button Confirmcreate;
    @FXML Button Cancelbutton;

    @FXML ComboBox<String> Blknum;  
    @FXML ComboBox<String> Floor;

    @FXML DatePicker Date;

    @FXML LimitedTextField Hour = new LimitedTextField();
    @FXML LimitedTextField Min = new LimitedTextField();
    @FXML LimitedTextField Price = new LimitedTextField();

    @FXML TextField Roomnumber;
    @FXML TextField Capacity;
    
    @FXML Label Newroomerror;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        Blknum.setValue("A");
        Blknum.setItems(Blknumber);

        Floor.setValue("1");
        Floor.setItems(RFloor);
        Hour.TimerestrictHour();
        Min.TimerestrictMin();
        Price.Restrictprice();
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

    //Confirm to create a new room
    public void confirmcreate() throws Exception{
        Newroomerror.setVisible(false);
        try{
            Connection con = m.getC().getConnection();
            PreparedStatement ps;

            ps = con.prepareStatement("SELECT * FROM `room` WHERE `RoomNum` = ? AND `BlkNum` = ? AND `Floor` = ? AND `Date` = ? AND `Time` = ?");
            ps.setString(1, Roomnumber.getText());
            ps.setString(2, Blknum.getValue());
            ps.setString(3, Floor.getValue());
            ps.setString(4, date);
            gettime();
            ps.setString(5, time);
            ResultSet rs = (ResultSet) ps.executeQuery();

            if(((ResultSet)rs).next()){
                Newroomerror.setText("Room already exist");
                Newroomerror.setVisible(true);
            }
            else{
                ps = con.prepareStatement("INSERT INTO room (`ID`, `RoomNum`, `BlkNum`, `Floor`, `Date`, `Time`, `Capacity`, `Status`, `Price`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, null);
                ps.setString(2, Roomnumber.getText());
                ps.setString(3, Blknum.getValue());
                ps.setString(4, Floor.getValue());
                ps.setString(5, date);

                gettime();
                ps.setString(6, time);
                ps.setString(7, Capacity.getText());
                ps.setString(8, "Not available");
                ps.setString(9, Price.getText());

                if(ps.executeUpdate() != 0){
                    cancel(Cancelbutton);
                    Main m = new Main();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
                    Success_message sm1 = new Success_message("Room added");
                    loader.setController(sm1);
                    m.popup(loader, "Success", 332, 194, 650, 250);  
                    ad.updateRoom();
                }
                else{
                    System.out.println("update failed");
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
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
