package hellofx;

import java.io.IOException;
import java.sql.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class MysqlConnect {
    public static Connection conn;

    //Current template
    static String template = "jdbc:mysql://localhost/%s?useEncoding=true&characterEncoding=UTF-8&user=%s&password=%s";

    //Default constructor
    public MysqlConnect(String database, String user, String password) throws IOException{
        conn = null;
        connect(database, user, password);
    }

    public Connection getConnection(){
        return conn;
    }
    
    //Establish connection
    public void connect(String database, String user, String password) throws IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(String.format(template, database, user, password));
            //...
        } catch (Exception ex) {

            //Display error message
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Make sure that Xampp is running");
            alert.setContentText(ex.getMessage());  
            alert.showAndWait();


            //Force exit
            System.exit(0);
        }
    }
}
