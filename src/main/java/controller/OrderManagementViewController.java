package controller;

import controller.util.Status;
import controller.util.ValidationType;
import controller.util.ValidationUtil;
import controller.util.Zone;
import dto.ItemDto;
import dto.StaffDto;
import io.github.palexdev.materialfx.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class OrderManagementViewController {

    @FXML
    private Label lblStaffId;

    @FXML
    private MFXFilterComboBox<String> fcmbxSelectCustomer;

    @FXML
    private MFXTextField txtFirstName;

    @FXML
    private MFXTextField txtLastName;

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXTextField txtContactNo;

    @FXML
    private MFXTextField txtItemName;

    @FXML
    private MFXTextField txtRepairCost;

    @FXML
    private MFXButton btnAddItem;

    @FXML
    private MFXComboBox<String> cmbxSelectCategory;

    @FXML
    private TextArea txtaDescription;

    @FXML
    private MFXTableView<?> tblItemList;

    @FXML
    private MFXButton btnPlaceOrder;

    @FXML
    private MFXTextField txtOrderId;

    @FXML
    private MFXTextField txtCustomerName;

    @FXML
    private MFXComboBox<String> cmbxSelectStatus;

    @FXML
    private MFXButton btnChangeOrderStatus;

    private StaffDto loggedStaff;
    private List<ItemDto> itemList = new ArrayList<>();

    public void initialize(){
        // Add options to combo boxes

        cmbxSelectCategory.getItems().add("Electronic");
        cmbxSelectCategory.getItems().add("Electrical");

        cmbxSelectStatus.getItems().add("Pending");
        cmbxSelectStatus.getItems().add("Processing");
        cmbxSelectStatus.getItems().add("Completed");
        cmbxSelectStatus.getItems().add("Closed");

        //Initialize button actions

        btnAddItem.setOnAction(actionEvent -> addItemToOrder());
        btnPlaceOrder.setOnAction(actionEvent -> placeOrder());
    }

    private void placeOrder() {
    }

    private void addItemToOrder() {
        if(!ValidationUtil.validate(txtRepairCost.getText(), ValidationType.DOUBLE_VALUE)){
            new Alert(Alert.AlertType.ERROR,"Invalid Repair Cost");
            return;
        }

        ItemDto dto = new ItemDto(
                null,
                txtItemName.getText(),
                cmbxSelectCategory.getSelectedItem(),
                Status.PENDING.toString(),
                Zone.ORANGE.toString(),
                Double.parseDouble(txtRepairCost.getText()),
                null
        );

        System.out.println(dto);
    }


    public void initLoggedUser(StaffDto staffDto) {
        loggedStaff = staffDto;
        lblStaffId.setText(staffDto.getStaffId());
    }

}
