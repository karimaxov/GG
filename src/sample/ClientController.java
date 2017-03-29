package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

//import com.mysql.jdbc.Statement;


public class ClientController implements Initializable {

    @FXML
    private JFXTreeTableView<Client> treeView;

    @FXML
    private TextField text1;

    @FXML
    private TextField text2;

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
    private JFXButton supprimer;

    @FXML
    private JFXButton modifier;

    @FXML
    private JFXButton ajouter;

    @FXML
    private Button app;

    Connection conn = null;
   Statement stmt = null;

    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_URL = "jdbc:mysql://localhost/projets2";

    final String USER = "root";
    final String PASS = "135792468";




    private ObservableList<Client> getClientList()  {

        ObservableList<Client> clientData = FXCollections.observableArrayList();
        int i=1;
        try {
            String sql="select tel,typee,email,nom,adresse,tel2,tel3 from client ";
            ResultSet res;
            res=stmt.executeQuery(sql);

            while (res.next()) {
                Client client = new Client();
                client.tel = new SimpleStringProperty(res.getString("tel"));
                client.type = new SimpleStringProperty(res.getString("typee"));
                client.email = new SimpleStringProperty(res.getString("email"));
                client.nom = new SimpleStringProperty(res.getString("nom"));
                client.adresse = new SimpleStringProperty(res.getString("adresse"));
                client.tel2 = new SimpleStringProperty(res.getString("tel2"));
                client.tel3 = new SimpleStringProperty(res.getString("tel3"));
                client.bouton = new Button();
                clientData.add(client);


        }}catch (SQLException e)
        {
        }




        return clientData;


}

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ajouter.setDisable(false);
        modifier.setDisable(true);
       // supprimer.setDisable(true);

        try { Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = (java.sql.Statement) conn.createStatement();}
        catch (ClassNotFoundException ex) {
        }catch (SQLException ex) {
        }
        JFXTreeTableColumn<Client, String> telColumn = new JFXTreeTableColumn<>("telephone");
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

