package sample.Controllers;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;


/**
 * Created by Karim on 04/04/2017.
 */
public class Stat {


    Connection conn = null;
    Statement stmt =null;
    // JDBC driver name and database URL
    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";


    public double[] aporterDonnee(int annee){

            double tab[]=new double[12];
        try {
            for(int i=0;i<12;i++){
                tab[i]=0;
            }
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (Statement) conn.createStatement();
            ResultSet set=stmt.executeQuery("select * from facture");
            while(set.next()){


                java.util.Date date=parseDate(set.getDate("datefact").toString());
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int year  = localDate.getYear();
                int month = localDate.getMonthValue();

                if(year==annee){
                    System.out.println(year);
                    double dstr=set.getDouble("drph");
                    double ot=set.getDouble("ot");
                    tab[month-1]+=dstr+ot;
                }

            }




        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
                    return tab;
    }
    public  java.util.Date parseDate(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(date);}
        catch (ParseException e)
        {
            return null;
        }
    }

    public int[] compterDossierTT() {
        int type[] =new int[2];

        try {
            for(int i=0;i<2;i++){
                type[i]=0;
            }
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (Statement) conn.createStatement();
            ResultSet set=stmt.executeQuery("select typee from dossier");
            while(set.next()){
            String t=set.getString("typee");
            System.out.println(t);
            if(t.toUpperCase().equals("DSTR"))type[0]++;
            else if(t.toUpperCase().equals("OT"))type[1]++;

            }




        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

     return type;
    }

    public int[] compterDossierAnnee(int annee)
    {



        int tab[]=new int[2];
        try {
            for(int i=0;i<2;i++){
                tab[i]=0;
            }
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (Statement) conn.createStatement();
            ResultSet set=stmt.executeQuery("select typee,dateEntree from dossier");
            while(set.next()){


                java.util.Date date=parseDate(set.getDate("dateEntree").toString());
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int year  = localDate.getYear();

                if(year==annee){
                    String t=set.getString("typee");
                    if(t.toUpperCase().equals("DSTR"))tab[0]++;
                    else if(t.toUpperCase().equals("OT"))tab[1]++;

                }

            }




        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tab;
    }


    public double[] apporterDonneePeriode(int debut,int fin)
    {
        int taille=fin-debut+1;
        if(taille<=0)return null;
        double tab[] =new double[taille];


        try {
            for(int i=0;i<taille;i++){
                tab[i]=0;
            }
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (Statement) conn.createStatement();


                ResultSet set = stmt.executeQuery("select * from facture");
                while (set.next()) {


                    java.util.Date date = parseDate(set.getDate("datefact").toString());
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int year = localDate.getYear();
                    // int month = localDate.getMonthValue();

                    for(int j=0;j<taille;j++) {

                    if (year == debut+j) {

                        double dstr = set.getDouble("drph");
                        double ot = set.getDouble("ot");
                        tab[j] += dstr + ot;
                    }

                }
            }




        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }




        return tab;
    }



	
	public void topTenClient(String[] clientt,double[] revenuu){

        try {
            for(int i=0;i<10;i++){
                clientt[i]="";
                revenuu[i]=0;
            }
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(DB_CNX.getDbUrl(), DB_CNX.getUSER(), DB_CNX.getPASS());
            stmt = (Statement) conn.createStatement();

            ResultSet set=stmt.executeQuery("select * from client order by partRevenu DESC limit 10");
            int i=0;
            while (set.next()){
                clientt[i]=set.getString("nom");
                revenuu[i]=set.getFloat("partRevenu");
                i++;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
