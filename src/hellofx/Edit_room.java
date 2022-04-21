package hellofx;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Edit_room  extends Admin {
    ObservableList<String> Blknumber = FXCollections.observableArrayList("A", "B", "C");
    ObservableList<String> RFloor = FXCollections.observableArrayList("1", "2", "3", "4", "5");
    ObservableList<String> RHourA = FXCollections.observableArrayList("07","08","09","10","11");
    ObservableList<String> RHourP = FXCollections.observableArrayList("12","01","02","03","04", "05", "06", "07", "08", "09");
    ObservableList<String> RMin = FXCollections.observableArrayList("00", "15", "30", "45");
    ObservableList<String> RTimeSet = FXCollections.observableArrayList("AM", "PM");
    private Main m;
    private int id;
    private LocalDate mydate;
    private Admin ad;
    private String time;
    private String date; 

    private String rname;
    private String rblk;
    private String rfloor;
    private String rdate;
    private String rtime;   
    private String rhour;
    private String rmin;
    private String timeset;
    private String cap;
    private String price;


    //Constructor
    public Edit_room(String username, String role, Main m, Admin ad, int id) throws Exception{
        super(username, role);
        this.m = m;
        this.ad = ad;
        this.id = id;
        getData();
    }

    @FXML Button Confirmedit;
    @FXML Button Cancelbutton;
    @FXML Button Delete;
    @FXML ComboBox<String> Blknum;  
    @FXML ComboBox<String> Floor;
    @FXML ComboBox<String> Hour;
    @FXML ComboBox<String> Min;
    @FXML ComboBox<String> TimeSet;

    @FXML DatePicker Date;

    @FXML LimitedTextField Price = new LimitedTextField();

    @FXML TextField Roomnumber;
    @FXML TextField Capacity;
    @FXML TextField Deleteconfirm;
    
    @FXML Label Editroomerror;
    @FXML Label Deletewarn;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        Roomnumber.setText(rname);
        Capacity.setText(cap);

        Blknum.setValue(rblk);
        Blknum.setItems(Blknumber);

        Floor.setValue(rfloor);
        Floor.setItems(RFloor);

        TimeSet.setValue(timeset);
        TimeSet.setItems(RTimeSet);

        Hour.setValue(rhour);
        if(timeset.equals("AM")){
            Hour.setItems(RHourA);
        }
        else{
            Hour.setItems(RHourP);
        }

        Min.setValue(rmin);
        Min.setItems(RMin);

        Price.setText(price);
        Price.Restrictprice();

        LocalDate thedate = LocalDate.parse(rdate);
        Date.setValue(thedate);

        Delete.setDisable(true);
    }

    //Event trigger
    public void Canceledit(ActionEvent event) throws Exception{
        cancel(Cancelbutton);
    }

    //Event trigger
    public void Deleteroom(ActionEvent event) throws Exception{
        delete();
    }

    //Delete confirm
    public void Deletecheck(KeyEvent event){
        if ((Deleteconfirm.getText()).equals("Confirm")){
            Delete.setDisable(false);
        }
        else{
            Delete.setDisable(true);
        }
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
    public void Confirmeditroom(ActionEvent event) throws Exception{
        confirmedit();
    }
    
    //Event trigger (Select date)
    public void getDate(ActionEvent event) throws Exception{
        getdateformat();
    }

    //Show delete warning
    public void Deletewarnshow(MouseEvent event){
    Deletewarn.setVisible(true);
    }
    
    public void confirmedit() throws Exception{
        Editroomerror.setVisible(false);
        try{
            Connection con = m.getC().getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("SELECT * FROM `room` WHERE `RoomNum` = ? AND `BlkNum` = ? AND 'Floor' = ? AND 'Date' = ? AND 'Time' = ? AND 'Timeset' = ? ");
            ps.setString(1, Roomnumber.getText());
            ps.setString(2, Blknum.getValue());
            ps.setString(3, Floor.getValue());
            ps.setString(4, date);
            gettime();
            ps.setString(5, time);
            ps.setString(6, TimeSet.getValue());
            ResultSet rs = (ResultSet) ps.executeQuery();

            if(((ResultSet)rs).next()){
                Editroomerror.setText("Room already exist");
                Editroomerror.setVisible(true);
            }
            else{
                ps = con.prepareStatement("UPDATE room SET RoomNum = ?, BlkNum = ?, Floor = ?, Date = ?, Time = ?, Timeset = ?, Price = ?, Capacity = ? WHERE ID = ?");
                ps.setString(1, Roomnumber.getText());
                ps.setString(2, Blknum.getValue());
                ps.setString(3, Floor.getValue());
                ps.setString(4, (Date.getValue()).toString());
                ps.setString(5, time);
                ps.setString(6, TimeSet.getValue());
                ps.setString(7, Price.getText());
                ps.setString(8, Capacity.getText());
                ps.setString(9, String.valueOf(id));
                ps.execute();
                
                cancel(Confirmedit);
                Main m = new Main();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
                Success_message sm1 = new Success_message("Room updated");
                loader.setController(sm1);
                m.popup(loader, "Success", 332, 194, 650, 250);  
                ad.updateRoom();;
            }
        }
        catch(Exception e){
            System.out.println("Something wrong!");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete() throws Exception{
        try{
            Connection con = m.getC().getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("DELETE FROM `room` WHERE `ID` = ?");
            ps.setString(1, String.valueOf(id));
            ps.execute();

            cancel(Delete);
            Main m = new Main();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
            Success_message sm1 = new Success_message("Room deleted");
            loader.setController(sm1);
            m.popup(loader, "Success", 332, 194, 650, 250);  
            ad.updateRoom();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void getData() throws SQLException{
        Connection con = m.getC().getConnection();
        PreparedStatement ps;
        ps = con.prepareStatement("SELECT * FROM `room` WHERE `ID` = ?");
        ps.setString(1, String.valueOf(id));
        ResultSet rs = (ResultSet) ps.executeQuery();
        while(rs.next()){
            rname = rs.getString("RoomNum");
            rblk = rs.getString("BlkNum");
            rfloor = rs.getString("Floor");
            rdate = rs.getString("Date");
            rtime = rs.getString("time");
            cap = String.valueOf(rs.getInt("Capacity"));
            price = rs.getString("Price");
            timeset = rs.getString("Timeset");
        }

        String[] time = rtime.split(":");
        rhour = time[0].trim();
        rmin = time[1].trim();
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

        if(hour < 10){
            time = String.format("0%s:%s:00", String.valueOf(hour), minute);
        }
        else{
            time = String.format("%s:%s:00", String.valueOf(hour), minute);
        }
    }
}