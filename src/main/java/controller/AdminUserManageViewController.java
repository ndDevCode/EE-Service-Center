package controller;

import bo.BoFactory;
import bo.custom.UserAuthenticationBo;
import bo.util.BoType;
import dto.StaffDto;
import io.github.palexdev.materialfx.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.sql.SQLException;

public class AdminUserManageViewController {

    UserAuthenticationBo userAuthenticationBo = BoFactory.getInstance().getBo(BoType.USER_AUTHENTICATION);

    @FXML
    private MFXTableView<?> tblUsers;

    @FXML
    private MFXTextField txtLastName;

    @FXML
    private MFXTextField txtContactNo;

    @FXML
    private MFXButton btnUpdateUser;

    @FXML
    private MFXPasswordField txtPassword;

    @FXML
    private MFXTextField txtFirstName;

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXComboBox<String> cmbxRole;

    @FXML
    private MFXButton btnRegisterUser;

    public void initialize(){
        cmbxRole.getItems().add("admin");
        cmbxRole.getItems().add("user");

    }

    public void initLoggedUser(StaffDto staff){
        txtEmail.setText(staff.getEmail());
        txtPassword.setText(staff.getPassword());
    }

    @FXML
    void btnRegisterUserOnAction() throws SQLException, ClassNotFoundException {
        StaffDto newStaff = new StaffDto(
                null,
                txtFirstName.getText(),
                txtLastName.getText(),
                txtContactNo.getText(),
                txtEmail.getText(),
                txtPassword.getText(),
                cmbxRole.getSelectedItem()
        );

        if (!userAuthenticationBo.saveUser(newStaff)) {
            operationErrorAlert("Save Error", "Registration Failed");
        } else {
            operationSuccessAlert("User Saved!", "Registration Successful!");
        }
    }

    @FXML
    void btnUpdateUserOnAction(){
        //To be Implemented the update user info
    }


    void operationSuccessAlert(String title,String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    void operationErrorAlert(String title,String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
