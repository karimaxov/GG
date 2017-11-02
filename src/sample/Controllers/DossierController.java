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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;

//////////////////////////////////////////////////////////


/**
 *
 * @author DELL
 */
public class DossierController implements Initializable {
    //
    public static Stage dossierStage,menuStage;


    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    com.mysql.jdbc.Connection conn = null;
    com.mysql.jdbc.Statement stmt = null;
    ResultSet res;
    public static String idDossierCourant;
    public static String nomClientCourant;
    public static String dateCourante;
    public static String typeDossierCourant;
    private String idSelectedClient;

    static ObservableList<TabDossier> jeuDessai= FXCollections.observableArrayList();
    static  ObservableList<TabClient> jeuDessaiCli= FXCollections.observableArrayList();
    static ObservableList<TabCont> jeuDessaiCont= FXCollections.observableArrayList();
    static ObservableList<VisiteController.TabSelectedCont> listSelectedConts=FXCollections.observableArrayList();
    static ObservableList<SortieController.TabLeavingCont> listLeavingConts=FXCollections.observableArrayList();
    static ObservableList<String> listFactNmc=FXCollections.observableArrayList();
    static ObservableList<String> listFactType=FXCollections.observableArrayList();
    boolean dosTableselected;

    public void refreshJeuDessaiCli(String id, String no, String tlf, String ema, String adr){
        jeuDessaiCli.add(new TabClient(id,no,tlf,ema,adr));
    }

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
    private JFXButton btnReturn;

    @FXML
    private JFXButton btnSupprimer;

    @FXML
    private JFXButton btnEnrigestrer;

    @FXML
    private JFXButton btnVisiter;

    @FXML
    private JFXButton btnFacturer;

    @FXML
    private JFXButton btnSortir;

    @FXML
    private JFXTreeTableView<TabDossier> dosTable;

    @FXML
    private JFXTextField txtFieldFiltrer;

    @FXML
    private JFXTextField txtFieldFiltrerClient;

    @FXML
    private JFXTextField SelectedClientTxtFld;

