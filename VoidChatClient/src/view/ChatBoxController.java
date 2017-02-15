/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import model.ClientModelInt;
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
    private Button saveBtn;
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

    ArrayList<Message> History = new ArrayList<>();

    Boolean recMsgFlag = true;
    Boolean sendMsgFlag = true;
    Boolean conFlag = false;

    private ChatBoxController() {
        clientView = ClientView.getInstance();
        clientView.chatBoxController = this;
    }

    //3amlt deh
    public ChatBoxController(String receiver) {
        this();
        this.receiver = receiver;
    }

    public ChatBoxController(Message message) {
        this();
        this.message = message;

        if (!message.getTo().contains("##")) {
            receiver = message.getFrom();
            conFlag = true;
        }

    }

//    Boolean sendFlag = true;
//    Boolean recFlag = true;
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customizeEditorPane();
        if ((message != null && message.getTo().contains("##")) || (receiver != null && receiver.contains("##"))) {
            btnSendAttach.setDisable(true);
            saveBtn.setDisable(true);
        }
        if (clientView.getHistory(receiver) != null) {
            System.out.println("");
            loadHistory(clientView.getHistory(receiver));
        }
        
        btnSendAttach.setTooltip(new Tooltip("Send attachment"));
        saveBtn.setTooltip(new Tooltip("Save Message"));
    }

    @FXML
    private void iconCloseChatAction(MouseEvent event) {

    }

    @FXML
    void saveBtnAction(ActionEvent event) {
        Platform.runLater(() -> {

            Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            //Set extension filter
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("xml files (*.xml)", "*.xml")
            );
            //Show save file dialog
            File file = fileChooser.showSaveDialog(st);

            if (file != null && !file.isFile()) {
                ArrayList<Message> history = clientView.getHistory(receiver);
                clientView.saveXMLFile(file, history);
            }

        });
    }

    @FXML
    private void btnSendAttachAction(ActionEvent event) {

        System.out.println("Start sending File..");
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();

        //Show save file dialog
        File file = fileChooser.showOpenDialog(st);

        //no file choosen
        if (file == null) {
            return;
        }

        String[] split = file.getName().split("\\.(?=[^\\.]+$)");

        String extension = "." + split[1];

        //  make connection with peer client 
        ClientModelInt peer = clientView.getConnection(receiver);

        //peer user is offline
        if (peer == null) {
            clientView.showError("Offline", "Offline User", "User you try to connect is offline right now");
            return;
        }

        Thread tr = new Thread(() -> {
            try {
                FileInputStream in = null;

                //get path to save file on other user
                String path = peer.getSaveLocation(clientView.getUserInformation().getUsername());
                //other client refuse file transfare
                if (path == null) {

                    Platform.runLater(() -> {
                        clientView.showError("Refuse", "Request refused", "User not accept your file trans request");
                    });
                    return;
                }

                System.out.println(path);

                in = new FileInputStream(file);
                byte[] data = new byte[1024 * 1024];
                int dataLength = in.read(data);

                while (dataLength > 0) {
                    System.out.println("Send : " + dataLength);
                    peer.reciveFile(path, extension, data, dataLength);
                    dataLength = in.read(data);
                }

            } catch (RemoteException ex) {
                Logger.getLogger(ChatBoxController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ChatBoxController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ChatBoxController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        tr.start();
    }

    private void sendMessageAction() {

        sendMsgFlag = true;

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
            VBox vbox = new VBox();

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

            if (recMsgFlag) {

                sendLabel.getStyleClass().add("LabelSender");
                if (!receiver.contains("##")) {
                    cell.getChildren().addAll(img, sendLabel);
                } else {
                    cell.getChildren().addAll(sendLabel);
                }
                recMsgFlag = false;
            } else {
                sendLabel.getStyleClass().add("LabelSenderSec");
                cell.getChildren().add(sendLabel);
                if (!receiver.contains("##")) {
                    cell.setMargin(sendLabel, new Insets(0, 0, 0, 32));
                }
            }

            listviewChat.getItems().add(cell);
            listviewChat.scrollTo(cell);
            txtFieldMsg.setText(null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("btnSendMsg Action");

    }

    public void reciveMsg(Message message) throws IOException {

        // Msg received in initialize don't send it again
        if (conFlag) {
            conFlag = false;
            return;
        }

        Boolean groupFlag = false;
        // hey there is new received msg, you will send the next msg with image 
        recMsgFlag = true;
        if (message.getTo().contains("##")) {
            System.out.println("test rec group >>" + message.getTo());
            receiver = message.getTo();
            groupFlag = true;
        } else {
            receiver = message.getFrom();
        }

        if (message != null) {
            HBox cell = new HBox();
            VBox vbox = new VBox();

            ImageView img = new ImageView(new Image(getClass().getResource("..//resouces//user_32.png").openStream()));

            Text recName = new Text(message.getFrom());
            recName.setStyle("-fx-font: 10 arial;");

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

            if (sendMsgFlag) {
                recLabel.getStyleClass().add("LabelRec");

                vbox.getChildren().addAll(recName, recLabel);

                if (groupFlag) {
                    vbox.setAlignment(Pos.TOP_RIGHT);
                    vbox.setMargin(recName, new Insets(0, 8, 0, 0));
                    cell.getChildren().addAll(vbox);
                    groupFlag = false;
                } else {
                    cell.getChildren().addAll(recLabel, img);
                }
                cell.setAlignment(Pos.TOP_RIGHT);
                sendMsgFlag = false;
            } else {
                recLabel.getStyleClass().add("LabelRecSec");
                vbox.getChildren().addAll(recName, recLabel);
                if (groupFlag) {
                    vbox.setAlignment(Pos.TOP_RIGHT);
                    vbox.setMargin(recName, new Insets(0, 8, 0, 0));
                    cell.getChildren().addAll(vbox);
                    groupFlag = false;
                } else {
                    cell.getChildren().addAll(recLabel);
                    cell.setMargin(recLabel, new Insets(0, 32, 0, 0));
                }
                cell.setAlignment(Pos.TOP_RIGHT);

            }

            listviewChat.getItems().add(cell);
            listviewChat.scrollTo(cell);
        }

    }

    //handle Enter pressed action on txtFieldMessage and call the sendMessageAction ..
    @FXML
    private void txtFieldOnKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            sendMessageAction();
        }
    }

    /*
     *customize Editor pane with styles  (bold,italic,font,size ..) 
     */
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

    public void loadHistory(ArrayList<Message> messages) {

        Boolean myFlag = true;
        Boolean otherFlag = true;

        for (Message message : messages) {
            System.out.println("from:" + message.getFrom() + " " + message.getBody());
            if (message.getFrom().equals(clientView.getUserInformation().getUsername())) {
                myFlag = true;
                try {
                    HBox cell = new HBox();

                    ImageView img = new ImageView(new Image(getClass().getResource("..//resouces//user_32.png").openStream()));

                    Label sendLabel = new Label(message.getBody());
                    sendLabel.setMaxWidth(300);
                    sendLabel.setWrapText(true);
                    sendLabel.setStyle("-fx-text-fill:" + message.getFontColor()
                            + ";-fx-font-weight:" + message.getFontWeight()
                            + ";-fx-font-size:" + message.getFontsSize()
                            + ";-fx-font-style:" + message.getFontStyle()
                            + ";-fx-font-family:\"" + message.getFontFamily()
                            + "\";-fx-underline:" + message.getUnderline()
                            + ";");

                    if (otherFlag) {

                        sendLabel.getStyleClass().add("LabelSender");
                        cell.getChildren().addAll(img, sendLabel);
                        otherFlag = false;

                    } else {
                        sendLabel.getStyleClass().add("LabelSenderSec");
                        cell.getChildren().add(sendLabel);
                        cell.setMargin(sendLabel, new Insets(0, 0, 0, 32));
                    }

                    listviewChat.getItems().add(cell);
                    listviewChat.scrollTo(cell);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {

                try {
                    otherFlag = true;
                    HBox cell = new HBox();
                    VBox vbox = new VBox();

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

                    if (myFlag) {

                        recLabel.getStyleClass().add("LabelRec");
                        cell.getChildren().addAll(recLabel, img);
                        cell.setAlignment(Pos.TOP_RIGHT);
                        myFlag = false;

                    } else {

                        recLabel.getStyleClass().add("LabelRecSec");
                        cell.getChildren().addAll(recLabel);
                        cell.setMargin(recLabel, new Insets(0, 32, 0, 0));
                        cell.setAlignment(Pos.TOP_RIGHT);
                    }

                    listviewChat.getItems().add(cell);
                    listviewChat.scrollTo(cell);
                } catch (IOException ex) {
                    Logger.getLogger(ChatBoxController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
