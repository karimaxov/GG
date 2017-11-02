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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class FactureDstrController implements Initializable {

    Connection conn = null;
    Statement stmt = null;

    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";



    static float tableau[][] = new float[11][4];

    public void initialize(URL location, ResourceBundle resources) {


    }
    public void Facturer(ObservableList listType,ObservableList listNmc){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (Statement) conn.createStatement();


            // Récupération des prix unitaires

            String sql = "select fraisDossier,dechargement20,visDouane20,char20,mag120,plombage from prixunitaires ";
            System.out.println(sql);
            ResultSet res;
            stmt = conn.createStatement();
            res = stmt.executeQuery(sql);
            res.next();


            // *****  Parcours des listes *****

            parcourt(listType, listNmc);

            // *****  Initialisation des champs du tableau *****

            int i = 0;
            for (i = 0; i < 6; i++) {
                tableau[i][2] = res.getFloat(i + 1);


                if (i == 0) tableau[i][0] = 1;
                else tableau[i][0] = nb40 + nb20;

                if (i == 0) tableau[i][1] = 1;
                else if (i == 4) tableau[i][1] = DateDemo.period(getResultat(idDossier).getDate(2));
                else if (i == 2 ) tableau[i][1] = nbVisite20 + nbVisite40;
                else tableau[i][1] = 0;


            }
            String sql1 = "select * from conteneur WHERE idDossier =" + "'" + idDossier + "'";
            System.out.println(sql1);
            ResultSet res1;
            stmt = conn.createStatement();
            res1 = stmt.executeQuery(sql1);
            if (res1.next()) {
                System.out.println( res1.getInt(3));
                tableau[0][0] = 0;
            } else {
                tableau[0][0] = 1;
            }
            i = 0;
            while (i < listType.size()) {
                stmt.executeUpdate("update conteneur set sortieAutorise='1' where NMC='" + listNmc.get(i) + "'");
                i++;
            }

            tableau[0][3] = tableau[0][2];
            tableau[1][3] = tableau[1][2] * tableau[1][0];
            tableau[2][3] = tableau[2][2] * tableau[2][0];
            tableau[3][3] = tableau[3][2] * tableau[3][0];
            tableau[4][3] = tableau[4][2] * tableau[4][0] * tableau[4][1];
            tableau[5][3] = tableau[5][2] * tableau[5][0];
            float somme = 0.f;
            for (i = 0; i < 6; i++) {
                sommeHt += tableau[i][3];
            }
            tva = sommeHt * 0.17f;
            timbre = tva * 0.01f;
            montantTotal = sommeHt + tva + timbre;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


            stmt.executeUpdate("insert into facture(ot,datefact) values('" + montantTotal + "','" + formatter.format(new Date()) + "')");

            ResultSet set2 = stmt.executeQuery("select idClient from dossier where idDossier='" + idDossier + "'");
            set2.next();
            int idC = set2.getInt("idClient");
            ResultSet set3 = stmt.executeQuery("select partRevenu from client where idClient='" + idC + "'");
            set3.next();
            double ss = set3.getDouble("partRevenu") + montantTotal;
            stmt.executeUpdate("update client set partRevenu='" + ss + "' where idClient='" + idC + "'");

        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float sommeHt, montantTotal, tva, timbre;
    public static int nb20, nb40, nbVisite20, nbVisite40=1;
    public static int idDossier=Integer.parseInt(DossierController.idDossierCourant);

    public static class TableRow {
        private int vol_qt = -1;
        private int nbre = -1;
        private float prix_unit = 0;
        private float montant_ht = 0;
        /*private float debours = -1;
        private String observation;*/

        public TableRow(int vol_qt, int nbre, float prix_unit, float montant_ht/*, float debours,
                    String observation*/) {
            this.vol_qt = vol_qt;
            this.nbre = nbre;
            this.prix_unit = prix_unit;
            this.montant_ht = montant_ht;
            /*this.debours = debours;
            this.observation = observation;*/
        }

        public String getVolQt() {
            if (vol_qt > 0) {
                return String.valueOf(vol_qt);
            } else {
                return "";
            }
        }

        public String getNbre() {
            if (nbre > 0) {
                return String.valueOf(nbre);
            } else {
                return "";
            }
        }

        public String getPrixUnit() {
            if (prix_unit > 0) {
                return String.valueOf(prix_unit);
            } else {
                return "";
            }
        }

        public String getMontantHT() {
            if (montant_ht > 0) {
                return String.valueOf(montant_ht);
            } else {
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


    public void parcourt(ObservableList<String> listType, ObservableList<String> listNmc) throws Exception {
        int i = 0;
        int nb40 = 0, nb20 = 0, nbvisit40 = 0, nbvisit20 = 0;


        System.out.println(i + "" + listType.size());
        while (i < listType.size()) {
            System.out.println(i + "" + listType.get(i));
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (Statement) conn.createStatement();

            String sql = "select nbVisite from conteneur WHERE NMC =" + "'" + listNmc.get(i) + "'" + "";
            System.out.println(sql);
            ResultSet res;
            stmt = conn.createStatement();
            res = stmt.executeQuery(sql);
            if (listType.get(i) == "40") {
                nb40++;
                if (res.next()) {
                    nbvisit40 = nbvisit40 + res.getInt(1);
                }


            } else {
                nb20++;
                if (res.next()) {
                    nbvisit20 = nbvisit20 + res.getInt(1);
                }
            }
            i++;
        }
        //System.out.println("heda houwa nbr visite: 20_"+nbvisite20+" 40_"+nbvisite40);
        this.nb20 = nb20;
        this.nb40 = nb40;
        this.nbVisite20 = nbvisit20;
        nbVisite40 = nbvisit40;

    }

    public static void RemplirFactureDSTR(String modelName, String destination, String[] keys,
                                          String[] values, TableRow[] t,
                                          float[] payments, boolean openFile) throws Exception {
        final String folderLocation = "src\\sample\\Models\\";

        SimpleDateFormat year = new SimpleDateFormat ("yy");
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");

        values[0] = Integer.toString(idDossier)+"/"+year.format(new Date());
        values[4] = ( nb20 == 0) ? "40''" : ( ( nb40 == 0) ? "20''" : "20'' et 40''" ) ;



        t = new FactureDstrController.TableRow[]{


                new FactureDstrController.TableRow((int) tableau[0][0], (int) tableau[0][1], tableau[0][2], tableau[0][3]),//frais const dossier
                new FactureDstrController.TableRow((int) tableau[1][0], (int) tableau[1][1], tableau[1][2], tableau[1][3]),//
                new FactureDstrController.TableRow((int) tableau[2][0], (int) tableau[2][1], tableau[2][2], tableau[2][3]),//
                new FactureDstrController.TableRow((int) tableau[3][0], (int) tableau[3][1], tableau[3][2], tableau[3][3]),//
                new FactureDstrController.TableRow((int) tableau[4][0], (int) tableau[4][1], tableau[4][2], tableau[4][3]),
                new FactureDstrController.TableRow((int) tableau[5][0], (int) tableau[5][1], tableau[5][2], tableau[5][3]),


        };

        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(folderLocation + modelName + ".docx"));

            //Remplissage des champs
            //champs libres
            int curKey = 0;
            for (XWPFParagraph p : doc.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                System.out.println(p.getText() + '\n');

                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        System.out.println(text);
                        if (text != null && curKey < keys.length) {
                            for (int i = 0; i < keys.length; i++) {
                                if (text.contains("{$" + keys[i] + "}")) {
                                    text = text.replace("{$" + keys[i] + "}", values[i]);
                                    r.setText(text, 0);
                                    System.out.println("found key : " + curKey);
                                    curKey++;
                                }
                            }
                        }
                    }
                }
            }
            //champs des tableaux
            List<XWPFTable> tables = doc.getTables();
            if (tables == null || tables.size() == 0) {
                System.out.println("Pas de tableau dans le fichier " + modelName);
                return;
            }
            List<XWPFTableRow> rows = tables.get(0).getRows();
            if (rows == null || rows.size() < 1) {
                System.out.println("Le tableau du fichier " + modelName + " ne peut pas etre rempli");
                return;
            }
            for (XWPFParagraph p : rows.get(0).getCell(0).getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                System.out.println(p.getText() + '\n');

                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        System.out.println(text);
                        if (text != null && curKey < keys.length) {
                            for (int i = 0; i < keys.length; i++) {
                                if (text.contains("{$" + keys[i] + "}")) {
                                    text = text.replace("{$" + keys[i] + "}", values[i]);
                                    r.setText(text, 0);
                                    System.out.println("found key : " + curKey);
                                    curKey++;
                                }
                            }
                        }
                    }
                }
            }

            rows = tables.get(1).getRows();
            int i = 0;
            for (XWPFTableRow row : rows) {
                if (i == 0 || i > 6) {
                    i++;
                    continue;
                }
                XWPFRun r;

                System.out.println(i);

                r = row.getCell(1).getParagraphs().get(0).createRun();
                r.setText(t[i - 1].getVolQt());

                r = row.getCell(2).getParagraphs().get(0).createRun();
                r.setText(t[i - 1].getNbre());

                r = row.getCell(3).getParagraphs().get(0).createRun();
                r.setText(t[i - 1].getPrixUnit());

                r = row.getCell(4).getParagraphs().get(0).createRun();
                r.setText(t[i - 1].getMontantHT());

                i++;
            }

            // *********** CALCUL DES TOTAUX ********************

            float[] paie = new float[4];

            for (int j = 0; j < 6; j++) paie[0] += tableau[j][3];
            paie[1] = paie[0] * 0.17f;
            paie[2] = (paie[1] + paie[0]) * 0.01f;
            paie[3] = paie[0] + paie[1] + paie[2];
            montantTotal = paie[3];
            for (i = 0; i < 4; i++) {
                XWPFRun r = rows.get(7 + i).getCell(2).getParagraphs().get(0).createRun();
                r.setText(String.valueOf(paie[i]));
            }

            doc.write(new FileOutputStream(folderLocation + destination + ".docx"));
            doc.close();
            if (openFile) {
                File f = new File(folderLocation + destination + ".docx");
                Desktop.getDesktop().open(f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float[][] getTableau() {
        return tableau;
    }

    public ResultSet getResultat(int idDossier) throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
        stmt = (Statement) conn.createStatement();
        String sql = "select typee,dateEntree,nbConteneur from dossier WHERE idDossier = " + idDossier + "";
        stmt = conn.createStatement();
        ResultSet res;
        res = stmt.executeQuery(sql);
        res.next();
        return res;
    }

    public static ObservableList<String> getListNmc() {
        ObservableList<String> listNmc = FXCollections.observableArrayList();
        int i = 0;
        while (i < 5) {
            String nmc;
            nmc = new String(String.valueOf(i));
            listNmc.add(nmc);
            i++;
        }
        return listNmc;
    }



    public static ObservableList<String> getListType() {
        ObservableList<String> listType = FXCollections.observableArrayList();
        int i = 0;
        while (i < 5) {
            String type = new String();
            if (i % 3 == 1) {
                type = "40";
            } else {
                type = "20";
            }
            listType.add(type);
            i++;
        }
        return listType;
    }

    public static int getIdDossier(){return idDossier ;}
}