package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.User;

/**
 * FXML Controller class
 *
 * @author Mostafa
 */
public class ServerViewController implements Initializable {

    @FXML
    private Tab WelcomTap;
    @FXML
    private Tab SendTab;
    @FXML
    private Button sendBtn;
    @FXML
    private TextArea announcement;
    @FXML
    private Button btnToggle;
    @FXML
    private AnchorPane userContent;

    @FXML
    private TextField username;

    private ServerView serverView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serverView = ServerView.getInstance();
        serverView.setServerViewController(this);
    }

    /**
     * get user data from data base
     */
    public void getUserData() {
        User user = serverView.getUserInfo(username.getText());
        username.setText("");
        if (user == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("no User with this username");
            alert.showAndWait();
        } else {

            try {
                userContent.getChildren().clear();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserInfoView.fxml"));
                fxmlLoader.setController(new UserInfoController(user));
                Pane pane=fxmlLoader.load();
                userContent.getChildren().add(pane);
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendAnnouncement(){
        serverView.sendAnnouncement(announcement.getText());
        announcement.setText("");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Announcement send to all online users");
        alert.showAndWait();
    }
}
