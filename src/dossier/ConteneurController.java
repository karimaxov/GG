/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dossier;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import static dossier.DossierController.jeuDessaiCont;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class ConteneurController implements Initializable {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/projets2";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "135792468";
    com.mysql.jdbc.Connection conn = null;
    com.mysql.jdbc.Statement stmt = null;

    Stage stage;
    ObservableList<StringProperty> typeCt= FXCollections.observableArrayList();

    @FXML
    private JFXTextField numCont;
    @FXML
    private JFXTextField marchandise;
    @FXML
    private JFXTextField numPlomb;
    @FXML
    private ComboBox<String> comboTypCont;
    @FXML
    private ComboBox<String> comboCam;
    @FXML
    private ComboBox<String> comboChauff;
    @FXML
    private JFXButton okCont;
    @FXML
    private JFXButton cancelCont;


    @FXML
    void AnulerCont(ActionEvent event) {
        stage.close();

    }

    @FXML
    void OkCont(ActionEvent event) {
        ///BASE DE DONNEE
        TabCont tmp=new TabCont();
        tmp.numPlomb.set(numPlomb.getText());
        tmp.numCont.set(numCont.getText());
        tmp.type.set(comboTypCont.getSelectionModel().getSelectedItem().toString());
        tmp.chauffEntre.set(comboChauff.getSelectionModel().getSelectedItem().toString());
        tmp.camionEntre.set(comboCam.getSelectionModel().getSelectedItem().toString());
        tmp.marchandise.set(marchandise.getText());
        tmp.facture.set("0");
        tmp.etat.set("0");
        jeuDessaiCont.add(tmp);
        stage.close();
    }
    public void setStage(Stage stage){this.stage=stage;}
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql;
            sql="SELECT chauffeur.nom FROM chauffeur";
            ResultSet res= null;
            res = stmt.executeQuery(sql);
            while(res.next()){
                comboChauff.getItems().addAll(res.getString("nom"));
            }
            sql="SELECT camion.matricule FROM camion";
            res= null;
            res = stmt.executeQuery(sql);
            while(res.next()){
                comboCam.getItems().addAll(res.getString("Matricule"));
            }
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        comboTypCont.getItems().addAll("40","20");
    }
    
}
