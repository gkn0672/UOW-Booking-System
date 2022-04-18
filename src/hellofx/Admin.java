package hellofx;

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

public class Admin implements Initializable{
    private String username;
    private String role;

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
    @FXML Button editactive;
    @FXML Button Modifyroom;
    @FXML Button Launchbutton;

    @FXML Label Promonameselected;
    @FXML Label Promovalueselected;
    @FXML Label Idselected;

    @FXML
    protected TableColumn<Promocode, String> col_promoname;
    @FXML
    protected TableColumn<Promocode, Integer> col_promovalue;
    @FXML
    protected TableView<Promocode> table_promo;

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

    @FXML
    private TableView<?> Activepromo;
    @FXML
    private TableColumn<?, ?> Activecode;

    ObservableList<Promocode> listP = FXCollections.observableArrayList();
    ObservableList<Room> listR = FXCollections.observableArrayList();
    int index = -1;
    Resultset rs = null;
    PreparedStatement ps = null;
    
    //Welcome menu
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Welcome.setText("Welcome, "+username+" !");
        Role.setText("UOW "+role);

        updatePromo();
        updateRoom();

        Editpromo.setDisable(true);
        addnewactive.setDisable(true);
        editactive.setDisable(true);
        Launchbutton.setDisable(true);
        Modifyroom.setDisable(true);
    }

    //Update the promotion code table ~ refresh
    public void updatePromo(){
        col_promoname.setCellValueFactory(new PropertyValueFactory<Promocode, String>("name"));
        col_promovalue.setCellValueFactory(new PropertyValueFactory<Promocode, Integer>("value"));
        listP = getDataPromo();
        table_promo.setItems(listP);
    }

    //Update room list ~ refresh
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
    }

    //Get promotion code data from mysql
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

    //Get room data from mysql
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
                                rs.getInt("Capacity")));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }

    //Event trigger (log out)
    public void Adlogout(ActionEvent event) throws Exception{
        logout();
    }

    //Event trigger (create room)
    public void Createroom(ActionEvent event) throws Exception{
        createroom();
    }

    //Event trigger (create promo)
    public void Createpromo(ActionEvent event) throws Exception{
        createpromo();
    }

    //Event trigger (edit promo)
    public void Editpromo(ActionEvent event) throws Exception{
        editpromo();
    }

    //Event trigger (Launch room)
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
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //Event trigger (Modify room)
    public void Modify(ActionEvent event) throws Exception{
        //modify();
    }

    //Log out
    public void logout() throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Login_form.fxml"));
        m.createScene(loader, "UOW room booking system", 600, 400, 450, 150);
    }

    //Close popup
    @FXML
    public void cancel(Button name) throws Exception{
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    //Create new room form
    public void createroom() throws Exception{
        Main m = new Main();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Create_room_form.fxml"));
        Add_new_room adr1 = new Add_new_room(this.username, this.role, m, this);
        loader.setController(adr1);
        m.popup(loader, "Create room", 395, 508, 650, 150);
    }
    
    //Promo selected
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

    //Room selected
    @FXML
    public void getSelectedRoom(MouseEvent event){
        index = Room.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        Idselected.setText(RID.getCellData(index).toString());
        String status = Rstatus.getCellData(index).toString();

        if(status.equals("Not available")){
            Launchbutton.setDisable(false);
        }else{
            Launchbutton.setDisable(true);
        }

        Modifyroom.setDisable(false);
    }

    //Create new editpromo form
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
}
