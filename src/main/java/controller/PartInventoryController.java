package controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PartInventoryController {

    @FXML
    private Label lblStaffId;

    @FXML
    private MFXTableView<?> tblPartInventory;

    @FXML
    private MFXTextField txtQtyRequired;

    @FXML
    private MFXButton btnAddPartToOrder;

    @FXML
    private MFXFilterComboBox<?> fcmbxParts;

    @FXML
    private MFXFilterComboBox<?> fcmbxOrders;

    @FXML
    private MFXTextField txtQty;

    @FXML
    private MFXButton btnAddPart;

    @FXML
    private MFXTextField txtPartName;

    @FXML
    private MFXTextField txtPartId;

    @FXML
    private MFXButton btnUpdatePart;

    @FXML
    private MFXTextField txtPartPrice;
}
