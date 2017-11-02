/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.validation.NumberValidator;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXTextField;

import java.awt.*;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * FXML LoginController class
 *
 * @author DELL
 */
public class ConteneurController implements Initializable {
    //stage
    public static Stage conteneurStage;


    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

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
        DossierController.dossierStage.show();

    }
    public boolean estNumero(String s){
        boolean found=true;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)!='0' && s.charAt(i)!='1' &&  s.charAt(i)!='2' && s.charAt(i)!='3' &&  s.charAt(i)!='4' &&  s.charAt(i)!='5' &&
                    s.charAt(i)!='6' &&  s.charAt(i)!='7' && s.charAt(i)!='8' &&  s.charAt(i)!='9')
                found=false;
        }
        return found;
    }
    @FXML
    void OkCont(ActionEvent event) {
        ///BASE DE DONNEE
        String selectedChauff="Aucun",selectedCam="0";
        //POUR RENDRE LES CHAUFFEURS D'ENTREE OPTIONNELLES
        if(!(comboChauff.getSelectionModel().isEmpty()))selectedChauff=comboChauff.getSelectionModel().getSelectedItem().toString();
        if(!(comboCam.getSelectionModel().isEmpty()))selectedCam=comboCam.getSelectionModel().getSelectedItem().toString();
        if(estNumero(numPlomb.getText()) && !numPlomb.getText().isEmpty() && !numCont.getText().isEmpty() && !comboTypCont.getSelectionModel().isEmpty() && !marchandise.getText().isEmpty())
        {
            TabCont tmp=new TabCont();
            tmp.numPlomb.set(numPlomb.getText());
            tmp.numCont.set(numCont.getText());
            tmp.type.set(comboTypCont.getSelectionModel().getSelectedItem().toString());
            tmp.chauffEntre.set(selectedChauff);
            tmp.camionEntre.set(selectedCam);
            tmp.marchandise.set(marchandise.getText());
            tmp.factureAut.set("OK");
            tmp.sortieAut.set("0");
            DossierController.jeuDessaiCont.add(tmp);
            stage.close();
            DossierController.dossierStage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "-Des champs obligatoires manquants!\n-Type incompatible!");
            Toolkit.getDefaultToolkit().beep();
            alert.showAndWait();
        }
    }

        public void setStage(Stage stage){this.stage=stage;}
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        NumberValidator val=new NumberValidator();
        val.setMessage("Numero svp!");
        numPlomb.getValidators().add(val);
        numPlomb.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) numPlomb.validate();
            }
        });

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
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
