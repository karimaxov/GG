package sample;

/**
 * Created by NS on 20/02/2017.
 */

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

/**
 * Created by Karim on 14/02/2017.
 **/
public class Client extends RecursiveTreeObject<Client> {
    //nom typee email tel

    StringProperty tel;
    StringProperty type;
    StringProperty email;
    StringProperty nom;
    StringProperty adresse;
    StringProperty tel2;
    StringProperty tel3;
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

    public void setTel2 (StringProperty a)
    {
        tel2 = a;
    }

    public void setTel3 (StringProperty a)
    {
        tel3 = a;
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

    public StringProperty getTel2()
    {
        return tel2;
    }

    public StringProperty getTel3()
    {
        return tel3;
    }

    public StringProperty getAdresse()
    {
        return adresse ;
    }
}

