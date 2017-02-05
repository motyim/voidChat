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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private ImageView iconLogout;
    @FXML
    private ListView<String> listview;
    @FXML
    private Pane content;
    @FXML
    private VBox chatBox;
    @FXML
    private Label friendName;
    @FXML
    private Image clips;
    @FXML

    private ClientView clinetView;

    public ChatSceneController() {
        //get instance form view
        clinetView = ClientView.getInstance();
        System.out.println("chat connect Client view");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<String> data = FXCollections.observableArrayList(
                "Roma attia", "Mustafa Ismail", "Roma attia", "Mustafa Ismail"
        );
        listview.setItems(data);
        listview.setCellFactory(listView -> new ListCell<String>() {
            private final ImageView imageView = new ImageView();
            private final ImageView imageViewStatus = new ImageView();

            @Override
            public void updateItem(String friend, boolean empty) {
                super.updateItem(friend, empty);
                if (friend != null) {

                    FlowPane flow = new FlowPane();
                    flow.setHgap(4);

                    Label friendName = new Label();
                    friendName.setText(friend);

                    Image image = new Image("/resouces/user.png", true);
                    Image statusImg = new Image("/resouces/circle.png", true);

                    imageView.setImage(image);
                    imageView.setFitWidth(35);
                    imageView.setFitHeight(35);

                    imageViewStatus.setImage(statusImg);
                    imageViewStatus.setFitWidth(6);
                    imageViewStatus.setFitHeight(6);

                    flow.getChildren().addAll(imageView, friendName, imageViewStatus);

                    setGraphic(flow);

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

    @FXML
    private void iconLogoutAction(MouseEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle(" Sin_in Page");
        stage.show();
       // String username = "zas";
        clinetView.logout();

    }

}
