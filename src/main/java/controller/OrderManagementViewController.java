package controller;

import bo.BoFactory;
import bo.custom.CustomerBo;
import bo.custom.OrderBo;
import bo.util.BoType;
import bo.util.JakartaEmail;
import bo.util.OrderMail;
import controller.util.*;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDto;
import dto.StaffDto;
import dto.tm.ItemTm;
import dto.tm.OrderTm;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import org.hibernate.criterion.Order;

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

    @FXML
    private MFXTableView<OrderTm> tblOrders;

    private StaffDto loggedStaff;
    private List<ItemDto> itemList = new ArrayList<>();
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoType.ORDER);
    private List<CustomerDto> customerList = new ArrayList<>();
    Map<String, CustomerDto> customerDtoMap = new HashMap<>();

    private CustomerDto selectedCustomer;
    private OrderTm selectedOrder;

    public void initialize() throws SQLException, ClassNotFoundException {
        // Add options to combo boxes

        cmbxSelectCategory.getItems().add("Electronic");
        cmbxSelectCategory.getItems().add("Electrical");

        cmbxSelectStatus.getItems().add("Pending");
        cmbxSelectStatus.getItems().add("Processing");
        cmbxSelectStatus.getItems().add("Completed");
        cmbxSelectStatus.getItems().add("Closed");
        cmbxSelectStatus.getItems().add("Cancelled");

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
        btnChangeOrderStatus.setOnAction(actionEvent -> {
            try {
                updateOrderStatus();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        //initial item table loading
        loadItemTable();

        //initial order table loading
        loadOrderTable();
        tblOrders.getSelectionModel().selectionProperty().addListener((observableValue, oldValue, newValue) -> setOrderData(newValue));
    }

    private void updateOrderStatus() throws SQLException, ClassNotFoundException {
        if (selectedOrder != null) {
            OrderDto order = new OrderDto(
                    selectedOrder.getOrderId(),
                    selectedOrder.getDescription(),
                    selectedOrder.getOrderDate(),
                    cmbxSelectStatus.getSelectedItem(),
                    selectedOrder.getCustomer(),
                    lblStaffId.getText(),
                    null
            );

            if (orderBo.updateOrderStatus(order)) {
                if(order.getStatus().equalsIgnoreCase(StatusType.COMPLETED.toString())){
                    OrderMail.sendOrderCompleted(customerBo.getCustomerById(selectedOrder.getCustomer()).getEmail());
                }else if(order.getStatus().equalsIgnoreCase(StatusType.CLOSED.toString())){
                    Map<String,Object> orderParams = new HashMap<>();
                    orderParams.put("orderId", order.getOrderId());
                    orderParams.put("customer", order.getCustomer());
                    orderParams.put("description", order.getDescription());
                    orderParams.put("status", order.getStatus());
                    orderParams.put("staff", order.getStaff());
                    orderParams.put("orderDate",order.getOrderDate());

                    JasperReportUtil.generatePDFReport(orderParams,"src/main/resources/reports/report.pdf", StatusType.CLOSED);
                    OrderMail.sendOrderClosedMail(customerBo.getCustomerById(selectedOrder.getCustomer()).getEmail());
                }

                clearChangeStatusFields();
                tblOrders.setItems(getOrders());
                new Alert(Alert.AlertType.CONFIRMATION, "Update Successfull!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update Failed").show();
            }
        }
        selectedOrder = null;
    }

    private void clearChangeStatusFields() {
        txtOrderId.clear();
        txtCustomerName.clear();
        cmbxSelectStatus.clear();
    }

    private void setOrderData(ObservableMap<Integer, OrderTm> newValue) {
        Object[] array = newValue.keySet().toArray();
        if (array.length == 1) {
            selectedOrder = newValue.get(array[0]);
            txtOrderId.setText(selectedOrder.getOrderId());
            txtCustomerName.setText(selectedOrder.getCustomer());
            cmbxSelectStatus.setText(selectedOrder.getStatus());
        }
    }

    private void loadOrderTable() throws SQLException, ClassNotFoundException {
        MFXTableColumn<OrderTm> orderId =
                new MFXTableColumn<>("Order ID", false, Comparator.comparing(OrderTm::getOrderId));
        MFXTableColumn<OrderTm> description =
                new MFXTableColumn<>("Description", false, Comparator.comparing(OrderTm::getDescription));
        MFXTableColumn<OrderTm> orderDate =
                new MFXTableColumn<>("Order Date", false, Comparator.comparing(OrderTm::getOrderDate));
        MFXTableColumn<OrderTm> status =
                new MFXTableColumn<>("Status", false, Comparator.comparing(OrderTm::getStatus));
        MFXTableColumn<OrderTm> customerId =
                new MFXTableColumn<>("Customer ID", false, Comparator.comparing(OrderTm::getCustomer));

        orderId.setRowCellFactory(item -> new MFXTableRowCell<>(OrderTm::getOrderId));
        description.setRowCellFactory(item -> new MFXTableRowCell<>(OrderTm::getDescription));
        orderDate.setRowCellFactory(item -> new MFXTableRowCell<>(OrderTm::getOrderDate));
        status.setRowCellFactory(item -> new MFXTableRowCell<>(OrderTm::getStatus));
        customerId.setRowCellFactory(item -> new MFXTableRowCell<>(OrderTm::getCustomer));

        orderId.setPrefWidth(150);
        description.setPrefWidth(350);

        tblOrders.getTableColumns().addAll(orderId, description, orderDate, status, customerId);

        tblOrders.getFilters().addAll(
                new StringFilter<>("Order ID", OrderTm::getOrderId),
                new StringFilter<>("Description", OrderTm::getDescription),
                new StringFilter<>("Order Date", OrderTm::getOrderDate),
                new StringFilter<>("Status", OrderTm::getStatus),
                new StringFilter<>("Customer Name", OrderTm::getCustomer)
        );

        tblOrders.setItems(getOrders());
    }

    private ObservableList<OrderTm> getOrders() throws SQLException, ClassNotFoundException {
        ObservableList<OrderTm> orderList = FXCollections.observableArrayList();
        List<OrderDto> dtoList = orderBo.getAllOrder();
        for (OrderDto dto : dtoList) {
            orderList.add(new OrderTm(
                    dto.getOrderId(),
                    dto.getDescription(),
                    dto.getOrderDate(),
                    dto.getStatus(),
                    dto.getCustomer()
            ));
        }
        return orderList;
    }

    private void updateCustomer() throws SQLException, ClassNotFoundException {
        if (selectedCustomer != null && ValidationUtil.validateForSave(selectedCustomer)) {
            CustomerDto customerDto = selectedCustomer;
            customerDto.setFirstName(txtFirstName.getText());
            customerDto.setLastName(txtLastName.getText());
            customerDto.setEmail(txtEmail.getText());
            customerDto.setContactNo(txtContactNo.getText());

            customerBo.updateCustomer(customerDto);
            new Alert(Alert.AlertType.CONFIRMATION, "Customer Details Updated!").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Please select a customer!").show();
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
            LocalDate today = LocalDate.now();

            CustomerDto customer = customerBo.getCustomer(customerDto.getEmail());

            OrderDto orderDto = new OrderDto(
                    null,
                    txtaDescription.getText(),
                    today.toString(),
                    StatusType.PENDING.toString(),
                    customer.getCustomerId(),
                    loggedStaff.getStaffId(),
                    itemList
            );
            try {
                OrderDto order = orderBo.saveOrder(orderDto);
                selectedCustomer = null;
                tblOrders.setItems(getOrders());

                Map<String,Object> orderParams = new HashMap<>();
                orderParams.put("orderId", order.getOrderId());
                orderParams.put("customer", order.getCustomer());
                orderParams.put("description", order.getDescription());
                orderParams.put("status", order.getStatus());
                orderParams.put("staff", order.getStaff());
                orderParams.put("orderDate",order.getOrderDate());

                JasperReportUtil.generatePDFReport(orderParams,"src/main/resources/reports/report.pdf", StatusType.PENDING);

                OrderMail.sendPlaceOrderMail(txtEmail.getText());
                clearPlaceOrderView();
                new Alert(Alert.AlertType.CONFIRMATION, "Order Placed Successfully!").show();
                loadCustomerFilterList();

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
