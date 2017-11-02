package sample.Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage stage;



    @Override
    public void start(Stage primaryStage) throws Exception{
        this.stage=primaryStage;
        FXMLLoader fxml = new FXMLLoader();
        fxml.setLocation(getClass().getResource("../Views/sample.fxml"));
        primaryStage.setTitle("login page ");
        primaryStage.setScene(new Scene(fxml.load(),800, 440));
        LoginController controller=fxml.getController();
        controller.Main(stage);//(res,primaryStage);
        primaryStage.setResizable(false);

        primaryStage.show();
        LoginController.loginStage=primaryStage;
       // primaryStage.close();
       // controller.setCurrentStage(primaryStage);
        //while(!(controller.isCloseCurrentS()) ){System.out.println("not yet");}
        //System.out.println(controller.echecLabal.getText());
       // primaryStage.close();
        //currentStage=primaryStage;

    }


    public static void main(String[] args) {
        launch(args);
    }
    /*public void setCloseS(boolean closeS) {
        this.closeS = closeS;
    }*/
}
