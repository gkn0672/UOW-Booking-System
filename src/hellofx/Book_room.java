package hellofx;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;


public class Book_room extends Student{
    private Main m;
    private Student s;
    private int id;
    public Book_room (String username, String role, Main m, int id, Student s){
        super(username, role);
        this.m = m;
        this.id = id;
        this.s = s;
    }

public void initialize(URL arg0, ResourceBundle arg1){
    try{
        Connection con = m.getC().getConnection();
        PreparedStatement ps;
        ResultSet rs;
        ps = con.prepareStatement("SELECT `Code_name` FROM `room_promo` WHERE `RoomID` = ?");
        ps.setString(1, String.valueOf(id));
        rs = (ResultSet) ps.executeQuery();

        while(rs.next()){
            Activelist.getItems().add(rs.getString(1));
        }
        Confirmbooking.setDisable(false);
    }
    catch(Exception e){
        e.printStackTrace();
    }
}

//Event trigger (Active promo code check)
public void Checkactive(ActionEvent event){
    String value = Activelist.getValue();

    if(!value.equals("")){
        Confirmbooking.setDisable(false);
    }
    else{
        Confirmbooking.setDisable(true);
    }
}

@FXML
public ComboBox<String> Activelist;

@FXML
private Button Cancel;

@FXML
private Button Confirmbooking;

@FXML
void Cancelbooking(ActionEvent event) throws Exception{
    cancel(Cancel);
}


@FXML
void Confirmbooking(ActionEvent event) throws Exception{
    Connection con = m.getC().getConnection();
    PreparedStatement ps;
    PreparedStatement ps1;
    PreparedStatement ps2;
    PreparedStatement ps3;
    double discountPrice = 0.0;
    double originalPrice = 0.0;
    int promoCode = 0;
    int afterDiscount = 0;

    //Get promotion code 
    ps1 = con.prepareStatement("SELECT `Code_sale` FROM `promotioncode` WHERE `Code_name` = ?");
    ps1.setString(1,Activelist.getValue());
    ResultSet rs = (ResultSet) ps1.executeQuery();
    if(rs.next()){
        promoCode = rs.getInt(1);
    }
    //Get original price
    ps2 = con.prepareStatement("SELECT * FROM `room` WHERE `ID` = ?");
    ps2.setString(1, String.valueOf(id));
    rs = ps2.executeQuery();
    if(rs.next()){
        originalPrice = rs.getDouble(9);
    }

    //get discount price
    afterDiscount = 100 - promoCode;
    discountPrice = (afterDiscount * originalPrice) / 100;

    //Insert data into booking table
    ps3 = con.prepareStatement("INSERT INTO `booking` (`RID`, `uname`, `Code_name`, `discountPrice`) VALUES (?, ?, ?, ?)");
    ps3.setString(1, String.valueOf(s.selectedid));
    ps3.setString(2, s.username);
    if(Activelist.getItems() == null) {
        ps3.setString(3,"N/A");
        ps3.setString(4,String.valueOf(originalPrice));
    }
    else
    {
        ps3.setString(3, Activelist.getValue());
        ps3.setString(4, String.valueOf(discountPrice));
    }
    
    ps3.execute();

    //Set selected room status to "Booked"
    ps = con.prepareStatement("UPDATE `room` SET `Status` = ? WHERE `ID` = ?");
    ps.setString(1, "Booked");
    ps.setString(2, String.valueOf(id));
    ps.execute();

    //Update room available
    if(ps.executeUpdate() != 0){
    s.updateRoom();
    cancel(Cancel);
    Main m = new Main();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
    Success_message sm1 = new Success_message("Booking Successful");
    loader.setController(sm1);
    m.popup(loader, "Success", 332, 194, 650, 250);  
    s.updateBookingList();
    }

    }
}

    








