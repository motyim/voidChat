package view;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Button;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;

import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
/**
 * FXML Controller class
 *
 * @author Mostafa
 */
public class ServerViewController implements Initializable {

    @FXML
    private Tab WelcomTap;
    @FXML
    private Tab SendTab;
    @FXML
    private Button SendButton;
    @FXML
    private Button btnToggle;
    @FXML
    private GridPane gridView;
    @FXML
    private PieChart pieChart;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Online", 50),
                 new PieChart.Data("Offline", 30),
                 new PieChart.Data("Busy", 20));
        System.out.println("chart");
        pieChart.setData(data);
        pieChart.setLegendSide(Side.LEFT);
    }
    
    private void addTooltipToChartSlice(PieChart chart){
        double total = 0;
        for(PieChart.Data d : chart.getData()){
            total += d.getPieValue();
        }
        for(PieChart.Data d : chart.getData()){
            Node slice = d.getNode();
            double sliceValue = d.getPieValue();
            double precent = (sliceValue / total)* 100;
            
            String tip = d.getName() + "=" +String.format("%.2f", precent)+ "%";
            
            Tooltip tt = new Tooltip(tip);
            Tooltip.install(slice, tt);
        }
    }

}
