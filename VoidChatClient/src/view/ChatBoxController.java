/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Message;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class ChatBoxController implements Initializable {

    @FXML
    private VBox chatBox;
    @FXML
    private Label labelFriendName;
    @FXML
    private ImageView imgFriendStatus;
    @FXML
    private Label labelFriendStatus;
    @FXML
    private ImageView iconCloseChat;
    @FXML
    private TextField txtFieldMsg;
    @FXML
    private Button btnSendAttach;
    @FXML
    private Image clips;
    @FXML
    private ListView<HBox> listviewChat;
    @FXML
    private Button btnSendMsg;
    @FXML
    private ToggleButton boldToggleBtn;

    @FXML
    private ToggleButton italicTogglebtn;

    @FXML
    private ToggleButton lineToggleBtn;

    @FXML
    private ComboBox<String> fontComboBox;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ComboBox<String> fontSizeComboBox;

    ClientView clientView;

    //3amlt deh
    public ChatBoxController() {
        clientView = ClientView.getInstance();

    }

    Boolean sendFlag = true;
    Boolean recFlag = true;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        customizeEditorPane();
    }

    @FXML
    private void iconCloseChatAction(MouseEvent event) {

    }

    @FXML
    private void btnSendAttachAction(ActionEvent event) {
        System.out.println("btnSendAttach Action");
    }

    @FXML
    private void btnSendMsgAction(ActionEvent event) {
        
        String color = "#" + Integer.toHexString(colorPicker.getValue().hashCode());
        String weight = (boldToggleBtn.isSelected()) ? "Bold" : "normal";
        String size = fontSizeComboBox.getSelectionModel().getSelectedItem();
        String style = (italicTogglebtn.isSelected()) ? "italic" : "normal";
        String font = fontComboBox.getSelectionModel().getSelectedItem();
        Boolean underline = lineToggleBtn.isSelected();
        
        File f = new File("src/resouces/chatBoxStyle.css");
        listviewChat.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        try {

            HBox cell = new HBox();
            if (sendFlag) {
                if (txtFieldMsg.getText() != null) {
                    ImageView img = new ImageView(new Image(getClass().getResource("..//resouces//user_32.png").openStream()));
                    Label sendLabel = new Label(txtFieldMsg.getText());

                    sendLabel.setMaxWidth(300);
                    sendLabel.setWrapText(true);
                    sendLabel.getStyleClass().add("LabelSender");
                    sendLabel.setStyle("-fx-text-fill:" + color
                            + ";-fx-font-weight:" + weight
                            + ";-fx-font-size:" + size 
                            + ";-fx-font-style:\""+style
                            +"\";-fx-font-family:\""+font
                            +"\";-fx-underline:"+underline
                            +";");
                    
                    cell.getChildren().addAll(img, sendLabel);
                }
                sendFlag = false;
            } else {
                Label sendLabel = new Label(txtFieldMsg.getText());
                sendLabel.getStyleClass().add("LabelSenderSec");
                sendLabel.setMaxWidth(300);
                sendLabel.setWrapText(true);
                sendLabel.setPadding(Insets.EMPTY);
                cell.getChildren().add(sendLabel);
                cell.setMargin(sendLabel, new Insets(0, 0, 0, 32));
                recFlag = false;
            }
//           
//           if(!recFlag){
//               if(txtFieldMsg.getText()!=null){
//                    ImageView img = new ImageView(new Image(getClass().getResource("..//resouces//user_32.png").openStream()));
//                    
//                    Label recLabel =new Label(txtFieldMsg.getText());
//                    recLabel.setMaxWidth(300);
//                    recLabel.setWrapText(true);
//                    recLabel.getStyleClass().add("LabelRec");
//                    cell.getChildren().addAll(recLabel,img);
//                    cell.setAlignment(Pos.TOP_RIGHT);
//     
//                }
//           }

            listviewChat.getItems().add(cell);
            txtFieldMsg.setText(null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("btnSendMsg Action");
        Message message=new Message();
        message.setTo("Aya");
        message.setBody("how are you aya");
        System.out.println("in chat box controller sendMsg");
        clientView.sendMsg(message);
 }
    
    public void reciveMsg(Message message){
        System.out.println("in chatboxcontroller");  
        System.out.println(message.getBody());
    }


    void customizeEditorPane() {
        ObservableList<String> limitedFonts = FXCollections.observableArrayList("Arial", "Times", "Courier New", "Comic Sans MS");
        fontComboBox.setItems(limitedFonts);
        fontComboBox.getSelectionModel().selectFirst();

        ObservableList<String> fontSizes = FXCollections.observableArrayList("8", "10", "12", "14", "18", "24");
        fontSizeComboBox.setItems(fontSizes);
        fontSizeComboBox.getSelectionModel().select(2);

        colorPicker.setValue(Color.BLACK);
    }
}
