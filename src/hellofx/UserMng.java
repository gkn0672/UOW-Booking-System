package hellofx;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Resultset;

import javafx.beans.property.ReadOnlyStringWrapper;
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

public class UserMng implements Initializable {
    private String username;
    private String role;

    public UserMng(String username, String role){
        this.username = username;
        this.role = role;
    }

    //Declare FXML elements
    @FXML
     Label Role;

    @FXML
     TableColumn<User, String> Sfname;

    @FXML
     TableColumn<User, String> Slname;

    @FXML
     TableColumn<User, String> Suname;

    @FXML
     TableColumn<User, String> Supassword;

    @FXML
     Label Welcome;

    @FXML
     Button deleteuser;

    @FXML
     Button historylog;

    @FXML
     Button modifyuser;

    @FXML
     Button newuser;

    @FXML
     Button removesuspend;

    @FXML
     TableColumn<User, String> surole;

    @FXML
     Button suspend;

    @FXML
     TableView<User> suspendlist;

    @FXML
     TableColumn<User, String> ufname;

    @FXML
     TableColumn<User, String> ulname;

    @FXML
     Button umlogout;

    @FXML
     TableColumn<User, String> uname;

    @FXML
     TableColumn<User, String> upassword;

    @FXML
     TableColumn<User, String> urole;

    @FXML
     TableView<User> userlist;

    ObservableList<User> listU = FXCollections.observableArrayList();
    ObservableList<User> listS = FXCollections.observableArrayList();
    Resultset rs = null;
    PreparedStatement ps = null;

    @FXML
    void createuser(ActionEvent event) {

    }

    @FXML
    void deleteaccount(ActionEvent event) {

    }

    @FXML
    void modifyuseraccount(ActionEvent event) {

    }

    @FXML
    void suspendaccount(ActionEvent event) {

    }

    @FXML
    void ulogout(ActionEvent event) throws Exception{
       logout();
    }



    @FXML
    void viewhistorylog(ActionEvent event) {

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

    public void logout() throws Exception{
    Main m = new Main();
    getLog(m);
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Login_form.fxml"));
     m.createScene(loader, "UOW room booking system", 600, 400, 450, 150);
    }

    /*
    Get User information from mysql server:
        - Establish connection
        - Run SQL
        - Save data to observable list
    */
    
    public ObservableList<User> getDataUser(){
        Main m = new Main();
        Connection con = m.getC().getConnection();
        ObservableList<User> list = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE suspend = ?");
            ps.setInt(1,0);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new User(rs.getString("uname"), 
                                rs.getString("password"), 
                                rs.getString("fname"), 
                                rs.getString("lname"), 
                                rs.getString("role")));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }

    public ObservableList<User> getDataUserSuspend(){
        Main m = new Main();
        Connection con = m.getC().getConnection();
        ObservableList<User> listL = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE suspend = ?");
            ps.setInt(1,1);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                listL.add(new User(rs.getString("uname"), 
                                rs.getString("password"), 
                                rs.getString("fname"), 
                                rs.getString("lname"), 
                                rs.getString("role")));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return listL;
    }

    /*
    Update user list:
        - Assign column <View User.java>
        - Call function getData
        - Reset value
    */
    
    public void updateUser(){
        uname.setCellValueFactory(new PropertyValueFactory<User, String>("uname"));
        upassword.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        ufname.setCellValueFactory(new PropertyValueFactory<User, String>("fname"));
        ulname.setCellValueFactory(new PropertyValueFactory<User, String>("lname"));
        urole.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
        listU = getDataUser();
        userlist.setItems(listU);
    }

    public void updateUserSuspend(){
        Suname.setCellValueFactory(new PropertyValueFactory<User, String>("uname"));
        Supassword.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        Sfname.setCellValueFactory(new PropertyValueFactory<User, String>("fname"));
        Slname.setCellValueFactory(new PropertyValueFactory<User, String>("lname"));
        surole.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
        listS = getDataUserSuspend();
        suspendlist.setItems(listS);
    }
    


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Welcome.setText("Welcome, "+username+" !");
        Role.setText("UOW "+role);
        updateUser();
        updateUserSuspend();


        suspend.setDisable(true);
        removesuspend.setDisable(true);
        deleteuser.setDisable(true);
        historylog.setDisable(true);
        
    }
    
}
