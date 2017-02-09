package view;

import controller.ServerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.User;
/**
 *
 * @author Mostafa
 */
public class ServerView extends Application implements ServerViewInt{

    private ServerController controller ; 
    private static ServerView instance;
    
    private ServerViewController serverViewController ; 
    
    public ServerView(){
        //connect to Controller
        controller = new ServerController(this);
        instance = this; 
    }
    
    /**
     * get static instance form client view
     *
     * @return ClientView instance
     */
    public static ServerView getInstance() {
        return instance;
    }
    
    public void setServerViewController(ServerViewController serverViewController){
        this.serverViewController = serverViewController;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Server");
        stage.show();
        stage.setOnCloseRequest((WindowEvent ew) -> {
                Platform.exit();
                //TODO : why not close
                System.exit(0);
            });
    }

    @Override
    public User getUserInfo(String username) {
        return controller.getUserInfo(username);
    }

    @Override
    public void sendAnnouncement(String message) {
        controller.sendAnnouncement(message);
    }
    
    

}
