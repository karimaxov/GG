package sample.Controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UsersController implements Initializable {
    @FXML
    private JFXTextField userf;
    @FXML
    private JFXTextField passf;
    @FXML
    private JFXButton supprimer;
    @FXML
    private JFXButton ajouter;
    @FXML
    private JFXButton modifier;
    ObservableList<Users> jeuDessai;
    Connection conn = null;
    Statement stmt =null;
    // JDBC driver name and database URL
    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";






    public ObservableList<Users> getUsersList()  {
        ObservableList<Users> userData = FXCollections.observableArrayList();

        try
        {String sql="select username,password from login where 1";
            ResultSet res;

            res=stmt.executeQuery(sql);

            Users user;
            while(res.next())
            {
               user= new Users();
                user.pass=new SimpleStringProperty(res.getString("password"));
                user.username=new SimpleStringProperty(res.getString("username"));
                userData.add(user);

            }}
            catch (SQLException ex) {
        }
        return userData;

    }



    @FXML
    private JFXTreeTableView<Users> table;


@FXML
private JFXButton ajour;
    @FXML
    void ajour(ActionEvent event) {



                int i;
                if((i=table.getSelectionModel().getSelectedIndex())>=0 && !passf.getText().isEmpty() && !userf.getText().isEmpty())
                {
                    String user=table.getSelectionModel().getSelectedItem().getValue().username.get(),writen="";

                    if(user.equals("root")){writen="root";userf.setText("root");}else{writen=userf.getText().toString();}
                    String sql="update login set username='"+writen+"',password='"+passf.getText().toString()+"' where username='"+jeuDessai.get(i).username.get()+"'";
                    try {
                        jeuDessai.get(i).username.set(userf.getText().toString());
                        jeuDessai.get(i).pass.set(passf.getText().toString());
                        Statement s = conn.createStatement();
                        s.executeUpdate(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException ex){

                    }


                }
            }




    //}

    @FXML
    void ajouter(ActionEvent event) {
        if(userf.getText().isEmpty() || passf.getText().isEmpty())
        {

        }
            else
        {
            supprimer.setDisable(false);
            String sql2 ="select username from login where username='"+userf.getText().toString() +"'";

            try {
                Statement s=conn.createStatement();

                ResultSet r=s.executeQuery(sql2);

                    if(!(r.next())) {
                        sql2 = "insert into login(username,password,super) values('" + userf.getText().toString() + "','" + passf.getText().toString() + "','0')";
                        s = conn.createStatement();
                        s.executeUpdate(sql2);
                        jeuDessai.add(new Users(userf.getText().toString(), passf.getText().toString()));
                        table.getSelectionModel().select(null);
                        passf.clear();
                        userf.clear();
                    }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        table.getSelectionModel().select(null);
        passf.clear();
        userf.clear();

    }


   static Stage menu,current;
    static public void getStage(Stage menuu,Stage currentt)
    {
        menu=menuu;
        current=currentt;
    }

    @FXML
    void retour(ActionEvent event) {

        current.close();
        menu.show();
    }

    @FXML
    void supprimer(ActionEvent event) {




            int i = table.getSelectionModel().getSelectedIndex();
            if (i>=0 ) {
                if(!table.getSelectionModel().getSelectedItem().getValue().username.get().equals("root")){
                try {

                    String sqls = "delete from login where username='" + jeuDessai.get(i).username.get() + "'";

                    Statement s = conn.createStatement();
                    s.executeUpdate(sqls);
                    jeuDessai.remove(i);
                    if (jeuDessai.isEmpty()) supprimer.setDisable(true);
                    userf.clear();
                    passf.clear();
                    table.getSelectionModel().select(null);

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException ex) {

                }
            }
            }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try { Class.forName("com.mysql.jdbc.Driver");
        conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
        stmt = (Statement) conn.createStatement();}
        catch (ClassNotFoundException ex) {
        }catch (SQLException ex) {
        }
        jeuDessai=getUsersList();
        JFXTreeTableColumn<Users,String> user=new JFXTreeTableColumn<>("Utilisateur");
        user.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Users, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Users, String> param) {
                return param.getValue().getValue().username;
            }
        });
        JFXTreeTableColumn<Users,String> pass=new JFXTreeTableColumn<>("Mot de passe");
        pass.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Users, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Users, String> param) {
                return param.getValue().getValue().pass;
            }
        });
        user.setPrefWidth(157);
        pass.setPrefWidth(156);

        final TreeItem<Users> root =new RecursiveTreeItem<Users>(jeuDessai, RecursiveTreeObject::getChildren);
        table.getColumns().addAll(user,pass);
        table.setRoot(root);
        table.setShowRoot(false);
        table.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue)
                -> {
            if(newValue!=null) {
                passf.setText(newValue.getValue().pass.get());
                userf.setText(newValue.getValue().username.get());
            }
        });

        supprimer.setTooltip(new Tooltip("Selectionnez une ligne a partir de la table a droite puis cliquez sur moi pour la supprimer"));
        ajour.setTooltip(new Tooltip("selectionnez une ligne du tableau puis modifier le nom d'utilisateur et le mot de passe a partir des entrers de texte"));
        modifier.setTooltip(new Tooltip("vider les champs de texte pour entrer un nouveau utilisateur"));
        ajouter.setTooltip(new Tooltip("entrer un nom d'utilisateur et accorder lui un mot de passe puis cliquer sur moi"));


    }
}
