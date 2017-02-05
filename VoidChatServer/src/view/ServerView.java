package view;

import controller.ServerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 *
 * @author Mostafa
 */
public class ServerView extends Application implements ServerViewInt{

    private ServerController controller ; 
    public ServerView(){
        //connect to Controller
        controller = new ServerController(this);
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

}
