package hellofx;

import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MysqlConnect {
    private Connection conn;
    public MysqlConnect(String database, String user, String password){
        conn = null;
        connect(database, user, password);
    }

    static String template = "jdbc:mysql://localhost/%s?useEncoding=true&characterEncoding=UTF-8&user=%s&password=%s";

    public void connect(String database, String user, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(String.format(template, database, user, password));
            //...
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Make sure that Xampp is running");
            alert.setContentText(ex.getMessage());  
            alert.showAndWait();
            System.exit(0);
        }
    }
}
