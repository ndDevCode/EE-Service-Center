package controller;

import bo.BoFactory;
import bo.custom.CustomerBo;
import bo.custom.OrderBo;
import bo.util.BoType;
import controller.util.StatusType;
import controller.util.ValidationType;
import controller.util.ValidationUtil;
import controller.util.ZoneType;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDto;
import dto.StaffDto;
import dto.tm.ItemTm;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

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
    private MFXButton btnUpdateCustomer;

    @FXML
    private MFXComboBox<String> cmbxSelectCategory;

    @FXML
    private TextArea txtaDescription;

    @FXML
    private MFXTableView<ItemTm> tblItemList;

    @FXML
    private MFXButton btnPlaceOrder;

    @FXML
    private MFXButton btnRemoveItems;

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

    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoType.ORDER);

    private List<CustomerDto> customerList = new ArrayList<>();

    Map<String, CustomerDto> customerDtoMap = new HashMap<>();

    private CustomerDto selectedCustomer;

    public void initialize() throws SQLException, ClassNotFoundException {
        // Add options to combo boxes

        cmbxSelectCategory.getItems().add("Electronic");
        cmbxSelectCategory.getItems().add("Electrical");

        cmbxSelectStatus.getItems().add("Pending");
        cmbxSelectStatus.getItems().add("Processing");
        cmbxSelectStatus.getItems().add("Completed");
        cmbxSelectStatus.getItems().add("Closed");

        loadCustomerFilterList();
        fcmbxSelectCustomer.setOnAction(actionEvent -> setCustomerData(fcmbxSelectCustomer.getSelectedItem()));

        //Initialize button actions

        btnAddItem.setOnAction(actionEvent -> addItemToOrder());
        btnUpdateCustomer.setOnAction(actionEvent -> {
            try {
                updateCustomer();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        btnPlaceOrder.setOnAction(actionEvent -> {
            try {
                placeOrder();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        btnRemoveItems.setOnAction(actionEvent -> removeAllItem());

        //initial item table loading
        loadItemTable();
    }

    private void updateCustomer() throws SQLException, ClassNotFoundException {
        if (selectedCustomer != null && ValidationUtil.validateForSave(selectedCustomer)) {
            CustomerDto customerDto = selectedCustomer;
            customerDto.setFirstName(txtFirstName.getText());
            customerDto.setLastName(txtLastName.getText());
            customerDto.setEmail(txtEmail.getText());
            customerDto.setContactNo(txtContactNo.getText());

            customerBo.updateCustomer(customerDto);
            new Alert(Alert.AlertType.CONFIRMATION,"Customer Details Updated!").show();
        }
    }

    private void setCustomerData(String selectedItem) {
        selectedCustomer = customerDtoMap.get(selectedItem);
        txtFirstName.setText(selectedCustomer.getFirstName());
        txtLastName.setText(selectedCustomer.getLastName());
        txtEmail.setText(selectedCustomer.getEmail());
        txtContactNo.setText(selectedCustomer.getContactNo());
    }

    private void loadCustomerFilterList() throws SQLException, ClassNotFoundException {
        fcmbxSelectCustomer.getItems().addAll(getCustomerContactList());
        for (CustomerDto customerDto : customerList) {
            customerDtoMap.put(customerDto.getContactNo(), customerDto);
        }
    }

    private void placeOrder() throws SQLException, ClassNotFoundException, ParseException {
        CustomerDto customerDto = new CustomerDto(
                null,
                txtFirstName.getText(),
                txtLastName.getText(),
                txtEmail.getText(),
                txtContactNo.getText()
        );

        if (!ValidationUtil.validateForSave(customerDto)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Details!");
            return;
        }

        if (!customerBo.verifyCustomer(customerDto.getEmail())) {
            customerBo.saveCustomer(customerDto);
            fcmbxSelectCustomer.getItems().addAll(getCustomerContactList());
        }

        if (itemList.size() > 0) {
            System.out.println("inside the method");
            LocalDate today = LocalDate.now();
            double totalPrice = 0;
            for (ItemDto item : itemList) {
                totalPrice += item.getRepairPrice();
            }

            CustomerDto customer = customerBo.getCustomer(customerDto.getEmail());

            OrderDto orderDto = new OrderDto(
                    null,
                    txtaDescription.getText(),
                    today.toString(),
                    StatusType.PENDING.toString(),
                    totalPrice,
                    customer.getCustomerId(),
                    loggedStaff.getStaffId(),
                    itemList
            );
            try {
                orderBo.saveOrder(orderDto);
                clearPlaceOrderView();
                selectedCustomer = null;
                new Alert(Alert.AlertType.CONFIRMATION, "Order Placed Successfully!").show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong");
                e.printStackTrace();
            }

        } else {
            new Alert(Alert.AlertType.WARNING, "Please Add items to place the order!");
        }

    }

    private void clearPlaceOrderView() {
        itemList.clear();
        tblItemList.setItems(getItems());
        txtaDescription.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtContactNo.clear();
    }

    private List<String> getCustomerContactList() throws SQLException, ClassNotFoundException {
        List<String> contactList = new ArrayList<>();
        customerList = customerBo.getAllCustomer();
        for (CustomerDto dto : customerList) {
            contactList.add(dto.getContactNo());
        }
        return contactList;
    }

    private void addItemToOrder() {
        if (!ValidationUtil.validate(txtRepairCost.getText(), ValidationType.DOUBLE_VALUE)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Repair Cost").show();
            return;
        }

        if (txtItemName.getText().isEmpty() || cmbxSelectCategory.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Item Name or Category").show();
            return;
        }

        ItemDto dto = new ItemDto(
                null,
                txtItemName.getText(),
                cmbxSelectCategory.getSelectedItem(),
                StatusType.PENDING.toString(),
                ZoneType.ORANGE.toString(),
                Double.parseDouble(txtRepairCost.getText()),
                null
        );

        itemList.add(dto);
        tblItemList.setItems(getItems());
        clearItemFields();
    }

    private void clearItemFields() {
        txtItemName.clear();
        txtRepairCost.clear();
        cmbxSelectCategory.clear();
    }

    private void loadItemTable() {
        MFXTableColumn<ItemTm> itemName =
                new MFXTableColumn<>("Item Name", false, Comparator.comparing(ItemTm::getName));
        MFXTableColumn<ItemTm> category =
                new MFXTableColumn<>("Category", false, Comparator.comparing(ItemTm::getCategory));
        MFXTableColumn<ItemTm> price =
                new MFXTableColumn<>("Repair Price", false, Comparator.comparing(ItemTm::getRepairPrice));

        itemName.setRowCellFactory(item -> new MFXTableRowCell<>(ItemTm::getName));
        category.setRowCellFactory(item -> new MFXTableRowCell<>(ItemTm::getCategory));
        price.setRowCellFactory(item -> new MFXTableRowCell<>(ItemTm::getRepairPrice));

        itemName.setPrefWidth(300.0);
        category.setPrefWidth(200.0);

        tblItemList.getTableColumns().addAll(itemName, category, price);

        tblItemList.getFilters().addAll(
                new StringFilter<>("Item Name", ItemTm::getName),
                new StringFilter<>("Category", ItemTm::getCategory)
        );

        tblItemList.setItems(getItems());
    }

    private ObservableList<ItemTm> getItems() {
        ObservableList<ItemTm> list = FXCollections.observableArrayList();
        int index = 0;
        for (int i = 0; i < itemList.size(); i++) {
            itemList.get(i).setItemId(++index + "");
            list.add(new ItemTm(
                    itemList.get(i).getItemId(),
                    itemList.get(i).getName(),
                    itemList.get(i).getCategory(),
                    itemList.get(i).getRepairPrice(),
                    ""
            ));
        }
        return list;
    }

    private void removeAllItem() {
        itemList.clear();
        tblItemList.setItems(getItems());
    }

    public void initLoggedUser(StaffDto staffDto) {
        loggedStaff = staffDto;
        lblStaffId.setText(staffDto.getStaffId());
    }
}
