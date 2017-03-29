package sample;


import java.awt.*;
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

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import sample.nextStage.MenuController;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Node;


public class Controller implements Initializable {
    //////////////
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/projets2";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "135792468";


     public JFXTextField usernameField;
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
    public void verifyIdentity() throws SQLException {
        String username=usernameField.getText();
        String password=passwordField.getText();

        /////////////////////////////////////////////////////////////////
        try {
            com.mysql.jdbc.Connection conn = null;
            com.mysql.jdbc.Statement stmt = null;
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();

            String sql;
            //////validation needed here !
            sql ="SELECT * FROM login WHERE username='"+username+"' AND  password='"+password+"'";//"insert into login (username,password,super)values('bakir','9iw9iw',0)";
            ResultSet res=stmt.executeQuery(sql);

            //System.out.println(res.next());
            if (res.next()==true){
                //if(){
                 boolean   sup=(res.getInt("super")==1);
                Stage s=new Stage();
                MenuController.SetUser(username,sup,s);

                //create a new scene with root and set the stage
                FXMLLoader loader=new FXMLLoader(getClass().getResource("nextStage/MainMenu.fxml"));
                Parent rt=loader.load();

                s.setScene(new Scene(rt));
                stage.close();
                s.show();
                System.out.println("Bienvenue Mr "+username +" !");
                }else
            {
                usernameField.setUnFocusColor(Color.RED );
                passwordField.setUnFocusColor(Color.RED );
                echecLabal.setText("nom d'utilisateur ou mot de passe incorrecte");
            }

            stmt.close();
            conn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Something is wrong ! ... check ur network or the address of the server");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something is wrong ! ... check ur network or the address of the server");
        }


    }

    public void showPassword(){

        //echecLabal.setText(passwordField.getText());
        //passwordField.managedProperty().bind(checkpassword.selectedProperty().not());
        System.out.println("checked ! ");
        //passwordField.setVisible(true);
        //passwordField.visibleProperty().bind(checkpassword.selectedProperty().not());

        //.visibleProperty().bind(checkpassword.selectedProperty().not());//.accessibleTextProperty();
    }


    public  void clear()
    {

        usernameField.clear();
        passwordField.clear();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    clear();
    }

   /* @Override
    public void initialize(URL location, ResourceBundle resources) {
       /* FXMLLoader Loader=new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root= null;
        try {
            root = Loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //currentstage.close();
        loginStage.setScene(new Scene(root));
        //s.setFullScreen(true);


        loginStage.show();*/

    }


    /* public String getUsername(){
        return username;
    }*/
    /*public Parent chargerFXML(String s) throws IOException {

        return FXMLLoader.load(getClass().getResource(s));
    }*/

   /* public boolean isCloseCurrentS() {
        return closeCurrentS;
    }*/
