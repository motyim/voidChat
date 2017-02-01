/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


/**
 * FXML Controller class
 *
 * @author Merna
 */
public class ChatSceneController implements Initializable {
    
    
    @FXML
    private ListView<String> listview;

    @FXML
    private ImageView logout;
    
    @FXML
    private Label homeLabel;
  
    @FXML
    private BorderPane chatBorderPane;
    
    @FXML
    private Button homeBtn;
  
     @FXML
     private Pane content;
    
     @FXML
     private Button btnTransferFile;
     
     @FXML
     private ImageView imgHome;
     
     @FXML
     private Label labelFriendName;
     
     
    @FXML
     private void homeAction(MouseEvent event) {
        try {
            content.getChildren().clear(); 
            content.getChildren().add(FXMLLoader.load(getClass().getResource("HomeBox.fxml")));
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
     }
    
     
   
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> data = FXCollections.observableArrayList(
        "Roma attia","Mustafa Ismail","Roma attia","Mustafa Ismail"
        );
        
        listview.setItems(data);
        listview.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    System.out.println("clicked on " + listview.getSelectionModel().getSelectedItem());
                    content.getChildren().clear();
                    content.getChildren().add(FXMLLoader.load(getClass().getResource("ChatBox.fxml")));
                    //labelFriendName.setText(listview.getSelectionModel().getSelectedItem());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }    
    
}
