package hellofx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    private static Stage stg;

    @Override
    //Show Login form
    public void start(Stage PrimaryStage) throws Exception{      

        //Init Xampp connection
        MysqlConnect c1 = new MysqlConnect("uow_booking_system", "root", "");

        //Login form settings
        stg = PrimaryStage;
        PrimaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("Login_form.fxml"));
        PrimaryStage.setTitle("UOW room booking system");
        PrimaryStage.setScene(new Scene(root, 600, 400));
        PrimaryStage.show();
    }

    //Change the scene
    public void changeScene(String fxml) throws IOException{
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }

    //Main method
    public static void main(String[] args){
        launch(args);
    }
}