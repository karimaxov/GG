package sample;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Karim on 19/02/2017.
 */
public class Camion extends RecursiveTreeObject<Camion> {
    public StringProperty matricule=new SimpleStringProperty();
    public Camion(String m)
    {
        matricule.set(m);
    }
    public Camion()
    {

    }
}
