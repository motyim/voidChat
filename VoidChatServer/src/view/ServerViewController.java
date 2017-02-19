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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
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

    public ArrayList<UserFx> users;
    public ObservableList<UserFx> data;
    //-------- merna -----------
    @FXML
    private BarChart<?, ?> BarCharOnline;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    @FXML
    private BarChart<?, ?> BarCharUserGender;
    @FXML
    private CategoryAxis xGender;
    @FXML
    private NumberAxis yGender;
    @FXML
    private AnchorPane analysisPane;

    private Pagination pagination;
    BarChart<String, Number> bc;
    BarChart<String, Number> gender;

    public int itemsPerPage() {
        return 1;
    }
    //-------- end merna -----------

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

        // load Charts Pane
        loadChartsPane();

        try {
            //set sponser
            sponser.setImage(new Image(getClass().getResource("/resources/Voidlogo.png").openStream()));
        } catch (IOException ex) {
            Logger.getLogger(ServerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

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
        
        if (start.isSelected()) {
            
            start.setText("Stop");
            serverView.startServer();
        } else {
            start.setText("Start");
            
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

        tableView.setItems(data);
    }

    //------ merna ------
    public VBox createPage(int pageIndex) {

        VBox box = new VBox(5);
        int page = pageIndex * itemsPerPage();
        if (page == 0) {
            VBox element = new VBox();
            element.getChildren().add(bc);
            box.getChildren().add(element);
        }
        if (page == 1) {
            VBox element = new VBox();
            element.getChildren().add(gender);
            box.getChildren().add(element);
        }
        return box;
    }

    public void loadChartsPane() {

        pagination = new Pagination(2, 0);
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });

        analysisPane.setTopAnchor(pagination, 10.0);
        analysisPane.setRightAnchor(pagination, 10.0);
        analysisPane.setBottomAnchor(pagination, 10.0);
        analysisPane.setLeftAnchor(pagination, 10.0);
        analysisPane.getChildren().addAll(pagination);

        //chart1 --- status ---  
        final NumberAxis yAxis = new NumberAxis();
        final CategoryAxis xAxis = new CategoryAxis();
        bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Online Status");
        yAxis.setLabel("User");
        yAxis.setLowerBound(0);
        if (!serverView.getStatistics().isEmpty()) {
            yAxis.setUpperBound(serverView.getStatistics().get(0) + serverView.getStatistics().get(1));
        }
        bc.setPrefSize(250, 200);

        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data("online", 2));
        series1.getData().add(new XYChart.Data("offline", 2));

        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (XYChart.Series<String, Number> series : bc.getData()) {
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        if (data.getXValue().equals("online")) {
                            data.setYValue(serverView.getStatistics().get(0));
                        } else if (data.getXValue().equals("offline")) {
                            data.setYValue(serverView.getStatistics().get(1));
                        }
                    }
                }
            }
        }));
        bc.getData().addAll(series1);
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();

        //chart2 --- gender ---
        final NumberAxis genderyAxis = new NumberAxis();
        final CategoryAxis genderxAxis = new CategoryAxis();
        gender = new BarChart<String, Number>(genderxAxis, genderyAxis);
        gender.setTitle("gender Status");
        genderxAxis.setLabel("Value");
        genderyAxis.setLowerBound(0);
        if(!serverView.getStatistics().isEmpty())
        genderyAxis.setUpperBound(serverView.getStatistics().get(0) + serverView.getStatistics().get(1));
        genderyAxis.setLabel("User");
        gender.setPrefSize(250, 200);

        XYChart.Series series3 = new XYChart.Series();
        series3.getData().add(new XYChart.Data("Female", 45));
        series3.getData().add(new XYChart.Data("Male", 45));

        Timeline t2 = new Timeline();
        t2.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (XYChart.Series<String, Number> series : gender.getData()) {
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        if (data.getXValue().equals("Female")) {

                            data.setYValue((Number) serverView.getGender().get(0).getSecond());
                        } else if (data.getXValue().equals("Male")) {
                            data.setYValue((Number) serverView.getGender().get(1).getSecond());
                        }
                    }
                }
            }
        }));

        gender.getData().add(series3);
        t2.setCycleCount(Animation.INDEFINITE);
        t2.play();

    }
    //------ end merna ------
}
