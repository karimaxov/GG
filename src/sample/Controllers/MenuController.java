/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
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

        loader =new FXMLLoader(getClass().getResource("../Views/config.fxml"));

        Stage primaryStage =new Stage();

        ConfigurationController.getStage(menu,primaryStage);

        try {
            Parent root = loader.load();


            //ConfigurationController chauffeur=loader.getController();


            primaryStage.setTitle("Configuration Générale");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
    public static Stage menu;



    @FXML
    void client(ActionEvent event) {
        menu.close();
        Stage client=new Stage();
        ClientController.getStage(menu,client);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Views/Client.fxml"));
            client.setTitle("Gestion Des Clients");
            client.setScene(new Scene(root,1000,480));
            menu.close();
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
            //DossierController.getStage(menu,stage);
           // DossierController.menuStage=menu;
            DossierController.getStage(menu,stage);
            root = FXMLLoader.load(getClass().getResource("../Views/Dossier.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            menu.close();
            stage.show();
            stage.setResizable(false);
            DossierController.dossierStage=stage;

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @FXML
    void statm(ActionEvent event) {
        Stage stagee=new Stage();

        Parent root = null;
        try {
            MenuStatsController.getStage(menu,stagee);
            root = FXMLLoader.load(getClass().getResource("../Views/MenuStats.fxml"));
            Scene scene = new Scene(root);

            stagee.setScene(scene);
            menu.close();
            stagee.show();
            stagee.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void users(ActionEvent event) {
        menu.close();
        Stage users=new Stage();
        FXMLLoader loader =new FXMLLoader(getClass().getResource("../Views/users.fxml"));
        UsersController.getStage(menu,users);
        Parent root = null;
        try {
            root = loader.load();
            users.setTitle("Gestion Des Utilisateurs");
            users.setScene(new Scene(root));
            users.setResizable(false);
            menu.close();
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
    public static void SetUser(String name, boolean _isAdmin)
    {
        userName = name;
        isAdmin = _isAdmin;


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

    Connection conn = null;
    Statement stmt =null;
    // JDBC driver name and database URL
    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    String noms;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("init");
        DisableAdminButtons(!isUserAdmin());
        menuTitle.setText("Bienvenue " + getUserName());

        new MyThread().start();
    }


    class MyThread extends Thread{
        @Override
        public void run() {


            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
                stmt = (Statement) conn.createStatement();


                Class.forName(JDBC_DRIVER);
                conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());

                String sql = "select * from prixunitaires ";
                ResultSet res;
                stmt = conn.createStatement();
                boolean continu=true;
                while(continu) {

                    res = stmt.executeQuery(sql);
                    res.next();
                    int traite = res.getInt("traite");
                    System.out.println(traite);
                    if (traite == 0 && isAdmin) {
                        noms = res.getString("userr");
                        System.out.println(noms);


                        Notifications n = Notifications.create()
                                .graphic(null)
                                .position(Pos.BOTTOM_RIGHT)
                                .title("Modification detecté")
                                .text("Des modification dans les prix unitaires ont été effectué par : \n  " + noms)
                                .hideAfter(Duration.seconds(5));
                        javafx.application.Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                n.showInformation();
                            }
                        });
                        // n.showInformation();
                        System.out.println("notification");
                        stmt.executeUpdate("update prixunitaires set traite='" + 1 + "'where 1");
                        System.out.println("notification2");

                    }
                    System.out.println("pas notification");
                }
            } catch (SQLException e) {
                System.out.println("sqlexception");
            } catch (ClassNotFoundException e) {
                System.out.println("classe not found");

                e.printStackTrace();
            }



        }
    }
}
