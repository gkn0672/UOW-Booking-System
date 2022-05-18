package hellofx;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TextField;


public class Modify_booking extends Student {

    private Main m;
    private int id;
    private Student s;
    private String time; 
    private String date;
    ObservableList<String> RTimeSet = FXCollections.observableArrayList("AM", "PM");


    private String Rname;
    private String Rblk;
    private String Rnum;
    private String Rfloor;
    private String Rdate;
    private String Rtime;
    private String capacity;
    private String price;
    private String code;
    private String timeset;
    private String Rhour;
    private String Rmin;
    private String Rid;
    private int OrigRid;
    private int newRid;


    //constructor
    public Modify_booking(String username, String role, Main m, Student s, int id)throws Exception{
        super(username, role);
        this.m = m;
        this.s = s;
        this.id = id;
        getData();
    }

    @FXML
    private Button Cancelbutton;

    @FXML
    private TextField Capacity;

    @FXML
    private Button Confirmmodify;

    @FXML
    private TextField Date;

    @FXML
    private ComboBox<String> DiscountCode;

    @FXML
    private TextField Price;

    @FXML
    private TextField Roomname;

    @FXML
    private ComboBox<String> TimeSet;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        try{
        Connection con = m.getC().getConnection();
        PreparedStatement ps;
        ResultSet rs;
        
        //Get selected room details
        ps = con.prepareStatement("SELECT * FROM `room` WHERE `RoomNum` = ? AND `BlkNum` = ? AND `Floor` = ? AND `Date` = ? AND `Status` = ?;");
        ps.setString(1, Rnum);
        ps.setString(2, Rblk);
        ps.setString(3, Rfloor);
        ps.setString(4, Rdate);
        ps.setString(5, "booked");
        rs = (ResultSet) ps.executeQuery();
        Rname = String.valueOf(Rblk) + "." + String.valueOf(Rfloor) + "." + String.valueOf(Rnum);
        
        //Set original room ID to attribute
        if(rs.next()){
            OrigRid = rs.getInt(1);
        }

        Roomname.setText(Rname);
        Date.setText(Rdate);
        String totalTime = Rtime;
        TimeSet.setValue(totalTime);
        
        //Combobox data for time
        ps = con.prepareStatement("SELECT `Time` FROM `room` WHERE `RoomNum` = ? AND `BlkNum` = ? AND `Floor` = ? AND `Date` = ?;");
        ps.setString(1, Rnum);
        ps.setString(2, Rblk);
        ps.setString(3, Rfloor);
        ps.setString(4, Rdate);
        rs = (ResultSet) ps.executeQuery();
        while(rs.next()){
            TimeSet.getItems().add(rs.getString(1));
        }
        Capacity.setText(capacity);
        Price.setText(price);

        //Combobox data for promotion code
        ps = con.prepareStatement("SELECT `Code_name` FROM `room_promo` WHERE `RoomID` = ?;");
        ps.setString(1,Rid);
        rs = (ResultSet) ps.executeQuery();
        while(rs.next()){
            DiscountCode.getItems().add(rs.getString(1));
        }

        TimeSet.setOnAction((event) ->{
        PreparedStatement ps1;
        ResultSet rs1;
            try
            {
            //clear combo box
            DiscountCode.getItems().removeAll(DiscountCode.getItems());

            //Get room ID
            ps1 = con.prepareStatement("SELECT `ID` FROM `room` WHERE `RoomNum` = ? AND `BlkNum` = ? AND `Floor` = ? AND `Date` = ? AND `Time` = ?;");
            ps1.setString(1, Rnum);
            ps1.setString(2, Rblk);
            ps1.setString(3, Rfloor);
            ps1.setString(4, Rdate);
            ps1.setString(5, TimeSet.getValue());
            rs1 = (ResultSet) ps1.executeQuery();
            if(rs1.next()){
                Rid = rs1.getString(1);
                newRid = rs1.getInt(1);
            }

             //Combobox data for promotion code
             ps1 = con.prepareStatement("SELECT `Code_name` FROM `room_promo` WHERE `RoomID` = ?;");
             ps1.setString(1,Rid);
             rs1 = (ResultSet) ps1.executeQuery();
            while(rs1.next()){
                DiscountCode.getItems().add(rs1.getString(1));
            }
            }
            catch(Exception e){
                e.printStackTrace();
            }

        });

        //Get room ID
        ps = con.prepareStatement("SELECT `ID` FROM `room` WHERE `RoomNum` = ? AND `BlkNum` = ? AND `Floor` = ? AND `Date` = ? AND `Time` = ? AND Timeset = ?;");
        ps.setString(1, Rnum);
        ps.setString(2, Rblk);
        ps.setString(3, Rfloor);
        ps.setString(4, Rdate);
        ps.setString(5, TimeSet.getValue());
        ps.setString(6, timeset);
        rs = (ResultSet) ps.executeQuery();
        if(rs.next()){
            Rid = rs.getString(1);
        }

    }
    catch(Exception e){
        e.printStackTrace();
    }
}


