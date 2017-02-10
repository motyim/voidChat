
package view;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Button;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;

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
    private ToggleButton start;
    
    ServerView serverView; 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        serverView = ServerView.getInstance();
        serverView.setServerViewController(this);
    }
    
    
     @FXML
    private void ToggleButtonAction(ActionEvent event) {
         System.out.println("serverViewController");
        
        if(start.isSelected()){
              System.out.println("serverViewController start");
              start.setText("Stop");
              serverView.startServer();
        }
          
        else{
            start.setText("Start");
            System.out.println("serverViewController stop");
            serverView.stopServer();
        }
        
    }

}

