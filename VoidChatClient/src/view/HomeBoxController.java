package view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;
import model.User;
import utilitez.Constant;

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
            sponser.setImage(new Image(getClass().getResource("..//resouces//Voidlogo.png").openStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnNewFriendAction(ActionEvent event) {

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
            System.out.println("user=" + emailCategory.getKey() + ", category=" + emailCategory.getValue());
            switch (clinetView.sendRequest(emailCategory.getKey(), emailCategory.getValue())) {
                case Constant.ALREADY_FRIENDS:
                    clinetView.showError("Error", "Can't  Send Requset", "User Already Friend to you..");
                    break;
                case Constant.REQUEST_ALREADY_EXIST:
                    clinetView.showError("Error", "Can't  Send Requset", "you Already send request before "
                            + "\nor have request from this person");
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
            }

        });

    }

    public void updatePageInfo() {
        User user = clinetView.getUserInformation();
        labelUserName.setText(user.getUsername());
    }
     
     public void receiveAnnouncement(String message){
         serverMessage.setText("Void Chat Server : "+message);
     }
     
     public void reciveSponser(byte[] data, int dataLength){
         System.out.println("hzhzhzhzh");
         InputStream myInputStream = new ByteArrayInputStream(data); 
         sponser.setImage(new Image(myInputStream));
     }
}
