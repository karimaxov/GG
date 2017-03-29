/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dossier;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.sql.*;
//////////////////////////////////////////////////////////
import java.sql.Date;
import java.sql.DriverManager;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.ClientController;
import sun.util.calendar.BaseCalendar;

import javax.swing.*;







/**
 *
 * @author DELL
 */
public class DossierController implements Initializable {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/projets2";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "135792468";
    com.mysql.jdbc.Connection conn = null;
    com.mysql.jdbc.Statement stmt = null;
    ResultSet res;

    public ObservableList<TabDossier> jeuDessai= FXCollections.observableArrayList();
    public ObservableList<TabClient> jeuDessaiCli= FXCollections.observableArrayList();
    static ObservableList<TabCont> jeuDessaiCont= FXCollections.observableArrayList();

    boolean dosTableselected;

    @FXML
    private JFXComboBox<String> comboBox_prov;

    @FXML
    private JFXComboBox<String> comboTypeDoss;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXCheckBox todayCheck;

    @FXML
    private JFXTreeTableView<TabClient> cliTable;

    @FXML
    private JFXButton addCli;

    @FXML
    private JFXTreeTableView<TabCont> contTable;

    @FXML
    private JFXButton addCont;

    @FXML
    private JFXButton suppCont;

    @FXML
    private JFXButton btnAjouter;

    @FXML
    private JFXButton btnSupprimer;

    @FXML
    private JFXButton btnEnrigestrer;

    @FXML
    private JFXTreeTableView<TabDossier> dosTable;

    @FXML
    private JFXTextField txtFieldFiltrer;

