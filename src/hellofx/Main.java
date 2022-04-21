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

    //Get stage
    public Stage getStage(){
        return stg;
    }

    @Override
    //Show Login form (First run)
    public void start(Stage PrimaryStage) throws Exception{      

        //Init Xampp connection
        c1 = new MysqlConnect("uow_booking_system", "root", "");

        //Login form settings
        stg = PrimaryStage;
        PrimaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("Layout/Login_form.fxml"));
        PrimaryStage.setTitle("UOW room booking system ver 1.0");
        PrimaryStage.setScene(new Scene(root, 600, 400));
        PrimaryStage.show();
    }

    //Change the scene
    public void createScene(FXMLLoader loader, String title, double width, double length, double x, double y) throws IOException{
        try{
            //Load the scene
            Scene sc = new Scene(loader.load(), width, length);
            stg.setTitle(title);
            stg.setResizable(false);

            //Set position
            stg.setY(y);
            stg.setX(x);

            //Display
            stg.setScene(sc);
            stg.show();
        }
        catch(IOException e){
            System.out.println("Error when loading the scene");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //Create pop up window
    public void popup(FXMLLoader loader, String title, double width, double length, double x, double y) throws IOException{
        try{
            //New pop up window
            Stage stage = new Stage();
            Scene sc = new Scene(loader.load(), width, length);

            //Set title
            stage.setTitle(title);

            //Cannot resize window
            stage.setResizable(false);

            //Set position
            stage.setY(y);
            stage.setX(x);

            //Disable close button on top right
            stage.initStyle(StageStyle.UNDECORATED);

            //Show screen
            stage.setScene(sc);
            stage.show();
        }
        catch (IOException e){
            System.out.println("Error when load pop up window");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //Main method (only for launching)
    public static void main(String[] args){
        launch(args);
    }
}