    @FXML
     void facturer(ActionEvent event) throws Exception {
        FXMLLoader loader =new FXMLLoader(getClass().getResource("../Views/InterfaceFacture.fxml"));
        try {
            AnchorPane Pr=loader.load();
            Stage Stg= new Stage();
            Stg.setScene(new Scene(Pr));
            dossierStage.hide();
            InterfaceFactureController.factureStage = Stg;
            Stg.setAlwaysOnTop(true);
            Stg.setTitle("Facturation");
            Stg.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

        /*
        int i;
        for(i=0;i<jeuDessaiCont.size();i++){
            if (jeuDessaiCont.get(i).chk.isSelected() && jeuDessaiCont.get(i).factureAut.getValue().toString().equals("OK")){
                listFactNmc.add(jeuDessaiCont.get(i).numCont.getValue().toString());
                listFactType.add(jeuDessaiCont.get(i).type.getValue().toString());
            }
        }
        if(listFactNmc.size()!=0){
            DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy_HH;mm;ss");
            java.util.Date date=new java.util.Date();
            if(typeDossierCourant.equals("DSTR")) {
                FactureDstrController c=new FactureDstrController();
                c.Facturer(listFactType,listFactNmc);
                SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy");
                FactureDstrController.RemplirFactureDSTR("model_facture_dstr","Facture_DSTR_"+dateFormat2.format(date).toString() ,
                        new String[]{"nbDossier", "dateEntree", "nbTC", "dateSortie", "typeTC",
                                "nbJours", "nTC", "gros", "article"},
                        new String[]{idDossierCourant+"/"+ft.format(new java.util.Date()).charAt(8)+ft.format(new java.util.Date()).charAt(9),ft.format(c.getResultat(Integer.parseInt(idDossierCourant)).getDate(2)),String.valueOf(listFactType.size()),ft.format(new java.util.Date()),"40''",
                                Integer.toString(DateDemo.period(c.getResultat(Integer.parseInt(idDossierCourant)).getDate(2))),"","57","87"}, new FactureDstrController.TableRow[6],new float[4],true);

            }
            if(typeDossierCourant.equals("OT")) {
                FactureOtController c=new FactureOtController();
                c.Facturer(listFactType,listFactNmc);
                float tableau[][];
                tableau = FactureOtController.getTableau();
                FactureOtController.TableRow[] tableauLignes = new FactureOtController.TableRow[]
                        {
                                new FactureOtController.TableRow((int) tableau[0][0], (int) tableau[0][1],  tableau[0][2],tableau[0][3]),//frais const dossier
                                new FactureOtController.TableRow((int) tableau[1][0], 0,  tableau[1][2],tableau[1][3]),//suivi transfert
                                new FactureOtController.TableRow((int)tableau[2][0], 0,  tableau[2][2],tableau[2][3]),//manutension
                                new FactureOtController.TableRow((int)tableau[3][0], 0,  tableau[3][2],tableau[3][3]),//transport
                                new FactureOtController.TableRow((int)tableau[4][0], 0,  tableau[4][2],tableau[4][3]),//immobilisation
                                new FactureOtController.TableRow((int)tableau[5][0], 0,  tableau[5][2],tableau[5][3]),//dechargement 40
                                new FactureOtController.TableRow((int)tableau[6][0], 0,  tableau[6][2],tableau[6][3]),//dechargement 20
                                new FactureOtController.TableRow((int)tableau[7][0], 0,  tableau[7][2],tableau[7][3]),//acces camion
                                new FactureOtController.TableRow((int)tableau[8][0], 0,  tableau[8][2],tableau[8][3]),//visite/ferm 40
                                new FactureOtController.TableRow((int)tableau[9][0], 0,  tableau[9][2],tableau[9][3]),//visite/ferm 20
                                new FactureOtController.TableRow(0, (int)tableau[10][1],  tableau[10][2],tableau[10][3]),//visite douane 40
                                new FactureOtController.TableRow(0, (int)tableau[11][1],  tableau[11][2],tableau[11][3]),//visite douane 20
                                new FactureOtController.TableRow((int)tableau[12][0], 0,  tableau[12][2],tableau[12][3]),//chargement 40
                                new FactureOtController.TableRow((int)tableau[13][0], 0,  tableau[13][2],tableau[13][3]),//chargement 20
                                new FactureOtController.TableRow((int)tableau[14][0], (int)tableau[14][1],  tableau[14][2],tableau[14][3]),//mag 30j 40
                                new FactureOtController.TableRow((int)tableau[15][0], (int)tableau[15][1],  tableau[15][2],tableau[15][3]),//mag 30j 20
                                new FactureOtController.TableRow((int)tableau[16][0], (int)tableau[16][1],  tableau[16][2],tableau[16][3]),//mag 31j 40
                                new FactureOtController.TableRow((int)tableau[17][0], (int)tableau[17][1],  tableau[17][2],tableau[17][3]),//mag 31j 20
                                new FactureOtController.TableRow((int)tableau[18][0], 0,  tableau[18][2],tableau[18][3]),//tel
                                new FactureOtController.TableRow((int)tableau[19][0], 0,  tableau[19][2],tableau[19][3]),//frais d'expertise
                                new FactureOtController.TableRow((int)tableau[20][0], 0,  tableau[20][2],tableau[20][3]),//plombage
                                new FactureOtController.TableRow(0, 0,  0,tableau[21][3]),//magasinage portuaire
                                new FactureOtController.TableRow((int)tableau[22][0], 0,  0,tableau[22][3]) ,//scanner
                                new FactureOtController.TableRow(0, 0,  0,tableau[23][3]),//autre frais


                        };


                //  SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy");
                try {
                    FactureOtController.RemplirFactureOT("model_facture_ot", "Factur_OT_"+dateFormat2.format(date).toString() ,
                            new String[]{"nbDossier", "dateEntree", "nbTC", "dateSortie", "typeTC",
                                    "nbJours", "nTC", "gros", "article"},
                            new String[]{idDossierCourant+"/"+ft.format(new java.util.Date()).charAt(8)+ft.format(new java.util.Date()).charAt(9),ft.format(c.getResultat(Integer.parseInt(idDossierCourant)).getDate(2)),String.valueOf(listFactType.size()),ft.format(new java.util.Date()),"40''",
                                    String.valueOf((int)tableau[14][1]),"","57","87"},
                            tableauLignes,
                            true);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "-Aucun conteneur sélectionné.\n-Des coonteneurs séléctionnés mais pas autorisés à sortir");
            Toolkit.getDefaultToolkit().beep();
            alert.showAndWait();
        }


    }
*/
    @FXML
    void Sortir(ActionEvent event){
        listLeavingConts.clear();
        ///CONSTRUIRE LES LISTES DES COMBOS
        ObservableList<String> CamList=FXCollections.observableArrayList(),ChauList=FXCollections.observableArrayList();
        try {
            com.mysql.jdbc.Connection conn = null;
            com.mysql.jdbc.Statement stmt = null;
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();

            String sql;

            ResultSet res= null;
            sql="SELECT * FROM camion";

            res = stmt.executeQuery(sql);
            while(res.next()){
                CamList.addAll(res.getString("Matricule"));
            }
            sql="SELECT * FROM chauffeur";

            res = stmt.executeQuery(sql);
            while(res.next()){
                ChauList.addAll(res.getString("nom"));
            }


            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //CONSTRUIRE LA LISTE FINALE A AFFICHER
        int i;
        for(i=0;i<jeuDessaiCont.size();i++){
            if (jeuDessaiCont.get(i).chk.isSelected() && jeuDessaiCont.get(i).factureAut.getValue().toString().equals("OK") && jeuDessaiCont.get(i).sortieAut.getValue().toString().equals("1")){
                listLeavingConts.add(new SortieController.TabLeavingCont(jeuDessaiCont.get(i).numCont.getValue().toString(),ChauList,CamList));
            }
        }
        if(listLeavingConts.size()!=0){
            FXMLLoader loader =new FXMLLoader(getClass().getResource("../Views/Sortie.fxml"));
            try {
                Parent Pr=loader.load();
                Stage Stg= new Stage();
                Stg.setScene(new Scene(Pr));
                dossierStage.hide();
                Stg.setAlwaysOnTop(true);
                Stg.setTitle("Bon de Sortie");
                SortieController.sortieStage=Stg;
                Stg.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "-Aucun conteneur sélectionné.\n-Des coonteneurs séléctionnés mais pas autorisés à sortir");
            Toolkit.getDefaultToolkit().beep();
            alert.showAndWait();
        }

    }

    @FXML
    void Filtrer(KeyEvent event) {
        dosTable.setPredicate(elm -> {
            Boolean bl=elm.getValue().nomClient.getValue().contains(txtFieldFiltrer.getText())
                    || elm.getValue().idDossier.getValue().contains(txtFieldFiltrer.getText())
                    || elm.getValue().date.getValue().contains(txtFieldFiltrer.getText())
                    || elm.getValue().provenance.getValue().contains(txtFieldFiltrer.getText())
                    || elm.getValue().type.getValue().contains(txtFieldFiltrer.getText());

            return bl;
        });

    }

    @FXML
    void FiltrerClient(KeyEvent event) {
        cliTable.setPredicate(elm -> {
            Boolean bl=elm.getValue().nomClient.getValue().contains(txtFieldFiltrerClient.getText())
                    || elm.getValue().idClient.getValue().contains(txtFieldFiltrerClient.getText())
                    || elm.getValue().telephone.getValue().contains(txtFieldFiltrerClient.getText())
                    || elm.getValue().email.getValue().contains(txtFieldFiltrerClient.getText())
                    || elm.getValue().adresse.getValue().contains(txtFieldFiltrerClient.getText());

            return bl;
        });

    }

    @FXML
    void today(ActionEvent event){

        if(todayCheck.isSelected()){
                //System.out.print("Im checked");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                datePicker.setValue(LocalDate.parse(date,formatter));

        }
        else{
            //System.out.print("Im not checked");
            datePicker.setValue(null);
        }
    }

    @FXML
    void Visiter(ActionEvent event){
        listSelectedConts.clear();
        int i;
        for(i=0;i<jeuDessaiCont.size();i++){
            if (jeuDessaiCont.get(i).chk.isSelected()){
                listSelectedConts.add(new  VisiteController.TabSelectedCont(jeuDessaiCont.get(i).numCont.getValue().toString(),jeuDessaiCont.get(i).numPlomb.getValue().toString(),jeuDessaiCont.get(i).marchandise.getValue().toString()));
            }
        }
        if(listSelectedConts.size()!=0){

            try {
                FXMLLoader loader =new FXMLLoader(getClass().getResource("../Views/Visite.fxml"));
                Parent Rt=loader.load();
                Stage Sg= new Stage();
                Sg.setScene(new Scene(Rt));
                Sg.setTitle("Bon de visite");
                Sg.show();
                Sg.setAlwaysOnTop(true);
                dossierStage.hide();

                VisiteController.visiteStage=Sg;


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "-Aucun conteneur sélectionné.");
            Toolkit.getDefaultToolkit().beep();
            alert.showAndWait();
        }
    }

    @FXML
    void AddClient(ActionEvent event) {
        //call Nasim Stage !!! ........

        Stage client=new Stage();
        ClientController.getStage(current,client);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Views/Client.fxml"));
            client.setTitle("Gestion Des Clients");
            client.setScene(new Scene(root,1000,480));
            client.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void AddContainer(ActionEvent event) {

        FXMLLoader loader =new FXMLLoader(getClass().getResource("../Views/Conteneur.fxml"));
        try {
            Parent Rtt=loader.load();
            Stage Stg= new Stage();
            Stg.setScene(new Scene(Rtt));
            ConteneurController ctrl=loader.getController();
            ctrl.setStage(Stg);
            dossierStage.hide();
            ConteneurController.conteneurStage=Stg;
            Stg.setTitle("ajouter un conteneur");
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
            //System.out.println(selected);
            jeuDessaiCont.remove(selected);
            //contTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void AjouterDossier(ActionEvent event) {
        dosTable.getSelectionModel().clearSelection();
        comboBox_prov.setValue("");
        comboTypeDoss.setValue("");
        datePicker.setValue(null);
        comboTypeDoss.setDisable(false);
        dosTableselected=false;
        btnEnrigestrer.setDisable(false);
        addCli.setDisable(false);
        addCont.setDisable(false);
        suppCont.setDisable(false);
        comboBox_prov.setDisable(false);
        datePicker.setDisable(false);
        todayCheck.setSelected(false);
        comboTypeDoss.setDisable(false);
        jeuDessaiCont.clear();
        jeuDessaiCli.clear();

        try {
            jeuDessaiCli.clear();jeuDessaiCont.clear();//vider les table view
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql="r";
            //System.out.println("yawww2"+sql);
            sql="SELECT client.idClient,client.nom,client.tel,client.email,client.adresse FROM client" ;
            //System.out.println("yawww"+sql);
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
        Alert conf = new Alert((Alert.AlertType.CONFIRMATION), "Voulez-vous vraiment enregistrer?");
        Toolkit.getDefaultToolkit().beep();
        conf.showAndWait();
        ///DEMANDE DE CONFIRMATION
        if (conf.getResult().getText().equals("OK")) {


            if (!SelectedClientTxtFld.getText().isEmpty() && !jeuDessaiCont.isEmpty() && !comboBox_prov.getSelectionModel().isEmpty() && !comboTypeDoss.getSelectionModel().isEmpty() && datePicker.getValue() != null
                    ) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
                    stmt = (com.mysql.jdbc.Statement) conn.createStatement();

                    String sql;

                    sql = "INSERT INTO dossier (idClient,typee,provenance,dateEntree,nbConteneur)VALUES(" + idSelectedClient
                            + ",'" + comboTypeDoss.getSelectionModel().getSelectedItem().toString() + "','" + comboBox_prov.getSelectionModel().getSelectedItem().toString() + "','" +
                            datePicker.getValue() + "'," + jeuDessaiCont.size() + ")";
                    stmt.executeUpdate(sql, stmt.RETURN_GENERATED_KEYS);

                    ResultSet r = stmt.getGeneratedKeys();
                    int lastId = 0;
                    while (r.next()) {
                        lastId = r.getInt(1);
                    }
                    int i;
                    for (i = 0; i < jeuDessaiCont.size(); i++) {

                        sql = "INSERT INTO conteneur(NMC,idDossier, numPlomb, typeTc, typeMarchandise, chauffeurEntree, camionEntree, factureAutorise, sortieAutorise, nbVisite) VALUES ('" + jeuDessaiCont.get(i).numCont.getValue().toString() +
                                "'," + lastId + "," + jeuDessaiCont.get(i).numPlomb.getValue().toString() + "," +
                                jeuDessaiCont.get(i).type.getValue().toString() + ",'" + jeuDessaiCont.get(i).marchandise.getValue().toString() + "','" + jeuDessaiCont.get(i).chauffEntre.getValue().toString() + "','" +
                                jeuDessaiCont.get(i).camionEntre.getValue().toString() + "','OK',0,0" + ")";
                        //System.out.print(sql);
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
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR, "Il y a des informations manquantes!");
                Toolkit.getDefaultToolkit().beep();
                alert.showAndWait();

            }
            dosTable.getSelectionModel().clearSelection();
        }
    }


    @FXML
    void SupprimerDossier(ActionEvent event) {
        //deleting conteneru item from database
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql;
            String id = dosTable.getFocusModel().getFocusedItem().getValue().idDossier.getValue().toString();
            sql = "DELETE FROM  conteneur WHERE conteneur.idDossier=" + id;
            //System.out.println(sql);
            stmt.executeUpdate(sql);
            jeuDessaiCont.clear();
            sql = "DELETE FROM dossier WHERE dossier.idDossier=" + id;//contTable.getSelectionModel().getSelectedItem().getValue().numCont.getValue().toString()+"'";
            //System.out.println(sql);
            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int selected;
       // if (dosTableselected) {
            selected = dosTable.getSelectionModel().getSelectedIndex();
            jeuDessai.remove(selected);
       // }
        dosTableselected = false;


    }
    public void refresh(){
        try {
            jeuDessai.clear();
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql;
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
            System.out.println("Error found check your connection !");
            e.printStackTrace();
        }
    }
    public void SelectedDoss(String idDoss){
        try {
            jeuDessaiCli.clear();jeuDessaiCont.clear();//vider les table view
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql;

            sql="SELECT client.idClient,client.nom,client.tel,client.email,client.adresse FROM dossier JOIN client WHERE dossier.idClient=client.idClient and dossier.idDossier="+idDoss;
            ResultSet tabcli= stmt.executeQuery(sql);
            while(tabcli.next()) {
                //System.out.println(tabcli.getString("nom"));//+tabcli.getString("nom")+tabcli.getString("tel")+tabcli.getString("email")+tabcli.getString("adresse"));
                jeuDessaiCli.add(new TabClient(tabcli.getString("idClient"),tabcli.getString("nom"),tabcli.getString("tel"),tabcli.getString("email"),tabcli.getString("adresse")));
            }
            sql="SELECT conteneur.NMC,conteneur.numPlomb,conteneur.typeMarchandise,conteneur.typeTc,conteneur.chauffeurEntree,conteneur.camionEntree,conteneur.factureAutorise,conteneur.sortieAutorise,dossier.provenance,dossier.dateEntree,Dossier.typee FROM dossier JOIN conteneur WHERE dossier.idDossier=conteneur.idDossier and dossier.idDossier="+idDoss;
            ResultSet tabcont= stmt.executeQuery(sql);
            String prv="",tpd="";boolean i=true;
            Date date=new Date(2017,12,12);
            while(tabcont.next()) {
                if (i){prv=tabcont.getString("provenance");date=tabcont.getDate("dateEntree");tpd=tabcont.getString("typee");i=false;}
                //(tabcli.getString("nom"));//+tabcli.getString("nom")+tabcli.getString("tel")+tabcli.getString("email")+tabcli.getString("adresse"));
                jeuDessaiCont.add(new TabCont(tabcont.getString("NMC"),tabcont.getString("numPlomb"),tabcont.getString("typeMarchandise"),tabcont.getString("typeTc"),tabcont.getString("factureAutorise"),tabcont.getString("sortieAutorise"),tabcont.getString("chauffeurEntree"), tabcont.getString("camionEntree")));

            }



            //contTable.setId("contTableGreen");
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

    //public Date getDate(){ }
   // public String getClientName(){return dosTable.getSelectionModel().getSelectedItem().getValue().nomClient.getValue().toString(); }


    static Stage menu,current;
    static public void getStage(Stage menuu,Stage currentt)
    {
        menu=menuu;
        current=currentt;
    }

    @FXML
    void retour(ActionEvent event) {
        jeuDessai.clear();
        current.close();
        menu.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // on selecting item on conteneur table view if it's illegal  red else with green
        contTable.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) -> {
            if(newValue!=null) {
                if (contTable.getSelectionModel().getSelectedItem().getValue().factureAut.getValue().toString().equals("OK")) {
                    contTable.setId("contTableGreen");
                } else {
                    contTable.setId("contTableRed");
                }
            }
        });
        //ADDING ITEMS TO TABDOSSIER
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
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

            System.out.println("check your connection ! ");

            //e.printStackTrace();
        }

        comboTypeDoss.getItems().addAll("DSTR","OT");
        dosTable.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) -> {
            dosTableselected=true;

            if(newValue !=null){
                typeDossierCourant=newValue.getValue().type.getValue().toString();
                nomClientCourant=newValue.getValue().nomClient.getValue().toString();
                dateCourante=newValue.getValue().date.getValue().toString();
                idDossierCourant=newValue.getValue().idDossier.getValue();//dosTable.getSelectionModel().getSelectedItem().getValue().idDossier.getValue().toString();
                SelectedDoss(idDossierCourant);
                cliTable.getSelectionModel().clearSelection();

            }
        });
        cliTable.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) -> {
           if (newValue!=null) {SelectedClientTxtFld.setText(newValue.getValue().nomClient.getValue().toString());idSelectedClient=newValue.getValue().idClient.getValue().toString();}
        });
        //YOU CANT CHANGE DATE WITH KEYBOARD
        datePicker.setEditable(false);

        //////AWESOME CLASS TO SEARCHE IN COMBOS :D
        new ComboBoxAutoComplete<String>(comboBox_prov,"Selecetionner ou rechercher une provenance");

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
        JFXTreeTableColumn<TabCont,String> fact=new JFXTreeTableColumn<>("Etat");
        fact.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabCont, String> param) {
                return param.getValue().getValue().factureAut;
            }
        });
        JFXTreeTableColumn<TabCont,String> eta=new JFXTreeTableColumn<>("Facturé");
        eta.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabCont, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabCont, String> param) {
                return param.getValue().getValue().sortieAut;
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
        JFXTreeTableColumn<TabCont,CheckBox> check=new JFXTreeTableColumn<>("Choisi");
        check.setCellValueFactory(new Callback<JFXTreeTableColumn.CellDataFeatures<TabCont, CheckBox>, ObservableValue<CheckBox>>() {
            @Override
            public  ObservableValue<CheckBox> call(TreeTableColumn.CellDataFeatures<TabCont,CheckBox> param) {
                return new SimpleObjectProperty<CheckBox>(param.getValue().getValue().chk);
            }
        });



        /**CONSTRUCTON DE LA LISTE DES CONTENEUR*/

        final TreeItem<TabCont> ro =new RecursiveTreeItem<TabCont>(jeuDessaiCont,RecursiveTreeObject::getChildren);
        contTable.getColumns().setAll(check,numCon,numPlom,marchandis,typ1,chauffEntr,camionEntr,fact,eta);
        contTable.setRoot(ro);
        contTable.setShowRoot(false);

        /**TOOLTIPS**/
        txtFieldFiltrer.setTooltip(new Tooltip("Filtrer n'importe quelle colonne du tableau"));
        comboTypeDoss.setTooltip(new Tooltip("Selecetionner le type du dossier"));
        datePicker.setTooltip(new Tooltip("Selectionner la date"));
        cliTable.setTooltip(new Tooltip("Selecetionner un client"));
        addCli.setTooltip(new Tooltip("Ajouter un client s'il n'existe pas"));
        contTable.setTooltip(new Tooltip("Table des conteneurs"));
        addCont.setTooltip(new Tooltip("Ajouter des conteneurs"));
        suppCont.setTooltip(new Tooltip("Supprimer un conteneur (lors de l'ajout d'un dossier seulement))"));
        btnAjouter.setTooltip(new Tooltip("Ajouter un dossier"));
        btnEnrigestrer.setTooltip(new Tooltip("Enregistrer le dossier"));
        btnVisiter.setTooltip(new Tooltip("Générer un bon de visite"));
        btnFacturer.setTooltip(new Tooltip("Générer une facture"));
        btnSortir.setTooltip(new Tooltip("Générer un bon de sortie"));
        dosTable.setTooltip(new Tooltip("Table des dossiers"));
        btnReturn.setTooltip(new Tooltip("Retour au menu principale"));

        txtFieldFiltrerClient.setStyle("-fx-text-fill: #FFFFFF;");


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
    StringProperty factureAut;
    StringProperty sortieAut;
    StringProperty chauffEntre;
    StringProperty camionEntre;
    CheckBox chk=new CheckBox();
    public  TabCont(){
        this.numCont=new SimpleStringProperty("");
        this.numPlomb=new SimpleStringProperty("");
        this.type=new SimpleStringProperty("");
        this.marchandise=new SimpleStringProperty("");
        this.factureAut=new SimpleStringProperty("");
        this.sortieAut=new SimpleStringProperty("");
        this.chauffEntre=new SimpleStringProperty("");
        this.camionEntre=new SimpleStringProperty("");
        this.chk.setSelected(true);
    }

    public TabCont (String numCont,String numPlomb,String marchandise,String type,String factureAut,String sortieAut,String chaffEntr,String camionEntr)
    {
        this.numCont=new SimpleStringProperty(numCont);
        this.numPlomb=new SimpleStringProperty(numPlomb);
        this.marchandise=new SimpleStringProperty(marchandise);
        this.type=new SimpleStringProperty(type);
        this.factureAut=new SimpleStringProperty(factureAut);
        this.sortieAut=new SimpleStringProperty(sortieAut);
        this.chauffEntre=new SimpleStringProperty(chaffEntr);
        this.camionEntre=new SimpleStringProperty(camionEntr);
        this.chk.setSelected(true);
    }
}








    
