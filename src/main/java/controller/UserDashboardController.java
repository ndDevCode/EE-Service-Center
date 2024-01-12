package controller;

import dto.StaffDto;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UserDashboardController {

    @FXML
    private AnchorPane paneMainContainer;

    @FXML
    private AnchorPane paneContent;

    @FXML
    private AnchorPane paneHeader;

    @FXML
    private Label lblLoggedUser;

    @FXML
    private Label lblTimeStamp;

    @FXML
    private AnchorPane paneSideMenu;

    @FXML
    private MFXButton btnMenu;

    @FXML
    private MFXButton btnHome;

    @FXML
    private MFXButton btnManageProfile;

    @FXML
    private MFXButton btnOrderMgt;

    @FXML
    private MFXButton btnItemCatalog;

    @FXML
    private MFXButton btnItemInventory;

    @FXML
    private MFXButton btnPartInventory;

    @FXML
    private MFXButton btnViewReport;

    @FXML
    private Circle btnClose;

    @FXML
    private Circle btnMaximize;

    @FXML
    private Circle btnMinimize;

    private double xOffset;
    private double yOffset;
    private StaffDto loggedStaff;

    public void initialize(){

        // Set Timestamp
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


        //---- SidePane Slide in/out Animation
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.8), paneSideMenu);
        paneSideMenu.setTranslateX(-315);

        // Set action on toggle button
        btnMenu.setOnAction(event -> {
            if (paneSideMenu.getTranslateX() < 0) {
                translateTransition.setToX(0);
                translateTransition.play();
            } else {
                translateTransition.setToX(-315);
                translateTransition.play();
            }
        });

        // Set action on Mouse move out >> slide out
        paneSideMenu.setOnMouseExited(mouseEvent -> {
            translateTransition.setToX(-315);
            translateTransition.play();
        });

        //load home page
        loadHomePage();
    }

    @FXML
    void paneOnMouseEntered() {
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

    private void loadHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull
                    (getClass().getResource("/view/HomeView.fxml")));
            Parent root = loader.load();
            paneContent.getChildren().setAll((Node) root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
