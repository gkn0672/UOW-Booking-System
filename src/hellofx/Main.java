package hellofx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    private static Stage stg;
    private static MysqlConnect c1;

    public MysqlConnect getC(){
        return c1;
    }

    @Override
    //Show Login form
    public void start(Stage PrimaryStage) throws Exception{      

        //Init Xampp connection
        c1 = new MysqlConnect("uow_booking_system", "root", "");

        //Login form settings
        stg = PrimaryStage;
        PrimaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("Login_form.fxml"));
        PrimaryStage.setTitle("UOW room booking system");
        PrimaryStage.setScene(new Scene(root, 600, 400));
        PrimaryStage.show();
    }
    
    //Change the scene
    public void createScene(FXMLLoader loader) throws IOException{
        Scene sc = new Scene(loader.load(), 1280, 720);
        stg.setResizable(false);
        stg.setY(50);
        stg.setX(200);
        stg.setScene(sc);
        stg.show();
    }

    //Main method
    public static void main(String[] args){
        launch(args);
    }
}