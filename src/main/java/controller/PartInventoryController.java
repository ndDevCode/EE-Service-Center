package controller;

import bo.BoFactory;
import bo.custom.OrderBo;
import bo.custom.PartBo;
import bo.util.BoType;
import controller.util.ValidationType;
import controller.util.ValidationUtil;
import dto.CustomerDto;
import dto.OrderDto;
import dto.PartDto;
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
import java.util.*;

public class PartInventoryController {

    @FXML
    private Label lblStaffId;

    @FXML
    private MFXTableView<PartDto> tblPartInventory;

    @FXML
    private MFXTextField txtQtyRequired;

    @FXML
    private MFXButton btnAddPartToOrder;

    @FXML
    private MFXFilterComboBox<String> fcmbxParts;

    @FXML
    private MFXFilterComboBox<String> fcmbxOrders;

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
    private StaffDto loggedStaff;
    private PartDto selectedPart;
    private final PartBo partBo = BoFactory.getInstance().getBo(BoType.PART);
    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoType.ORDER);

    private Map<String,PartDto> partDtoMap = new HashMap<>();
    public void initialize() throws SQLException, ClassNotFoundException {
        // Button Actions
        btnAddPart.setOnAction(actionEvent -> {
            try {
                savePart();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        btnUpdatePart.setOnAction(actionEvent -> {
            try {
                updatePart();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        btnAddPartToOrder.setOnAction(actionEvent -> addPartToOrder());

        //Initial loading of combo boxes
        loadFilterComboBoxes();

        //Initial Loading of parts
        loadPartTable();
        tblPartInventory.getSelectionModel().selectionProperty().addListener
                ((observableValue, oldValue, newValue) -> setOrderData(newValue));
    }

    private void addPartToOrder() {
        if(fcmbxOrders.getSelectedItem().isEmpty() || fcmbxParts.getSelectedItem().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please select a order and a part").show();
            return;
        }

        if(!ValidationUtil.validate(txtQtyRequired.getText(),ValidationType.NUMBER)){
            new Alert(Alert.AlertType.ERROR,"Invalid Quantity").show();
            return;
        }

        PartDto partDto = partDtoMap.get(fcmbxParts.getSelectedItem());

        System.out.println(partDto);

        try {
            if(partBo.addToOrder(partDto.getPartId(), fcmbxOrders.getSelectedItem(), txtQtyRequired.getText())){
                new Alert(Alert.AlertType.CONFIRMATION,"Part added Successfully!").show();
                clearAddToOrder();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Cannot add the same Item twice!");
        }


    }

    private void clearAddToOrder(){
        fcmbxParts.clear();
        fcmbxOrders.clear();
        txtQtyRequired.clear();
    }

    private void loadFilterComboBoxes() throws SQLException, ClassNotFoundException {
        fcmbxOrders.getItems().addAll(getOrderList());
        fcmbxParts.getItems().addAll(getPartList());
    }

    private List<String> getPartList() throws SQLException, ClassNotFoundException {
        List<String> partList = new ArrayList<>();
        List<PartDto> parts = partBo.getAllPart();
        for (PartDto dto : parts) {
            partList.add(dto.getName());
            partDtoMap.put(dto.getName(),dto);
        }
        return partList;
    }

    private List<String> getOrderList() throws SQLException, ClassNotFoundException {
        List<String> orderList = new ArrayList<>();
        List<OrderDto> orders = orderBo.getAllOrder();
        for (OrderDto dto : orders) {
            orderList.add(dto.getOrderId());
        }
        return orderList;
    }

    private void updatePart() throws SQLException, ClassNotFoundException {
        if(txtPartId.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please select a part to edit!").show();
            return;
        }

        PartDto partDto = new PartDto(
                txtPartId.getText(),
                txtPartName.getText(),
                Double.parseDouble(txtPartPrice.getText()),
                Integer.parseInt(txtQty.getText())
        );

        if(partBo.updatePart(partDto)){
            new Alert(Alert.AlertType.CONFIRMATION,"Part Updated!").show();
            tblPartInventory.setItems(getParts());
            clearSavePartForm();
        }

    }

    private void setOrderData(ObservableMap<Integer, PartDto> newValue) {
        Object[] array = newValue.keySet().toArray();
        if (array.length == 1) {
            selectedPart = newValue.get(array[0]);
            txtPartId.setText(selectedPart.getPartId());
            txtPartName.setText(selectedPart.getName());
            txtPartPrice.setText(String.valueOf(selectedPart.getPrice()));
            txtQty.setText(String.valueOf(selectedPart.getQtyOnHand()));
            txtPartId.setAllowEdit(false);
        }
    }

    private void loadPartTable() throws SQLException, ClassNotFoundException {
        MFXTableColumn<PartDto> partId = new MFXTableColumn<>("Part ID", false, Comparator.comparing(PartDto::getPartId));
        MFXTableColumn<PartDto> name = new MFXTableColumn<>("Part Name", false, Comparator.comparing(PartDto::getName));
        MFXTableColumn<PartDto> price = new MFXTableColumn<>("Part Price", false, Comparator.comparing(PartDto::getPrice));
        MFXTableColumn<PartDto> qty = new MFXTableColumn<>("Qty On Hand", false, Comparator.comparing(PartDto::getQtyOnHand));

        partId.setRowCellFactory(item -> new MFXTableRowCell<>(PartDto::getPartId));
        name.setRowCellFactory(item -> new MFXTableRowCell<>(PartDto::getName));
        price.setRowCellFactory(item -> new MFXTableRowCell<>(PartDto::getPrice));
        qty.setRowCellFactory(item -> new MFXTableRowCell<>(PartDto::getQtyOnHand));

        partId.setPrefWidth(150);
        price.setPrefWidth(150);
        name.setPrefWidth(300);

        tblPartInventory.getTableColumns().addAll(partId,name,price,qty);

        tblPartInventory.getFilters().addAll(
                new StringFilter<>("Part ID", PartDto::getPartId),
                new StringFilter<>("Part Name", PartDto::getName)
        );

        tblPartInventory.setItems(getParts());
    }

    private ObservableList<PartDto> getParts() throws SQLException, ClassNotFoundException {
        return FXCollections.observableArrayList(partBo.getAllPart());
    }

    public void initLoggedUser(StaffDto staffDto) {
        loggedStaff = staffDto;
        lblStaffId.setText(loggedStaff.getStaffId());
    }

    private void savePart() throws SQLException, ClassNotFoundException {
        if(txtPartName.getText().isEmpty() &&
                !ValidationUtil.validate(txtPartPrice.getText(), ValidationType.DOUBLE_VALUE) &&
                !ValidationUtil.validate(txtQty.getText(),ValidationType.NUMBER)){
            new Alert(Alert.AlertType.ERROR,"Invalid Part Details!").show();
            return;
        }

        if(txtPartId.getText().contains("PT")){
            new Alert(Alert.AlertType.ERROR,"Wrong Operation!").show();
            return;
        }

        PartDto partDto = new PartDto(
                null,
                txtPartName.getText(),
                Double.parseDouble(txtPartPrice.getText()),
                Integer.parseInt(txtQty.getText())
        );

        if(partBo.savePart(partDto)){
            new Alert(Alert.AlertType.CONFIRMATION,"Part Saved Successfully!").show();
            tblPartInventory.setItems(getParts());
            clearSavePartForm();
            loadFilterComboBoxes();
        }else {
            new Alert(Alert.AlertType.ERROR,"Error Occurred!").show();
        }
    }

    private void clearSavePartForm() {
        txtPartId.clear();
        txtPartId.setAllowEdit(true);
        txtPartName.clear();
        txtPartPrice.clear();
        txtQty.clear();
    }
}
