package controller;

import bo.BoFactory;
import bo.custom.UserAuthenticationBo;
import bo.util.BoType;
import bo.util.JakartaEmail;
import controller.util.ValidationType;
import controller.util.ValidationUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

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
    private MFXButton btnSendOTP;

    @FXML
    private Hyperlink linkBackToLogin;

    UserAuthenticationBo userAuthenticationBo = BoFactory.getInstance().getBo(BoType.USER_AUTHENTICATION);
    private String otpCode;

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

    @FXML
    void initializeSendOTP(){
        String email = txtEmail.getText();
        if(!ValidationUtil.validate(email,ValidationType.EMAIL)){
            new Alert(Alert.AlertType.WARNING,"Invalid Email!").show();
            return;
        }
        sendOTP(email);
    }

    private void sendOTP(String email) {
        if(userAuthenticationBo.verifyUser(email)){
            txtEmail.setAllowEdit(false);

            otpCode = generateOTP();
            String subject = "Password Reset OTP";
            String msgBody = "Dear User,\n" +
                    "\n" +
                    "You have requested to reset your password. Please use the following OTP code to proceed:\n" +
                    "\n" +
                    "OTP Code: "+ otpCode + "\n" +
                    "\n" +
                    "If you did not request this password reset, please ignore this email. Your account's security is important to us.\n" +
                    "\n" +
                    "Thank you,\n" +
                    "E&E Service Center";


            JakartaEmail.sendEmail(subject, msgBody, email);
            new Alert(Alert.AlertType.INFORMATION,"OTP was sent to your email!").show();
            return;
        }

        new Alert(Alert.AlertType.ERROR,"Invalid Email or User Not Registered!").show();
    }

    @FXML
    void btnResetPasswordOnAction(){
        if(txtEmail.isEditable() || txtOTP.getText().isEmpty() ||
                txtConfirmPassword.getText().isEmpty() || txtNewPassword.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Invalid Form Data!").show();
            return;
        }

        if(otpCode==null || otpCode.isEmpty()){
            new Alert
                    (Alert.AlertType.INFORMATION,"Please request OTP to proceed!").show();
            return;
        }

        if(!txtOTP.getText().equals(otpCode)){
            new Alert
                    (Alert.AlertType.ERROR,"OTP Invalid! Please Check Again.").show();
            return;
        }

        if(!ValidationUtil.validate(txtNewPassword.getText(),ValidationType.PASSWORD)){
            new Alert
                    (Alert.AlertType.ERROR,"Password Invalid! Include both case letters,numbers and special characters.").show();
            return;
        }

        if(!txtNewPassword.getText().equals(txtConfirmPassword.getText())){
            new Alert
                    (Alert.AlertType.ERROR,"Passwords do not match! Please Check Again.").show();
            return;
        }

        if(userAuthenticationBo.resetPassword(txtEmail.getText(),txtConfirmPassword.getText())){
            new Alert
                    (Alert.AlertType.INFORMATION,"Passwords Reset Successful! Go to Login.").show();
            otpCode = null;
            clearForm();
        }
    }

    private void clearForm(){
        txtEmail.setAllowEdit(true);
        txtEmail.clear();
        txtOTP.clear();
        txtConfirmPassword.clear();
        txtNewPassword.clear();
    }

    private static String generateOTP() {
        int otpLength = 6;
        StringBuilder otp = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < otpLength; i++) {
            int digit = random.nextInt(10);
            otp.append(digit);
        }

        return otp.toString();
    }
}
