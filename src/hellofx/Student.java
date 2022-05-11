package hellofx;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Resultset;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Student implements Initializable{
    private String username;
    private String role;
    private int selectedid;
    ObservableList<Room> listR = FXCollections.observableArrayList();
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
    private TableColumn<Room, Integer> RID;

    @FXML
    private TableColumn<?, ?> RID1;

    @FXML
    private TableColumn<Room, Integer> Rcapacity;

    @FXML
    private TableColumn<Room, String> Rdate;

    @FXML
    private Button Refreshbutton;

    @FXML
    private TableColumn<Room, String> Rname;

    @FXML
    private TableView<Room> Roomavailable;

    @FXML
    private TableColumn<Room, Double> Rprice;

    @FXML
    private TableColumn<Room, String> Rtime;

    //Welcome menu when log in
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Susername.setText("Welcome, "+username+" !");
        Srole.setText("UOW "+role);

        updateRoom();
        Modifybutton.setDisable(true);
        Bookbutton.setDisable(true);
        //updateBooking();
    }
    
    public void updateBookingList(){

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
    }

    public ObservableList<Room> getDataRoom(){
        Main m = new Main();
        Connection con = m.getC().getConnection();
        ObservableList<Room> list = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM room");
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
    void Bookroom(ActionEvent event) throws IOException {
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Book_room_form.fxml"));
        Book_room br1 = new Book_room(this.username, this.role, m, selectedid, this);
        loader.setController(br1);
        m.popup(loader, "Booking", 395, 584, 650, 150);
    }

    @FXML
    void Modifyroom(ActionEvent event) {
        
    }

    @FXML
    void Refresh(ActionEvent event) {

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