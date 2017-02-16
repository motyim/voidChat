package view;

import controller.ClientController;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ClientModelInt;
import model.Message;
import model.User;
import utilitez.Pair;

/**
 *
 * @author Merna
 */
public class ClientView extends Application implements ClientViewInt {

    private ClientController controller;
    private static ClientView instance;
    private Stage mainStage;

    //2na 2le 3amlaha
    ChatBoxController chatBoxController;

    //views Controller
    ChatSceneController chatSceneController;
    //HomeBox Controller
    HomeBoxController homeBoxController;

    public ClientView() {
        controller = new ClientController(this);
        instance = this;

    }

    /**
     * get static instance form client view
     *
     * @return ClientView instance
     */
    public static ClientView getInstance() {
        return instance;
    }

    public void setChatSceneController(ChatSceneController chatSceneController) {
        this.chatSceneController = chatSceneController;
    }

    public void setHomeBoxController(HomeBoxController homeBoxController) {
        this.homeBoxController = homeBoxController;
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("LaunchScene.fxml"));
        Scene scene = new Scene(root);
        stage.setOnCloseRequest((WindowEvent ew) -> {
            Platform.exit();
            //TODO : why not close
            System.exit(0);
        });
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    @Override
    public boolean signup(User user) throws Exception {

        return controller.signup(user);

    }

    @Override
    public User signin(String username, String password) throws Exception {
        return controller.signin(username, password);
    }

    @Override
    public void loadHomePage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
/////////////////////////////////////////////////////////////////////

    @Override
    public void changeStatus(String status) {
        controller.changeStatus(status);
    }
/////////////////////////////////////////////////////////////////////

    @Override
    public void logout() {
        controller.logout();
    }

    @Override
    public int sendRequest(String friend, String category) {
        return controller.sendRequest(friend, category);

    }

    @Override
    public void notify(String message, int type) {
        chatSceneController.notify(message, type);
        System.out.println("notify in client view");
    }

    @Override
    public boolean acceptRequest(String friend) {
        return controller.acceptRequest(friend);
    }

    @Override
    public void notifyStatus(String username, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMsg(Message message) {
        System.out.println("in client view sendMsg");
        controller.sendMsg(message);
    }

    /**
     * get received message and pass it to chatSeneController
     *
     * @param message
     */
    @Override
    public void reciveMsg(Message message) {
        try {
            System.out.println("recieve msg in client view" + message.getBody());
            chatSceneController.reciveMsg(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void groupMsg() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reciveMsgGroup(String msg, ArrayList<String> groupChatUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * show error alert
     *
     * @param title
     * @param header
     * @param content
     */
    @Override
    public void showError(String title, String header, String content) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

    }

    /**
     * alert success operation
     *
     * @param title
     * @param header
     * @param content
     */
    @Override
    public void showSuccess(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public ArrayList<User> getContacts() {
        return controller.getContacts();
    }

    @Override
    public ArrayList<String> checkRequest() {
        return controller.checkRequest();
    }

    public Stage getMainStage() {
        return this.mainStage;
    }

    @Override
    public User getUserInformation() {
        return controller.getUserInformation();
    }

    @Override
    public void receiveAnnouncement(String message) {
        homeBoxController.receiveAnnouncement(message);
    }

    @Override
    public void ignoreRequest(String senderName) {
        controller.ignoreRequest(senderName);
    }

    @Override
    public void saveXMLFile(File file, ArrayList<Message> messages) {
        controller.saveXMLFile(file, messages);
    }

    @Override
    public ClientModelInt getConnection(String Client) {
        return controller.getConnection(Client);
    }

    @Override
    public String getSaveLocation(String sender) {
        return chatSceneController.getSaveLocation(sender);
    }
    

    @Override
    public void createGroup(String groupName, ArrayList<String> groupMembers) {
        controller.createGroup(groupName, groupMembers);
    }

    @Override
    public ArrayList<Message> getHistory(String receiver) {
        return controller.getHistory(receiver);
    }
    
    @Override
    public ArrayList<Pair> getContactsWithType() {
        return controller.getContactsWithType();
    }
    
    @Override
    public void errorServer() {
       chatSceneController.loadErrorServer();
    }

    @Override
    public void reciveSponser(byte[] data, int dataLength) {
        homeBoxController.reciveSponser(data, dataLength);
    }

    @Override
    public boolean sendMail(String to, String emailBody) {
        return controller.sendMail(to, emailBody);
    }

    @Override
    public boolean conncetToServer(String host) {
        return controller.conncetToServer(host);
    }
}

