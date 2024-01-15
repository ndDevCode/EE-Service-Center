package controller;

import bo.BoFactory;
import bo.custom.UserAuthenticationBo;
import bo.util.BoType;
import controller.util.ValidationUtil;
import dto.StaffDto;
import dto.tm.StaffTm;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class AdminUserManageViewController {

    UserAuthenticationBo userAuthenticationBo = BoFactory.getInstance().getBo(BoType.USER_AUTHENTICATION);

    @FXML
    private MFXTableView<StaffTm> tblUsers;

    @FXML
    private MFXTextField txtLastName;

    @FXML
    private MFXTextField txtContactNo;

    @FXML
    private MFXButton btnRegisterUser;

    @FXML
    private MFXButton btnUpdateUser;

    @FXML
    private MFXButton btnRefresh;

    @FXML
    private MFXPasswordField txtPassword;

    @FXML
    private MFXTextField txtFirstName;

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXComboBox<String> cmbxRole;

    private String selectedStaffId;

    public void initialize() throws SQLException, ClassNotFoundException {
        cmbxRole.getItems().add("admin");
        cmbxRole.getItems().add("user");

        tblUsers.getSelectionModel().selectionProperty().addListener((observableValue, oldValue, newValue) -> setData(newValue));

        btnRefresh.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> clearForm());

        loadTableData();
    }

    private void setData(ObservableMap<Integer, StaffTm> newValue) {
        Object[] array = newValue.keySet().toArray();
        if (array.length==1){
            StaffTm staffTm = newValue.get(array[0]);
            txtFirstName.setText(staffTm.getFirstName());
            txtLastName.setText(staffTm.getLastName());
            txtContactNo.setText(staffTm.getContactNo());
            txtEmail.setText(staffTm.getEmail());
            cmbxRole.setText(staffTm.getRole());
            selectedStaffId = staffTm.getStaffId();
        }
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {

        MFXTableColumn<StaffTm> staffId = new MFXTableColumn<>("Staff ID", false, Comparator.comparing(StaffTm::getStaffId));
        MFXTableColumn<StaffTm> firstName = new MFXTableColumn<>("FirstName", false, Comparator.comparing(StaffTm::getFirstName));
        MFXTableColumn<StaffTm> lastName = new MFXTableColumn<>("LastName", false, Comparator.comparing(StaffTm::getLastName));
        MFXTableColumn<StaffTm> contact = new MFXTableColumn<>("Contact", false, Comparator.comparing(StaffTm::getContactNo));
        MFXTableColumn<StaffTm> email = new MFXTableColumn<>("Email", false, Comparator.comparing(StaffTm::getEmail));
        MFXTableColumn<StaffTm> role = new MFXTableColumn<>("Role", false, Comparator.comparing(StaffTm::getRole));
        MFXTableColumn<StaffTm> action = new MFXTableColumn<>("Action", false);

        staffId.setRowCellFactory(staff -> new MFXTableRowCell<>(StaffTm::getStaffId));
        firstName.setRowCellFactory(staff -> new MFXTableRowCell<>(StaffTm::getFirstName));
        lastName.setRowCellFactory(staff -> new MFXTableRowCell<>(StaffTm::getLastName));
        contact.setRowCellFactory(staff -> new MFXTableRowCell<>(StaffTm::getContactNo));
        email.setRowCellFactory(staff -> new MFXTableRowCell<>(StaffTm::getEmail));
        role.setRowCellFactory(staff -> new MFXTableRowCell<>(StaffTm::getRole));

        action.setRowCellFactory(staff -> {
            MFXTableRowCell<StaffTm,String> mfxTableRowCell = new MFXTableRowCell<>(StaffTm::getAction);
            MFXButton btnDelete = new MFXButton("❌");
            btnDelete.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                try {
                    deleteCustomer(staff.getStaffId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

            mfxTableRowCell.setLeadingGraphic(btnDelete);
            mfxTableRowCell.setAlignment(Pos.CENTER);
            mfxTableRowCell.mouseTransparentProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    mfxTableRowCell.setMouseTransparent(false);
                }
            });
            return mfxTableRowCell;
        });

        email.setPrefWidth(350.0);
        firstName.setPrefWidth(200.0);
        lastName.setPrefWidth(200.0);

        tblUsers.getTableColumns().addAll(staffId,firstName,lastName,contact,email,role,action);

        tblUsers.getFilters().addAll(
                new StringFilter<>("StaffID", StaffTm::getStaffId),
                new StringFilter<>("FirstName", StaffTm::getFirstName),
                new StringFilter<>("LastName", StaffTm::getLastName),
                new StringFilter<>("Contact",StaffTm::getContactNo),
                new StringFilter<>("Role",StaffTm::getRole)
        );

        tblUsers.setItems(getUsers());
    }

    private ObservableList<StaffTm> getUsers() throws SQLException, ClassNotFoundException {
        ObservableList<StaffTm> staffList = FXCollections.observableArrayList();
        List<StaffDto> staffDtoList = userAuthenticationBo.getAllStaff();

        for (StaffDto dto: staffDtoList){
            staffList.add(new StaffTm(
                    dto.getStaffId(),
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getContactNo(),
                    dto.getEmail(),
                    dto.getRole(),
                    ""
            ));
        }

        return staffList;
    }
    private void deleteCustomer(String staffId) throws SQLException, ClassNotFoundException {
        Alert confirmAlert = new Alert
                (Alert.AlertType.CONFIRMATION,
                "Do you want to delete this user?",
                ButtonType.YES,ButtonType.NO);

        confirmAlert.showAndWait();

        if(confirmAlert.getResult()==ButtonType.YES){
            if(userAuthenticationBo.deleteUser(staffId)){
                operationSuccessAlert("Deleted!","User Deleted Successfully!");
            }
            confirmAlert.close();
            tblUsers.setItems(getUsers());
            selectedStaffId = null;
            return;
        }


        confirmAlert.close();
    }

    public void initLoggedUser(StaffDto staff){
        if(staff.getRole().equals("admin_init")){
            txtEmail.setText(staff.getEmail());
            txtPassword.setText(staff.getPassword());
        }
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

        if(!ValidationUtil.validateForSave(newStaff)){
            operationErrorAlert("Invalid User Details!","Please check the entered details are correct");
            return;
        }

        if (!userAuthenticationBo.saveUser(newStaff)) {
            operationErrorAlert("Error Occurred!", "Registration Failed");
        } else {
            operationSuccessAlert("User Saved!", "Registration Successfully!");
            tblUsers.setItems(getUsers());
            clearForm();
            selectedStaffId = null;
        }
    }

    @FXML
    void btnUpdateUserOnAction() throws SQLException, ClassNotFoundException {
        StaffDto newStaff = new StaffDto(
                selectedStaffId,
                txtFirstName.getText(),
                txtLastName.getText(),
                txtContactNo.getText(),
                txtEmail.getText(),
                txtPassword.getText(),
                cmbxRole.getText()
        );

        if(!ValidationUtil.validateForUpdate(newStaff)){
            operationErrorAlert("Invalid User Details!","Please check the entered details are correct");
            return;
        }
        if (!userAuthenticationBo.updateUser(newStaff)) {
            operationErrorAlert("Error Occurred!", "Update Failed");
        } else {
            operationSuccessAlert("User Updated!", "Update Successfully!");
            tblUsers.setItems(getUsers());
            clearForm();
            selectedStaffId = null;
        }
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

    void clearForm(){
        txtPassword.clear();
        txtEmail.clear();
        txtContactNo.clear();
        txtFirstName.clear();
        txtLastName.clear();
        cmbxRole.clear();
        cmbxRole.clearSelection();
    }
}
