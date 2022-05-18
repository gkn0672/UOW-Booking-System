package hellofx;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Resultset;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Student implements Initializable{
    public String username;
    private Main m;
    private String role;
    protected int selectedid;
    ObservableList<Room> listR = FXCollections.observableArrayList();
    ObservableList<Bookinglist> ListA = FXCollections.observableArrayList();
    int index = -1;
    Resultset rs = null;
    PreparedStatement ps = null;
    //Constructor
    public Student(String username, String role){
        this.username = username;
        this.role = role;
    }

    @FXML Label Susername;
    @FXML Label Srole;
    @FXML Button Lgnoutbutton;
    @FXML
    private Button Bookbutton;


    @FXML
    private Button Modifybutton;

    @FXML
    private Button Cancelbutton;

    @FXML
    private TableColumn<Room, Integer> RID;

    @FXML
    private TableColumn<Bookinglist, Integer> RID1;

    @FXML
    private TableColumn<Room, Integer> Rcapacity;

    @FXML
    private TableColumn<Room, String> Rdate;
    
    @FXML
    private TableColumn<Bookinglist, String> Rdate1;

    @FXML
    private Button Refreshbutton;

    @FXML
    private TableColumn<Room, String> Rname;

    
    @FXML
    private TableColumn<Bookinglist, String> Rname1;

    @FXML
    private TableView<Room> Roomavailable;

    @FXML
    protected TableView<Bookinglist> Bookinglist;

    @FXML
    private TableColumn<Room, Double> Rprice;

    @FXML
    private TableColumn<Room, String> Rtime;

    @FXML
    private TableColumn<Bookinglist, String> Rtime1;

    @FXML
    private TableColumn<Bookinglist, String> RDcode;

    @FXML
    private TableColumn<Bookinglist, String> RDprice;

    //Welcome menu when log in
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Susername.setText("Welcome, "+username+" !");
        Srole.setText("UOW "+role);

        updateRoom();
        updateBookingList();
        
        Modifybutton.setDisable(true);
        Bookbutton.setDisable(true);
        Cancelbutton.setDisable(true);
    }
    
    public void updateBookingList(){
        RID1.setCellValueFactory(new PropertyValueFactory<Bookinglist, Integer>("id"));
        Rname1.setCellValueFactory(new PropertyValueFactory<Bookinglist, String>("name"));
        Rdate1.setCellValueFactory(new PropertyValueFactory<Bookinglist, String>("date"));
        Rtime1.setCellValueFactory(new PropertyValueFactory<Bookinglist, String>("time"));
        RDcode.setCellValueFactory(new PropertyValueFactory<Bookinglist, String>("code"));
        RDprice.setCellValueFactory(new PropertyValueFactory<Bookinglist, String>("price"));
        ListA = getDataRoomBooking(selectedid);
        Bookinglist.setItems(ListA);
        Bookinglist.refresh();

    }

    public void updateModifyBookingList(int index){
        RID1.setCellValueFactory(new PropertyValueFactory<Bookinglist, Integer>("id"));
        Rname1.setCellValueFactory(new PropertyValueFactory<Bookinglist, String>("name"));
        Rdate1.setCellValueFactory(new PropertyValueFactory<Bookinglist, String>("date"));
        Rtime1.setCellValueFactory(new PropertyValueFactory<Bookinglist, String>("time"));
        RDcode.setCellValueFactory(new PropertyValueFactory<Bookinglist, String>("code"));
        RDprice.setCellValueFactory(new PropertyValueFactory<Bookinglist, String>("price"));
        ListA = getDataRoomBooking(selectedid);
        Bookinglist.setItems(ListA);
        Bookinglist.refresh();

    }

    public void updateRoom(){
        RID.setCellValueFactory(new PropertyValueFactory<Room, Integer>("id"));
        Rname.setCellValueFactory(new PropertyValueFactory<Room, String>("name"));
        Rdate.setCellValueFactory(new PropertyValueFactory<Room, String>("date"));
        Rtime.setCellValueFactory(new PropertyValueFactory<Room, String>("time"));
        Rcapacity.setCellValueFactory(new PropertyValueFactory<Room, Integer>("capacity"));
        Rprice.setCellValueFactory(new PropertyValueFactory<Room, Double>("price"));
        listR = getDataRoom();
        Roomavailable.setItems(listR);
        Roomavailable.refresh();
    }

    public ObservableList<Bookinglist> getDataRoomBooking(int index){
        Main m = new Main();
        Connection con = m.getC().getConnection();
        ObservableList<Bookinglist> list = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT room.ID, room.BlkNum, room.Floor, room.RoomNum, room.Date, room.Time, room.Timeset, booking.Code_name, room.Status, booking.discountPrice, booking.uname FROM `room` LEFT JOIN `booking` ON room.ID = booking.RID WHERE `uname` = ? AND room.Status = ?");
            ps.setString(1, username);
            ps.setString(2, "Booked");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Bookinglist(rs.getInt("ID"), 
                                String.format("%s.%s.%s", rs.getString("BlkNum"), rs.getString("Floor"), rs.getString("RoomNum")), 
                                rs.getString("Date"), 
                                rs.getString("Time"),
                                rs.getString("Code_name"),
                                rs.getString("discountPrice"),
                                rs.getString("Timeset")));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }

    public ObservableList<Room> getDataRoom(){
        Main m = new Main();
        Connection con = m.getC().getConnection();
        ObservableList<Room> list = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM room WHERE `Status` = ?");
            ps.setString(1, "Available");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Room(rs.getInt("ID"), 
                                String.format("%s.%s.%s", rs.getString("BlkNum"), rs.getString("Floor"), rs.getString("RoomNum")), 
                                rs.getString("Date"), 
                                rs.getString("Time"), 
                                rs.getString("Status"), 
                                rs.getString("Price"), 
                                rs.getInt("Capacity"),
                                rs.getString("Timeset")));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }


    @FXML
    public void getSelectedR(MouseEvent event){
        index = Roomavailable.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        selectedid = RID.getCellData(index);
        Bookbutton.setDisable(false);
    }

    @FXML
    public void getSelectedL(MouseEvent event){
        index = Bookinglist.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        selectedid = RID1.getCellData(index);
        Modifybutton.setDisable(false);
        Cancelbutton.setDisable(false);
    }
    
    @FXML
    void Bookroom(ActionEvent event) throws IOException {
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Book_room_form.fxml"));
        Book_room br1 = new Book_room(this.username, this.role, m, selectedid, this);
        loader.setController(br1);
        m.popup(loader, "Booking", 600, 400, 650, 150);
    }

    @FXML
    void Modifyroom(ActionEvent event) throws Exception {
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Modify_room.fxml"));
        Modify_booking mb1 = new Modify_booking(this.username, this.role, m, this, selectedid);
        loader.setController(mb1);
        m.popup(loader, "Modify Booking", 395, 609, 650, 150);
    }

    @FXML
    void Cancelbooking(ActionEvent event) throws Exception{
        Main m = new Main();
        Connection con = m.getC().getConnection();

        // create a alert
        Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle("Cancel booking");
        a.setHeaderText("Are you sure you want to cancel booking?");
        Optional<ButtonType> result = a.showAndWait();

        if(!result.isPresent( ) &&  result.get()  == ButtonType.CANCEL)
        {
            System.out.println("Cancelled");
        }
        else if(result.get() == ButtonType.OK)
        {
            //ok button is pressed
            //Delete data from booking list
            Bookinglist.getItems().clear();
            
            //Delete room from booking table
            ps = con.prepareStatement("DELETE FROM `booking` WHERE `RID` = ?");
            ps.setInt(1,selectedid);
            ps.execute();

            //Set room to be available
            ps = con.prepareStatement("UPDATE `room` SET `Status` = ? WHERE `ID` = ?");
            ps.setString(1, "Available");
            ps.setInt(2,selectedid);
            ps.execute();
            updateModifyBookingList(selectedid);
            updateRoom();
        }
    }

    @FXML
    void Refresh(ActionEvent event) {
        updateRoom();
        Roomavailable.refresh();
        Bookinglist.refresh();
    }

    @FXML
    public void cancel(Button name) throws Exception{
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }
    
    //Log out
    public void Stulogout(ActionEvent event) throws IOException{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Login_form.fxml"));
        m.createScene(loader, "UOW room booking system", 600, 400, 450, 150);
    }
}