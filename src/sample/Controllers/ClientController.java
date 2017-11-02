package sample.Controllers;


import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientController implements Initializable {

    @FXML
    private JFXTreeTableView<Client> treeView;

    @FXML
    private JFXTextField text1;

    @FXML
    private JFXTextField text2;

    @FXML
    private TextField text3;

    @FXML
    private TextField text4;

    @FXML
    private TextField text5;

    @FXML
    private TextField text6;

    @FXML
    private TextField text7;

    @FXML
    private JFXButton ajouter;

    @FXML
    private JFXButton modifier;

    @FXML
    private JFXButton supprimer;

    @FXML
    private AnchorPane pane;

	
	static Stage menu,current;
    public static void getStage(Stage menuu,Stage currentt)
    {
        menu=menuu;
        current=currentt;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //pane.setMaxWidth(375);
        try { Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (java.sql.Statement) conn.createStatement();}
        catch (ClassNotFoundException ex) {
        }catch (SQLException ex) {
        }
        Tooltip tipAjouter = new Tooltip("Ajouter le client avec ses informations à la liste des Clients ... ");
        ajouter.setTooltip(tipAjouter);
        Tooltip tipModifier= new Tooltip("Modifier les informations du client sélectionné ... ");
        modifier.setTooltip(tipModifier);
        Tooltip tipSupprimer= new Tooltip("Supprimer le client sélectionné ...");
        supprimer.setTooltip(tipSupprimer);

        ajouter.setDisable(false);
        modifier.setDisable(true);
        supprimer.setDisable(true);


        JFXTreeTableColumn<Client, String> telColumn = new JFXTreeTableColumn<>("teleph");
        telColumn.setPrefWidth(150);
        telColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client , String> param) {
                return param.getValue().getValue().tel;
            }
        });
        JFXTreeTableColumn<Client, String> typColumn = new JFXTreeTableColumn<>("type");
        typColumn.setPrefWidth(150);
        typColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client , String> param) {
                return param.getValue().getValue().type;
            }
        });
        JFXTreeTableColumn<Client, String> faxeColumn = new JFXTreeTableColumn<>("faxe");
        faxeColumn.setPrefWidth(150);
        faxeColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client , String> param) {
                return param.getValue().getValue().faxe;
            }
        });
        JFXTreeTableColumn<Client, String> fixeColumn = new JFXTreeTableColumn<>("fixe");
        fixeColumn.setPrefWidth(150);
        fixeColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client , String> param) {
                return param.getValue().getValue().fixe;
            }
        });
        JFXTreeTableColumn<Client, String> emailColumn = new JFXTreeTableColumn<>("email");
        emailColumn.setPrefWidth(150);
        emailColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client , String> param) {
                return param.getValue().getValue().email;
            }
        });
        JFXTreeTableColumn<Client, String> nomColumn = new JFXTreeTableColumn<>("nom");
        nomColumn.setPrefWidth(150);
        nomColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client , String> param) {
                return param.getValue().getValue().nom;
            }
        });
        JFXTreeTableColumn<Client, String> adrColumn = new JFXTreeTableColumn<>("adresse");
        adrColumn.setPrefWidth(150);
        adrColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client , String> param) {
                return param.getValue().getValue().adresse;
            }
        });


         ObservableList<Client> clients = null;
            clients = getClientList();



        System.out.print(clients.size());
        // build tree
        final TreeItem<Client> root = new RecursiveTreeItem<Client>(clients, RecursiveTreeObject::getChildren);
        System.out.println(root);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);

        treeView.getColumns().setAll(nomColumn,typColumn,emailColumn,telColumn,faxeColumn,fixeColumn,adrColumn);
    }

    public void retour(){
        current.close();
        menu.show();
    }
    public void delete ()   {
        try {
            int i=0;
            TreeItem<Client> selected = treeView.getSelectionModel().getSelectedItem();
            Class.forName(JDBC_DRIVER);
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            String ins="DELETE FROM client WHERE tel ="+"'"+ selected.getValue().getTel().getValue()+"'";
            //ResultSet res;
            System.out.println(ins+"element supprimé"+i);
            i++;
            stmt=conn.createStatement();
            // System.out.println("nadji mnawer my code is"+ins);
            System.out.println(ins);
            System.out.println(stmt.executeUpdate(ins));
            System.out.println(i);
            i++;
            selected.getParent().getChildren().remove(selected);
            ajouter.setDisable(false);
            modifier.setDisable(true);
            supprimer.setDisable(true);

        }

        catch (Exception e)
        {}
    }
    public void modifier () throws Exception{
        ajouter.setDisable(true);
        modifier.setDisable(false);
        supprimer.setDisable(false);

        TreeItem<Client> selected = treeView.getSelectionModel().getSelectedItem();
        Client nadjii = selected.getValue();
        text1.setText(String.valueOf(nadjii.getNom().getValue()));
        text2.setText(String.valueOf(selected.getValue().getEmail().getValue()));
        text3.setText(String.valueOf(selected.getValue().getAdresse().getValue()));
        text4.setText(String.valueOf(selected.getValue().getTel().getValue()));
        text5.setText(String.valueOf(selected.getValue().getFixe().getValue()));
        text6.setText(String.valueOf(selected.getValue().getFaxe().getValue()));
        text7.setText(String.valueOf(selected.getValue().getType().getValue()));

    }
    public void modifier1 () {
        TreeItem<Client> selected = treeView.getSelectionModel().getSelectedItem();
        Client nadji = selected.getValue();

        try {
            String cond = selected.getValue().getNom().getValue();
            if (text1.getText().matches("[A-z]+")){System.out.println("a");nadji.setNom(new SimpleStringProperty(text1.getText()));}else {
                text1.setText("");}
            Pattern pattern = Pattern.compile("^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[a-z]{2,6}");
            System.out.println(Pattern.quote(text2.getText()));
            Matcher matcher = pattern.matcher((CharSequence) text2.getText());

            if (matcher.find()) {
                System.out.println("rani hna");
                nadji.setEmail(new SimpleStringProperty(text2.getText()));
            } else {
                text2.setText("");
            }
            if (text3.getText().matches("[A-z]+")) {
                System.out.println("f");
                nadji.setAdresse(new SimpleStringProperty(text3.getText()));
            } else {
                text3.setText("");
            }
            if (text4.getText().matches("[0-9]+")) {
                System.out.println("i");
                nadji.setTel(new SimpleStringProperty(text4.getText()));
            } else {
                text4.setText("");
            }
            if (text5.getText().matches("[0-9]+")) {
                System.out.println("n");
                nadji.setFixe(new SimpleStringProperty(text5.getText()));
            } else {
                text5.setText("");
            }
            if (text6.getText().matches("[0-9]+")) {
                System.out.println("a");
                nadji.setFaxe(new SimpleStringProperty(text6.getText()));
            } else {
                text6.setText("");
            }
            if (text7.getText().matches("[A-z]+")) {
                System.out.println("l");
                nadji.setType(new SimpleStringProperty(text7.getText()));
            } else {
                text7.setText("");
            }

                if (!text1.getText().isEmpty() && !text2.getText().isEmpty() && !text3.getText().isEmpty() && !text4.getText().isEmpty() && !text7.getText().isEmpty()) {
                Class.forName(JDBC_DRIVER);
                conn =(com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
                stmt = conn.createStatement();


                selected.setValue(nadji);
                String sql = "UPDATE client SET nom = '" + nadji.getNom().getValue() + "', typee = '" + nadji.getType().getValue() + "',tel = '" + nadji.getTel().getValue() + "',faxe = '" + nadji.getFaxe().getValue() + "', fixe = '" + nadji.getFixe().getValue() + "',adresse = '" + nadji.getAdresse().getValue() + "', email = '" + nadji.getEmail().getValue() + "' WHERE nom = " + "'" + cond + "'";
                stmt.executeUpdate(sql);
                treeView.refresh();
                ajouter.setDisable(false);
                modifier.setDisable(true);
                supprimer.setDisable(true);
            }
        }catch (SQLException | ClassNotFoundException  e){System.out.println("exception");}

    }

    public void ajouter()  {
        try{

            Client cli = new Client();
            if (text1.getText().matches("[A-z]+")){System.out.println("f");cli.setNom(new SimpleStringProperty(text1.getText()));}else {
                text1.setText("");}
                Pattern pattern = Pattern.compile("^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[a-z]{2,6}");
                System.out.println(Pattern.quote(text2.getText()));
                Matcher matcher = pattern.matcher((CharSequence) text2.getText());

                if (matcher.find()) {
                    cli.setEmail(new SimpleStringProperty(text2.getText()));
                } else {
                    text2.setText("");
                }
                if (text3.getText().matches("[A-z0-9]+")) {
                    System.out.println("n");
                    cli.setAdresse(new SimpleStringProperty(text3.getText()));
                } else {
                    text3.setText("");
                }
                if (text4.getText().matches("[0-9]+")) {
                    System.out.println("a");
                    cli.setTel(new SimpleStringProperty(text4.getText()));
                } else {
                    text4.setText("");
                }
                if ((text5.getText().matches("[0-9]+")) && (!text5.getText().isEmpty())) {
                    System.out.println("");
                    cli.setFixe(new SimpleStringProperty(text5.getText()));
                } else {
                    cli.setFixe(new SimpleStringProperty("null"));
                }
                if ((text6.getText().matches("[0-9]+")) && (!text6.getText().isEmpty())) {
                    System.out.println("");
                    cli.setFaxe(new SimpleStringProperty(text6.getText()));
                } else {
                    cli.setFaxe(new SimpleStringProperty("null"));
                }
                if (text7.getText().matches("[A-z]+")) {
                    System.out.println("l");
                    cli.setType(new SimpleStringProperty(text7.getText()));
                } else {
                    text7.setText("");
                }
                if (!text1.getText().isEmpty() && !text2.getText().isEmpty() && !text3.getText().isEmpty() && !text4.getText().isEmpty() && !text7.getText().isEmpty()) {


                    Class.forName(JDBC_DRIVER);
                    conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
                    String ins = "INSERT INTO client (nom, typee, tel, fixe, faxe, email, adresse) VALUES ('" + cli.getNom().getValue() + "','" + cli.getType().getValue() + "','" + cli.getTel().getValue() + "','" + cli.getFixe().getValue() + "','" + cli.getFaxe().getValue() + "','" + cli.getEmail().getValue() + "','" + cli.getAdresse().getValue() + "')";
                    stmt = conn.createStatement();
                    //stmt.executeUpdate(ins);
                    stmt.executeUpdate(ins, stmt.RETURN_GENERATED_KEYS);

                    ResultSet r = stmt.getGeneratedKeys();
                    int lastId = 0;
                    while (r.next()) {
                        lastId = r.getInt(1);
                    }
                    TreeItem<Client> added = new TreeItem<Client>(cli);
                    if (treeView.getRoot() != null) {
                        int ii = 0;

                        while(treeView.getRoot().getChildren().size() !=0 && (ii < treeView.getRoot().getChildren().size() )&& !(added.getValue().getTel().getValue().equalsIgnoreCase(treeView.getRoot().getChildren().get(ii).getValue().getTel().getValue())  )){System.out.println(ii+treeView.getRoot().getChildren().get(ii).getValue().getTel().getValue()+"iww"+added.getValue().getTel().getValue());ii++;}
                        if (ii == treeView.getRoot().getChildren().size()){treeView.getRoot().getChildren().add(added);}

                        treeView.refresh();
                    }
                    treeView.refresh();
                    DossierController c=new DossierController();
                    c.refreshJeuDessaiCli(String.valueOf(lastId),cli.getNom().getValue(),cli.getTel().getValue(),cli.getEmail().getValue(),cli.getAdresse().getValue());
                }


        }catch ( SQLException | ClassNotFoundException  e){}


    }

    Connection conn = null;
    Statement stmt = null;

    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";





    private ObservableList<Client> getClientList() {

        ObservableList<Client> clientData = FXCollections.observableArrayList();
        int i=1;
        try {

            Class.forName(JDBC_DRIVER);
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());

            String sql="select nom,email,adresse,tel,fixe,faxe,typee from client ";
            ResultSet res;
            stmt=conn.createStatement();
            res=stmt.executeQuery(sql);
            while (res.next()) {
                Client client = new Client();
                client.tel = new SimpleStringProperty(res.getString("tel"));
                client.fixe = new SimpleStringProperty(res.getString("fixe"));
                client.faxe = new SimpleStringProperty(res.getString("faxe"));
                client.type = new SimpleStringProperty(res.getString("typee"));
                client.email = new SimpleStringProperty(res.getString("email"));
                client.nom = new SimpleStringProperty(res.getString("nom"));
                client.adresse = new SimpleStringProperty(res.getString("adresse"));
                clientData.add(client);


            }}catch (SQLException e)
        {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return clientData;


    }
    public void vider(){
        ajouter.setDisable(false);
        modifier.setDisable(true);
        supprimer.setDisable(true);
        text1.clear();
        text2.clear();
        text3.clear();
        text4.clear();
        text5.clear();
        text6.clear();
        text7.clear();
    }

}
