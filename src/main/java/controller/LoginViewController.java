package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginViewController {

    @FXML
    private Circle btnClose;

    @FXML
    private Circle btnMaximize;

    @FXML
    private Circle btnMinimize;

    @FXML
    private AnchorPane paneMainContainer;

    @FXML
    private AnchorPane paneSideWindow;

    @FXML
    private AnchorPane paneLeftSide;

    private double xOffset;
    private double yOffset;

    public void initialize(){

        // Setting up custom window controls
        final boolean[] isMaximized = {false};

        btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Platform.exit());

        btnMaximize.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Stage stage = (Stage)paneLeftSide.getScene().getWindow();
            isMaximized[0] = !isMaximized[0];

            if (!isMaximized[0]) {
                stage.setMaximized(true);
                btnMaximize.setFill(new Color(1,1,0,1));
            } else {
                stage.setMaximized(false);
                btnMaximize.setFill(new Color(0.32,1,0.13,1));
            }
        });

        btnMinimize.addEventHandler
                (MouseEvent.MOUSE_CLICKED, event -> ((Stage) paneLeftSide.getScene().getWindow()).setIconified(true));


        // Load default login page
        try {
            paneSideWindow.
                    getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull
                            (getClass().getResource("/view/LoginForm.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void paneOnMouseEntered(){ // Enable Dragging for the login window by holding side pane
        Stage stage = (Stage) paneLeftSide.getScene().getWindow();
        paneLeftSide.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        paneLeftSide.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }

}
