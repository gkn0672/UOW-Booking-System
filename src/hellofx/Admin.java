package hellofx;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class Admin implements Initializable{
    private String username;
    private String role;
    private int activeid;
    private String activename;
    private Main m;

    //Constructor
    public Admin(){

    }

    //Other constructor
    public Admin(String username, String role){
        this.username = username;
        this.role = role;
    }

    //Declare FXML elements
    @FXML Label Welcome;
    @FXML Label Role;
    @FXML Label admin_role;
    @FXML Label admin_username;

    @FXML Button logoutbutton;
    @FXML Button Createroom;
    @FXML Button Createpromo;
    @FXML Button Editpromo;
    @FXML Button addnewactive;
    @FXML Button Deleteactive;
    @FXML Button Modifyroom;
    @FXML Button Launchbutton;
    @FXML Button CloseRoom;

    //Not visible FXML
    @FXML Label Promonameselected;
    @FXML Label Promovalueselected;
    @FXML Label Idselected;

    //Promotion code list FXML
    @FXML
    protected TableColumn<Promocode, String> col_promoname;
    @FXML
    protected TableColumn<Promocode, Integer> col_promovalue;
    @FXML
    protected TableView<Promocode> table_promo;

    //Room list FXML
    @FXML
    private TableView<Room> Room;
    @FXML
    private TableColumn<Room, Double> Rprice;
    @FXML
    private TableColumn<Room, Integer> RID;
    @FXML
    private TableColumn<Room, String> Rstatus;
    @FXML
    private TableColumn<Room, String> Rtime;
    @FXML
    private TableColumn<Room, Integer> Rcapacity;
    @FXML
    private TableColumn<Room, String> Rdate;
    @FXML
    private TableColumn<Room, String> Rname;

    //Active promotion code list FXML
    @FXML
    private TableView<Activepromo> Activepromo;
    @FXML
    private TableColumn<Activepromo, String> Activecode;

    ObservableList<Promocode> listP = FXCollections.observableArrayList();
    ObservableList<Room> listR = FXCollections.observableArrayList();
    ObservableList<Activepromo> listAP = FXCollections.observableArrayList();
    int index = -1;
    Resultset rs = null;
    PreparedStatement ps = null;
    
    /*
    Welcome menu:
    - Display welcome message and user's role
    - Update room list and promotion list
    - Disable some elements (need input from user before enable again)
    */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Welcome.setText("Welcome, "+username+" !");
        Role.setText("UOW "+role);

        updatePromo();
        updateRoom();

        Idselected.setText(null);
        Promonameselected.setText(null);
        Promovalueselected.setText(null);

        Editpromo.setDisable(true);
        addnewactive.setDisable(true);
        Deleteactive.setDisable(true);
        Launchbutton.setDisable(true);
        Modifyroom.setDisable(true);
        CloseRoom.setDisable(true);

        
    }

    /*
    Update promotion code list:
        - Assign columns in table view with value of promotion code object <View Promocode.java for detail>
        - Call function getDataPromo to get promotion code data from mysql server
        - Reset value of some element (refresh)
    */
    public void updatePromo(){
        col_promoname.setCellValueFactory(new PropertyValueFactory<Promocode, String>("name"));
        col_promovalue.setCellValueFactory(new PropertyValueFactory<Promocode, Integer>("value"));
        listP = getDataPromo();
        table_promo.setItems(listP);
        Promonameselected.setText(null);
        Promovalueselected.setText(null);
        Editpromo.setDisable(true);
    }

    /*
    Update active promotion code list:
        - Assign column <View Activepromo.java>
        - Call function getData
    */
    public void updateActivePromo(int index){
        Activecode.setCellValueFactory(new PropertyValueFactory<Activepromo, String>("name"));
        listAP = getDataAP(index);
        Activepromo.setItems(listAP);
    }

    /*
    Update room list:
        - Assign column <View Room.java>
        - Call function getData
        - Reset value
    */
    public void updateRoom(){
        RID.setCellValueFactory(new PropertyValueFactory<Room, Integer>("id"));
        Rname.setCellValueFactory(new PropertyValueFactory<Room, String>("name"));
        Rdate.setCellValueFactory(new PropertyValueFactory<Room, String>("date"));
        Rtime.setCellValueFactory(new PropertyValueFactory<Room, String>("time"));
        Rcapacity.setCellValueFactory(new PropertyValueFactory<Room, Integer>("capacity"));
        Rprice.setCellValueFactory(new PropertyValueFactory<Room, Double>("price"));
        Rstatus.setCellValueFactory(new PropertyValueFactory<Room, String>("status"));
        listR = getDataRoom();
        Room.setItems(listR);
        Idselected.setText(null);
        Launchbutton.setDisable(true);
        Modifyroom.setDisable(true);
        CloseRoom.setDisable(true);
    }

    /*
    Get active promotion code from mysql server:
        - Establish connection
        - Run SQL
        - Save data to observable list
    */
    public ObservableList<Activepromo> getDataAP(int index){
        Main m = new Main();
        Connection con = m.getC().getConnection();
        ObservableList<Activepromo> list = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM `room_promo` WHERE `RoomID` = ?");
            ps.setString(1, String.valueOf(index));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Activepromo(Integer.parseInt(rs.getString("RoomID")), rs.getString("Code_name")));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }

    /*
    Get promotion code from mysql server:
        - Establish connection
        - Run SQL
        - Save data to observable list
    */
    public ObservableList<Promocode> getDataPromo(){ 
        Main m = new Main();
        Connection con = m.getC().getConnection();
        ObservableList<Promocode> list = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM promotioncode");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Promocode(rs.getString("Code_name"), Integer.parseInt(rs.getString("Code_sale"))));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }

    /*
    Get room information from mysql server:
        - Establish connection
        - Run SQL
        - Save data to observable list
    */
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

    //Event trigger when user log out
    public void Adlogout(ActionEvent event) throws Exception{
        logout();
    }

    //Event trigger when user create a room
    public void Createroom(ActionEvent event) throws Exception{
        createroom();
    }

    //Event trigger when user create a promotion code
    public void Createpromo(ActionEvent event) throws Exception{
        createpromo();
    }

    //Event trigger when user edit a promotion code
    public void Editpromo(ActionEvent event) throws Exception{
        editpromo();
    }

    //Event trigger when user close a room
    public void Closeroom(ActionEvent event) throws Exception{
        closeroom();
    }

    /*
    Event trigger when user launch/open a room
        - Establish connection
        - Run SQL
        - Change room status to available
        - Print message
    */
    public void Launch(ActionEvent event) throws Exception{
        Main m = new Main();
        Connection con = m.getC().getConnection();
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE `room` SET `Status` = ? WHERE `ID` = ?");
            ps.setString(1, "Available");
            ps.setString(2, Idselected.getText());
            ps.execute();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
            Success_message sm1 = new Success_message("Launch success!");
            loader.setController(sm1);
            m.popup(loader, "Success", 332, 194, 650, 250);  
            updateRoom();
            Launchbutton.setDisable(true);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /*
    Event trigger when user add promotion code to a room
        - Set to new active code form
        - Create controller class object
        - Set controller
        - Pop up window
    */
    public void AddActive(ActionEvent event) throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/New_active_code.fxml"));
        Add_new_active ana1 = new Add_new_active(this.username, this.role, m, Idselected.getText(), this);
        loader.setController(ana1);
        m.popup(loader, "Add promocode", 373, 235, 650, 150);
    }

    /*
    Event trigger when user remove promotion code from a room
        - Establish connection
        - Run SQL
        - Display message
    */
    public void Deleteactive(ActionEvent event) throws Exception{
        try{
            Main m = new Main();
            Connection con = m.getC().getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("DELETE FROM `room_promo` WHERE `RoomID` = ? AND `Code_name` = ?");
            ps.setString(1, Idselected.getText());
            ps.setString(2, activename);
            ps.execute();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
            Success_message sm1 = new Success_message("Promocode deactivated");
            loader.setController(sm1);
            m.popup(loader, "Success", 332, 194, 650, 250);  
            updateActivePromo(Integer.parseInt(Idselected.getText()));
            Deleteactive.setDisable(true);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /*
    Event trigger when user change room information
        - Set to edit room form
        - Create controller class object
        - Set controller
        - Pop up window
    */
    public void Modify(ActionEvent event) throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Edit_room_form.fxml"));
        Edit_room edr1 = new Edit_room(this.username, this.role, m, this, Integer.parseInt(Idselected.getText()));
        loader.setController(edr1);
        m.popup(loader, "Create room", 395, 584, 650, 150);
    }

    public void getLog(Main m) throws SQLException{
        Connection con = m.getC().getConnection();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                    LocalDateTime now = LocalDateTime.now(); 
                    ps = con.prepareStatement("UPDATE `users` SET `logout` = ? WHERE `uname` = ?");
                    ps.setString(1, dtf.format(now).toString());
                    ps.setString(2, username);
                    ps.execute();
    }
    
    /*
    User log out:
        - Close dashboard
        - Set to log in form
    */
    public void logout() throws Exception{
        Main m = new Main();
        getLog(m);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Login_form.fxml"));
        m.createScene(loader, "UOW room booking system", 600, 400, 450, 150);
    }

    /*
    Cancel button:
        - Get the pop up window
        - Close pop up window
    */
    @FXML
    public void cancel(Button name) throws Exception{
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }
    
    /*
    When user click on a promotion code row in table:
        - Get the row index (index = -1 -> blank table)
        - Get the row data
        - Enable edit feature
    */
    @FXML
    public void getSelectedPromo(MouseEvent event){
        index = table_promo.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        Promonameselected.setText(col_promoname.getCellData(index).toString()); 
        Promovalueselected.setText(col_promovalue.getCellData(index).toString());
        Editpromo.setDisable(false);
    }

    /*
    When user click on a room row in table:
        - Get the row index (index = -1 -> blank table)
        - Get the row data
        - Get the room status
            + Not available -> enable launch & disable close
            + Available -> enable close & disable launch
        - Enable edit feature
        - Enable active promotion code feature
    */
    @FXML
    public void getSelectedRoom(MouseEvent event){
        index = Room.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        Idselected.setText(RID.getCellData(index).toString());
        updateActivePromo(Integer.parseInt(Idselected.getText()));
        String status = Rstatus.getCellData(index).toString();

        if(status.equals("Not available")){
            Launchbutton.setDisable(false);
            CloseRoom.setDisable(true);
        }else{
            Launchbutton.setDisable(true);
            CloseRoom.setDisable(false);
        }
        Modifyroom.setDisable(false);
        addnewactive.setDisable(false);
    }

    /*
    When user click on a  active promotion code row in table:
        - Get the row index (index = -1 -> blank table)
        - Get the row data
        - Enable deactive feature
    */
    public void getSelectedActive(MouseEvent event){
        index = Activepromo.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        activeid = index;
        activename = Activecode.getCellData(activeid).toString();
        Deleteactive.setDisable(false);
    }

    /*
    After press "Create room":
        - Set to create room form
        - Create controller class's object
        - Set control to the object
        - Create pop up window
    */
    public void createroom() throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Create_room_form.fxml"));
        Add_new_room adr1 = new Add_new_room(this.username, this.role, m, this);
        loader.setController(adr1);
        m.popup(loader, "Create room", 395, 508, 650, 150);
    }

    /*
    After press "Edit promo":
        - Set to edit promotion code form
        - Create controller class's object
        - Set control to the object
        - Create pop up window
    */
    public void editpromo() throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Edit_promo.fxml"));
        Edit_promo ed1 = new Edit_promo(Promonameselected.getText(), Integer.parseInt(Promovalueselected.getText()), this, m);
        loader.setController(ed1);
        m.popup(loader, "Edit promocode", 394, 330, 650, 150);
    }

    //Create new promocode form
    public void createpromo() throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Create_promo_code.fxml"));
        Add_new_promo_code adrpc1 = new Add_new_promo_code(this.username, this.role, m, this);
        loader.setController(adrpc1);
        m.popup(loader, "Create promocode", 373, 453, 650, 150);
    }

    /*
    Close opened room
        - Establish connection
        - Run SQL
        - Display message
    */
    public void closeroom() throws Exception{
        Main m = new Main();
        Connection con = m.getC().getConnection();
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE `room` SET `Status` = ? WHERE `ID` = ?");
            ps.setString(1, "Not available");
            ps.setString(2, Idselected.getText());
            ps.execute();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
            Success_message sm1 = new Success_message("Room closed");
            loader.setController(sm1);
            m.popup(loader, "Success", 332, 194, 650, 250);  
            updateRoom();
            CloseRoom.setDisable(true);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
