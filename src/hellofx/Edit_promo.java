package hellofx;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Edit_promo extends Admin{
    private String name;
    private int value;
    private Admin ad;
    private Main m;
    public Edit_promo(String name, int value, Admin ad, Main m){
        super();
        this.name = name;
        this.value = value;
        this.ad = ad;
        this.m = m;
    }

    @FXML Button Cancel;

    @FXML Button Deletepromo;

    @FXML TextField Editcodename;

    @FXML TextField Editcodevalue;

    @FXML Button Updatepromo;

    @FXML Label Editerror;

    @FXML TextField Deleteconfirm;

    @FXML Label Deletewarn;

    //Event trigger (cancel)
    public void ECancel(ActionEvent event) throws Exception{
        cancel(Cancel);
    }
    
    //Event trigger (update)
    public void EUpdate(ActionEvent event) throws Exception{
        editupdate();
    }

    //Event trigger (Delete)
    public void EDelete(ActionEvent event) throws Exception{
        editdelete();
    }

    //Delete confirm
    public void Deletecheck(KeyEvent event){
        if ((Deleteconfirm.getText()).equals("Confirm")){
            Deletepromo.setDisable(false);
        }
    }

    //Show delete warning
    public void Deletewarnshow(MouseEvent event){
        Deletewarn.setVisible(true);
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Editcodename.setText(name);
        Editcodevalue.setText(String.valueOf(value));
        Deletepromo.setDisable(true);
        Deletewarn.setVisible(false);
    }

    //Update promotion code
    public void editupdate() throws Exception{
        Editerror.setVisible(false);
        try{
            Connection con = m.getC().getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("SELECT * FROM `promotioncode` WHERE `Code_name` = ? AND `Code_name` != ?");
            ps.setString(1, Editcodename.getText());
            ps.setString(2, this.name);
            ResultSet rs = (ResultSet) ps.executeQuery();

            if(((ResultSet)rs).next()){
                Editerror.setText("Promotion code already exist");
                Editerror.setVisible(true);
            }
            else{
                String newname = Editcodename.getText();
                String newvalue = Editcodevalue.getText();
                if (newname == ""){
                    newname = this.name;
                }

                if(newvalue == ""){
                    newvalue = String.valueOf(this.value);
                }

                ps = con.prepareStatement("UPDATE promotioncode SET Code_name = ?, Code_sale = ? WHERE Code_name = ?");
                ps.setString(1, newname);
                ps.setString(2, newvalue);
                ps.setString(3, this.name);
                ps.execute();
                
                cancel(Updatepromo);
                Main m = new Main();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
                Success_message sm1 = new Success_message("Promotion code updated");
                loader.setController(sm1);
                m.popup(loader, "Success", 332, 194, 650, 250);  
                ad.updatePromo();
            }
        }
        catch(Exception e){
            System.out.println("Something wrong!");
            System.out.println(e.getMessage());
        }
    }

    //Delete promotion code
    public void editdelete() throws Exception{
        try{
            Connection con = m.getC().getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("DELETE FROM promotioncode WHERE Code_name = ?");
            ps.setString(1, this.name);
            ps.execute();

            cancel(Deletepromo);
            Main m = new Main();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Success_message.fxml"));
            Success_message sm1 = new Success_message("Promotion code deleted");
            loader.setController(sm1);
            m.popup(loader, "Success", 332, 194, 650, 250);  
            ad.updatePromo();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
