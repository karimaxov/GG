/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.Controllers;

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

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import static java.awt.Desktop.getDesktop;

public class SortieController implements Initializable {
        //stage
        public static Stage sortieStage;

        public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

        com.mysql.jdbc.Connection conn = null;
        com.mysql.jdbc.Statement stmt = null;



        @FXML
        private JFXButton addDriver;

        @FXML
        private JFXTreeTableView<TabLeavingCont> LeavingConts;

        @FXML
        private JFXButton Sortir;

        @FXML
        private JFXButton annuler;

        @FXML
        private JFXCheckBox checkPrint;

        @FXML
        private JFXCheckBox checkVisualize;

        @FXML
        private JFXComboBox<String> comboNomDouanier;

        @FXML
        private JFXComboBox<Integer> comboBadge;
        @FXML
        private JFXTextField placeInput;

        @FXML
        private JFXButton abortButton;

        @FXML
        void Annuler(ActionEvent event) {
            sortieStage.close();
            DossierController.dossierStage.show();
        }

        @FXML
        void Sortir(ActionEvent event) {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy_HH;mm;ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
            String[] tab1 = new String[DossierController.listLeavingConts.size()];
            String[] tab2 = new String[DossierController.listLeavingConts.size()];
            String[] tab3 = new String[DossierController.listLeavingConts.size()];
            for (int i = 0; i < DossierController.listLeavingConts.size(); i++) {
                tab1[i] = DossierController.listLeavingConts.get(i).Mrq.getValue().toString();
                tab2[i] = DossierController.listLeavingConts.get(i).camionCmb.getValue().toString();
                tab3[i] = DossierController.listLeavingConts.get(i).chauffCmb.getValue().toString();
            }
            System.out.println("Bon_sortie_" + dateFormat2.format(date).toString());

            System.out.println(DossierController.nomClientCourant);
            DocumentGenerator.RemplacerLesChamps("model_bon_sortie", "Bon_sortie_" + dateFormat2.format(date).toString(),
                    new String[]{"mr", "date", "transit", "compte", "declarant", "badge", "date2"},
                    new String[]{placeInput.getText().toString(), dateFormat.format(date).toString(), "SALMI", DossierController.nomClientCourant, comboNomDouanier.getValue().toString(), comboBadge.getValue().toString(), DossierController.dateCourante},
                    checkVisualize.isSelected());
            DocumentGenerator.RemplirTableu("Bon_sortie_" + dateFormat2.format(date).toString(), "Bon_sortie_" + dateFormat2.format(date).toString(), new String[][]{tab1, tab2, tab3}, checkVisualize.isSelected());

            //if checked then print document

            if (checkPrint.isSelected()) {
                File file = new File("src\\sample\\Models\\Bon_sortie_" + dateFormat2.format(date).toString() + ".doc");
                try {
                    getDesktop().print(file);// printing document
                } catch (Exception e) {
                    // error while printing !
                    System.out.println("Error while printing ! ... ");
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Aucune imprimante est détéctée ! \nSvp verifiez votre connection avec l'imprimante ");
                    Toolkit.getDefaultToolkit().beep();
                    alert.showAndWait();
                }
            }
            int i;

                try {

                    String sql;
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
                    stmt = (com.mysql.jdbc.Statement) conn.createStatement();

                    for(i=0; i< DossierController.listLeavingConts.size(); i++) {
                        sql = "DELETE FROM conteneur WHERE conteneur.NMC='" + DossierController.listLeavingConts.get(i).Mrq.getValue().toString() + "'";
                        stmt.executeUpdate(sql);
                        //System.out.println(sql);
                        for(int j = 0; j< DossierController.jeuDessaiCont.size(); j++){
                            if(DossierController.jeuDessaiCont.get(j).numCont.getValue().toString().equals(DossierController.listLeavingConts.get(i).Mrq.getValue().toString())){
                                DossierController.jeuDessaiCont.remove(j);
                            }
                        }

                    }
                    //UPFATING NBCONTENEUR,PSK Kherjouu!
                    sql="UPDATE dossier SET dossier.nbConteneur=dossier.nbConteneur-"+ DossierController.listLeavingConts.size()+" WHERE dossier.idDossier="+ DossierController.idDossierCourant;
                    stmt.executeUpdate(sql);
                    for(int j = 0; j< DossierController.jeuDessai.size(); j++){
                        if(DossierController.jeuDessai.get(j).idDossier.getValue().toString().equals(DossierController.idDossierCourant)){
                            DossierController.jeuDessai.remove(j);
                        }
                    }

                    //DELETIN' DOSSIER SI LE NOMBRE DE CONTENEUR EST EGALE A ZERO
                    sql="DELETE FROM dossier WHERE dossier.nbConteneur=0";
                    stmt.executeUpdate(sql);
                    stmt.close();
                    conn.close();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            sortieStage.close();
            DossierController.dossierStage.show();
        }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //

        //

        JFXTreeTableColumn<TabLeavingCont,String> Mrqq=new JFXTreeTableColumn<>("Marque Et N° TCS");
        Mrqq.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabLeavingCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabLeavingCont, String> param) {
                return param.getValue().getValue().Mrq;
            }
        });

        JFXTreeTableColumn<TabLeavingCont,JFXComboBox> camionCm=new JFXTreeTableColumn<>("Camion");
        camionCm.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabLeavingCont,JFXComboBox>, ObservableValue<JFXComboBox>>() {
            @Override
            public ObservableValue<JFXComboBox> call(TreeTableColumn.CellDataFeatures<TabLeavingCont,JFXComboBox> param) {
                return new SimpleObjectProperty<JFXComboBox>(param.getValue().getValue().camionCmb);
            }
        });

        JFXTreeTableColumn<TabLeavingCont,JFXComboBox> chauffCm=new JFXTreeTableColumn<>("Chauffeur");
        chauffCm.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabLeavingCont,JFXComboBox>, ObservableValue<JFXComboBox>>() {
            @Override
            public ObservableValue<JFXComboBox> call(TreeTableColumn.CellDataFeatures<TabLeavingCont,JFXComboBox> param) {
                return new SimpleObjectProperty<JFXComboBox>(param.getValue().getValue().chauffCmb);
            }
        });

        final TreeItem<TabLeavingCont> root =new RecursiveTreeItem<TabLeavingCont>(DossierController.listLeavingConts,RecursiveTreeObject::getChildren);
        Mrqq.setPrefWidth(LeavingConts.getPrefWidth()/3);
        camionCm.setPrefWidth(LeavingConts.getPrefWidth()/3);
        chauffCm.setPrefWidth(LeavingConts.getPrefWidth()/3);
        LeavingConts.getColumns().addAll(Mrqq,camionCm,chauffCm);
        LeavingConts.setRoot(root);
        LeavingConts.setShowRoot(false);
        //initilizing comboboxes
        initializeCombos();
        ///FOR THE AUTOMATIC SELECTION
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
    public void initializeCombos(){
        // initializing combos
        try {
            com.mysql.jdbc.Connection conn = null;
            com.mysql.jdbc.Statement stmt = null;
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
    static class TabLeavingCont extends RecursiveTreeObject<TabLeavingCont>
    {
        private StringProperty Mrq;
        JFXComboBox camionCmb;
        JFXComboBox chauffCmb;


        public TabLeavingCont(String Mrkk,ObservableList<String> chauff,ObservableList<String> cam) {


            this.Mrq=new SimpleStringProperty(Mrkk);
            this.camionCmb = new JFXComboBox(cam);
            this.chauffCmb = new JFXComboBox(chauff);
            new ComboBoxAutoComplete<String>(camionCmb,"selectionner ou rechercher un camion");
            new ComboBoxAutoComplete<String>(chauffCmb,"selectionner ou rechercher un chauffeur");
            /*

            camionCmb.getSelectionModel().selectedItemProperty().addListener((Observable,oldValue,newValue)->{
                try {
                    com.mysql.jdbc.Connection conn = null;
                    com.mysql.jdbc.Statement stmt = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL, USER, PASS);
                    stmt = (com.mysql.jdbc.Statement) conn.createStatement();
                    if (newValue!=null) System.out.println("******************************");
                    String sql;
                    ResultSet res= null;
                    sql="SELECT chauffeur.nom FROM chauffeur JOIN camion WHERE chauffeur.idChauffeur=camion.idChauffeur AND camion.Matricule="+newValue.toString();
                    System.out.print(sql);

                    res = stmt.executeQuery(sql);
                    ObservableList<String> filteredCombos=FXCollections.observableArrayList();

                    while(res.next()){
                        filteredCombos.add(res.getString("nom"));
                    }
                   chauffCmb.getItems().setAll(filteredCombos);
                    stmt.close();
                    conn.close();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        });
            chauffCmb.getSelectionModel().selectedItemProperty().addListener((Observable,oldValue,newValue)->{
                try {
                    com.mysql.jdbc.Connection conn = null;
                    com.mysql.jdbc.Statement stmt = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL, USER, PASS);
                    stmt = (com.mysql.jdbc.Statement) conn.createStatement();
                    if (newValue!=null) System.out.println("******************************");
                    String sql;
                    ResultSet res= null;
                    sql="SELECT camion.Matricule FROM chauffeur JOIN camion WHERE chauffeur.idChauffeur=camion.idChauffeur AND chauffeur.nom='"+newValue.toString()+"'";
                    System.out.print(sql);

                    res = stmt.executeQuery(sql);
                    ObservableList<String> filteredCombos=FXCollections.observableArrayList();

                    while(res.next()){
                        filteredCombos.add(res.getString("Matricule"));
                    }
                   camionCmb.getItems().setAll(filteredCombos);
                    stmt.close();
                    conn.close();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        });*/

            }


        }
    }
