/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class ChatSceneController implements Initializable {

    @FXML
    private BorderPane chatBorderPane;
    @FXML
    private ImageView imgHome;
    @FXML
    private Label homeLabel;
    @FXML
    private ImageView logout;
    @FXML
    private ListView<String> listview;
    @FXML
    private Pane content;
    @FXML
    private VBox chatBox;
    @FXML
    private Label labelFriendName;
    @FXML
    private Image clips;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
            ObservableList<String> data = FXCollections.observableArrayList(
                "Roma attia", "Mustafa Ismail", "Roma attia", "Mustafa Ismail"
        );
              listview.setItems(data);
              listview.setCellFactory(listView -> new ListCell<String>() {
            private final ImageView imageView = new ImageView();

            @Override
            public void updateItem(String friend, boolean empty) {
                super.updateItem(friend, empty);
                if (friend != null) {
                    Image image = new Image("/resouces/user.png", true);
                    imageView.setImage(image);
                    imageView.setFitWidth(35);
                    imageView.setFitHeight(35);
                    setText(friend);
                    setGraphic(imageView);
                }
            }
        });
        listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
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

    @FXML
    private void homeAction(MouseEvent event) {
          try {
            content.getChildren().clear();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("HomeBox.fxml")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
