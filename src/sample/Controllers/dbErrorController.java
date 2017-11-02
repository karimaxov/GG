package sample.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by MESSAOUD on 13/03/2017.
 */

public class dbErrorController implements Initializable {
public static Stage databseErrorStage;
    @FXML
    private JFXTextField ip1;

    @FXML
    private JFXTextField ip2;

    @FXML
    private JFXTextField ip3;

    @FXML
    private JFXTextField ip4;

    @FXML
    private Text invalidInputText;

    @FXML
    private JFXButton applyButton;

    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXTextField usernameInput;

    @FXML
    private JFXPasswordField passwordInput;

    @FXML
    private JFXCheckBox ipCheck;

    @FXML
    private JFXCheckBox userInformationsCheck;
    @FXML
    private JFXButton createButton1;
    @FXML
    private JFXCheckBox DatabaseExistsCheck1;

    @FXML
    void enableCreatingDatabase(ActionEvent event) {
        if(DatabaseExistsCheck1.isSelected() && !usernameInput.getText().isEmpty())createButton1.setDisable(false);
        else {createButton1.setDisable(true);DatabaseExistsCheck1.setSelected(false);}
    }


    @FXML
    void createDatabase(ActionEvent event) {
        createButton1.setDisable(true);
cancelButton.setDisable(true);
applyButton.setDisable(true);
        boolean exist=false;
        InetAddress ipAddr = null;
        try {
            ipAddr = InetAddress.getLocalHost();
            System.out.println(ipAddr.getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //DB_CNX.setIp(ip1.getText() + "." + ip2.getText() + "." + ip3.getText() + "." + ip4.getText());
            con = (com.mysql.jdbc.Connection) DriverManager.getConnection("jdbc:mysql://localhost/",DB_CNX.getUSER(), DB_CNX.getPASS());

            ResultSet resultSet = con.getMetaData().getCatalogs();

            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if(databaseName.equals("projets2"))exist=true;
            }
            resultSet.close();
            if(!exist) {
                try {

                    System.out.println("grrrr");
                    ScriptRunner runner = new ScriptRunner(con, false, false);

                    runner.runScript(new BufferedReader(new FileReader("src\\sample\\Models\\projets.sql")));


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }





        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }





        createButton1.setDisable(false);
        cancelButton.setDisable(false);
        applyButton.setDisable(false);

    }

    @FXML
    void apply(ActionEvent event) {
        // updating ipaddress
        if(ipCheck.isSelected()) {
            DB_CNX.setIp(ip1.getText() + "." + ip2.getText() + "." + ip3.getText() + "." + ip4.getText());
        }
        if(userInformationsCheck.isSelected()) {
            DB_CNX.setUSER(usernameInput.getText());
            DB_CNX.setPASS(passwordInput.getText());

        }
        DB_CNX.saveDbInfo();
        databseErrorStage.close();
        LoginController.loginStage.centerOnScreen();
        LoginController.loginStage.show();


    }




    @FXML
    void cancel(ActionEvent event) {
        databseErrorStage.close();
        LoginController.loginStage.show();
    }

    @FXML
    void ipModification(ActionEvent event) {
        if (ipCheck.isSelected()){ip1.setDisable(false);ip2.setDisable(false);ip3.setDisable(false);ip4.setDisable(false); }
        else{
            ip1.setDisable(true);ip2.setDisable(true);ip3.setDisable(true);ip4.setDisable(true);
        }
    }

    @FXML
    void userModification(ActionEvent event) {
        if (userInformationsCheck.isSelected()){
            usernameInput.setDisable(false);
            passwordInput.setDisable(false);
            DatabaseExistsCheck1.setSelected(false);
        }else{
            usernameInput.setDisable(true);
            passwordInput.setDisable(true);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Toolkit.getDefaultToolkit().beep();
        usernameInput.setDisable(true);
        passwordInput.setDisable(true);
        ipCheck.setSelected(true);
        createButton1.setDisable(true);
        DatabaseExistsCheck1.setSelected(false);
        createButton1.setTooltip(new Tooltip("Entrer les informations de la base de données d'abord"));
        DatabaseExistsCheck1.setTooltip(new Tooltip("Entrer les informations de la base de données d'abord"));
        //userInformationsCheck.setSelected(true);
    }
}
