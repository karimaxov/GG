package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;


public class FactureOtController implements Initializable {

    Connection conn = null;
    Statement stmt = null;
    int idDossier=1;
    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public int nb40;
    public int nb20;
    public int nbvisite40;
    public int nbvisite20;
    public static double somme = 0.;
    public static double somme1 = 0.;
    public static double somme2 = 0.;
    public static double somme3 = 0.;
    public void getIdDossier(int id)
    {
        idDossier=id;
    }


    public static float  tableau[][] = new float[30][4];
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
    public void Facturer(ObservableList<String> listNmc, ObservableList<String> listType){
        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (Statement) conn.createStatement();

            String sql = "select fraisDossier,suivi,transport,immobilisation,manutention,mag120,mag140,mag220,mag240,dechargement20,dechargement40,visite20,visite40,visDouane20,visDouane40,char20,char40,tel,fraisExpertise,accesCamion,magPort,scanner,plombage from prixunitaires ";
            System.out.println(sql);
            ResultSet res;
            stmt = conn.createStatement();
            res = stmt.executeQuery(sql);
            res.next();

            parcourt(listType, listNmc);

            int i = 0;
            for (i = 0; i < 23; i++) {
                tableau[i][2] = res.getFloat(i + 1);
                tableau[i][1] = 1;
                if (i == 5 || i == 8 || i == 10 || i == 12 || i == 14 || i == 16) {
                    tableau[i][0] = nb40;
                    if (i == 10) {
                        tableau[i][1] = nbvisite40;
                        tableau[i][0] = 1;
                    }
                    if (i == 14 || i == 16) {
                        if (DateDemo.period(getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2)) - 30 > 0) {
                            tableau[16][1] = DateDemo.period(getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2)) - 30;
                            tableau[14][1] = 30;
                        } else {
                            tableau[14][1] = DateDemo.period(getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2));
                            tableau[16][1] = 0;
                        }
                    }
                } else {
                    if (i == 6 || i == 9 || i == 11 || i == 13 || i == 15 || i == 17) {
                        tableau[i][0] = nb20;
                        if (i == 11) {
                            tableau[i][1] = nbvisite20;
                            tableau[i][0] = 1;
                        }
                        if (i == 15 || i == 17) {
                            if (DateDemo.period(getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2)) - 30 > 0) {
                                tableau[17][1] = DateDemo.period(getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2)) - 30;
                                tableau[15][1] = 30;
                            } else {
                                tableau[15][1] = DateDemo.period(getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2));
                                tableau[17][1] = 0;
                            }
                        }

                        if (i == 15) {
                            tableau[i][1] = DateDemo.period(getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2));
                        }
                        if (i == 17) {
                            if (DateDemo.period(getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2)) - 30 > 0) {
                                tableau[i][1] = DateDemo.period(getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2)) - 30;
                            } else {
                                tableau[i][1] = 0;
                            }
                        }
                    } else {
                        if (i == 0) {
                            tableau[1][0] = 1;
                        } else {
                            tableau[i][0] = listType.size();
                        }
                    }
                }

            }

            String sql1 = "select * from conteneur WHERE idDossier =" + "'" + idDossier + "'" + "AND sortieAutorise = '1'";
            System.out.println(sql1);
            ResultSet res1;
            stmt = conn.createStatement();
            res1 = stmt.executeQuery(sql1);
            if (res1.next()) {
                System.out.println("9iw" + res1.getInt(3));
                tableau[0][0] = 0;
            } else {
                System.out.println("ta3 ki ydir ");
                tableau[0][0] = 1;
            }
            i=0;
            while (i < listType.size())
            {stmt.executeUpdate("update conteneur set sortieAutorise='1' where NMC='" + listNmc.get(i) + "'");
                i++;
            }


            for (i = 0; i < 21; i++) {
                tableau[i][3] = tableau[i][2] * tableau[i][1] * tableau[i][0];
                System.out.println(tableau[i][3]);
                somme += tableau[i][3];
            }
            tableau[21][3] = tableau[21][2];
            tableau[23][3] = tableau[23][2];
            tableau[22][3] = tableau[22][2]* tableau[i][0];

            somme1 = somme * 0.17;
            somme2 = tableau[21][3] + tableau[22][3] + tableau[23][3];
            somme3 = somme + somme1+ somme2 ;

            System.out.println("Facture effectuée. Le montant total est " + somme3);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


            stmt.executeUpdate("insert into facture(ot,datefact) values('"+somme3+"','"+formatter.format(new Date())+"')");

            ResultSet set2=stmt.executeQuery("select idClient from dossier where idDossier='"+Integer.parseInt(DossierController.idDossierCourant)+"'");
            set2.next();
            int idC=set2.getInt("idClient");
            ResultSet set3=stmt.executeQuery("select partRevenu from client where idClient='"+idC+"'");
            set3.next();
            double ss=set3.getDouble("partRevenu")+somme3;
            stmt.executeUpdate("update client set partRevenu='"+ss+"' where idClient='"+idC+"'");
        }
        catch (SQLException e) {
            System.out.println("Exceeeeptttiiiiiiiiiiiiiion");
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getId()
    {
        return idDossier;
    }

    public int getNb40()
    {
        return nb40;
    }
    public int getNb20()
    {
        return nb20;
    }


    public ObservableList<String> getListNmc () {
        ObservableList<String> listNmc = FXCollections.observableArrayList();
        int i= 0;
        while (i<5)
        {
            String nmc ;
            nmc = new String(String.valueOf(i));
            listNmc.add(nmc);
            i++;
        }
        return listNmc;
    }

    public ObservableList<String> getListType () {
        ObservableList<String> listType = FXCollections.observableArrayList();
        int i= 0;
        while (i<5)
        {
            String type = new String();
            if (i % 3 == 1) {type = "40";}
            else {type = "20";}
            listType.add(type);
            i++;
        }
        return listType;
    }
    public void parcourt  (ObservableList<String> listType, ObservableList<String> listNmc) throws Exception
    {
        int i = 0;
        System.out.println(i+""+listType.size());
        while (i< listType.size())
        {
            System.out.println(i+""+listType.get(i));
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (Statement) conn.createStatement();

            String sql = "select nbVisite from conteneur WHERE NMC ="+"'"+ listNmc.get(i) +"'"+"";

            System.out.println(sql);
            ResultSet res;
            stmt = conn.createStatement();
            res = stmt.executeQuery(sql);
            System.out.println("........................"+i);
            if (listType.get(i) == "40")
            {
                nb40++;
                if (res.next())
                {
                    nbvisite40= nbvisite40+res.getInt(1);}
                else
                {/*nbvisite40++;*/}



            } else
            {
                nb20++;
                if ( res.next() )
                {
                    nbvisite20=nbvisite20+res.getInt(1);}
                else
                {/*nbvisite20++;*/}
            }

            i++;
        }

    }

    private int nb20 (ObservableList<String> listType)
    {
        int i = 0, nombre = 0;
        while (i< listType.size())
        {    if (listType.get(i) == "20")
        {
            nombre ++;
        }
            i++;}
        return nombre;

    }


    public static class TableRow{
        private int vol_qt = -1;
        private int nbre = -1;
        private float prix_unit = 0;
        private float montant_ht = 0;
        /*private float debours = -1;
        private String observation;*/

        public TableRow(int vol_qt, int nbre, float prix_unit, float montant_ht/*, float debours,
                    String observation*/){
            this.vol_qt = vol_qt;
            this.nbre = nbre;
            this.prix_unit = prix_unit;
            this.montant_ht = montant_ht;
            /*this.debours = debours;
            this.observation = observation;*/
        }

        public String getVolQt(){
            if(vol_qt > 0){
                return String.valueOf(vol_qt);
            }
            else{
                return "";
            }
        }

        public String getNbre(){
            if(nbre > 0){
                return String.valueOf(nbre);
            }
            else{
                return "";
            }
        }

        public String getPrixUnit(){
            if(prix_unit > 0){
                return String.valueOf(prix_unit);
            }
            else{
                return "";
            }
        }

        public String getMontantHT(){
            if(montant_ht > 0){
                return String.valueOf(montant_ht);
            }
            else{
                return "";
            }
        }

        /*public String getDebours(){
            if(debours > 0){
                return String.valueOf(debours);
            }
            else{
                return "";
            }
        }

        public String getObserv(){
            return observation;
        }*/
    }


    public static void RemplirFactureOT(String modelName, String destination, String[] keys,
                                        String[] values, TableRow[] t, boolean openFile){
        final String folderLocation = "src\\sample\\Models\\";
        try{
            XWPFDocument doc = new XWPFDocument(new FileInputStream(folderLocation+modelName+".docx"));

            //Remplissage des champs
            //champs libres
            int curKey = 0;
            for (XWPFParagraph p : doc.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                System.out.println(p.getText());

                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if(text != null && curKey < keys.length){
                            for(int i = 0;i<keys.length;i++){
                                if(text.contains("{$"+keys[i]+"}")){
                                    text = text.replace("{$"+keys[i]+"}", values[i]);
                                    r.setText(text, 0);
                                    System.out.println("found key : "+curKey);
                                    curKey++;
                                }
                            }
                        }
                    }
                }
            }
            //champs des tableaux
            List<XWPFTable> tables = doc.getTables();
            if(tables == null || tables.size() == 0)
            {
                System.out.println("Pas de tableau dans le fichier "+modelName);
                return;
            }
            List<XWPFTableRow> rows = tables.get(0).getRows();
            if(rows == null || rows.size() < 1)
            {
                System.out.println("Le tableau du fichier "+modelName+" ne peut pas etre rempli");
                return;
            }
            for(XWPFParagraph p : rows.get(0).getCell(0).getParagraphs()){
                List<XWPFRun> runs = p.getRuns();
                System.out.println(p.getText());

                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if(text != null && curKey < keys.length){
                            for(int i = 0;i<keys.length;i++){
                                if(text.contains("{$"+keys[i]+"}")){
                                    text = text.replace("{$"+keys[i]+"}", values[i]);
                                    r.setText(text, 0);
                                    System.out.println("found key : "+curKey);
                                    curKey++;
                                }
                            }
                        }
                    }
                }
            }

            rows = tables.get(1).getRows();
            int i = 0;
            for(XWPFTableRow row : rows){
                if(i == 0 || i > 24){i++;continue;}
                XWPFRun r;

                System.out.println(i);

                r = row.getCell(1).getParagraphs().get(0).createRun();
                r.setText(t[i-1].getVolQt());
                System.out.println(t[i-1].getVolQt());

                r = row.getCell(2).getParagraphs().get(0).createRun();
                r.setText(t[i-1].getNbre());
                System.out.println(t[i-1].getNbre());

                r = row.getCell(3).getParagraphs().get(0).createRun();
                r.setText(t[i-1].getPrixUnit());
                System.out.println(t[i-1].getPrixUnit());

                r = row.getCell(4).getParagraphs().get(0).createRun();
                r.setText(t[i-1].getMontantHT());
                System.out.println(t[i-1].getMontantHT());

                /*r = row.getCell(5).getParagraphs().get(0).createRun();
                r.setText(t[i-1].getDebours());

                r = row.getCell(6).getParagraphs().get(0).createRun();
                r.setText(t[i-1].getObserv());*/

                i++;
            }

            float[] payments = new float[]{(float)somme,(float)somme1,0,(float)somme2,(float)somme3};
            for(i = 0;i<5;i++){
                XWPFRun r = rows.get(25 + i).getCell(2).getParagraphs().get(0).createRun();
                r.setText(String.valueOf(payments[i]));
            }

            doc.write(new FileOutputStream(folderLocation+destination+".docx"));
            doc.close();
            if(openFile){
                File f = new File(folderLocation+destination+".docx");
                Desktop.getDesktop().open(f);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float[][] getTableau(){return tableau ;}
    public  ResultSet getResultat(int iddossier) throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
        conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
        stmt = (Statement) conn.createStatement();
        String sql = "select typee,dateEntree,nbConteneur from dossier WHERE idDossier = "+ iddossier +"";
        System.out.println(sql);
        stmt = conn.createStatement();
        ResultSet res;
        res = stmt.executeQuery(sql);
        res.next();
        return res;
    }
}