@FXML
void Cancelmodify(ActionEvent event) throws Exception {
    cancel(Cancelbutton);

}

@FXML
void Confirmmodify(ActionEvent event) throws Exception {
    Connection con = m.getC().getConnection();
    PreparedStatement ps;
    PreparedStatement ps1;
    PreparedStatement ps2;
    PreparedStatement ps3;
    PreparedStatement ps4;
    PreparedStatement ps5;
    PreparedStatement ps6;
    double discountPrice = 0.0;
    double originalPrice = 0.0;
    int promoCode = 0;
    int afterDiscount = 0;

    //Delete data from booking table
    ps1 = con.prepareStatement("DELETE FROM `booking` WHERE `RID` = ?");
    ps1.setInt(1, (OrigRid));
    ps1.execute();

    //update status of original to "available"
    ps2 = con.prepareStatement("UPDATE `room` SET `Status` = ? WHERE `ID` = ?");
    ps2.setString(1, "Available");
    ps2.setInt(2, (OrigRid));
    ps2.execute();

    //Get promotion code 
    ps3 = con.prepareStatement("SELECT `Code_sale` FROM `promotioncode` WHERE `Code_name` = ?");
    ps3.setString(1,DiscountCode.getValue());
    ResultSet rs = (ResultSet) ps3.executeQuery();
    if(rs.next()){
        promoCode = rs.getInt(1);
    }

    //Get price
    ps4 = con.prepareStatement("SELECT * FROM `room` WHERE `ID` = ?");
    ps4.setInt(1, (newRid));
    rs = ps4.executeQuery();
    if(rs.next()){
        originalPrice = rs.getDouble(9);
    }

    //get discount price
    afterDiscount = 100 - promoCode;
    discountPrice = (afterDiscount * originalPrice) / 100;  
    
    //Insert data into booking table
    ps5 = con.prepareStatement("INSERT INTO `booking` (`RID`, `uname`, `Code_name`, `discountPrice`) VALUES (?, ?, ?, ?)");
    ps5.setInt(1, (newRid));
    ps5.setString(2, s.username);
    if(DiscountCode.getItems() == null) {
        ps5.setString(3,"N/A");
        ps5.setString(4,String.valueOf(originalPrice));        
    }
    else{
        ps5.setString(3, DiscountCode.getValue());
        ps5.setString(4, String.valueOf(discountPrice));
    }
    ps5.execute();
    
    //update status of selected to "booked"
    ps6 = con.prepareStatement("UPDATE `room` SET `Status` = ? WHERE `ID` = ?");
    ps6.setString(1, "booked");
    ps6.setInt(2, (newRid));
    ps6.execute();    
    s.Bookinglist.refresh();

    //Update room available
    if(ps6.executeUpdate() != 0){
        s.updateRoom();
        cancel(Cancelbutton);
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
        Success_message sm1 = new Success_message("");
        loader.setController(sm1);
        m.popup(loader, "Success!", 332, 194, 650, 250); 
        s.updateModifyBookingList(newRid);
        s.Bookinglist.refresh(); 
        }


}


    public void getData() throws SQLException{
        Connection con = m.getC().getConnection();
        PreparedStatement ps;
        ps = con.prepareStatement("SELECT * FROM `room` WHERE `ID` = ?");
        ps.setString(1, String.valueOf(id));
        ResultSet rs = (ResultSet) ps.executeQuery();
        while(rs.next()){
            Rnum = rs.getString("RoomNum");
            Rblk = rs.getString("BlkNum");
            Rfloor = rs.getString("Floor");
            Rdate = rs.getString("Date");
            Rtime = rs.getString("time");
            capacity = String.valueOf(rs.getInt("Capacity"));
            price = rs.getString("Price");
            timeset = rs.getString("Timeset");
        }

        String[] time = Rtime.split(":");
        Rhour = time[0].trim();
        Rmin = time[1].trim();
    }
    
}
