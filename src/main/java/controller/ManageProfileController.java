package controller;

import bo.BoFactory;
import bo.custom.UserAuthenticationBo;
import bo.util.BoType;
import bo.util.JakartaEmail;
import bo.util.MailType;
import controller.util.ValidationType;
import controller.util.ValidationUtil;
import dto.StaffDto;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.Random;

public class ManageProfileController {

    @FXML
    private Label lblStaffId;

    @FXML
    private MFXButton btnEditProfile;

    @FXML
    private MFXTextField txtFirstName;

    @FXML
    private MFXTextField txtLastName;

    @FXML
    private MFXButton btnUpdateProfile;

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXTextField txtContactNo;

    @FXML
    private MFXButton btnResetPassword;

    @FXML
    private MFXTextField txtOTP;

    @FXML
    private MFXButton btnSendOTP;

    @FXML
    private MFXPasswordField txtConfirmPassword;

    @FXML
    private MFXPasswordField txtNewPassword;

    private StaffDto loggedStaff;

    private String otpCode;

    private final UserAuthenticationBo userAuthenticationBo = BoFactory.getInstance().getBo(BoType.USER_AUTHENTICATION);
    public void initialize() {
        btnEditProfile.setOnAction(actionEvent -> setFormEditable(true));
        btnUpdateProfile.setOnAction(actionEvent -> {
            try {
                updateProfile();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        btnResetPassword.setOnAction(actionEvent -> resetPassword());
        btnSendOTP.setOnAction(actionEvent -> sendOTP(loggedStaff.getEmail()));

    }

    private void resetPassword() {
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

        if(!ValidationUtil.validate(txtNewPassword.getText(), ValidationType.PASSWORD)){
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
                    (Alert.AlertType.INFORMATION,"Passwords Reset Successful!").show();
            otpCode = null;
            clearForm();
        }
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


            JakartaEmail.sendEmail(subject, msgBody, email, MailType.TEXT_ONLY);
            new Alert(Alert.AlertType.INFORMATION,"OTP was sent to your email!").show();
            return;
        }

        new Alert(Alert.AlertType.ERROR,"Invalid Email or User Not Registered!").show();
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

    private void clearForm() {
        txtNewPassword.clear();
        txtConfirmPassword.clear();
        txtOTP.clear();
        otpCode = null;
    }

    private void updateProfile() throws SQLException, ClassNotFoundException {
        StaffDto newStaffProfile = new StaffDto(
                loggedStaff.getStaffId(),
                txtFirstName.getText(),
                txtLastName.getText(),
                txtContactNo.getText(),
                txtEmail.getText(),
                "",
                loggedStaff.getRole()
        );

        try{
            if(ValidationUtil.validateForUpdate(newStaffProfile)){
                userAuthenticationBo.updateUser(newStaffProfile);
                loggedStaff = newStaffProfile;
                initLoggedUser(newStaffProfile);
                new Alert(Alert.AlertType.CONFIRMATION,"Operation Success!").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Invalid Data Provided!").show();
            }
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR,"Operation Failed!").show();
        }

    }

    public void initLoggedUser(StaffDto staffDto) {
        loggedStaff = staffDto;
        lblStaffId.setText(staffDto.getStaffId());
        txtFirstName.setText(staffDto.getFirstName());
        txtLastName.setText(staffDto.getLastName());
        txtEmail.setText(staffDto.getEmail());
        txtContactNo.setText(staffDto.getContactNo());
        setFormEditable(false);
    }

    private void setFormEditable(boolean editable) {
        if (editable) {
            txtContactNo.setAllowEdit(true);
            txtEmail.setAllowEdit(true);
            txtLastName.setAllowEdit(true);
            txtFirstName.setAllowEdit(true);
        } else {
            txtContactNo.setAllowEdit(false);
            txtEmail.setAllowEdit(false);
            txtLastName.setAllowEdit(false);
            txtFirstName.setAllowEdit(false);
        }
    }
}
