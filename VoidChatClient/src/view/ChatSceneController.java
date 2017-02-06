package view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
    private ImageView imgUser;
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
    private Tab requestsTab;
    @FXML
    private MenuButton menuBntStatus;
    @FXML
    private ComboBox comboBoxStatus;
    @FXML
    private AnchorPane friendsPane;

    ObservableList<String> statusList = FXCollections.observableArrayList("online", "offline", "busy");

    private ClientView clinetView;

    public ChatSceneController() {
        //get instance form view
        clinetView = ClientView.getInstance();
        System.out.println("chat connect Client view");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updatePageInfo();

        comboBoxStatus.setItems(statusList);

        try {
            content.getChildren().clear();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("HomeBox.fxml")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ArrayList<User> contacts = clinetView.getContacts();

        //check not empty contact list
        if (contacts != null) {
            ArrayList<String> contactsName = new ArrayList<>();
            for (User contact : contacts) {
                contactsName.add(contact.getUsername());
            }
            ObservableList<String> data = FXCollections.observableArrayList(contactsName);
            friendsListview.setItems(data);
        } else {
//            VBox hbox= new VBox();
//            hbox.setPrefHeight(200);
//            hbox.setPrefWidth(200);
//            friendsPane.getChildren().clear();
//            Label label=new Label("Add New Friends");
//            ImageView imgView=new ImageView(new Image("resouces/circle.png",20,20,false,false));
//            hbox.getChildren().addAll(label,imgView);
//            hbox.setAlignment(Pos.CENTER);
//            friendsPane.getChildren().add(hbox);
        }

        friendsListview.setCellFactory(listView -> new ListCell<String>() {

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


        ArrayList<String> requestsArrayList = clinetView.checkRequest();

        if (requestsArrayList != null) {
            requestsTab.setDisable(false);
            ObservableList<String> requestsList = FXCollections.observableArrayList(requestsArrayList);
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
        } else {
            requestsTab.setDisable(true);
        }
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
        try {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            Parent parent = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle(" Sin_in Page");
            stage.show();
            clinetView.logout();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //updateContactsList();

    }


    

   /* private void updateContactsList() {
     //get requests form database
        ArrayList<String> requests = clinetView.checkRequest();
        
        if (requests != null) {
            ObservableList<String> requestsList = FXCollections.observableArrayList(requests);
            requestsListview.setItems(requestsList);
        }

        
        
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

    }*/

    public void updatePageInfo() {
        User user = clinetView.getUserInformation();
        homeLabel.setText(user.getUsername());
        comboBoxStatus.setValue("online");
        if (user.getGender().equals("Female")) {
            imgUser.setImage(new Image("/resouces/female.png"));
        }
    }

}
