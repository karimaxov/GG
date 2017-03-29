/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.nextStage;

import dossier.DossierController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.*;

import java.io.IOException;

/**
 *
 * @author pc
 */
public class MenuController {
    @FXML
    private Label menuTitle;

    @FXML
    private JFXButton menuGestUserButton;

    @FXML
    private JFXButton menuStatButton;
    @FXML
    private JFXButton chauffeur;
    FXMLLoader loader;
    @FXML
    void chauffeur(ActionEvent event) {
        menu.close();
      //Main.openChauffeur(menu);

        loader =new FXMLLoader(getClass().getResource("../config.fxml"));

        Stage primaryStage =new Stage();

        ChauffeurController.getStage(menu,primaryStage);

        try {
            Parent root = loader.load();


            //ChauffeurController chauffeur=loader.getController();


            primaryStage.setTitle("Configuration Générale");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
    static Stage menu;

    
    @FXML
    private void initialize() {
        System.out.println("init");
        DisableAdminButtons(!isUserAdmin());
        menuTitle.setText("Bienvenue " + getUserName());
    }

    @FXML
    void client(ActionEvent event) {
        menu.close();
        Stage client=new Stage();
        ClientController.getStage(menu,client);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Client.fxml"));
            client.setTitle("Gestion Des Clients");
            client.setScene(new Scene(root));
            client.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private JFXButton dossier;


    @FXML
    void dossier(ActionEvent event) {
        Stage stage=new Stage();
        Parent root = null;
        try {
            DossierController.getStage(menu,stage);
            root = FXMLLoader.load(getClass().getResource("../../dossier/Dossier.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    @FXML
    void users(ActionEvent event) {
        menu.close();
        Stage users=new Stage();
        FXMLLoader loader =new FXMLLoader(getClass().getResource("../users.fxml"));
        UsersController.getStage(menu,users);
        Parent root = null;
        try {
            root = loader.load();
            users.setTitle("Gestion Des Utilisateurs");
            users.setScene(new Scene(root));
            users.setResizable(false);
            users.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void deco(ActionEvent event) {
        menu.close();
        Main.stage.show();
    }


    ////
    private static boolean isAdmin = false;
    private static String userName = "Karim";
    public static void SetUser(String name, boolean _isAdmin,Stage menuu)
    {
        userName = name;
        isAdmin = _isAdmin;
        menu=menuu;

    }

    public static boolean isUserAdmin()
    {
        return isAdmin;
    }

    public static String getUserName()
    {
        return userName;
    }
    
    public void DisableAdminButtons(boolean _disable)
    {
        menuGestUserButton.setDisable(_disable);
        menuStatButton.setDisable(_disable);
    }
}
