/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.Controllers;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author pc
 */
public class Statistics {
    
    private static ObservableList<PieChart.Data> pieChartData;
    
    public static void Show_OT_DSTR_Pie(StackPane pane, int ot, int dstr) {
        pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("OT ("+ot+")", ot),
                new PieChart.Data("DSTR ("+dstr+")", dstr));
        
        
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("OT/DSTR");
        chart.setLabelLineLength(10);
        chart.setLegendSide(Side.LEFT);
        pane.getChildren().add(chart);
        
        Set_OT_DSTR_Style();
    }
    public static void Set_OT_DSTR_Style(){
        pieChartData.get(0).getNode().setStyle("-fx-pie-color: #f2c54b;");
        pieChartData.get(1).getNode().setStyle("-fx-pie-color: #dc5455;");
    }
    
    public static void Show_MonthsRevenues(StackPane pane, int annee, double[] revenues) {
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        //creating the chart
        final LineChart<String,Number> lineChart = 
                new LineChart<String,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Revenues d'année "+annee);
        
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        lineChart.setAlternativeRowFillVisible(false);
        lineChart.setAlternativeColumnFillVisible(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.getXAxis().setVisible(false);
        lineChart.getYAxis().setVisible(false);
        
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Revenues");
        //populating the series with data
        
        final String[] mois = new String[]{"Jan","Fev","Mar","Avr","Mai","Jun","Jul","Aou","Sep","Oct","Nov","Dec"};
        
        for(int i = 0;i<revenues.length;i++){
            series.getData().add(new XYChart.Data(mois[i], revenues[i]));
        }
        
        
        lineChart.getData().add(series);
        pane.getChildren().add(lineChart);
    }
    
    public static void Show_YearsRevenues(StackPane pane, int anneeDebut, double[] revenues) {
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Années");
        //creating the chart
        final LineChart<String,Number> lineChart = 
                new LineChart<String,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Revenues des années "+anneeDebut + "-" + (anneeDebut+revenues.length-1));
        
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        lineChart.setAlternativeRowFillVisible(false);
        lineChart.setAlternativeColumnFillVisible(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.getXAxis().setVisible(false);
        lineChart.getYAxis().setVisible(false);
        
        XYChart.Series series = new XYChart.Series();
        series.setName("Revenues");
        
        for(int i = 0;i<revenues.length;i++){
            series.getData().add(new XYChart.Data(String.valueOf(anneeDebut + i), revenues[i]));
        }
        
        lineChart.getData().add(series);
        pane.getChildren().add(lineChart);
    }
    
    public static void Show_BestClients(StackPane pane, String[] noms, double[] valeurs){
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = 
            new BarChart<String,Number>(xAxis,yAxis);
        bc.setLegendVisible(false);
        bc.setTitle("Meilleurs Clients");
        xAxis.setLabel("Clients");       
        yAxis.setLabel("Revenues");
 
        XYChart.Series series = new XYChart.Series();
        
        for(int i = 0;i<noms.length;i++){
            series.getData().add(new XYChart.Data(noms[i], valeurs[i]));
        }
        
        pane.getChildren().add(bc);
        bc.getData().add(series);
    }
}