    @FXML
    void AddClient(ActionEvent event) {

        Stage client=new Stage();ClientController.getStage(current,client);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../sample/Client.fxml"));
            client.setTitle("Gestion Des Clients");
            client.setScene(new Scene(root));
            client.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void AddContainer(ActionEvent event) {

        FXMLLoader loader =new FXMLLoader(getClass().getResource("Conteneur.fxml"));
        try {
            Parent Rtt=loader.load();
            Stage Stg= new Stage();
            Stg.setScene(new Scene(Rtt));
            ConteneurController ctrl=loader.getController();
            ctrl.setStage(Stg);
            Stg.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @FXML
    void SupprimerConteneur(ActionEvent event) {
      if(!contTable.getSelectionModel().isEmpty()) {

              int selected;
              selected= contTable.getSelectionModel().getSelectedIndex();
              System.out.println(selected);
              jeuDessaiCont.remove(selected);
              //contTable.getSelectionModel().clearSelection();


    }
    }

    @FXML
    void AjouterDossier(ActionEvent event) {
        //dosTable.getSelectionModel().select();
        dosTableselected=false;
        btnEnrigestrer.setDisable(false);
        addCli.setDisable(false);
        addCont.setDisable(false);
        suppCont.setDisable(false);
        comboBox_prov.setDisable(false);
        datePicker.setDisable(false);
        todayCheck.setDisable(false);
        jeuDessaiCont.clear();
        jeuDessaiCli.clear();

        try {
            jeuDessaiCli.clear();jeuDessaiCont.clear();//vider les table view
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql;

            sql="SELECT client.idClient,client.nom,client.tel,client.email,client.adresse FROM client" ;
            ResultSet tabcli= stmt.executeQuery(sql);
            while(tabcli.next()) {
                jeuDessaiCli.add(new TabClient(tabcli.getString("idClient"),tabcli.getString("nom"),tabcli.getString("tel"),tabcli.getString("email"),tabcli.getString("adresse")));
            }
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void Enrigistrer(ActionEvent event) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();

            String sql;

            sql="INSERT INTO dossier (idClient,typee,provenance,dateEntree,nbConteneur)VALUES("+cliTable.getSelectionModel().getSelectedItem().getValue().idClient.getValue().toString()
                    +",'"+comboTypeDoss.getSelectionModel().getSelectedItem().toString() +"','"+comboBox_prov.getSelectionModel().getSelectedItem().toString()+"','"+
                    datePicker.getValue()+"',"+contTable.getCurrentItemsCount()+")";
            stmt.executeUpdate(sql,stmt.RETURN_GENERATED_KEYS);

            ResultSet r=stmt.getGeneratedKeys();
            int lastId=0;
            while(r.next()){
                lastId=r.getInt(1);
            }
            int i;
           for(i=0;i<jeuDessaiCont.size();i++){

               sql="INSERT INTO conteneur(NMC, idClient, idDossier, numPlomb, typeTc, typeMarchandise, chauffeurEntree, camionEntree, factureAutorise, sortieAutorise, nbVisite) VALUES ('"+  jeuDessaiCont.get(i).numCont.getValue().toString()+
               "',"+cliTable.getFocusModel().getFocusedItem().getValue().idClient.getValue().toString()+","+lastId+","+jeuDessaiCont.get(i).numPlomb.getValue().toString()+","+
                       jeuDessaiCont.get(i).type.getValue().toString()+",'"+  jeuDessaiCont.get(i).marchandise.getValue().toString()+"','"+jeuDessaiCont.get(i).chauffEntre.getValue().toString()+"','"+
                       jeuDessaiCont.get(i).camionEntre.getValue().toString()+"',0,0,"+jeuDessaiCont.size()+ ")";
               System.out.println(sql);
               stmt.executeUpdate(sql);

           }

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refresh();

        // jeuDessai.add(tmp);
    }


    @FXML
    void SupprimerDossier(ActionEvent event) {
            //deleting conteneru item from database
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = (com.mysql.jdbc.Statement) conn.createStatement();
                String sql;
                String id = dosTable.getFocusModel().getFocusedItem().getValue().idDossier.getValue().toString();
                sql = "DELETE FROM dossier WHERE dossier.idDossier=" + id;//contTable.getSelectionModel().getSelectedItem().getValue().numCont.getValue().toString()+"'";
                System.out.println(sql);
                stmt.executeUpdate(sql);
                sql = "DELETE FROM  conteneur WHERE conteneur.idDossier=" + id;
                System.out.println(sql);
                stmt.executeUpdate(sql);
                jeuDessaiCont.clear();

                stmt.close();
                conn.close();


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            int selected;
            if (dosTableselected) {
                selected = dosTable.getSelectionModel().getSelectedIndex();
                jeuDessai.remove(selected);
            }
            dosTableselected = false;


    }
    public void refresh(){
        try {
            jeuDessai.clear();
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql;
            sql="SELECT dossier.idDossier,client.nom, dossier.typee, dossier.dateEntree, dossier.provenance FROM dossier JOIN client WHERE dossier.idClient=client.idClient ";
            res= stmt.executeQuery(sql);
            while (res.next()) {
                jeuDessai.add(new TabDossier(res.getString("idDossier"),res.getString("nom"),res.getString("dateEntree"),res.getString("typee"),res.getString("provenance")));
                //jeuDessai.add(new TabDossier(res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)));
            }

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void SelectedDoss(String idDoss){
        try {
            jeuDessaiCli.clear();jeuDessaiCont.clear();//vider les table view
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql;

            sql="SELECT client.idClient,client.nom,client.tel,client.email,client.adresse FROM dossier JOIN client WHERE dossier.idClient=client.idClient and dossier.idDossier="+idDoss;
            ResultSet tabcli= stmt.executeQuery(sql);
            while(tabcli.next()) {
                //System.out.println(tabcli.getString("nom"));//+tabcli.getString("nom")+tabcli.getString("tel")+tabcli.getString("email")+tabcli.getString("adresse"));
                jeuDessaiCli.add(new TabClient(tabcli.getString("idClient"),tabcli.getString("nom"),tabcli.getString("tel"),tabcli.getString("email"),tabcli.getString("adresse")));
            }
            sql="SELECT conteneur.NMC,conteneur.numPlomb,conteneur.typeMarchandise,conteneur.typeTc,conteneur.chauffeurEntree,conteneur.camionEntree,conteneur.factureAutorise,conteneur.sortieAutorise,dossier.provenance,dossier.dateEntree,Dossier.typee FROM dossier JOIN conteneur WHERE dossier.idDossier=conteneur.idDossier and dossier.idDossier="+idDoss;
            System.out.println(sql);
            ResultSet tabcont= stmt.executeQuery(sql);
            String prv="",tpd="";boolean i=true;
            Date date=new Date(2017,12,12);
            while(tabcont.next()) {
                if (i){prv=tabcont.getString("provenance");date=tabcont.getDate("dateEntree");tpd=tabcont.getString("typee");i=false;}
                //(tabcli.getString("nom"));//+tabcli.getString("nom")+tabcli.getString("tel")+tabcli.getString("email")+tabcli.getString("adresse"));
                jeuDessaiCont.add(new TabCont(tabcont.getString("NMC"),tabcont.getString("numPlomb"),tabcont.getString("typeMarchandise"),tabcont.getString("typeTc"),tabcont.getString("factureAutorise"),tabcont.getString("sortieAutorise"),tabcont.getString("chauffeurEntree"), tabcont.getString("camionEntree")));

            }

            // on selecting item on conteneur table view if it's illegal  red else with green
            contTable.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) -> {

                if ( contTable.getSelectionModel().getSelectedItem().getValue().etat.getValue().toString().equals("1")){contTable.setId("contTableGreen");}else {contTable.setId("contTableRed");}

            });

          //  contTable.setId("contTableGreen");
            datePicker.setValue(date.toLocalDate());
            comboBox_prov.getSelectionModel().select(prv);
            comboTypeDoss.getSelectionModel().select(tpd);

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        btnEnrigestrer.setDisable(true);
        addCli.setDisable(true);
        addCont.setDisable(true);
        suppCont.setDisable(true);
        comboBox_prov.setDisable(true);
        comboTypeDoss.setDisable(true);
        datePicker.setDisable(true);
        todayCheck.setDisable(true);

    }

    static Stage menu,current;
    static public void getStage(Stage menuu,Stage currentt)
    {
        menu=menuu;
        current=currentt;
    }


    @FXML
    private JFXButton retour;
    @FXML
    void retour(ActionEvent event) {
        current.close();
        menu.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ADDING ITEMS TO TABDOSSIER

        try {
            menu.close();
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql;
            sql="SELECT * FROM pays";
            res=stmt.executeQuery(sql);
            while(res.next()){
                comboBox_prov.getItems().addAll(res.getString("nom_fr_fr"));
            }
            sql="SELECT dossier.idDossier,client.nom, dossier.typee, dossier.dateEntree, dossier.provenance FROM dossier JOIN client WHERE dossier.idClient=client.idClient ";
            res= stmt.executeQuery(sql);
            while (res.next()) {

                jeuDessai.add(new TabDossier(res.getString("idDossier"),res.getString("nom"),res.getString("dateEntree"),res.getString("typee"),res.getString("provenance")));
            }

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        comboTypeDoss.getItems().addAll("DSTR","OT");
        dosTable.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) -> {
            String id;
            dosTableselected=true;
            id=newValue.getValue().idDossier.getValue().toString();//dosTable.getSelectionModel().getSelectedItem().getValue().idDossier.getValue().toString();
            SelectedDoss(id);
        });

        /**CONSTRUCTION DU TABLEAU DOSSIER*/
           JFXTreeTableColumn<TabDossier,String> idDos=new JFXTreeTableColumn<>("Id dossier");
            idDos.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabDossier, String>, ObservableValue<String>>() {
               @Override
               public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabDossier, String> param) {
                  return param.getValue().getValue().idDossier;
               }
           });
           /*****************************************/
           JFXTreeTableColumn<TabDossier,String> nmClient=new JFXTreeTableColumn<>("Client");
            nmClient.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabDossier, String>, ObservableValue<String>>() {
               @Override
               public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabDossier, String> param) {
                  return param.getValue().getValue().nomClient;
               }
           });
           /*****************************************/
           
           JFXTreeTableColumn<TabDossier,String> typ=new JFXTreeTableColumn<>("Type");
            typ.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabDossier, String>, ObservableValue<String>>() {
               @Override
               public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabDossier, String> param) {
                  return param.getValue().getValue().type;
               }
           });
           /*****************************************/
           JFXTreeTableColumn<TabDossier,String> dat=new JFXTreeTableColumn<>("Date");
            dat.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabDossier, String>, ObservableValue<String>>() {
               @Override
               public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabDossier, String> param) {
                  return param.getValue().getValue().date;
               }
           });
           /*****************************************/
           JFXTreeTableColumn<TabDossier,String> prvn=new JFXTreeTableColumn<>("Provenance");
            prvn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabDossier, String>, ObservableValue<String>>() {
               @Override
               public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabDossier, String> param) {
                  return param.getValue().getValue().provenance;
               }
           });
           /*****************************************/
           /*CONSTRUCTON DE LA LISTE*/



        final TreeItem<TabDossier> root =new RecursiveTreeItem<TabDossier>(jeuDessai,RecursiveTreeObject::getChildren);
        dosTable.getColumns().addAll(idDos,nmClient,dat,typ,prvn);
        dosTable.setRoot(root);
        dosTable.setShowRoot(false);

                                /**CONSTRUCTION DU TABLEAU CLIENT*/
        JFXTreeTableColumn<TabClient,String> idCli=new JFXTreeTableColumn<>("Id Client");
        idCli.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabClient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabClient, String> param) {
                return param.getValue().getValue().idClient;
            }
        });
        JFXTreeTableColumn<TabClient,String> nomCli=new JFXTreeTableColumn<>("Client");
        nomCli.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabClient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabClient, String> param) {
                return param.getValue().getValue().nomClient;
            }
        });
        JFXTreeTableColumn<TabClient,String> tlphCli=new JFXTreeTableColumn<>("Telephone");
        tlphCli.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabClient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabClient, String> param) {
                return param.getValue().getValue().telephone;
            }
        });
        JFXTreeTableColumn<TabClient,String> emailCli=new JFXTreeTableColumn<>("Email");
        emailCli.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabClient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabClient, String> param) {
                return param.getValue().getValue().email;
            }
        });
        JFXTreeTableColumn<TabClient,String> adressCli=new JFXTreeTableColumn<>("Adresse");
        adressCli.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabClient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabClient, String> param) {
                return param.getValue().getValue().adresse;
            }
        });
           /*CONSTRUCTON DE LA LISTE*/


        final TreeItem<TabClient> roo =new RecursiveTreeItem<TabClient>(jeuDessaiCli,RecursiveTreeObject::getChildren);
        cliTable.getColumns().setAll(idCli,nomCli,tlphCli,emailCli,adressCli);
        cliTable.setRoot(roo);
        cliTable.setShowRoot(false);

        /**CONSTRUCTION DU TABLEAU CLIENT*/

        JFXTreeTableColumn<TabCont,String> numPlom=new JFXTreeTableColumn<>("N° du Plomb");
        numPlom.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabCont, String> param) {
                return param.getValue().getValue().numPlomb;
            }
        });
        JFXTreeTableColumn<TabCont,String> numCon=new JFXTreeTableColumn<>("N° Conteneur");
        numCon.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabCont, String> param) {
                return param.getValue().getValue().numCont;
            }
        });

        JFXTreeTableColumn<TabCont,String> marchandis=new JFXTreeTableColumn<>("Marchandises");
        marchandis.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabCont, String> param) {
                return param.getValue().getValue().marchandise;
            }
        });

        JFXTreeTableColumn<TabCont,String> typ1=new JFXTreeTableColumn<>("Type");
        typ1.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabCont, String> param) {
                return param.getValue().getValue().type;
            }
        });
        JFXTreeTableColumn<TabCont,String> fact=new JFXTreeTableColumn<>("Facturé");
        fact.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabCont, String> param) {
                return param.getValue().getValue().facture;
            }
        });
        JFXTreeTableColumn<TabCont,String> eta=new JFXTreeTableColumn<>("Etat");
        eta.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabCont, String> param) {
                return param.getValue().getValue().etat;
            }
        });
        JFXTreeTableColumn<TabCont,String> chauffEntr=new JFXTreeTableColumn<>("Chauffeur(E)");
        chauffEntr.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabCont, String> param) {
                return param.getValue().getValue().chauffEntre;
            }
        });
        JFXTreeTableColumn<TabCont,String> camionEntr=new JFXTreeTableColumn<>("Camion(E)");
        camionEntr.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabCont, String> param) {
                return param.getValue().getValue().camionEntre;
            }
        });



        /**CONSTRUCTON DE LA LISTE DES CONTENEUR*/

        final TreeItem<TabCont> ro =new RecursiveTreeItem<TabCont>(jeuDessaiCont,RecursiveTreeObject::getChildren);
        contTable.getColumns().setAll(numCon,numPlom,marchandis,typ1,chauffEntr,camionEntr,fact,eta);
        contTable.setRoot(ro);
        contTable.setShowRoot(false);




    }


    class TabDossier extends RecursiveTreeObject <TabDossier>
        {
        StringProperty idDossier= new SimpleStringProperty();
        StringProperty nomClient;
        StringProperty date;
        StringProperty type;
        StringProperty provenance;
        public  TabDossier(){
            this.idDossier=new SimpleStringProperty("");
            this.nomClient=new SimpleStringProperty("");
            this.type=new SimpleStringProperty("");
            this.date=new SimpleStringProperty("");
            this.provenance=new SimpleStringProperty("");
        }
        public TabDossier (String idDossier,String nomClient,String date,String type,String provenance)
        {
            this.idDossier=new SimpleStringProperty(idDossier);
            this.nomClient=new SimpleStringProperty(nomClient);
            this.type=new SimpleStringProperty(type);
            this.date=new SimpleStringProperty(date);
            this.provenance=new SimpleStringProperty(provenance);
        }

            public String getIdDossier() {
                return idDossier.get();
            }

            public void setIdDossier(String idDossier) {
                this.idDossier.set(idDossier);
            }

            public String getNomClient() {
                return nomClient.get();
            }


            public void setNomClient(String nomClient) {
                this.nomClient.set(nomClient);
            }

            public String getDate() {
                return date.get();
            }

            public void setDate(String date) {
                this.date.set(date);
            }

            public String getType() {
                return type.get();
            }

            public void setType(String type) {
                this.type.set(type);
            }

            public String getProvenance() {
                return provenance.get();
            }

            public void setProvenance(String provenance) {
                this.provenance.set(provenance);
            }
        }
    class TabClient extends RecursiveTreeObject <TabClient>
    {
        StringProperty idClient;
        StringProperty nomClient;
        StringProperty telephone;
        StringProperty email;
        StringProperty adresse;

        public TabClient (String idClient,String nomClient,String telephone,String email,String adresse)
        {
            this.idClient=new SimpleStringProperty(idClient);
            this.nomClient=new SimpleStringProperty(nomClient);
            this.telephone=new SimpleStringProperty(telephone);
            this.email=new SimpleStringProperty(email);
            this.adresse=new SimpleStringProperty(adresse);
        }
    }
    }

    class TabCont extends RecursiveTreeObject <TabCont>
{
        StringProperty numPlomb;
        StringProperty numCont;
        StringProperty marchandise;
        StringProperty type;
        StringProperty facture;
        StringProperty etat;
        StringProperty chauffEntre;
        StringProperty camionEntre;
    public  TabCont(){
        this.numCont=new SimpleStringProperty("");
        this.numPlomb=new SimpleStringProperty("");
        this.type=new SimpleStringProperty("");
        this.marchandise=new SimpleStringProperty("");
        this.facture=new SimpleStringProperty("");
        this.etat=new SimpleStringProperty("");
        this.chauffEntre=new SimpleStringProperty("");
        this.camionEntre=new SimpleStringProperty("");
    }

        public TabCont (String numCont,String numPlomb,String marchandise,String type,String facture,String etat,String chaffEntr,String camionEntr)
        {
            this.numCont=new SimpleStringProperty(numCont);
            this.numPlomb=new SimpleStringProperty(numPlomb);
            this.marchandise=new SimpleStringProperty(marchandise);
            this.type=new SimpleStringProperty(type);
            this.facture=new SimpleStringProperty(facture);
            this.etat=new SimpleStringProperty(etat);
            this.chauffEntre=new SimpleStringProperty(chaffEntr);
            this.camionEntre=new SimpleStringProperty(camionEntr);
    }
}








    
