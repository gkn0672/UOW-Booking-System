package hellofx;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Resultset;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class Add_new_promo_code extends Admin {
    private Main m;
    private Admin ad;

    public Add_new_promo_code(String username, String role, Main m, Admin ad) {
        super(username, role);
        this.m = m;
        this.ad = ad;
    }

    @FXML Button Confirmpromobutton;
    @FXML Button Cancelpromobutton;
    @FXML TextField Codename;
    @FXML TextField Codevalue;
    @FXML Label Promoerror;
    @FXML Label Valueerror;
    
    @Override 
    public void initialize(URL arg0, ResourceBundle arg1) {
    }
    
    //Event trigger (cancel create promo)
    public void Cancelpromo(ActionEvent event) throws Exception{
        cancel(Cancelpromobutton);
    }

    //Event trigger (confirm create promocode)
    public void Confirmpromo(ActionEvent event) throws Exception{
        confirmpromo();
    }

    //Confirm create a promocode (send data to mysql server)
    public void confirmpromo() throws Exception{
        Promoerror.setVisible(false);
        Valueerror.setVisible(false);
        try{
            Connection con = m.getC().getConnection();
            PreparedStatement ps;

            ps = con.prepareStatement("SELECT * FROM `promotioncode` WHERE `Code_name` = ?");
            ps.setString(1, Codename.getText());
            Resultset rs = (Resultset) ps.executeQuery();

            if(((ResultSet) rs).next()){
                Promoerror.setText("Promotioncode already exist");
                Promoerror.setVisible(true);
            }
            if(Codevalue.getText() == ""){
                Valueerror.setText("Value cannot be blank");
                Valueerror.setVisible(true);
            }
            else{
                ps = con.prepareStatement("INSERT INTO promotioncode (`Code_name`, `Code_sale`) VALUES (?, ?)");
                ps.setString(1, Codename.getText().toUpperCase());
                ps.setString(2, Codevalue.getText());

                if(ps.executeUpdate() != 0){
                    Main m = new Main();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
                    Success_message sm1 = new Success_message("Promotion code added");
                    loader.setController(sm1);
                    m.popup(loader, "Success", 332, 194, 650, 250);  
                    ad.updatePromo();
                }
                else{
                    System.out.println("Update failed");
                }
            }
        }
        catch(Exception e){
            System.out.println("Somthing wrong!");
            System.out.println(e.getMessage());
        }
    }
}
