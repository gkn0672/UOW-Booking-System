package hellofx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
    private static Stage stg;
    private static MysqlConnect c1;

    //Get connection to mysql server
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
    public void createScene(FXMLLoader loader, String title, double width, double length, double x, double y) throws IOException{
        try{
            Scene sc = new Scene(loader.load(), width, length);
            stg.setTitle(title);
            stg.setResizable(false);
            stg.setY(y);
            stg.setX(x);
            stg.setScene(sc);
            stg.show();
        }
        catch(IOException e){
            System.out.println("Error when loading the scene");
            System.out.println(e.getMessage());
        }
    }

    //Create pop up window
    public void popup(FXMLLoader loader, String title, double width, double length, double x, double y) throws IOException{
        try{
            Stage stage = new Stage();
            Scene sc = new Scene(loader.load(), width, length);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setY(y);
            stage.setX(x);
            stage.setScene(sc);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
        catch (IOException e){
            System.out.println("Error when load pop up window");
            System.out.println(e.getMessage());
        }
    }

    //Main method (only for launching)
    public static void main(String[] args){
        launch(args);
    }
}