package hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;

public class Login {
    public Login(){

    }

    @FXML Button Lgnbutton;
    @FXML TextField Username;
    @FXML PasswordField Password;
    @FXML Label Usernamemissing;
    @FXML Label Passwordmissing;
    @FXML Label LoginError;


    public void userLogin(ActionEvent event) throws Exception{
        checkLogin();
    }

    private void checkLogin() throws Exception{
        Main m = new Main();
        if(Username.getText().toString().equals("")){
            Usernamemissing.setVisible(true);
        }
        else{
            Usernamemissing.setVisible(false);
        }

        if (Password.getText().toString().equals("")){
            Passwordmissing.setVisible(true);
        }
        else{
            Passwordmissing.setVisible(false);
        }
            Connection con = m.getC().getConnection();
            PreparedStatement ps;
            try{
                ps = con.prepareStatement("SELECT * FROM users WHERE uname = ? AND password = ?");
                ps.setString(1, Username.getText());
                ps.setString(2, String.valueOf(Password.getText().toString()));

                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    //Role
                    String role = rs.getString(6);

                    if (role.equals("Staff") == true){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard_admin.fxml"));
                        Admin acontroller = new Admin(Username.getText(), role);  
                        loader.setController(acontroller);
                        m.createScene(loader);
                    }
                    else{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard_student.fxml"));
                        Student scontroller = new Student(Username.getText(), role);  
                        loader.setController(scontroller);
                        m.createScene(loader);
                    }

                }else{
                    LoginError.setText("Invalid Login, please try again");
                    LoginError.setVisible(true);
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
    }
}
