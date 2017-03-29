package sample;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Karim on 15/02/2017.
 */
public class Users extends RecursiveTreeObject<Users> {
    StringProperty username;
    StringProperty pass;

    public Users() {
    }

    public Users(String us, String ps) {
        username=new SimpleStringProperty(us);
        pass=new SimpleStringProperty(ps);

    }
}
