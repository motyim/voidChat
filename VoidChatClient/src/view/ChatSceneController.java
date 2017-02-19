package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import model.Message;
import model.User;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;
import utilitez.Constant;
import utilitez.Notification;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class ChatSceneController implements Initializable {

    @FXML
    private ImageView imgUser;
    @FXML
    private Label homeLabel;
    @FXML
    private ListView<String> requestsListview;
    @FXML
    private Tab requestsTab;
    @FXML
    private Tab homeBox;
    @FXML
    private TabPane tabPane;
    @FXML
    private ComboBox comboBoxStatus;
    @FXML
    private SplitPane splitPane;
    @FXML
    private VBox leftPane;
    //-----merna-----
    @FXML
    private TitledPane titlePaneFriends;

    @FXML
    private ListView<User> aListViewFriends;

    @FXML
    private TitledPane titlePaneFamily;

    @FXML
    private ListView<User> aListViewFamily;
    //------end merna----

    private static boolean falg = false;

    Map<String, Tab> tabsOpened = new HashMap<>();
    Map<String, ChatBoxController> tabsControllers = new HashMap<>();

    ObservableList<String> statusList = FXCollections.observableArrayList("online", "offline", "busy");

    private ClientView clinetView;

    public ChatSceneController() {
        //get instance form view
        clinetView = ClientView.getInstance();
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

        updateFriendsRequests();
        loadAccordionData();

    }

    @FXML
    private void iconLogoutAction(MouseEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        clinetView.getMainStage().show();
        clinetView.logout();
        clinetView.changeStatus("offline");
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
                                    if (clinetView.acceptRequest(getItem())) {
                                        clinetView.showSuccess("Operation Sccuess",
                                                "Friend Added Successfuly",
                                                "you now become friend with " + getItem());

                                        //update requests
                                        updateFriendsRequests();

                                        //update list of friends
                                        loadAccordionData();
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
        try {

            switch (type) {
                case Notification.FRIEND_REQUSET:
                    showNotifaction("Friend Request", message, new Image(getClass().getResource("/resouces/add-contact.png").openStream()));
                    updateFriendsRequests();
                    break;
                case Notification.FRIEND_OFFLINE:
                    showNotifaction("Friend Become offline", message, new Image(getClass().getResource("/resouces/closed.png").openStream()));
                    //updateContactsList();
                    loadAccordionData();
                    break;
                case Notification.FRIEND_ONLINE:
                    showNotifaction("Friend Become online", message, new Image(getClass().getResource("/resouces/open.png").openStream()));
                    //updateContactsList();
                    loadAccordionData();
                    break;
                case Notification.ACCEPT_FRIEND_REQUEST:
                    showNotifaction("Accept Request", message, new Image(getClass().getResource("/resouces/accept.png").openStream()));
                    //updateContactsList();
                    loadAccordionData();
                    break;
                case Notification.SERVER_MESSAGE:
                    showNotifaction("New Announcement", message, new Image(getClass().getResource("/resouces/megaphone.png").openStream()));
                    break;
                case Notification.FRIEND_BUSY:
                    // showNotifaction("Friend Become busy", message, new Image(getClass().getResource("../resouces/add-contact.png").openStream()));      
                    //updateContactsList();
                    loadAccordionData();

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

    public void changeStatus() {
        clinetView.changeStatus(comboBoxStatus.getValue().toString());
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
        String[] groupName = message.getTo().split("##");;
        // message sent to group? open tab (group name) :  open tab(sender name)
        if (message.getTo().contains("##")) {
            ////////////////////
            tabName = message.getTo();
/////////////////////
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

                        if (message.getTo().contains("##")) {
                            newTab.setText(groupName[2]);
                        } else {
                            newTab.setText(tabName);
                        }

                        newTab.setOnCloseRequest(new EventHandler<Event>() {
                            @Override
                            public void handle(Event event) {
                              
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

                        tabPane.getSelectionModel().select(tabsOpened.get(tabName));
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
                    if (!tabsOpened.containsKey(groupName)) {

                        // create new tab
                        Tab newTab = new Tab();
                        newTab.setId(groupName);
                        newTab.setText(splitString[2]);
                        newTab.setOnCloseRequest(new EventHandler<Event>() {
                            @Override
                            public void handle(Event event) {
                                
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
                        tabsOpened.put(groupName, newTab);
                        tabsControllers.put(groupName, chatBoxController);

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public String getSaveLocation(String sender, String filename) {
        try {

            FutureTask saveLocation = new FutureTask(() -> {

                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Recieve File ");
                alert.setHeaderText(sender + " send file to you .. ");
                alert.setContentText("Do you want to download file ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialFileName(filename);
                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(null);

                    return (file != null) ? file.getAbsolutePath() : null;
                } else {
                    return null;
                }

            });

            Platform.runLater(saveLocation);

            return (String) saveLocation.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void loadErrorServer() {
        //----- close this scene -----
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //requestsListview.getScene().getWindow().hide();
                if (!falg) {
                    homeLabel.getScene().getWindow().hide();
                    
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("OutOfServiceScene.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.setTitle(" ");
                        stage.show();
                        stage.setOnCloseRequest((WindowEvent ew) -> {
                            Platform.exit();
                            //TODO : why not close
                            System.exit(0);
                        });
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    falg = true;
                }
            }
        });

    }

    @FXML
    void iconAddNewFriendAction(MouseEvent event) {

        ObservableList<String> options
                = FXCollections.observableArrayList(
                        "Family",
                        "Friends",
                        "Block"
                );

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add New Friend");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField txtFieldUserName = new TextField();
        txtFieldUserName.setPromptText("username");

        ComboBox comboBox = new ComboBox(options);
        comboBox.getSelectionModel().selectFirst();

        grid.add(new Label("User Name :"), 0, 0);
        grid.add(txtFieldUserName, 1, 0);

        grid.add(new Label("Category:"), 0, 1);
        grid.add(comboBox, 1, 1);

//        dialog.getDialogPane().setStyle(" -fx-background-color: #535f85;");        
        dialog.getDialogPane().setContent(grid);

        // Request focus on the txtFieldEmail field by default.
        Platform.runLater(() -> txtFieldUserName.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(txtFieldUserName.getText(), comboBox.getSelectionModel().getSelectedItem().toString());
            }
            return null;
        });

        //Hna H3mal Insert LL Code Bta3e (Send Request)
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(emailCategory -> {
            
            if(emailCategory.getValue().equals("Block")){
                clinetView.sendRequest(emailCategory.getKey(), emailCategory.getValue());
                clinetView.showSuccess("Sccuess", "Blocked", "You block user " + emailCategory.getKey());
                return ;
            }
            
            switch (clinetView.sendRequest(emailCategory.getKey(), emailCategory.getValue())) {
                case Constant.ALREADY_FRIENDS:
                    clinetView.showError("Error", "Can't  Send Requset", "User Already Friend to you..");
                    break;
                case Constant.REQUEST_ALREADY_EXIST:
                    clinetView.showError("Error", "Can't  Send Requset", "you Already send request before "
                            + "\nor have request from this person\nor there is a block relation :( ");
                    break;
                case Constant.USER_NOT_EXIST:
                    clinetView.showError("Error", "Can't  Send Requset", "User Not Exsist in our System");
                    break;
                case Constant.EXCEPTION:
                    clinetView.showError("Error", "Can't  Send Requset", "An error Occure please Contact Admin");
                    break;
                case Constant.SENDED:
                    clinetView.showSuccess("Sccuess", "Requset Sended", "You send request to " + emailCategory.getKey());
                    break;
                case Constant.SAME_NAME:
                    clinetView.showError("Error", "Can't  Send Requset", "you can't add your self");
                    break;
            }

        });
    }

    //--- merna ---
    /**
     * update friends contact list
     */
    void loadAccordionData() {
        Platform.runLater(() -> {

            ArrayList<utilitez.Pair> allContact = clinetView.getContactsWithType();

            ObservableList<User> friendType = FXCollections.observableArrayList();
            ObservableList<User> familyType = FXCollections.observableArrayList();

            if (allContact != null) {

                for (utilitez.Pair contact : allContact) {
                    if (contact.getSecond().equals("Family")) {
                        familyType.add((User) contact.getFirst());
                    } else {
                        friendType.add((User) contact.getFirst());
                    }

                }

            }

            if (friendType.isEmpty()) {
                
                try {
                    Node node = FXMLLoader.load(getClass().getResource("EmptyList.fxml"));
                    titlePaneFriends.setContent(node);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (friendType.size() == 1) {
                
                titlePaneFriends.setContent(aListViewFriends);
                aListViewFriends.setItems(friendType);
            } else {
                
                aListViewFriends.setItems(friendType);
            }

            aListViewFriends.setCellFactory(listView -> new ListCell<User>() {

                @Override
                public void updateItem(User friend, boolean empty) {
                    super.updateItem(friend, empty);
                    if (empty || friend == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setGraphic(loadCell(friend));
                    }
                }

            });
            aListViewFriends.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try{
                        String friendName = aListViewFriends.getSelectionModel().getSelectedItem().getUsername();
                        cellClickAction(friendName);
                    }catch(NullPointerException ex){}
                    
                }

            });

            if (familyType.isEmpty()) {
                
                try {
                    Node node = FXMLLoader.load(getClass().getResource("EmptyList.fxml"));
                    titlePaneFamily.setContent(node);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (familyType.size() == 1) {
                
                titlePaneFamily.setContent(aListViewFamily);
                aListViewFamily.setItems(familyType);
            } else {
                
                aListViewFamily.setItems(familyType);
            }

            aListViewFamily.setCellFactory(listView -> new ListCell<User>() {

                @Override
                public void updateItem(User friend, boolean empty) {
                    super.updateItem(friend, empty);
                    if (empty || friend == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setGraphic(loadCell(friend));
                    }
                }

            });
            aListViewFamily.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try{
                        String friendName = aListViewFamily.getSelectionModel().getSelectedItem().getUsername();
                        cellClickAction(friendName);
                    }catch(NullPointerException ex){}
                    
                }

            });
        });
    }

    Node loadCell(User friend) {
        ImageView imageView = new ImageView();
        ImageView imageViewStatus = new ImageView();

        FlowPane flow = new FlowPane();
        flow.setHgap(4);
        flow.setPrefWidth(1);

        Label friendName = new Label();

        friendName.setText(friend.getUsername());

        Image image;

        if (friend.getGender().equals("Female")) {
            image = new Image("/resouces/female.png", true);
        } else {
            image = new Image("/resouces/user.png", true);
        }

        Image statusImg = null;

        //change status image                        
        switch (friend.getStatus()) {
            case "offline":
                
                statusImg = new Image("/resouces/circle.png", true);
                break;
            case "online":
                statusImg = new Image("/resouces/online.png", true);
                
                break;
            case "busy":
                
                statusImg = new Image("/resouces/busy.png", true);
                break;
        }

        imageView.setImage(image);
        imageView.setFitWidth(35);
        imageView.setFitHeight(35);

        imageViewStatus.setImage(statusImg);
        imageViewStatus.setFitWidth(6);
        imageViewStatus.setFitHeight(6);

        flow.getChildren().addAll(imageView, friendName, imageViewStatus);

        return flow;
    }

    void cellClickAction(String friendName) {
        if (!tabsOpened.containsKey(friendName)) {
            try {

                
                Tab newTab = new Tab();

                newTab.setId(friendName);
                newTab.setText(friendName);

                newTab.setClosable(true);
                newTab.setOnCloseRequest(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        
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
    //-- end merna ---

    //  friendsPane.getChildren().clear();
    //  friendsPane.getChildren().add(FXMLLoader.load(getClass().getResource("EmptyList.fxml")));
}
