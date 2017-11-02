package sample.Controllers;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Karim on 19/02/2017.
 */
public class Chauffeur extends RecursiveTreeObject<Chauffeur> {
    public StringProperty id=new SimpleStringProperty();
    public StringProperty nom=new SimpleStringProperty();
    public StringProperty tel=new SimpleStringProperty();

    public Chauffeur(String i,String n,String t)
    {
        id.set(i);
        nom.set(n);
        tel.set(t);
    }
    public Chauffeur()
    {

    }

}
