package sample;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.nextStage.MenuController;

import javax.print.DocFlavor;
import javax.swing.plaf.nimbus.State;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ChauffeurController implements Initializable{

    Connection conn = null;
    Statement stmt =null;
    // JDBC driver name and database URL
    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_URL = "jdbc:mysql://localhost/projets2";

    //  Database credentials
    final String USER = "root";
    final String PASS = "135792468";
    ObservableList<Camion> camionList=null;
    ObservableList<Chauffeur> chauffeurList=null;
    ObservableList<Douane> douaneList=null;

    public ObservableList<Chauffeur> getChauffeurList()
    {
        ObservableList<Chauffeur> ch= FXCollections.observableArrayList();


        try
        {String sqll="select idChauffeur,nom,numero from chauffeur";
            ResultSet res;
            Statement stmt=conn.createStatement();
            res=stmt.executeQuery(sqll);
            Chauffeur chauffeur;
            while(res.next())
            {
                chauffeur= new Chauffeur();
                chauffeur.id=new SimpleStringProperty(String.valueOf(res.getInt("idChauffeur")));
                chauffeur.nom=new SimpleStringProperty(res.getString("nom"));
                chauffeur.tel=new SimpleStringProperty(String.valueOf(res.getInt("numero")));
                ch.add(chauffeur);

            }}
        catch (SQLException ex) {
        }

        return ch;
    }



    @FXML
    private JFXTextField fraisDos;

    @FXML
    private JFXTextField suivi;

    @FXML
    private JFXTextField manutention;

    @FXML
    private JFXTextField transport;

    @FXML
    private JFXTextField immo;

    @FXML
    private JFXTextField dech20;

    @FXML
    private JFXTextField dech40;

    @FXML
    private JFXTextField accesCam;

    @FXML
    private JFXTextField vis20;

    @FXML
    private JFXTextField vis40;

    @FXML
    private JFXTextField visD20;

    @FXML
    private JFXTextField visD40;

    @FXML
    private JFXTextField char20;

    @FXML
    private JFXTextField char40;

    @FXML
    private JFXTextField mag120;

    @FXML
    private JFXTextField mag140;

    @FXML
    private JFXTextField mag220;

    @FXML
    private JFXTextField mag240;

    @FXML
    private JFXTextField tel;

    @FXML
    private JFXTextField fraisExpertise;

    @FXML
    private JFXTextField magPort;

    @FXML
    private JFXTextField scanner;

    @FXML
    private JFXTextField plombage;




    @FXML
    private JFXTreeTableView<Chauffeur> tableChauffeur;

    @FXML
    private JFXTreeTableView<Camion> tableCamion;

    @FXML
    private JFXTextField nomChauffeur;

    @FXML
    private JFXTextField numChauffeur;


    @FXML
    private JFXTextField nomAgentField;

    @FXML
    private JFXTextField badgeField;

    @FXML
    private JFXButton ajouterCamion;

    @FXML
    private JFXButton supprimerCamion;

    @FXML
    private JFXTextField matricule;

    @FXML
    private JFXButton ajouter;

    @FXML
    private JFXButton ajour;

    @FXML
    private JFXTreeTableView<Douane> tableDouane;

    @FXML
    private JFXButton supprimer;

    @FXML
    private JFXButton clear;


    static Stage menu,current;
    static public void getStage(Stage menuu,Stage currentt)
    {
        menu=menuu;
        current=currentt;
    }


    boolean recherche(String s)
    {
        for(int i=0;i<douaneList.size();i++)
        {
            if(douaneList.get(i).badge.get().equals(s))
            {
                return true;
            }
        }
        return false;

    }


    @FXML
    void ajouterdouane(ActionEvent event) {

        if(!nomAgentField.getText().isEmpty() && !badgeField.getText().isEmpty())
        {
            try {


                if(!recherche(badgeField.getText())) {
                    Statement s = conn.createStatement();
                    s.executeUpdate("INSERT into douane(nom,Badge) values('" + nomAgentField.getText() + "','" + badgeField.getText() + "')");
                    douaneList.add(new Douane(nomAgentField.getText(), badgeField.getText()));

                    tableDouane.getSelectionModel().select(null);
                    nomAgentField.clear();
                    badgeField.clear();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    @FXML
    void retour2(ActionEvent event) {
        current.close();
        System.out.print("retour");
        menu.show();
    }

    @FXML
    void supprimerdouane(ActionEvent event) {
        try {
            Statement s=conn.createStatement();
            if(tableDouane.getSelectionModel()!=null)
            {
                Douane d=tableDouane.getSelectionModel().getSelectedItem().getValue();
                s.executeUpdate("delete from douane where Badge='"+d.badge.get()+"'");
                douaneList.remove(d);
                tableDouane.getSelectionModel().select(null);
                nomAgentField.clear();
                badgeField.clear();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }







    @FXML
    void ajour(ActionEvent event) {
        if(tableChauffeur.getSelectionModel().getSelectedItem()!=null && numChauffeur.getText().matches("[0-9]+"))
        {
            try {
                Statement s=conn.createStatement();
                s.executeUpdate("update chauffeur Set nom='"+nomChauffeur.getText()+"',numero='"+numChauffeur.getText()+"' where idChauffeur='"+tableChauffeur.getSelectionModel().getSelectedItem().getValue().id.get()+"'");
                chauffeurList.get(tableChauffeur.getSelectionModel().getSelectedIndex()).tel.set(numChauffeur.getText());
                chauffeurList.get(tableChauffeur.getSelectionModel().getSelectedIndex()).nom.set(nomChauffeur.getText());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private JFXButton retour;
    @FXML
    void retour(ActionEvent event) {
        current.close();
        System.out.print("retour");
        menu.show();
    }
    @FXML
    void retour1(ActionEvent event) {
        current.close();
        System.out.print("retour");
        menu.show();
    }
    @FXML
    void appliquer(ActionEvent event) {

        PrixUnitaires pu = new PrixUnitaires();
        try {
            pu.setAccesCam(new SimpleDoubleProperty(Double.parseDouble(accesCam.getText())));
            pu.setVisite20(new SimpleDoubleProperty(Double.parseDouble(vis20.getText())));
            pu.setVisite40(new SimpleDoubleProperty(Double.parseDouble(vis40.getText())));
            pu.setVisD20(new SimpleDoubleProperty(Double.parseDouble(visD20.getText())));
            pu.setVisD40(new SimpleDoubleProperty(Double.parseDouble(visD40.getText())));
            pu.setCh20(new SimpleDoubleProperty(Double.parseDouble(char20.getText())));
            pu.setCh40(new SimpleDoubleProperty(Double.parseDouble(char40.getText())));
            pu.setDech20(new SimpleDoubleProperty(Double.parseDouble(dech20.getText())));
            pu.setDech40(new SimpleDoubleProperty(Double.parseDouble(dech40.getText())));
            pu.setMag120(new SimpleDoubleProperty(Double.parseDouble(mag120.getText())));
            pu.setMag140(new SimpleDoubleProperty(Double.parseDouble(mag140.getText())));
            pu.setMag220(new SimpleDoubleProperty(Double.parseDouble(mag220.getText())));
            pu.setMag240(new SimpleDoubleProperty(Double.parseDouble(mag240.getText())));
            pu.setManutention(new SimpleDoubleProperty(Double.parseDouble(manutention.getText())));
            pu.setImmo(new SimpleDoubleProperty(Double.parseDouble(immo.getText())));
            pu.setTransport(new SimpleDoubleProperty(Double.parseDouble(transport.getText())));
            pu.setSuivi(new SimpleDoubleProperty(Double.parseDouble(suivi.getText())));
            pu.setTel(new SimpleDoubleProperty(Double.parseDouble(tel.getText())));
            pu.setMagPort(new SimpleDoubleProperty(Double.parseDouble(magPort.getText())));
            pu.setScanner(new SimpleDoubleProperty(Double.parseDouble(scanner.getText())));
            pu.setFraisExpertise(new SimpleDoubleProperty(Double.parseDouble(fraisExpertise.getText())));
            pu.setFraisDos(new SimpleDoubleProperty(Double.parseDouble(fraisDos.getText())));
            pu.setPlombage(new SimpleDoubleProperty(Double.parseDouble(plombage.getText())));

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            System.out.println(pu.getSuivi().getValue().toString());
            String sql = "update prixunitaires set accesCamion="+ pu.getAccesCam().getValue().toString() + ",suivi ="+pu.getSuivi().getValue().toString()+",immobilisation ="+pu.getImmo().getValue().toString()+",manutention ="+pu.getManutention().getValue().toString()+",transport ="+pu.getTransport().getValue().toString()+",dechargement20 ="+pu.getDech20().getValue().toString()+",dechargement40 ="+pu.getDech40().getValue().toString()+",fraisDossier ="+pu.getFraisDos().getValue().toString()+",visite20 ="+pu.getVisite20().getValue().toString()+",visite40 ="+pu.getVisite40().getValue().toString()+",visDouane20 ="+pu.getVisD20().getValue().toString()+",visDouane40 ="+pu.getVisD40().getValue().toString()+",char20 ="+pu.getCh20().getValue().toString()+",char40 ="+pu.getCh40().getValue().toString()+",mag120 ="+pu.getMag120().getValue().toString()+",mag140 ="+pu.getMag140().getValue().toString()+",mag220 ="+pu.getMag220().getValue().toString()+",mag240 ="+pu.getMag240().getValue().toString()+",tel ="+pu.getTel().getValue().toString()+",fraisExpertise ="+pu.getFraisExpertise().getValue().toString()+ "where 1" ;
            System.out.println(sql);
            System.out.println(stmt.executeUpdate(sql));
            ResultSet set=stmt.executeQuery("select * from prixunitaires ");

            set.next();
            int traite=set.getInt("traite");
            String sql1;
            System.out.println(sql);
            if(traite==0 ) {
                String noms = set.getString("userr");
                 sql1 = "update prixunitaires set userr='" + noms + ", " + MenuController.getUserName() + "',plombage =" + pu.getPlombage().getValue().toString() + ",magPort =" + pu.getMagPort().getValue().toString() + ",scanner =" + pu.getScanner().getValue().toString() + "where 1";
                System.out.println(sql1);
            }else{
                 sql1 = "update prixunitaires set traite='"+0+"',userr='"+ MenuController.getUserName() + "',plombage =" + pu.getPlombage().getValue().toString() + ",magPort =" + pu.getMagPort().getValue().toString() + ",scanner =" + pu.getScanner().getValue().toString() + "where 1";

            }
            System.out.println(stmt.executeUpdate(sql1));


        }catch (SQLException e){System.out.println("exception");}

    }



    @FXML
    void ajouter(ActionEvent event) {
        if(!nomChauffeur.getText().isEmpty() && !numChauffeur.getText().isEmpty() && numChauffeur.getText().matches("[0-9]+"))
        {
            try {
                Statement s=conn.createStatement();
                s.executeUpdate("insert into chauffeur(nom,numero) values ('"+nomChauffeur.getText()+"','"+numChauffeur.getText()+"')",Statement.RETURN_GENERATED_KEYS);

                //s=conn.createStatement();
                ResultSet r=s.getGeneratedKeys();
               // r.last();
                int id=0;
                while( r.next() )
                {
                    id=r.getInt(1);
                }
                    chauffeurList.add(new Chauffeur(String.valueOf(id),nomChauffeur.getText(),numChauffeur.getText()));
                int i=0;
                while(i<camionList.size())
                {
                    Statement st=conn.createStatement();
                    st.executeUpdate("insert into camion(matricule,idChauffeur) values('"+camionList.get(i).matricule.get()+"','"+id+"')");
                    i++;
                }

                tableChauffeur.getSelectionModel().select(null);
                camionList.clear();

                nomChauffeur.clear();
                numChauffeur.clear();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void ajouterCamion(ActionEvent event) {
                if(!matricule.getText().isEmpty())
                {
                    try {
                        Statement s=conn.createStatement();
                        camionList.add(new Camion(matricule.getText()));
                        if(tableChauffeur.getSelectionModel().getSelectedItem()!=null) {
                            s.executeUpdate("INSERT into camion(matricule,idChauffeur) values ('" + matricule.getText() + "','" + Integer.parseInt(tableChauffeur.getSelectionModel().getSelectedItem().getValue().id.get()) + "')");
                        }
                        matricule.clear();


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
    }

    @FXML
    void clear(ActionEvent event) {
        nomChauffeur.clear();
        numChauffeur.clear();
        matricule.clear();
        camionList.clear();
        tableChauffeur.getSelectionModel().select(null);
        tableCamion.getSelectionModel().select(null);



    }

    @FXML
    void supprimer(ActionEvent event) {
            if(tableChauffeur.getSelectionModel().getSelectedItem()!=null)
            {
                Chauffeur c=tableChauffeur.getSelectionModel().getSelectedItem().getValue();
                try {
                    Statement st=conn.createStatement();
                    st.executeUpdate("delete from chauffeur where numero='"+c.tel.get()+"'");
                    chauffeurList.remove(c);
                    tableChauffeur.getSelectionModel().select(null);
                    camionList.clear();
                    nomChauffeur.clear();
                    numChauffeur.clear();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
    }

    @FXML
    void supprimerCamion(ActionEvent event) {
        if(tableCamion.getSelectionModel().getSelectedItem()!=null) {
            Camion cam = tableCamion.getSelectionModel().getSelectedItem().getValue();
            if (cam != null) {
                try {

                    Statement s = conn.createStatement();
                    s.executeUpdate("delete from camion where matricule='" + cam.matricule.get() + "'");
                    camionList.remove(cam);
                    matricule.clear();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menu.close();
        try { Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = (Statement) conn.createStatement();}
        catch (ClassNotFoundException ex) {
        }catch (SQLException ex) {
        }
        camionList=FXCollections.observableArrayList();
        chauffeurList=getChauffeurList();
       // System.out.println(chauffeurList.get(0).nom.get());
        JFXTreeTableColumn<Chauffeur,String> user=new JFXTreeTableColumn<>("Chauffeur");
        user.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Chauffeur, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Chauffeur, String> param) {
                return param.getValue().getValue().nom;
            }
        });
        JFXTreeTableColumn<Chauffeur,String> pass=new JFXTreeTableColumn<>("Numero de telephone");
        pass.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Chauffeur, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Chauffeur, String> param) {
                return param.getValue().getValue().tel;
            }
        });
        JFXTreeTableColumn<Chauffeur,String> idd=new JFXTreeTableColumn<>("id");
        idd.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Chauffeur, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Chauffeur, String> param) {
                return param.getValue().getValue().id;
            }
        });


        user.setPrefWidth(136);
        pass.setPrefWidth(136);
        idd.setPrefWidth(60);
        final TreeItem<Chauffeur> root =new RecursiveTreeItem<Chauffeur>(chauffeurList, RecursiveTreeObject::getChildren);
        tableChauffeur.getColumns().addAll(idd,user,pass);
        tableChauffeur.setRoot(root);
        tableChauffeur.setShowRoot(false);


        JFXTreeTableColumn<Camion,String> cam=new JFXTreeTableColumn<>("Matricule");
        cam.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Camion, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Camion, String> param) {
                return param.getValue().getValue().matricule;
            }
        });





        cam.setPrefWidth(248);
        final TreeItem<Camion> root2 =new RecursiveTreeItem<Camion>(camionList, RecursiveTreeObject::getChildren);
        tableCamion.getColumns().add(cam);
        tableCamion.setRoot(root2);
        tableCamion.setShowRoot(false);



        douaneList=FXCollections.observableArrayList();



        JFXTreeTableColumn<Douane,String> nomAgent=new JFXTreeTableColumn<>("Nom de l'agent");
        nomAgent.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Douane, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Douane, String> param) {
                return param.getValue().getValue().nomAgent;
            }
        });

        JFXTreeTableColumn<Douane,String> badge=new JFXTreeTableColumn<>("Badge");
        badge.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Douane, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Douane, String> param) {
                return param.getValue().getValue().badge;
            }
        });

        final TreeItem<Douane> root3 =new RecursiveTreeItem<Douane>(douaneList, RecursiveTreeObject::getChildren);
        tableDouane.getColumns().addAll(nomAgent,badge);
        tableDouane.setRoot(root3);
        tableDouane.setShowRoot(false);

        try {
            Statement sd=conn.createStatement();
            ResultSet set=sd.executeQuery("select nom,Badge from douane");
            while(set.next())
            {
                douaneList.add(new Douane(set.getString("nom"),set.getString("Badge")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }




        tableChauffeur.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue)
                -> {
            if(newValue!=null) {
                try {
                    nomChauffeur.setText(newValue.getValue().nom.get());
                    numChauffeur.setText(newValue.getValue().tel.get());
                    String sql="select matricule from camion where idChauffeur='"+newValue.getValue().id.get()+"'";
                    Statement st=conn.createStatement();
                    ResultSet s=st.executeQuery(sql);
                    Camion camion;
                    camionList.clear();
                    while(s.next())
                    {
                        camion=new Camion();
                        camion.matricule=new SimpleStringProperty(s.getString("matricule"));
                        camionList.add(camion);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = (java.sql.Statement) conn.createStatement();


            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "select fraisDossier,suivi,transport,immobilisation,manutention,mag120,mag140,mag220,mag240,dechargement20,dechargement40,visite20,visite40,visDouane20,visDouane40,char20,char40,tel,fraisExpertise,accesCamion,magPort,scanner,plombage from prixunitaires ";
            ResultSet res;
            stmt = conn.createStatement();
            res = stmt.executeQuery(sql);
            res.next();
            fraisDos.setText(res.getString("fraisDossier"));
            suivi.setText(res.getString("suivi"));
            manutention.setText(res.getString("manutention"));
            transport.setText(res.getString("transport"));

            immo.setText(res.getString("immobilisation"));
            vis20.setText(res.getString("visite20"));
            vis40.setText(res.getString("visite40"));
            visD20.setText(res.getString("visDouane20"));
            visD40.setText(res.getString("visDouane40"));
            char20.setText(res.getString("char20"));
            char40.setText(res.getString("char40"));
            dech20.setText(res.getString("dechargement20"));
            dech40.setText(res.getString("dechargement40"));
            accesCam.setText(res.getString("accesCamion"));
            mag120.setText(res.getString("mag120"));
            mag140.setText(res.getString("mag140"));
            mag220.setText(res.getString("mag220"));
            mag240.setText(res.getString("mag240"));
            tel.setText(res.getString("tel"));
            fraisExpertise.setText(res.getString("fraisExpertise"));
            scanner.setText(res.getString("scanner"));
            magPort.setText(res.getString("magPort"));
            plombage.setText(res.getString("plombage"));

        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    class Douane extends RecursiveTreeObject<Douane>{
        StringProperty nomAgent=new SimpleStringProperty();
        StringProperty badge=new SimpleStringProperty();

        public Douane(String nomAgent, String badge) {
            this.nomAgent.set(nomAgent);
            this.badge.set(badge);
        }
    }

}
