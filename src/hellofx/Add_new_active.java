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
import javafx.scene.control.Label;

public class Add_new_active extends Admin {
    private Main m;
    private Admin ad;
    private String id;
    public Add_new_active(String username, String role, Main m, String id, Admin ad){
        super(username, role);
        this.m = m;
        this.id = id;
        this.ad = ad;
    }

    public void initialize(URL arg0, ResourceBundle arg1){
        try{
            Connection con = m.getC().getConnection();
            PreparedStatement ps;
            ResultSet rs;
            ps = con.prepareStatement("SELECT `Code_name` FROM `promotioncode` WHERE 1");
            rs = (ResultSet) ps.executeQuery();

            while(rs.next()){
                Promolist.getItems().add(rs.getString(1));
            }   

            Confirmactive.setDisable(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //Event trigger (Active check)
    public void Activecheck(ActionEvent event){
        String value = Promolist.getValue();

        if(!value.equals("")){
            Confirmactive.setDisable(false);
        }
        else{
            Confirmactive.setDisable(true);
        }
    }
    
    @FXML
    private Button Cancel;

    @FXML
    private Button Confirmactive;

    @FXML
    private Label Activeerror;

    @FXML
    private ComboBox<String> Promolist;

    @FXML
    void Cancelactive(ActionEvent event) throws Exception{
        cancel(Cancel);
    }

    @FXML
    void Confirmactive(ActionEvent event) {
        try{
            Connection con = m.getC().getConnection();
            PreparedStatement ps;

            ps = con.prepareStatement("SELECT * FROM `room_promo` WHERE `RoomID` = ? AND `Code_name` = ?");
            ps.setString(1, id);
            ps.setString(2, Promolist.getValue());
            ResultSet rs = (ResultSet) ps.executeQuery();

            if(((ResultSet)rs).next()){
                Activeerror.setText("Promocode already activated");
                Activeerror.setVisible(true);
            }
            else{
                ps = con.prepareStatement("INSERT INTO `room_promo`(`RoomID`, `Code_name`) VALUES (?,?)");
                ps.setString(1, id);
                ps.setString(2, Promolist.getValue());

                if(ps.executeUpdate() != 0){
                    cancel(Cancel);
                    Main m = new Main();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
                    Success_message sm1 = new Success_message("Promocde activated");
                    loader.setController(sm1);
                    m.popup(loader, "Success", 332, 194, 650, 250);  
                    ad.updateActivePromo(Integer.parseInt(id));
                }
                else{
                    System.out.println("Activate failed");
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
