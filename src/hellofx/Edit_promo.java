package hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Edit_promo extends Admin{
    private String name;
    private int value;
    private Admin ad;
    public Edit_promo(String name, int value, Admin ad){
        super();
        this.name = name;
        this.value = value;
        this.ad = ad;
    }

    @FXML Button Cancel;

    @FXML Button Deletepromo;

    @FXML TextField Editcodename;

    @FXML TextField Editcodevalue;

    @FXML
    private Button Updatepromo;

    //Event trigger (cancel)
    public void ECancel(ActionEvent event) throws Exception{
        cancel(Cancel);
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Editcodename.setText(name);
        Editcodevalue.setText(String.valueOf(value));
    }
}
