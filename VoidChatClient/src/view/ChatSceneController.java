/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.User;

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
    private ListView<String> friendsListview;
    @FXML
    private ListView<String> requestsListview;
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

        ArrayList<User> contacts = clinetView.getContacts();

        //check not empty contact list
        if (contacts != null) {
            ArrayList<String> contactsName = new ArrayList<>();
            for (User contact : contacts) {
                contactsName.add(contact.getUsername());
            }
            ObservableList<String> data = FXCollections.observableArrayList(contactsName);
            listview.setItems(data);
        }

        listview.setCellFactory(listView -> new ListCell<String>() {

            private final ImageView imageView = new ImageView();
            private final ImageView imageViewStatus = new ImageView();

            @Override
            public void updateItem(String friend, boolean empty) {
                super.updateItem(friend, empty);
                if (friend != null) {

                    FlowPane flow = new FlowPane();
                    flow.setHgap(4);
                    flow.setPrefWidth(1);

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
        friendsListview.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    System.out.println("clicked on " + friendsListview.getSelectionModel().getSelectedItem());
                    content.getChildren().clear();
                    content.getChildren().add(FXMLLoader.load(getClass().getResource("ChatBox.fxml")));
                    //labelFriendName.setText(listview.getSelectionModel().getSelectedItem());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        ObservableList<String> requestsList = FXCollections.observableArrayList(
                "Sarah Ahmed", "Bosy Ismail"
        );
        requestsListview.setItems(requestsList);
        requestsListview.setCellFactory(listView -> new ListCell<String>() {

            Button btnAccept = new Button();
            Button btnIgnore = new Button();

            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (name != null) {

                    BorderPane pane = new BorderPane();

                    Label labelRequestFrom = new Label();
                    labelRequestFrom.setText(name);

                    btnAccept.setGraphic(new ImageView(new Image("/resouces/accept.png", 9, 9, false, false)));
                    btnAccept.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            System.out.println("Accept :" + getItem());
                        }
                    });
                    btnIgnore.setGraphic(new ImageView(new Image("/resouces/ignore.png", 9, 9, false, false)));
                    btnIgnore.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            System.out.println("Ignore :" + getItem());
                        }
                    });

                    HBox btnHbox = new HBox();

                    btnHbox.getChildren().addAll(btnIgnore, btnAccept);
                    btnHbox.setSpacing(3);
                    pane.setRight(btnHbox);
                    pane.setLeft(labelRequestFrom);
                    setGraphic(pane);

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
    private void iconLogoutAction(MouseEvent event) {
        System.out.println("logout");
    }

}
