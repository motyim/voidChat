package view;

import controller.ClientController;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.User;

/**
 *
 * @author Merna
 */
public class ClientView extends Application implements ClientViewInt {

    private ClientController controller;
    static ClientView instance;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notify(String senderName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
