package controller;

import bo.BoFactory;
import bo.custom.ItemInventoryBo;
import bo.util.BoType;
import controller.util.StatusType;
import controller.util.ValidationType;
import controller.util.ValidationUtil;
import controller.util.ZoneType;
import dto.ItemDto;
import dto.StaffDto;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ItemInventoryController {

    private final String[] zones = {"Orange", "Red", "Green", "Yellow"};
    private final String[] status = {"Pending", "Processing", "Completed", "Closed"};
    private final String[] action = {"Add Item", "Delete Item", "Update Item"};
    private final ItemInventoryBo itemInventoryBo = BoFactory.getInstance().getBo(BoType.INVENTORY_ITEM);
    @FXML
    private Label lblStaffId;
    @FXML
    private MFXTableView<ItemDto> tblItemInventory;
    @FXML
    private MFXTextField txtItemName;
    @FXML
    private MFXTextField txtItemCategory;
    @FXML
    private MFXComboBox<String> cmbxStatus;
    @FXML
    private MFXButton btnDeleteItem;
    @FXML
    private MFXButton btnUpdateItem;
    @FXML
    private MFXTextField txtOrderId;
    @FXML
    private MFXTextField txtItemId;
    @FXML
    private MFXComboBox<String> cmbxZone;
    @FXML
    private MFXTextField txtRepairCost;
    @FXML
    private MFXButton btnAddItem;
    @FXML
    private MFXComboBox<String> cmbxSelectAction;
    @FXML
    private MFXComboBox<String> cmbxSelectStatus;
    @FXML
    private MFXTextField txtQtyOfStatus;
    private StaffDto loggedStaff;
    private ItemDto selectedItem;
    private List<ItemDto> itemList = new ArrayList<>();


    public void initialize() throws SQLException, ClassNotFoundException {
        // Add options to combo boxes
        cmbxZone.getItems().addAll(zones);
        cmbxStatus.getItems().addAll(status);
        cmbxSelectStatus.getItems().addAll(status);
        cmbxSelectAction.getItems().addAll(action);
        // Combobox functions
        cmbxSelectStatus.setOnAction(actionEvent -> setQtyOnChange(cmbxSelectStatus.getSelectedIndex()));
        cmbxSelectAction.setOnAction(actionEvent -> initItemFormOnAction(cmbxSelectAction.getSelectedIndex()));
        // Initial Item loading
        loadItemTable();
        tblItemInventory.getSelectionModel().selectionProperty().addListener((observableValue, oldValue, newValue) -> setOrderData(newValue));
        // Setting up button function
        btnAddItem.setOnAction(actionEvent -> {
            try {
                saveItem();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        btnDeleteItem.setOnAction(actionEvent -> {
            try {
                deleteItem();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        btnUpdateItem.setOnAction(actionEvent -> {
            try {
                updateItem();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateItem() throws SQLException, ClassNotFoundException {
        if (cmbxSelectAction.getSelectedIndex() != 2) {
            new Alert(Alert.AlertType.ERROR, "Please Select the Update Item Option!").show();
        }

        if (txtOrderId.getText() != null && !txtOrderId.getText().isEmpty()) {

            ItemDto itemDto = new ItemDto(txtItemId.getText(), txtItemName.getText(), txtItemCategory.getText(), cmbxStatus.getText(), cmbxZone.getText(), Double.parseDouble(txtRepairCost.getText()), txtOrderId.getText());

            try {
                itemInventoryBo.updateItem(itemDto);
                resetItemForm();
                new Alert(Alert.AlertType.CONFIRMATION, "Item Update Successfull!").show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error Occured!");
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a item to update!");
        }
        tblItemInventory.setItems(getItems());
    }

    private void deleteItem() throws SQLException, ClassNotFoundException {
        if (cmbxSelectAction.getSelectedIndex() != 1) {
            new Alert(Alert.AlertType.ERROR, "Please Select the Delete Item Option!").show();
            return;
        }

        if (txtOrderId.getText() != null && !txtOrderId.getText().isEmpty()) {
            try {
                itemInventoryBo.deleteItem(selectedItem.getItemId());
                resetItemForm();
                new Alert(Alert.AlertType.CONFIRMATION, "Item Delete Successfull!").show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error Occured!");
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a item to delete!").show();
        }
        tblItemInventory.setItems(getItems());
    }

    private void saveItem() throws SQLException, ClassNotFoundException {
        if (cmbxSelectAction.getSelectedIndex() != 0) {
            new Alert(Alert.AlertType.ERROR, "Please Select the Save Item Option!").show();
        }

        if (!txtOrderId.getText().isEmpty() &&
                !txtItemName.getText().isEmpty() &&
                !txtItemCategory.getText().isEmpty() &&
                ValidationUtil.validate(txtRepairCost.getText(), ValidationType.DOUBLE_VALUE)) {

            ItemDto itemDto = new ItemDto(
                    txtItemId.getText(),
                    txtItemName.getText(),
                    txtItemCategory.getText(),
                    StatusType.PENDING.toString(),
                    ZoneType.ORANGE.toString(),
                    Double.parseDouble(txtRepairCost.getText()),
                    txtOrderId.getText());

            try {
                itemInventoryBo.saveItem(itemDto);
                resetItemForm();
                new Alert(Alert.AlertType.CONFIRMATION, "Item Saved Successfull!").show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error Occured!");
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter item details!");
        }
        tblItemInventory.setItems(getItems());
    }

    private void initItemFormOnAction(int selectedIndex) {
        switch (selectedIndex) {
            case 0:
                initForSave();
                break;
            case 1:
                initForDelete();
                break;
            case 2:
                initForUpdate();
                break;
        }
    }

    private void initForDelete() {
        resetItemForm();
        txtOrderId.setAllowEdit(false);
        txtItemId.setAllowEdit(false);
        txtItemName.setAllowEdit(false);
        txtItemCategory.setAllowEdit(false);
        cmbxStatus.setAllowEdit(false);
        cmbxZone.setAllowEdit(false);
        txtRepairCost.setAllowEdit(false);
    }

    private void initForUpdate() {
        resetItemForm();
        txtOrderId.setAllowEdit(false);
        txtItemId.setAllowEdit(false);
        txtItemName.setAllowEdit(false);
        txtItemCategory.setAllowEdit(false);
    }

    private void initForSave() {
        resetItemForm();
        txtItemId.setAllowEdit(false);
        cmbxStatus.getSelectionModel().selectFirst();
        cmbxZone.getSelectionModel().selectFirst();
    }

    private void resetItemForm() {
        txtOrderId.clear();
        txtItemId.clear();
        txtItemCategory.clear();
        txtRepairCost.clear();
        txtItemName.clear();
        cmbxZone.clear();
        cmbxStatus.clear();
        txtOrderId.setAllowEdit(true);
        txtItemId.setAllowEdit(true);
        txtItemName.setAllowEdit(true);
        txtItemCategory.setAllowEdit(true);
        cmbxStatus.setAllowEdit(true);
        cmbxZone.setAllowEdit(true);
        txtRepairCost.setAllowEdit(true);
    }

    private void setQtyOnChange(int selectedIndex) {
        int qty = 0;
        for (ItemDto dto : itemList) {
            if (dto.getStatus().equals(status[selectedIndex])) {
                qty += 1;
            }
        }
        txtQtyOfStatus.setText(String.valueOf(qty));
    }

    private void setOrderData(ObservableMap<Integer, ItemDto> newValue) {
        Object[] array = newValue.keySet().toArray();
        if (array.length == 1) {
            selectedItem = newValue.get(array[0]);
            txtOrderId.setText(selectedItem.getOrderId());
            txtItemId.setText(selectedItem.getItemId());
            txtItemName.setText(selectedItem.getName());
            txtItemCategory.setText(selectedItem.getCategory());
            txtRepairCost.setText(String.valueOf(selectedItem.getRepairPrice()));
            cmbxStatus.getSelectionModel().selectItem(selectedItem.getStatus());
            cmbxZone.getSelectionModel().selectItem(selectedItem.getZone());
        }
    }

    private void loadItemTable() throws SQLException, ClassNotFoundException {
        MFXTableColumn<ItemDto> itemId = new MFXTableColumn<>("Item ID", false, Comparator.comparing(ItemDto::getItemId));
        MFXTableColumn<ItemDto> orderId = new MFXTableColumn<>("Order ID", false, Comparator.comparing(ItemDto::getOrderId));
        MFXTableColumn<ItemDto> name = new MFXTableColumn<>("Item Name", false, Comparator.comparing(ItemDto::getName));
        MFXTableColumn<ItemDto> category = new MFXTableColumn<>("Category", false, Comparator.comparing(ItemDto::getCategory));
        MFXTableColumn<ItemDto> price = new MFXTableColumn<>("Repair Price", false, Comparator.comparing(ItemDto::getRepairPrice));
        MFXTableColumn<ItemDto> itemStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(ItemDto::getStatus));
        MFXTableColumn<ItemDto> zone = new MFXTableColumn<>("Zone", false, Comparator.comparing(ItemDto::getZone));

        itemId.setRowCellFactory(item -> new MFXTableRowCell<>(ItemDto::getItemId));
        orderId.setRowCellFactory(item -> new MFXTableRowCell<>(ItemDto::getOrderId));
        name.setRowCellFactory(item -> new MFXTableRowCell<>(ItemDto::getName));
        category.setRowCellFactory(item -> new MFXTableRowCell<>(ItemDto::getCategory));
        price.setRowCellFactory(item -> new MFXTableRowCell<>(ItemDto::getRepairPrice));
        itemStatus.setRowCellFactory(item -> new MFXTableRowCell<>(ItemDto::getStatus));
        zone.setRowCellFactory(item -> new MFXTableRowCell<>(ItemDto::getZone));

        itemId.setPrefWidth(150);
        orderId.setPrefWidth(150);
        name.setPrefWidth(300);

        tblItemInventory.getTableColumns().addAll(itemId, orderId, name, category, price, itemStatus, zone);

        tblItemInventory.getFilters().addAll(new StringFilter<>("Item ID", ItemDto::getItemId), new StringFilter<>("Order ID", ItemDto::getOrderId), new StringFilter<>("Item Name", ItemDto::getName), new StringFilter<>("Category", ItemDto::getCategory), new StringFilter<>("Status", ItemDto::getStatus), new StringFilter<>("Zone", ItemDto::getZone));

        tblItemInventory.setItems(getItems());
    }

    private ObservableList<ItemDto> getItems() throws SQLException, ClassNotFoundException {
        itemList = itemInventoryBo.getAllItem();
        return FXCollections.observableArrayList(itemList);
    }

    public void initLoggedUser(StaffDto staffDto) {
        loggedStaff = staffDto;
        lblStaffId.setText(loggedStaff.getStaffId());
    }
}


