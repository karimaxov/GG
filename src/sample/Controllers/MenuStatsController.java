/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sample.Controllers;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author pc
 */
public class MenuStatsController {
    
    public enum ChartType{Null, MeilleurClients,OT_DSTR,RevAnnuels,RevMensuels}
    public ChartType graphCourant = ChartType.Null;
    
    @FXML
    private JFXTextField debut_champs;

    @FXML
    private Label fin_label;

    @FXML
    private JFXTextField fin_champs;

    @FXML
    private StackPane chartPane;

    @FXML
    private void initialize() {
        debut_champs.setText("2010");
        fin_champs.setText("2015");
        graphCourant=ChartType.MeilleurClients;
        AfficherChart();
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
    void Afficher_MeilleurClient(ActionEvent event) {
        graphCourant = ChartType.MeilleurClients;
        AfficherChart();
    }

    @FXML
    void Afficher_OtDstr(ActionEvent event) {
        graphCourant = ChartType.OT_DSTR;
        AfficherChart();
    }

    @FXML
    void Afficher_RevAnn(ActionEvent event) {
        graphCourant = ChartType.RevAnnuels;
        AfficherChart();
    }

    @FXML
    void Afficher_RevMens(ActionEvent event) {
        graphCourant = ChartType.RevMensuels;
        AfficherChart();
    }
    
    @FXML
    void RafraichirAnnee(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            AfficherChart();
        }
    }
    
    private void AfficherChart(){
        chartPane.getChildren().clear();
        
        int debut = 0;
        int fin = 0;
        
        try{
            debut = Integer.parseInt(debut_champs.getText());
        }
        catch(Exception e){
            e.printStackTrace();
            debut_champs.setText("2010");
        }
        
        if(graphCourant == ChartType.RevAnnuels){
            try{
                fin = Integer.parseInt(fin_champs.getText());
            }
            catch(Exception e){
                e.printStackTrace();
                fin_champs.setText("2015");
            }
        }
        Stat s=new Stat();
        switch(graphCourant){
            case Null:
                
                break;
            case MeilleurClients:
                //ekteb hna ya karim

                String[] noms = new String[10];
                double[] revenues = new double[10];

                s.topTenClient(noms,revenues);



                //hna
                //hna
                //ekteb lfou9 bark
                Statistics.Show_BestClients(chartPane, noms, revenues);
                ActiverDoubleDate(false);
                break;
            case OT_DSTR:
                //ekteb hna ya karim
                int[] t=s.compterDossierAnnee(debut);
                System.out.println(t[0]+" "+t[1]);
                int nbOT =t[1];
                int nbDSTR = t[0];
                //hna
                //hna
                //ekteb lfou9 bark
                Statistics.Show_OT_DSTR_Pie(chartPane, nbOT, nbDSTR);
                ActiverDoubleDate(false);
                break;
            case RevAnnuels:
                if(fin <= debut){
                    fin = debut + 1;
                    fin_champs.setText(String.valueOf(fin));
                }
                
                //ekteb hna ya karim
                double[] revenuesAnn = new double[fin-debut+1];
                revenuesAnn=s.apporterDonneePeriode(debut,fin);
                //hna
                //hna
                //ekteb lfou9 bark
                
                Statistics.Show_YearsRevenues(chartPane, debut, revenuesAnn);
                ActiverDoubleDate(true);
                break;
            case RevMensuels:
                //ekteb hna ya karim
                double[] revenuesMens = new double[12];
                revenuesMens=s.aporterDonnee(debut);
                //hna
                //hna
                //ekteb lfou9 bark
                Statistics.Show_MonthsRevenues(chartPane, debut, revenuesMens);
                ActiverDoubleDate(false);
                break;
        }
    }
    
    private void ActiverDoubleDate(boolean activer){
        fin_champs.setDisable(!activer);
        fin_label.setDisable(!activer);
    }
}
