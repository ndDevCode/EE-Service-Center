package controller;

import animatefx.animation.FadeInUp;
import bo.BoFactory;
import bo.custom.UserAuthenticationBo;
import bo.util.BoType;
import dto.StaffDto;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AdminDashboardController {

    @FXML
    private AnchorPane paneMainContainer;

    @FXML
    private Label lblLoggedUser;

    @FXML
    private Label lblTimeStamp;

    @FXML
    private MFXButton btnManageUser;

    @FXML
    private MFXButton btnViewReport;

    @FXML
    private MFXButton btnLogOut;

    @FXML
    private Circle btnClose;

    @FXML
    private Circle btnMaximize;

    @FXML
    private Circle btnMinimize;

    @FXML
    private GridPane paneMainContent;

    @FXML
    private AnchorPane paneHeader;

    private double xOffset;
    private double yOffset;
    private StaffDto loggedStaff;

    UserAuthenticationBo userAuthenticationBo = BoFactory.getInstance().getBo(BoType.USER_AUTHENTICATION);

    public void initialize() {

        // Setting up the date and timer on the header

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                lblTimeStamp.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EE, dd LLLL yyyy hh:mm a")));
            }
        };
        timer.start();


        // Setting up custom window controls
        final boolean[] isMaximized = {false};


        btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Platform.exit());

        btnMaximize.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Stage stage = (Stage) paneHeader.getScene().getWindow();
            isMaximized[0] = !isMaximized[0];

            if (!isMaximized[0]) {
                stage.setMaximized(true);
                btnMaximize.setFill(new Color(1, 1, 0, 1));
            } else {
                stage.setMaximized(false);
                btnMaximize.setFill(new Color(0.32, 1, 0.13, 1));
            }
        });

        btnMinimize.addEventHandler
                (MouseEvent.MOUSE_CLICKED, event -> ((Stage) paneHeader.getScene().getWindow()).setIconified(true));


        // Mapping menu buttons to relevant view
        btnManageUser.setOnAction(actionEvent -> loadUserManageView(loggedStaff));

        btnLogOut.setOnAction(actionEvent -> logOut());

    }

    private void logOut() {
        try {
            Stage stage = (Stage) btnLogOut.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull
                    (getClass().getResource("/view/LoginView.fxml")));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            new FadeInUp(root).play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserManageView(StaffDto staff) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull
                    (getClass().getResource("/view/AdminUserManageView.fxml")));
            Parent root = loader.load();
            AdminUserManageViewController adminController = loader.getController();
            adminController.initLoggedUser(staff);
            paneMainContent.getChildren().setAll((Node) root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void paneOnMouseEntered() { // Enable Dragging for the login window by holding side pane
        Stage stage = (Stage) paneHeader.getScene().getWindow();
        paneHeader.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        paneHeader.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }

    void initLoggedUser(StaffDto staff) {
        // Initialize Logged User from LoginFormController
        this.loggedStaff = staff;
        String userName = userAuthenticationBo.getUserData(loggedStaff.getEmail()).getFirstName();
        lblLoggedUser.setText("Hello, "+userName);

        // Loading Initial View
        loadUserManageView(loggedStaff);

        // Initial Login Welcome Alert
        if (loggedStaff.getRole().equals("admin_init")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Welcome Admin..!");
            alert.setContentText("Welcome to the E & E Service Center. Please Register the Admin Account to initialize the System");
            alert.show();
        }
    }
}
