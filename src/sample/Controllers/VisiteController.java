/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.Controllers;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static java.awt.Desktop.getDesktop;


public class VisiteController implements Initializable {
    //stage
    public static Stage visiteStage;

    //database connection elements
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    com.mysql.jdbc.Connection conn = null;
    com.mysql.jdbc.Statement stmt = null;

    @FXML
    private JFXTreeTableView<TabSelectedCont> selectedContTable;

    @FXML
    private JFXComboBox<String> comboNomDouanier;

    @FXML
    private JFXComboBox<Integer> comboBadge;

    @FXML
    private JFXButton btnVisite;

    @FXML
    private JFXTextField placeInput;

    @FXML
    private JFXCheckBox checkPrint;

    @FXML
    private JFXCheckBox checkVisualize;

    @FXML
    private JFXButton abortButton;

    @FXML
    void Annuler(ActionEvent event) {
        visiteStage.close();
        DossierController.dossierStage.show();
    }
    @FXML
    void genererVisite (ActionEvent event) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy_HH;mm;ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
        String[] tab1= new String[DossierController.listSelectedConts.size()];
        String[] tab2= new String[DossierController.listSelectedConts.size()];
        for(int i = 0; i< DossierController.listSelectedConts.size(); i++){
            tab1[i]= DossierController.listSelectedConts.get(i).numCont.getValue().toString();
            tab2[i]="PN N°: "+ DossierController.listSelectedConts.get(i).numPlomb.getValue().toString();
        }
        System.out.println("Bon_visite_"+dateFormat2.format(date).toString());
        DocumentGenerator.RemplacerLesChamps("model_bon_visite", "Bon_visite_"+dateFormat2.format(date).toString(),
                new String[]{"mr","date","transit","declarant","badge","dstr"},
                new String[]{placeInput.getText().toString(),dateFormat.format(date).toString(),"SALMI",comboNomDouanier.getValue().toString(),comboBadge.getValue().toString(),DossierController.idDossierCourant},
                checkVisualize.isSelected());
        DocumentGenerator.RemplirTableu("Bon_visite_"+dateFormat2.format(date).toString(), "Bon_visite_"+dateFormat2.format(date).toString(),new String[][]{tab1,tab2}, checkVisualize.isSelected());

        //if checked then print document

