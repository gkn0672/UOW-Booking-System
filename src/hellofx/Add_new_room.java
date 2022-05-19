package hellofx;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;

import java.util.regex.Pattern;

public class Add_new_room  extends Admin {
    ObservableList<String> Blknumber = FXCollections.observableArrayList("A", "B", "C");
    ObservableList<String> RFloor = FXCollections.observableArrayList("1", "2", "3", "4", "5");
    ObservableList<String> RHourA = FXCollections.observableArrayList("07","08","09","10","11");
    ObservableList<String> RHourP = FXCollections.observableArrayList("12","01","02","03","04", "05", "06", "07", "08", "09");
    ObservableList<String> RMin = FXCollections.observableArrayList("00", "15", "30", "45");
    ObservableList<String> RTimeSet = FXCollections.observableArrayList("AM", "PM");
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
    @FXML ComboBox<String> Hour;
    @FXML ComboBox<String> Min;
    @FXML ComboBox<String> TimeSet;

    @FXML DatePicker Date;

    @FXML TextField Roomnumber;
    @FXML TextField Capacity;
    @FXML TextField Price;
    @FXML Label Newroomerror;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        Blknum.setValue("A");
        Blknum.setItems(Blknumber);

        Floor.setValue("1");
        Floor.setItems(RFloor);
        Hour.setValue("08");
        Hour.setItems(RHourA);
        Min.setValue("30");
        Min.setItems(RMin);
        TimeSet.setValue("AM");
        TimeSet.setItems(RTimeSet);

        LocalDate thedate = LocalDate.now();
        Date.setValue(thedate);
    }

    //Event trigger
    public void Cancelcreate(ActionEvent event) throws Exception{
        cancel(Cancelbutton);
    }

    //Event trigger (time set)
    public void timeset(ActionEvent event) throws Exception{
        String ts = TimeSet.getValue();
        if (ts.equals("PM")){
            Hour.setItems(RHourP);
            Hour.setValue("12");
        }
        else{
            Hour.setItems(RHourA);
            Hour.setValue("08");
        }
    }

    //Event trigger (confirm create room)
    public void Confirmcreateroom(ActionEvent event) throws Exception{
        confirmcreate();
    }
    
    //Event trigger (Select date)
    public void getDate(ActionEvent event) throws Exception{
        getdateformat();
    }

    public void ValidatePrice(KeyEvent event){
        final Pattern pattern = Pattern.compile("(\\d{1,2}\\.\\d{1,2})");
        TextFormatter<?> formatter = new TextFormatter<>(change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                Newroomerror.setVisible(false);
                Confirmcreate.setDisable(false);
             return change; 
            } else {
                Newroomerror.setText("Invalid input");
                Newroomerror.setVisible(true);
                Confirmcreate.setDisable(true);
            return change; 
            }
         });
        Price.setTextFormatter(formatter);
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
                ps = con.prepareStatement("INSERT INTO room (`ID`, `RoomNum`, `BlkNum`, `Floor`, `Date`, `Time`, `Timeset`, `Capacity`, `Status`, `Price`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, null);
                ps.setString(2, Roomnumber.getText());
                ps.setString(3, Blknum.getValue());
                ps.setString(4, Floor.getValue());
                ps.setString(5, date);

                gettime();
                ps.setString(6, time);
                ps.setString(7, TimeSet.getValue());
                ps.setString(8, Capacity.getText());
                ps.setString(9, "Not available");
                ps.setString(10, Price.getText());

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
    public void gettime() throws ParseException{
        int hour = Integer.parseInt(Hour.getValue());
        String minute = Min.getValue();
        String timeset = TimeSet.getValue();

        if(timeset.equals("PM") && hour != 12){
            hour += 12;
            time = String.format("%s:%s:00", String.valueOf(hour), minute);
        }

        else if(hour < 10){
            time = String.format("0%s:%s:00", String.valueOf(hour), minute);
        }
        else{
            time = String.format("%s:%s:00", hour, minute);
        }

        if(timeset.equals("AM") && hour < 10){
            time = String.format("0%s:%s:00", String.valueOf(hour), minute);
        }
        else{
            time = String.format("%s:%s:00", hour, minute);
        }

    }
}
