package hellofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage stg;

    @Override
    public void start(Stage PrimaryStage) throws Exception{
        stg = PrimaryStage;
        PrimaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("Login_form.fxml"));
        PrimaryStage.setTitle("UOW room booking system");
        PrimaryStage.setScene(new Scene(root, 600, 400));
        PrimaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}