        if (checkPrint.isSelected()){
            File file = new File("src\\sample\\Models\\Bon_visite_"+dateFormat2.format(date).toString()+".doc");
            try {
                getDesktop().print(file);// printing document
            } catch (Exception e) {
                // error while printing !
                System.out.println("Error while printing ! ... ");
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR,"Aucune imprimante est détéctée ! \nSvp verifiez votre connection avec l'imprimante ");

                Toolkit.getDefaultToolkit().beep();
                alert.showAndWait();

            }
        }
        //UPDATE THE DATABASE ,NBVISITE++

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql;int i;
            for(i=0; i< DossierController.listSelectedConts.size(); i++)
            {
                sql="UPDATE conteneur SET conteneur.nbVisite=conteneur.nbVisite+1 WHERE conteneur.NMC='"+ DossierController.listSelectedConts.get(i).numCont.getValue().toString()+"'";
                stmt.executeUpdate(sql);
                //System.out.println(sql);

                sql="UPDATE conteneur SET conteneur.factureAutorise='"+ DossierController.listSelectedConts.get(i).choiceBox.getValue().toString()+"' WHERE conteneur.NMC='"+ DossierController.listSelectedConts.get(i).numCont.getValue().toString()+"'";
                stmt.executeUpdate(sql);
                //System.out.println(sql);
            }
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        visiteStage.close();
        DossierController.dossierStage.show();


    }

    @FXML
    void AddClient(ActionEvent event) {

    }
    public void initializeCombos(){
        // initializing combos
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();

            String sql;
            ResultSet res= null;
            sql="SELECT * FROM douane";

            res = stmt.executeQuery(sql);
            while(res.next()){
                comboNomDouanier.getItems().addAll(res.getString("nom"));
                comboBadge.getItems().addAll(res.getInt("Badge"));
            }


            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //initializing checkbox
        checkPrint.setSelected(true);
        checkVisualize.setSelected(false);
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //
//        visiteStage.setAlwaysOnTop(true);
//        btnVisite.setTooltip(new Tooltip("Generer le bon de visite"));
  //      comboBadge.setTooltip(new Tooltip("Entrer le badge du declarant"));
    //    comboNomDouanier.setTooltip(new Tooltip("Entrer le nom du douanier"));
        //CONSTRUCTION OF THE TABLE OF SELECTED CONTAINERS
        JFXTreeTableColumn<TabSelectedCont,String> numPlom=new JFXTreeTableColumn<>("N° du Plomb");
        numPlom.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabSelectedCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabSelectedCont, String> param) {
                return param.getValue().getValue().numPlomb;
            }
        });
        JFXTreeTableColumn<TabSelectedCont,String> numCon=new JFXTreeTableColumn<>("N° Conteneur");
        numCon.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabSelectedCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabSelectedCont, String> param) {
                return param.getValue().getValue().numCont;
            }
        });

        JFXTreeTableColumn<TabSelectedCont,String> marchandis=new JFXTreeTableColumn<>("Marchandises");
        marchandis.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabSelectedCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabSelectedCont, String> param) {
                return param.getValue().getValue().marchandise;
            }
        });
        JFXTreeTableColumn<TabSelectedCont,ChoiceBox> choic=new JFXTreeTableColumn<>("Etat");
        choic.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabSelectedCont,ChoiceBox>, ObservableValue<ChoiceBox>>() {
            @Override
            public ObservableValue<ChoiceBox> call(TreeTableColumn.CellDataFeatures<TabSelectedCont,ChoiceBox> param) {
                return new SimpleObjectProperty<ChoiceBox>(param.getValue().getValue().choiceBox);
            }
        });
        final TreeItem<TabSelectedCont> root =new RecursiveTreeItem<TabSelectedCont>(DossierController.listSelectedConts,RecursiveTreeObject::getChildren);
        selectedContTable.getColumns().addAll(numCon,numPlom,marchandis,choic);
        selectedContTable.setRoot(root);
        selectedContTable.setShowRoot(false);
        //initilizing comboboxes
        initializeCombos();
        //
        comboNomDouanier.getSelectionModel().selectedItemProperty().addListener((Observable,oldValue,newValue)->{
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
                stmt = (com.mysql.jdbc.Statement) conn.createStatement();

                String sql;
                ResultSet res= null;
                sql="SELECT  douane.Badge FROM douane WHERE douane.nom='"+newValue.toString()+"'";

                res = stmt.executeQuery(sql);
                while(res.next()){
                    Integer i=res.getInt("Badge");
                    comboBadge.getSelectionModel().select(i);
                }


                stmt.close();
                conn.close();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        comboBadge.getSelectionModel().selectedItemProperty().addListener((Observable,oldValue,newValue)->{
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
                stmt = (com.mysql.jdbc.Statement) conn.createStatement();

                String sql;
                ResultSet res= null;
                sql="SELECT  douane.nom FROM douane WHERE douane.Badge="+newValue.toString();

                res = stmt.executeQuery(sql);
                while(res.next()){
                    comboNomDouanier.getSelectionModel().select(res.getString("nom"));
                }


                stmt.close();
                conn.close();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

}

    static class TabSelectedCont extends RecursiveTreeObject<TabSelectedCont>
{
    private StringProperty numPlomb;
    private StringProperty numCont;
    private StringProperty marchandise;
    private ObservableList<String> list=FXCollections.observableArrayList("OK","Fraude","Suspect");
    private ChoiceBox choiceBox=new ChoiceBox<String>(list);

    public  TabSelectedCont(){
        this.numCont=new SimpleStringProperty("");
        this.numPlomb=new SimpleStringProperty("");
        this.marchandise=new SimpleStringProperty("");
    }
    public TabSelectedCont (String numCont,String numPlomb,String marchandise)
    {
        this.numCont=new SimpleStringProperty(numCont);
        this.numPlomb=new SimpleStringProperty(numPlomb);
        this.marchandise=new SimpleStringProperty(marchandise);
        this.choiceBox.getSelectionModel().select("OK");
    }

}}