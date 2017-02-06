package view;

import controller.ClientController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.User;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author Merna
 */
public class ClientView extends Application implements ClientViewInt {

    private ClientController controller;
    static ClientView instance;
    private Stage mainStage;

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

    @Override
    public void start(Stage stage) throws Exception {
        mainStage=stage;
        Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
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

    @Override
    public void changeStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void logout() {
       controller.logout(); 
    }

    @Override
    public int sendRequest(String friend,String category) {
        System.out.println(friend+" "+category);
        return controller.sendRequest(friend, category);
       
    }

    @Override
    public void notify(String senderName) {
        showTrayNotification("New Request", "New Request From "+senderName, NotificationType.INFORMATION);
    }

    @Override
    public void acceptRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyStatus(String username, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMsg() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reciveMsg(String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void showError(String title, String header, String content) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
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
    
    public Stage getMainStage(){
        return this.mainStage;
    }
     /**
     * show Desktop Notification
     *  example:
     *  showTrayNotification("title","Message",NotificationType.SUCCESS);
     * @param title
     * @param Message
     * @param notificationType  types: INFORMATION,NOTICE, SUCCESS, WARNING, ERROR, CUSTOM
     */
    public void showTrayNotification(String title,String Message,NotificationType notificationType){
        System.out.println(">> "+Message);
    }
    
}
