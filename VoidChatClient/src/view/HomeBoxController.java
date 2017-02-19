package view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.User;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class HomeBoxController implements Initializable {

    @FXML
    private VBox homeBox;
    @FXML
    private Button btnNewFriend;
    @FXML
    private Image clips;
    @FXML
    private TextFlow txtFlowServerMsg;
    @FXML
    private Label labelUserName;
    @FXML
    private Text serverMessage;
    @FXML
    private ImageView sponser;

    private ClientView clinetView;

    public HomeBoxController() {
        //get instance form view
        clinetView = ClientView.getInstance();
        clinetView.setHomeBoxController(this);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            updatePageInfo();
            //set sponser
            sponser.setImage(new Image(getClass().getResource("/resouces/Voidlogo.png").openStream()));
            sponser.maxWidth(150);
        sponser.maxHeight(150);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    
    public void updatePageInfo() {
        User user = clinetView.getUserInformation();
        labelUserName.setText(user.getUsername());
    }
     
     public void receiveAnnouncement(String message){
         serverMessage.setText("Void Chat Server : "+message);
     }
     
     public void reciveSponser(byte[] data, int dataLength){
         
         InputStream myInputStream = new ByteArrayInputStream(data); 
         sponser.setImage(new Image(myInputStream));
     }
}
