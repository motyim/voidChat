package view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Message;
import model.User;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;
import utilitez.Notification;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class ChatSceneController implements Initializable {

    @FXML
    private BorderPane chatBorderPane;
    @FXML
    private ImageView imgUser;
    @FXML
    private Label homeLabel;
    @FXML
    private ImageView iconLogout;
    @FXML
    private ImageView iconCreateGroup;
    @FXML
    private ListView<User> friendsListview;
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
    private Tab homeBox;
    @FXML
    private TabPane tabPane;
    @FXML
    private MenuButton menuBntStatus;
    @FXML
    private ComboBox comboBoxStatus;
    @FXML
    private AnchorPane friendsPane;
    @FXML
    private SplitPane splitPane;
    @FXML
    private VBox leftPane;

    Map<String, Tab> tabsOpened = new HashMap<>();
    Map<String, ChatBoxController> tabsControllers = new HashMap<>();

    ObservableList<String> statusList = FXCollections.observableArrayList("online", "offline", "busy");

    private ClientView clinetView;

    public ChatSceneController() {
        //get instance form view
        clinetView = ClientView.getInstance();
        System.out.println("chat connect Client view");
        clinetView.setChatSceneController(this);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        updatePageInfo();

        // to make spliter disable 
        splitPane.setDividerPositions(0.3246);
        leftPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.3246));
        tabPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.6546));

        comboBoxStatus.setItems(statusList);

        try {
            homeBox.setContent(FXMLLoader.load(getClass().getResource("HomeBox.fxml")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        updateContactsList();
        updateFriendsRequests();

    }

    @FXML
    private void iconLogoutAction(MouseEvent event) {
        try {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            Parent parent = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Signin Page");
            stage.show();
            stage.setOnCloseRequest((WindowEvent ew) -> {
                Platform.exit();
                //TODO : why not close
                System.exit(0);
            });
            clinetView.logout();
            clinetView.changeStatus("offline");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void iconCreateGroupAction(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GroupScene.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Create New Group");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * update friends contact list
     */
    private void updateContactsList() {
        Platform.runLater(() -> {
            ArrayList<User> contacts = clinetView.getContacts();

            //check not empty contact list
            if (contacts != null) {
                System.out.println(">>><<<<" + contacts.size());
                ObservableList<User> data = FXCollections.observableArrayList(contacts);
                friendsListview.setItems(data);
            } else {
//            try {
//                friendsPane.getChildren().clear();
//                friendsPane.getChildren().add(FXMLLoader.load(getClass().getResource("EmptyList.fxml")));
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
            }

            friendsListview.setCellFactory(listView -> new ListCell<User>() {

                private final ImageView imageView = new ImageView();
                private final ImageView imageViewStatus = new ImageView();

                @Override

                public void updateItem(User friend, boolean empty) {
                    super.updateItem(friend, empty);
                    if (empty || friend == null) {
                        setText(null);
                        setGraphic(null);
                    } else {

                        FlowPane flow = new FlowPane();
                        flow.setHgap(4);
                        flow.setPrefWidth(1);

                        Label friendName = new Label();

                        friendName.setText(friend.getUsername());

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
                    
                    String friendName = friendsListview.getSelectionModel().getSelectedItem().getUsername();
                    if (!tabsOpened.containsKey(friendName)) {
                        try {
                            
                            System.out.println("clicked on " + friendName);
                            Tab newTab = new Tab();

                            newTab.setId(friendName);
                            newTab.setText(friendName);

                            newTab.setClosable(true);
                            newTab.setOnCloseRequest(new EventHandler<Event>() {
                                @Override
                                public void handle(Event event) {
                                    System.out.println("?>>" + newTab.getId());
                                    tabsOpened.remove(newTab.getId());
                                    tabsControllers.remove(newTab.getId());
                                }
                            });

                            tabPane.getTabs().add(newTab);
                            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatBox.fxml"));
                            ChatBoxController chatBoxController = new ChatBoxController(friendName); //receiver
                            loader.setController(chatBoxController);

                            tabsOpened.put(friendName, newTab);
                            tabsControllers.put(friendName, chatBoxController);

                            newTab.setContent(loader.load());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        tabPane.getSelectionModel().select(tabsOpened.get(friendName));
                    }

                }
            });
        });
    }

    public void updatePageInfo() {
        User user = clinetView.getUserInformation();
        homeLabel.setText(user.getUsername());
        comboBoxStatus.setValue("online");
        if (user.getGender().equals("Female")) {
            imgUser.setImage(new Image("/resouces/female.png"));
        }
    }

    /**
     * update Friends request from Database
     */
    public void updateFriendsRequests() {
        Platform.runLater(() -> {

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

                        if (empty || name == null) {
                            setText(null);
                            setGraphic(null);
                        } else {

                            BorderPane pane = new BorderPane();

                            Label labelRequestFrom = new Label();
                            labelRequestFrom.setText(name);

                            btnAccept.setGraphic(new ImageView(new Image("/resouces/accept.png", 9, 9, false, false)));
                            btnAccept.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    System.out.println("Accept :" + getItem());
                                    if (clinetView.acceptRequest(getItem())) {
                                        clinetView.showSuccess("Operation Sccuess",
                                                "Friend Added Successfuly",
                                                "you now become friend with " + getItem());

                                        //update requests
                                        updateFriendsRequests();

                                        //update list of friends
                                        updateContactsList();
                                    } else {
                                        clinetView.showError("Error", "you can't add friend right now \n"
                                                + "please try again later ..", "");
                                    }
                                }
                            });
                            btnIgnore.setGraphic(new ImageView(new Image("/resouces/ignore.png", 9, 9, false, false)));
                            btnIgnore.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    System.out.println("Ignore :" + getItem());
                                    clinetView.ignoreRequest(getItem());
                                    updateFriendsRequests();
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
        });
    }

    public void notify(String message, int type) {
        System.out.println("notify in chat controller");
        try {

            switch (type) {
                case Notification.FRIEND_REQUSET:
                    showNotifaction("Friend Request", message, new Image(getClass().getResource("../resouces/add-contact.png").openStream()));
                    updateFriendsRequests();
                    break;
                case Notification.FRIEND_OFFLINE:
                    showNotifaction("Friend Become offline", message, new Image(getClass().getResource("../resouces/add-contact.png").openStream()));
                    updateContactsList();
                    break;
                case Notification.FRIEND_ONLINE:
                    showNotifaction("Friend Become online", message, new Image(getClass().getResource("../resouces/add-contact.png").openStream()));
                    updateContactsList();
                    break;
                case Notification.ACCEPT_FRIEND_REQUEST:
                    showNotifaction("Accept Request", message, new Image(getClass().getResource("../resouces/add-contact.png").openStream()));
                    updateContactsList();
                    break;
                case Notification.SERVER_MESSAGE:
                    showNotifaction("New Announcement", message, new Image(getClass().getResource("../resouces/add-contact.png").openStream()));
                    break;
                case Notification.FRIEND_BUSY:
                    showNotifaction("Friend Become busy", message, new Image(getClass().getResource("../resouces/add-contact.png").openStream()));
                    updateContactsList();

            }

            //TODO change image to require image
        } catch (IOException ex) {
            Logger.getLogger(ChatSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void showNotifaction(String title, String message, Image image) {
        Platform.runLater(() -> {
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setRectangleFill(Paint.valueOf("#bdc3c7"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.setImage(image);
            tray.showAndWait();
        });
    }

    ///////////////////////////////////////////
    public void changeStatus() {
        System.out.println("change status button in chatScene Controller");
        clinetView.changeStatus(comboBoxStatus.getValue().toString());
        System.out.println(comboBoxStatus.getValue().toString());
    }

    /**
     * get message from clientView and open existing tab or create new tab and
     * load new chatBoxScene on it
     *
     * @param message
     * @throws java.io.IOException
     */
    public void reciveMsg(Message message) throws IOException {

        String tabName;
        // message sent to group? open tab (group name) :  open tab(sender name)
        if (message.getTo().contains("##")) {
            String[] groupName = message.getTo().split("##");
            tabName = groupName[1];

        } else {
            tabName = message.getFrom();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!tabsOpened.containsKey(tabName)) {

                        // create new tab
                        Tab newTab = new Tab();
                        newTab.setId(tabName);
                        newTab.setText(tabName);
                        newTab.setOnCloseRequest(new EventHandler<Event>() {
                            @Override
                            public void handle(Event event) {
                                System.out.println("?>>" + newTab.getId());
                                tabsOpened.remove(newTab.getId());
                                tabsControllers.remove(newTab.getId());
                            }
                        });

                        tabPane.getTabs().add(newTab);
                        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

                        // load new chat box    
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatBox.fxml"));
                        ChatBoxController chatBoxController = new ChatBoxController(message);
                        loader.setController(chatBoxController);
                        newTab.setContent(loader.load());
                        chatBoxController.reciveMsg(message);

                        // put the new tab and controller in the map
                        tabsOpened.put(tabName, newTab);
                        tabsControllers.put(tabName, chatBoxController);

                    } else {
                        // tab already exist so open it and pass msg to its controller

                        tabPane.getSelectionModel().select(tabsOpened.get(message.getFrom()));
                        tabsControllers.get(tabName).reciveMsg(message);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void createGroup(String groupName) {

        String[] splitString = groupName.split("##");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!tabsOpened.containsKey(splitString[1])) {

                        // create new tab
                        Tab newTab = new Tab();
                        newTab.setId(splitString[1]);
                        newTab.setText(splitString[1]);
                        newTab.setOnCloseRequest(new EventHandler<Event>() {
                            @Override
                            public void handle(Event event) {
                                System.out.println("?>>" + newTab.getId());
                                tabsOpened.remove(newTab.getId());
                                tabsControllers.remove(newTab.getId());
                            }
                        });

                        tabPane.getTabs().add(newTab);
                        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

                        // load new chat box    
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatBox.fxml"));
                        ChatBoxController chatBoxController = new ChatBoxController(groupName);
                        loader.setController(chatBoxController);
                        newTab.setContent(loader.load());

                        // put the new tab and controller in the map
                        tabsOpened.put(splitString[1], newTab);
                        tabsControllers.put(splitString[1], chatBoxController);

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