        JFXTreeTableColumn<Client, String> tellColumn = new JFXTreeTableColumn<>("fixe");
        tellColumn.setPrefWidth(150);
        tellColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client , String> param) {
                return param.getValue().getValue().tel2;
            }

        });

        JFXTreeTableColumn<Client, String> telllColumn = new JFXTreeTableColumn<>("faxe");
        telllColumn.setPrefWidth(150);
        telllColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client , String> param) {
                return param.getValue().getValue().tel3;
            }

        });

        /* JFXTreeTableColumn<Client, Button> essaiColumn = new JFXTreeTableColumn<>("essai");
        essaiColumn.setPrefWidth(150);
        essaiColumn.setCellFactory(new Callback<TreeTableColumn<Client, Button>, TreeTableCell<Client, Button>>()
        {
            @Override
            public TreeTableCell<Client, Button> call(TreeTableColumn<Client,Button> param)
            {
                TreeTableCell<Client, Button> cell = new TreeTableCell<Client, Button>()
                {
                    @Override
                    public void updateItem(Button app,boolean empty)
                    {
                        if (app != null)
                        {
                            if (!empty)
                        {
                            app.addEventHandler();
                            app.setGraphic(app);
                            app.setText("nadji");}
                        else
                        {
                            app.setGraphic(null);
                            app.setText(null);
                        }
                        }
                        else
                        {
                            app = new Button();
                        }
                    }
                };
                return cell;
            }

        });*/



        ObservableList<Client> clients = null;
            clients = getClientList();


        System.out.print(clients.size());
        // build tree
        final TreeItem<Client> root = new RecursiveTreeItem<Client>(clients, RecursiveTreeObject::getChildren);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.getColumns().setAll(telColumn,typColumn,emailColumn,nomColumn,adrColumn,telllColumn,tellColumn);//,essaiColumn);
    }

    static Stage menu,current;
    public static void getStage(Stage menuu,Stage currentt)
    {
        menu=menuu;
        current=currentt;
    }

    public void delete (ActionEvent event) throws SQLException {
        current.close();
        menu.show();
    }
    public void modifier () {
        TreeItem<Client> selected = treeView.getSelectionModel().getSelectedItem();
        if (selected != null)
        {
            Client nadji = selected.getValue();
            text1.setText(String.valueOf(nadji.getNom().getValue()));
            text2.setText(String.valueOf(selected.getValue().getEmail().getValue()));
            text3.setText(String.valueOf(selected.getValue().getAdresse().getValue()));
            text4.setText(String.valueOf(selected.getValue().getTel().getValue()));
            text5.setText(String.valueOf(selected.getValue().getType().getValue()));
            text6.setText(String.valueOf(selected.getValue().getTel2().getValue()));
            text7.setText(String.valueOf(selected.getValue().getTel3().getValue()));

        }
       // supprimer.setDisable(false);
        modifier.setDisable(false);
        ajouter.setDisable(true);


    }
    public void modifier1 () throws Exception{
        TreeItem<Client> selected = treeView.getSelectionModel().getSelectedItem();
        Client nadji = selected.getValue();
        stmt=conn.createStatement();
        String sqls="select * from client where nom ='"+ selected.getValue().getNom().getValue() +"'";
        ResultSet resuu=stmt.executeQuery(sqls);
        while (resuu.next()) {

            nadji.setNom(new SimpleStringProperty(text1.getText()));
            nadji.setEmail(new SimpleStringProperty(text2.getText()));
            nadji.setAdresse(new SimpleStringProperty(text3.getText()));
            nadji.setTel(new SimpleStringProperty(text4.getText()));
            nadji.setType(new SimpleStringProperty(text5.getText()));
            nadji.setTel2(new SimpleStringProperty(text6.getText()));
            nadji.setTel3(new SimpleStringProperty(text7.getText()));
            selected.setValue(nadji);
            treeView.refresh();
            Statement stmte = conn.createStatement();
            String sqld = "UPDATE `client` SET  tel = '" + text4.getText() + "',typee = '" + text5.getText() + "',email = '" + text2.getText() + "',nom = '" + text1.getText() + "',adresse = '" + text3.getText() + "',tel2 = '" + text6.getText() + "',tel3 = '" + text7.getText() + "'WHERE `client`.`idClient` = " + String.valueOf(resuu.getInt("idClient")) + "";
            System.out.println(sqld);
            System.out.println(stmte.executeUpdate(sqld));
        }
        //supprimer.setDisable(true);
        modifier.setDisable(true);
        ajouter.setDisable(false);



    }
    public void ajouter () throws Exception {
        Client nadji = new Client();
        nadji.setNom(new SimpleStringProperty(text1.getText()));
        nadji.setEmail(new SimpleStringProperty(text2.getText()));
        nadji.setAdresse(new SimpleStringProperty(text3.getText()));
        nadji.setTel(new SimpleStringProperty(text4.getText()));
        nadji.setType(new SimpleStringProperty(text5.getText()));
        nadji.setTel2(new SimpleStringProperty(text6.getText()));
        nadji.setTel3(new SimpleStringProperty(text7.getText()));
        TreeItem<Client> added = new TreeItem<Client>(nadji);
        treeView.getRoot().getChildren().add(added);
        treeView.refresh();
        Statement stmte = conn.createStatement();
        String sqld = "INSERT INTO `client` (tel,typee,email,nom,adresse,tel2,tel3) VALUES ('" + text4.getText() + "','" + text5.getText() + "','" + text2.getText() + "','" + text1.getText() + "','" + text3.getText() + "','" + text6.getText() + "','" + text7.getText() + "' );";
        System.out.println(sqld);
        System.out.println(stmte.executeUpdate(sqld));
        treeView.getSelectionModel().select(null);


    }
    public void vider () {

            text1.setText("");
            text2.setText("");
            text3.setText("");
            text4.setText("");
            text5.setText("");
            text6.setText("");
            text7.setText("");
            treeView.getSelectionModel().select(null);
       // supprimer.setDisable(true);
        modifier.setDisable(true);
        ajouter.setDisable(false);



    }
}
