package sample.Controllers;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
//////////////////////////////////////////////////////////
import java.sql.DriverManager;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

//////////////////////////////////////////////////////////

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class LoginController implements Initializable {
    public static Stage loginStage;
    //////////////


    public JFXTextField usernameField;
    public JFXTextField passwordFieldShown;
    public JFXPasswordField passwordField;
    public JFXButton connectionButton;
    public javafx.scene.control.Label echecLabal;
    public JFXCheckBox checkpassword;
    //methods
//

    Stage stage;
    public  void Main(Stage stage){
        this.stage=stage;
    }
    public void checkPassword(){
       // System.out.println(verifyChars(usernameField.getText(),"=+\"':/\\-"));
        if (checkpassword.isSelected()){
            passwordField.setVisible(false);
            passwordFieldShown.setVisible(true);
            passwordFieldShown.setText(passwordField.getText());
        }
        else{
            passwordFieldShown.setVisible(false);
            passwordField.setText(passwordFieldShown.getText());
            passwordField.setVisible(true);

        }
    }

    public boolean verifyChars(String word,String chars){
            boolean res=true;
            int i =0;
        while(i<chars.length() && res){
            if (word.contains(""+chars.charAt(i))){res= false;}
            i++;
        }
        return res;
    }

    public void verifyIdentity() throws SQLException, IOException {
            if (checkpassword.isSelected()){passwordField.setText(passwordFieldShown.getText());}
            String username = usernameField.getText();
            String password = passwordField.getText();

            DB_CNX.restoreDbInfo();
            /////////////////////////////////////////////////////////////////
       if (verifyChars(username,"=+\"':/\\-*")) {
           if(verifyChars(password,"=+\"':/\\-*")) {
           try {
               com.mysql.jdbc.Connection conn = null;
               com.mysql.jdbc.Statement stmt = null;
               Class.forName("com.mysql.jdbc.Driver");
               System.out.println("Connecting to database...");
               conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
               stmt = (com.mysql.jdbc.Statement) conn.createStatement();

               String sql;
               //////validation needed here !
               sql = "SELECT * FROM login WHERE username='" + username + "' AND  password='" + password + "'";//"insert into login (username,password,super)values('bakir','9iw9iw',0)";
               ResultSet res = stmt.executeQuery(sql);

               //System.out.println(res.next());
               if (res.next() == true) {
                   //if(){
                   boolean sup = (res.getInt("super") == 1);



                   //create a new scene with root and set the stage
                   MenuController.SetUser(username, sup);

                   FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/MainMenu.fxml"));
                   Parent rt = loader.load();
                   usernameField.clear();
                   passwordFieldShown.clear();
                   passwordField.clear();
                   Stage s = new Stage();
                   s.setScene(new Scene(rt));

                   stage.close();
                   MenuController.menu=s;

                   s.show();


                   System.out.println("Bienvenue Mr " + username + " !");
               } else {
                   usernameField.setUnFocusColor(Color.RED);
                   passwordField.setUnFocusColor(Color.RED);
                   echecLabal.setText("nom d'utilisateur ou mot de passe incorrecte");

               }

               stmt.close();
               conn.close();
           } catch (ClassNotFoundException ex) {
               Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
           } catch (Exception e) {
               //
               //e.printStackTrace();
               System.out.println("Something is wrong ! ... check ur network or the address of the server");
               FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/databaseError.fxml"));
               Parent rt = loader.load();
               Stage errorStage = new Stage();
               errorStage.setScene(new Scene(rt));
               errorStage.show();
               dbErrorController.databseErrorStage = errorStage;
               errorStage.setAlwaysOnTop(true);
               loginStage.hide();
               //(res,primaryStage);
           }
       }else {
               Tooltip tp = new Tooltip("Caractères interdits : \" = + \" ' : / \\ - *\"");
              // if(!checkpassword.isSelected()){
               passwordField.setTooltip(tp);
               passwordField.getTooltip().show(loginStage,loginStage.getX()+360,loginStage.getY()+240);
               passwordField.setText("");
               passwordFieldShown.setText("");
               passwordField.getTooltip().setAutoHide(true);
                passwordField.getTooltip().setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        passwordField.getTooltip().setText("Entrez le mot de passe");
                        passwordFieldShown.getTooltip().setText("Entrez le mot de passe");
                    }
                });

       }
       }else{
           Tooltip usertp = new Tooltip("Caractères interdits : \" = + \" ' : / \\ - *\"");
           usernameField.setTooltip(usertp);
           usernameField.getTooltip().show(loginStage,loginStage.getX()+360,loginStage.getY()+184);
           usernameField.setText("");
           usernameField.getTooltip().setAutoHide(true);
           usernameField.getTooltip().setOnHiding(new EventHandler<WindowEvent>() {
               @Override
               public void handle(WindowEvent event) {
                   usernameField.getTooltip().setText("Entrez le nom d'utilisateur");
               }
           });


       }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tooltip usertp=new Tooltip();
        usernameField.setTooltip(usertp);
        Tooltip passtp=new Tooltip();
        passwordFieldShown.setTooltip(passtp);
        passwordField.setTooltip(passtp);
        passwordField.getTooltip().setText("Entrez le mot de passe");
        usernameField.getTooltip().setText("Entrez le nom d'utilisateur");
        passwordFieldShown.setVisible(false);
    }

      }

