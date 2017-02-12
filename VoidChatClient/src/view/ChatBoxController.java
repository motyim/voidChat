/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import model.Message;

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
    String receiver;
    Message message;

    //3amlt deh
    public ChatBoxController(String receiver) {
        clientView = ClientView.getInstance();
        this.receiver = receiver;
    }

    public ChatBoxController(Message message) {
        clientView = ClientView.getInstance();
        this.message = message;
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
    void saveBtnAction(ActionEvent event) {

    }

    @FXML
    private void btnSendAttachAction(ActionEvent event) {
        System.out.println("btnSendAttach Action");
    }

    @FXML
    private void btnSendMsgAction(ActionEvent event) {

        clientView.sendMsgFlag = true;

        String color = "#" + Integer.toHexString(colorPicker.getValue().hashCode());
        String weight = (boldToggleBtn.isSelected()) ? "Bold" : "normal";
        String size = fontSizeComboBox.getSelectionModel().getSelectedItem();
        String style = (italicTogglebtn.isSelected()) ? "italic" : "normal";
        String font = fontComboBox.getSelectionModel().getSelectedItem();
        Boolean underline = lineToggleBtn.isSelected();

        Message msg = new Message();
        msg.setFontsSize(Integer.parseInt(size));
        msg.setFontColor(color);
        msg.setBody(txtFieldMsg.getText());
        msg.setFontWeight(weight);
        msg.setFontFamily(font);
        msg.setFrom(clientView.getUserInformation().getUsername());
        msg.setFontStyle(style);
        msg.setTo(receiver);
        msg.setDate(getXMLGregorianCalendarNow());
        msg.setUnderline(underline);

        clientView.sendMsg(msg);

        File f = new File("src/resouces/chatBoxStyle.css");
        listviewChat.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        try {

            HBox cell = new HBox();
            ImageView img = new ImageView(new Image(getClass().getResource("..//resouces//user_32.png").openStream()));
            Label sendLabel = new Label(txtFieldMsg.getText());
            sendLabel.setMaxWidth(300);
            sendLabel.setWrapText(true);
            sendLabel.setStyle("-fx-text-fill:" + color
                    + ";-fx-font-weight:" + weight
                    + ";-fx-font-size:" + size
                    + ";-fx-font-style:" + style
                    + ";-fx-font-family:\"" + font
                    + "\";-fx-underline:" + underline
                    + ";");

            if (clientView.recMsgFlag) {

                sendLabel.getStyleClass().add("LabelSender");
                cell.getChildren().addAll(img, sendLabel);
                clientView.recMsgFlag = false;
            } else {
                sendLabel.getStyleClass().add("LabelSenderSec");
                cell.getChildren().add(sendLabel);
                cell.setMargin(sendLabel, new Insets(0, 0, 0, 32));
            }

            listviewChat.getItems().add(cell);
            txtFieldMsg.setText(null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("btnSendMsg Action");

    }

    public void reciveMsg(Message message) throws IOException {

        // hey there is new received msg, you will send the next msg with image 
        clientView.recMsgFlag = true;
        receiver = message.getFrom();

        if (message != null) {
            HBox cell = new HBox();
            ImageView img = new ImageView(new Image(getClass().getResource("..//resouces//user_32.png").openStream()));
            Label recLabel = new Label(message.getBody());
            recLabel.setMaxWidth(300);
            recLabel.setWrapText(true);
            recLabel.setStyle("-fx-text-fill:" + message.getFontColor()
                    + ";-fx-font-weight:" + message.getFontWeight()
                    + ";-fx-font-size:" + message.getFontsSize()
                    + ";-fx-font-style:" + message.getFontStyle()
                    + ";-fx-font-family:\"" + message.getFontFamily()
                    + "\";-fx-underline:" + message.getUnderline()
                    + ";");

            if (clientView.sendMsgFlag) {
                recLabel.getStyleClass().add("LabelRec");
                cell.getChildren().addAll(recLabel, img);
                cell.setAlignment(Pos.TOP_RIGHT);
                clientView.sendMsgFlag = false;
            } else {
                recLabel.getStyleClass().add("LabelRecSec");
                cell.getChildren().addAll(recLabel);
                cell.setAlignment(Pos.TOP_RIGHT);
                cell.setMargin(recLabel, new Insets(0, 32, 0, 0));
            }

            listviewChat.getItems().add(cell);

        }

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

    public XMLGregorianCalendar getXMLGregorianCalendarNow() {
        try {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            return now;
        } catch (DatatypeConfigurationException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
