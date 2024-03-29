package hellofx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

public class Login {
    //Constructor
    public Login(){
    }

    //FXML element
    @FXML Button Lgnbutton;
    @FXML TextField Username;
    @FXML PasswordField Password;
    @FXML Label Usernamemissing;
    @FXML Label Passwordmissing;
    @FXML Label LoginError;

    //Event trigger when pressing "Enter" key on username field
    public void nextField(ActionEvent event) throws Exception{
        Password.requestFocus();
    }
    //Event trigger when login
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
                ps = con.prepareStatement("SELECT * FROM users WHERE uname = ? AND password = ? AND suspend = 0");
                ps.setString(1, Username.getText());
                ps.setString(2, String.valueOf(Password.getText().toString()));

                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                    LocalDateTime now = LocalDateTime.now(); 
                    ps = con.prepareStatement("UPDATE `users` SET `login` = ? WHERE `uname` = ?");
                    ps.setString(1, dtf.format(now).toString());
                    ps.setString(2, Username.getText());
                    ps.execute();

                    //Role
                    String role = rs.getString(5);
                    //If user is admin -> admin dashboard
                    if (role.equals("Staff") == true){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Dashboard_admin.fxml"));
                        Admin acontroller = new Admin(Username.getText(), role);  
                        loader.setController(acontroller);
                        m.createScene(loader, "UOW dashboard (staff version)", 787, 675, 450, 50);
                        m.getStage().setOnCloseRequest(event -> {
                            try {
                                acontroller.getLog(m);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Platform.exit();    
                        });
                    }
                    //User is student -> student dashboard
                    else if (role.equals("Student") == true){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/Dashboard_student.fxml"));
                        Student scontroller = new Student(Username.getText(), role);  
                        loader.setController(scontroller);
                        m.createScene(loader, "UOW dashboard (student version)", 744, 544, 450, 50);
                        m.getStage().setOnCloseRequest(event -> {
                            try {
                                scontroller.getLog(m);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Platform.exit();    
                        });
                    }
                    else if (role.equals("User Management") == true){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout/UserMng.fxml"));
                        UserMng ucontroller = new UserMng(Username.getText(), role);  
                        loader.setController(ucontroller);
                        m.createScene(loader, "UOW dashboard (User Management version)", 683, 534, 450, 50);
                        m.getStage().setOnCloseRequest(event -> {
                            try {
                                ucontroller.getLog(m);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Platform.exit();    
                        });

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
