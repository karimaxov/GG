package sample.Controllers;

/**
 * Created by NS on 20/02/2017.
 */

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class Client extends RecursiveTreeObject<Client> {
    //nom typee email tel

    StringProperty tel;
    StringProperty type;
    StringProperty email;
    StringProperty nom;
    StringProperty adresse;
    StringProperty fixe;
    StringProperty faxe;
    Button bouton;


    public void setNom (StringProperty a)
    {
         nom = a;
    }

    public void setType (StringProperty a)
    {
        type = a;
    }

    public  void setEmail (StringProperty a)
    {
        email = a;
    }

    public void setTel (StringProperty a)
    {
        tel = a;
    }

    public void setFixe (StringProperty a)
    {
        fixe = a;
    }

    public void setFaxe (StringProperty a)
    {
        faxe = a;
    }

    public void setAdresse (StringProperty a)
    {
        adresse = a;
    }

    public StringProperty getNom()
    {
        return nom;
    }

    public StringProperty getType()
    {
        return type;
    }

    public StringProperty getEmail()
    {
        return email;
    }

    public StringProperty getTel()
    {
        return tel;
    }

    public StringProperty getFixe()
    {
        return fixe;
    }

    public StringProperty getFaxe()
    {
        return faxe;
    }

    public StringProperty getAdresse()
    {
        return adresse ;
    }
}

