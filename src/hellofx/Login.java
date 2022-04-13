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

    //Event trigger
    public void userLogin(ActionEvent event) throws Exception{
        checkLogin();
    }

    //Validate log in
    private void checkLogin() throws Exception{
        Main m = new Main();
        //Check if username or password is NULL
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
        //Compare username and password with database
            Connection con = m.getC().getConnection();
            PreparedStatement ps;
            try{
                ps = con.prepareStatement("SELECT * FROM users WHERE uname = ? AND password = ?");
                ps.setString(1, Username.getText());
                ps.setString(2, String.valueOf(Password.getText().toString()));

                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    //Role
                    String role = rs.getString(5);
                    //If user is admin -> admin dashboard
                    if (role.equals("Staff") == true){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Dashboard_admin.fxml"));
                        Admin acontroller = new Admin(Username.getText(), role);  
                        loader.setController(acontroller);
                        m.createScene(loader, "UOW dashboard (staff version)", 1280, 600, 200, 50);
                    }
                    //User is student -> student dashboard
                    else{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Dashboard_student.fxml"));
                        Student scontroller = new Student(Username.getText(), role);  
                        loader.setController(scontroller);
                        m.createScene(loader, "UOW dashboard (student version)", 1280, 600, 200, 50);
                    }

                }else{
                    //Incorrect username or password
                    LoginError.setText("Invalid Login, please try again");
                    LoginError.setVisible(true);
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
    }
}
