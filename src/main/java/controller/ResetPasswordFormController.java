package controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Objects;

public class ResetPasswordFormController {

    @FXML
    private GridPane paneResetPassword;

    @FXML
    private AnchorPane paneMainContainer;

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXButton btnResetPassword;

    @FXML
    private MFXTextField txtOTP;

    @FXML
    private MFXPasswordField txtNewPassword;

    @FXML
    private MFXPasswordField txtConfirmPassword;

    @FXML
    private Hyperlink linkBackToLogin;

    public void initialize(){
        linkBackToLogin.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    paneMainContainer.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull
                            (getClass().getResource("/view/LoginForm.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
