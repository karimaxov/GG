package sample;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
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
    ObservableList<Users> jeuDessai;
    Connection conn = null;
    Statement stmt =null;
    // JDBC driver name and database URL
    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_URL = "jdbc:mysql://localhost/projets2";

    //  Database credentials
    final String USER = "root";
    final String PASS = "135792468";





    public ObservableList<Users> getUsersList()  {
        ObservableList<Users> userData = FXCollections.observableArrayList();

        try
        {String sql="select username,password from login where super='0'";
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

                    String sql="update login set username='"+userf.getText().toString()+"',password='"+passf.getText().toString()+"' where username='"+jeuDessai.get(i).username.get()+"'";
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
                    }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void modifier(ActionEvent event) {
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
                try {
                    String sqls = "delete from login where username='" + jeuDessai.get(i).username.get() + "'";

                    Statement s = conn.createStatement();
                    s.executeUpdate(sqls);
                    jeuDessai.remove(i);
                    if(jeuDessai.isEmpty())supprimer.setDisable(true);
                    userf.clear();
                    passf.clear();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                catch (IndexOutOfBoundsException ex){

                }

            }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try { Class.forName("com.mysql.jdbc.Driver");
        conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
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

    }
}
