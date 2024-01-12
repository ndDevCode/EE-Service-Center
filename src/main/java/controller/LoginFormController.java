package controller;

import animatefx.animation.FadeInUp;
import bo.BoFactory;
import bo.custom.UserAuthenticationBo;
import bo.util.BoType;
import controller.util.ValidationType;
import controller.util.ValidationUtil;
import dto.StaffDto;
import dto.UserCredentialsDto;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Objects;

public class LoginFormController {

    @FXML
    private GridPane paneLogin;

    @FXML
    private AnchorPane paneMainContainer;

    @FXML
    private MFXTextField txtUserName;

    @FXML
    private MFXPasswordField txtPassword;

    @FXML
    private Hyperlink linkForgotPassword;

    @FXML
    private MFXButton btnLogIn;

    UserAuthenticationBo userAuthenticationBo = BoFactory.getInstance().getBo(BoType.USER_AUTHENTICATION);

    public void initialize() {
        // Link to reset password view
        linkForgotPassword.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    paneMainContainer.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull
                            (getClass().getResource("/view/ResetPasswordForm.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void verifyLoginOnAction() throws IOException {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto(
                txtUserName.getText(),
                txtPassword.getText()
        );

        if(!validateUserInput(userCredentialsDto)){
            alertInvalidCredentials();
            return;
        }

        if (userAuthenticationBo.isInitialLogin()) {
            loadAdminPanel(new StaffDto(userCredentialsDto.getUserEmail(),userCredentialsDto.getUserPassword(),"admin_init"));
            return;
        }

        try {
            StaffDto loggedStaff = userAuthenticationBo.verifyUser(userCredentialsDto);
            if(loggedStaff!=null && loggedStaff.getRole().equals("admin")){
                loadAdminPanel(loggedStaff);
            }else if(loggedStaff!=null && loggedStaff.getRole().equals("user")){
                loadUserPanel(loggedStaff);
            }else{
                alertInvalidCredentials();
            }
        }catch (NoResultException e){
            alertInvalidCredentials();
        }
    }

    void loadAdminPanel(StaffDto staff) throws IOException {
        // Closing the LoginView Window
        Stage stage = (Stage) paneMainContainer.getScene().getWindow();
        stage.close();

        // Load Admin Panel FXML
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../view/AdminDashboard.fxml")));
        Parent root = loader.load();


        // Set Styles and Display the AdminView Window
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage primaryStage = new Stage(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("E & E Service Center");
        primaryStage.getIcons().add(new Image("assets/appLogo.png"));
        primaryStage.show();

        // Transfer LoggedUser Data to AdminController
        AdminDashboardController adminController = loader.getController();
        adminController.initLoggedUser(staff);

        //Animation
        new FadeInUp(root).play();

    }

    void loadUserPanel(StaffDto staff) throws IOException {
        // Closing the LoginView Window
        Stage stage = (Stage) paneMainContainer.getScene().getWindow();
        stage.close();

        // Load Admin Panel FXML
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../view/UserDashboard.fxml")));
        Parent root = loader.load();


        // Set Styles and Display the AdminView Window
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage primaryStage = new Stage(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("E & E Service Center");
        primaryStage.getIcons().add(new Image("assets/appLogo.png"));
        primaryStage.show();

        // Transfer LoggedUser Data to AdminController
//        AdminDashboardController adminController = loader.getController();
//        adminController.initLoggedUser(staff);

        //Animation
        new FadeInUp(root).play();

    }


    boolean validateUserInput(UserCredentialsDto userCredentialsDto){
        return ValidationUtil.validate(userCredentialsDto.getUserEmail(), ValidationType.EMAIL) &&
        ValidationUtil.validate(userCredentialsDto.getUserPassword(), ValidationType.PASSWORD);
    }


    void alertInvalidCredentials(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Credentials.. !");
        alert.setContentText("Invalid Email or " +
                "Password must contains least One uppercase, " +
                "One Lowercase, Special character, minimum 8 characters.");
        alert.setHeaderText("Check Username or Password!");
        alert.show();
    }
}


