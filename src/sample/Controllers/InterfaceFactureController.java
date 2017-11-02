package sample.Controllers;

import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import static java.awt.Desktop.getDesktop;

/**
 * Created by NS on 30/04/2017.
 */
public class InterfaceFactureController implements Initializable {
    public static Stage factureStage;
    @FXML
    private  JFXCheckBox checkPrint;

    @FXML
    private  JFXCheckBox checkVisualize;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//checkPrint.setSelected(false);
//checkVisualize.setSelected(true);
    }

    @FXML
    void Annuler(ActionEvent event) {
        factureStage.close();
        DossierController.dossierStage.show();
    }

     public void facturer() throws Exception {


        int i;
        for(i=0; i< DossierController.jeuDessaiCont.size(); i++){
            if (DossierController.jeuDessaiCont.get(i).chk.isSelected() && DossierController.jeuDessaiCont.get(i).factureAut.getValue().toString().equals("OK")){
                DossierController.listFactNmc.add(DossierController.jeuDessaiCont.get(i).numCont.getValue().toString());
                DossierController.listFactType.add(DossierController.jeuDessaiCont.get(i).type.getValue().toString());
            }
        }
        if(DossierController.listFactNmc.size()!=0){
            DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy_HH;mm;ss");
            java.util.Date date=new java.util.Date();
            if(DossierController.typeDossierCourant.equals("DSTR")) {
                FactureDstrController c=new FactureDstrController();
                c.Facturer(DossierController.listFactType, DossierController.listFactNmc);
                SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy");
                FactureDstrController.RemplirFactureDSTR("model_facture_dstr","Facture_DSTR_"+dateFormat2.format(date).toString() ,
                        new String[]{"nbDossier", "dateEntree", "nbTC", "dateSortie", "typeTC",
                                "nbJours", "nTC", "gros", "article"},
                        new String[]{DossierController.idDossierCourant+"/"+ft.format(new java.util.Date()).charAt(8)+ft.format(new java.util.Date()).charAt(9),ft.format(c.getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2)),String.valueOf(DossierController.listFactType.size()),ft.format(new java.util.Date()),"40''",
                                Integer.toString(DateDemo.period(c.getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2))),"","57","87"}, new FactureDstrController.TableRow[6],new float[4],checkVisualize.isSelected());

            }
            if(DossierController.typeDossierCourant.equals("OT")) {
                FactureOtController c=new FactureOtController();
                c.Facturer(DossierController.listFactType, DossierController.listFactNmc);
                float tableau[][];
                tableau = FactureOtController.getTableau();
                FactureOtController.TableRow[] tableauLignes = new FactureOtController.TableRow[]
                        {
                                new FactureOtController.TableRow((int) tableau[0][0], (int) tableau[0][1],  tableau[0][2],tableau[0][3]),//frais const dossier
                                new FactureOtController.TableRow((int) tableau[1][0], 0,  tableau[1][2],tableau[1][3]),//suivi transfert
                                new FactureOtController.TableRow((int)tableau[2][0], 0,  tableau[2][2],tableau[2][3]),//manutension
                                new FactureOtController.TableRow((int)tableau[3][0], 0,  tableau[3][2],tableau[3][3]),//transport
                                new FactureOtController.TableRow((int)tableau[4][0], 0,  tableau[4][2],tableau[4][3]),//immobilisation
                                new FactureOtController.TableRow((int)tableau[5][0], 0,  tableau[5][2],tableau[5][3]),//dechargement 40
                                new FactureOtController.TableRow((int)tableau[6][0], 0,  tableau[6][2],tableau[6][3]),//dechargement 20
                                new FactureOtController.TableRow((int)tableau[7][0], 0,  tableau[7][2],tableau[7][3]),//acces camion
                                new FactureOtController.TableRow((int)tableau[8][0], 0,  tableau[8][2],tableau[8][3]),//visite/ferm 40
                                new FactureOtController.TableRow((int)tableau[9][0], 0,  tableau[9][2],tableau[9][3]),//visite/ferm 20
                                new FactureOtController.TableRow(0, (int)tableau[10][1],  tableau[10][2],tableau[10][3]),//visite douane 40
                                new FactureOtController.TableRow(0, (int)tableau[11][1],  tableau[11][2],tableau[11][3]),//visite douane 20
                                new FactureOtController.TableRow((int)tableau[12][0], 0,  tableau[12][2],tableau[12][3]),//chargement 40
                                new FactureOtController.TableRow((int)tableau[13][0], 0,  tableau[13][2],tableau[13][3]),//chargement 20
                                new FactureOtController.TableRow((int)tableau[14][0], (int)tableau[14][1],  tableau[14][2],tableau[14][3]),//mag 30j 40
                                new FactureOtController.TableRow((int)tableau[15][0], (int)tableau[15][1],  tableau[15][2],tableau[15][3]),//mag 30j 20
                                new FactureOtController.TableRow((int)tableau[16][0], (int)tableau[16][1],  tableau[16][2],tableau[16][3]),//mag 31j 40
                                new FactureOtController.TableRow((int)tableau[17][0], (int)tableau[17][1],  tableau[17][2],tableau[17][3]),//mag 31j 20
                                new FactureOtController.TableRow((int)tableau[18][0], 0,  tableau[18][2],tableau[18][3]),//tel
                                new FactureOtController.TableRow((int)tableau[19][0], 0,  tableau[19][2],tableau[19][3]),//frais d'expertise
                                new FactureOtController.TableRow((int)tableau[20][0], 0,  tableau[20][2],tableau[20][3]),//plombage
                                new FactureOtController.TableRow(0, 0,  0,tableau[21][3]),//magasinage portuaire
                                new FactureOtController.TableRow((int)tableau[22][0], 0,  0,tableau[22][3]) ,//scanner
                                new FactureOtController.TableRow(0, 0,  0,tableau[23][3]),//autre frais


                        };


                SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy");
                try {
                    FactureOtController.RemplirFactureOT("model_facture_ot", "Facture_OT_"+dateFormat2.format(date).toString() ,
                            new String[]{"nbDossier", "dateEntree", "nbTC", "dateSortie", "typeTC",
                                    "nbJours", "nTC", "gros", "article"},
                            new String[]{DossierController.idDossierCourant+"/"+ft.format(new java.util.Date()).charAt(8)+ft.format(new java.util.Date()).charAt(9),ft.format(c.getResultat(Integer.parseInt(DossierController.idDossierCourant)).getDate(2)),String.valueOf(DossierController.listFactType.size()),ft.format(new java.util.Date()),"40''",
                                    String.valueOf((int)tableau[14][1]),"","57","87"},
                            tableauLignes,
                            checkVisualize.isSelected());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "-Aucun conteneur sélectionné.\n-Des coonteneurs séléctionnés mais pas autorisés à sortir");
            Toolkit.getDefaultToolkit().beep();
            alert.showAndWait();
        }
        if (checkPrint.isSelected()){
            DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy_HH;mm;ss");
            java.util.Date date=new java.util.Date();
            File file = new File("src\\sample\\Models\\Facture_"+DossierController.typeDossierCourant+"_"+DossierController.idDossierCourant+"/"+dateFormat2.format(date).toString()+".doc");
            try {
                getDesktop().print(file);// printing document
            } catch (Exception e) {
                // error while printing !
                System.out.println("Error while printing ! ... ");
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR,"Aucune imprimante est détéctée ! \nSvp verifiez votre connection avec l'imprimante ");

                Toolkit.getDefaultToolkit().beep();
                alert.showAndWait();

            }
        }

         factureStage.close();
         DossierController.dossierStage.show();
    }
    public void action1(){
        checkVisualize.setSelected(true);
    }

}
