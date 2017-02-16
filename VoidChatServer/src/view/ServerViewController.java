package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;
import model.UserFx;


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
    private GridPane gridView;
    @FXML
    private PieChart pieChart;
    @FXML
    private AnchorPane userContent;

    @FXML
    private TextField username;

    @FXML
    private ServerView serverView;

    @FXML
    ImageView sponser;

    @FXML
    private Button SendButton;

    @FXML
    private ToggleButton start;

    @FXML
    private TableView<UserFx> tableView;

    @FXML
    private TableColumn<UserFx, String> userNameCol;

    @FXML
    private TableColumn<UserFx, String> firstNameCol;

    @FXML
    private TableColumn<UserFx, String> lastNameCol;

    @FXML
    private TableColumn<UserFx, String> emailCol;

    @FXML
    private TableColumn<UserFx, String> genderCol;

    @FXML
    private TableColumn<UserFx, String> countryCol;
    
    public ArrayList<UserFx> users;
    public ObservableList<UserFx> data;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        serverView = ServerView.getInstance();
        serverView.setServerViewController(this);

        try {
            //set sponser
            sponser.setImage(new Image(getClass().getResource("..//resources//Voidlogo.png").openStream()));
        } catch (IOException ex) {
            Logger.getLogger(ServerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Online", 50),
                new PieChart.Data("Offline", 30),
                new PieChart.Data("Busy", 20));
        System.out.println("chart");
        pieChart.setData(data);
        pieChart.setLegendSide(Side.LEFT);


        /*
         * limit number of charachters, you can write in textArea 
         */
        announcement.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (announcement.getText().length() > 100) {
                    String s = announcement.getText().substring(0, 100);
                    announcement.setText(s);
                }
            }
        });

        // max size for image (200*100)
        sponser.maxWidth(200);
        sponser.maxHeight(100);

        // load tableView with users data 
        users = new ArrayList<>();
        if (serverView.getAllUsers() != null) {
            for (User user : serverView.getAllUsers()) {
                users.add(new UserFx(user.getUsername(), user.getEmail(),
                         user.getFname(), user.getLname(), user.getGender(),
                         user.getCountry()));
            }
            LoadTableData(users);
        }

    }

    private void addTooltipToChartSlice(PieChart chart) {
        double total = 0;
        for (PieChart.Data d : chart.getData()) {
            total += d.getPieValue();
        }
        for (PieChart.Data d : chart.getData()) {
            Node slice = d.getNode();
            double sliceValue = d.getPieValue();
            double precent = (sliceValue / total) * 100;

            String tip = d.getName() + "=" + String.format("%.2f", precent) + "%";

            Tooltip tt = new Tooltip(tip);
            Tooltip.install(slice, tt);
        }
    }

    @FXML
    private void ToggleButtonAction(ActionEvent event) {
        System.out.println("serverViewController");

        if (start.isSelected()) {
            System.out.println("serverViewController start");
            start.setText("Stop");
            serverView.startServer();
        } else {
            start.setText("Start");
            System.out.println("serverViewController stop");
            serverView.stopServer();
            serverView.loadErrorServer();
        }

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
                Pane pane = fxmlLoader.load();
                userContent.getChildren().add(pane);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendAnnouncement() {
        serverView.sendAnnouncement(announcement.getText());
        announcement.setText("");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Announcement send to all online users");
        alert.showAndWait();
    }

    public void setSponser(ActionEvent event) {
        try {
            Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
            );

            //Show save file dialog
            File file = fileChooser.showOpenDialog(st);

            //no file choosen
            if (file == null) {
                return;
            }

            if (file.length() > 1024 * 1024) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Image is so Big .. \nplease choose image less than 1 MB");
                alert.showAndWait();

                return;
            }

            sponser.setImage(new Image(file.toURI().toURL().toExternalForm()));

            //send to server controller
            FileInputStream in = new FileInputStream(file);
            byte[] data = new byte[1024 * 1024];
            int dataLength = in.read(data);

            serverView.sendSponser(data, dataLength);

        } catch (MalformedURLException ex) {
            Logger.getLogger(ServerViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void LoadTableData(ArrayList<UserFx> users) {

         data = FXCollections.observableArrayList(users);

        tableView.setEditable(true);

        userNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setOnEditCommit((TableColumn.CellEditEvent<UserFx, String> event) -> {
            UserFx user = ((UserFx) event.getTableView().getItems().get(event.getTablePosition().getRow()));
            user.setFname(event.getNewValue());
            serverView.updateUser(new User(user.getUsername(), user.getFname(),
                     user.getLname(), user.getGender(), user.getCountry()));
        });

        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lname"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setOnEditCommit((TableColumn.CellEditEvent<UserFx, String> event) -> {
            UserFx user = ((UserFx) event.getTableView().getItems().get(event.getTablePosition().getRow()));
            user.setLname(event.getNewValue());
            serverView.updateUser(new User(user.getUsername(), user.getFname(),
                     user.getLname(), user.getGender(), user.getCountry()));
        });

        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderCol.setCellFactory(TextFieldTableCell.forTableColumn());
        genderCol.setOnEditCommit((TableColumn.CellEditEvent<UserFx, String> event) -> {
            UserFx user = ((UserFx) event.getTableView().getItems().get(event.getTablePosition().getRow()));
            user.setGender(event.getNewValue());
            serverView.updateUser(new User(user.getUsername(), user.getFname(),
                     user.getLname(), user.getGender(), user.getCountry()));
        });

        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryCol.setCellFactory(TextFieldTableCell.forTableColumn());
        countryCol.setOnEditCommit((TableColumn.CellEditEvent<UserFx, String> event) -> {
            UserFx user = ((UserFx) event.getTableView().getItems().get(event.getTablePosition().getRow()));
            user.setGender(event.getNewValue());
            serverView.updateUser(new User(user.getUsername(), user.getFname(),
                     user.getLname(), user.getGender(), user.getCountry()));

        });

        tableView.setItems(data);


    }

}
