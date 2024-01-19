package controller;

import bo.BoFactory;
import bo.custom.CustomerBo;
import bo.custom.OrderBo;
import bo.util.BoType;
import controller.util.JasperReportUtil;
import db.DBConnection;
import dto.CustomerDto;
import dto.OrderDto;
import dto.StaffDto;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdminReportController {

    @FXML
    private Label lblStaffId;

    @FXML
    private MFXTextField txtOrderId;

    @FXML
    private MFXTextField txtCustomerContact;

    @FXML
    private MFXButton btnOrderRpt;

    @FXML
    private MFXButton btnOrderByCustomer;

    @FXML
    private MFXButton btnAllCustomerRpt;

    @FXML
    private MFXButton btnDaily;

    @FXML
    private MFXButton btnMonthly;

    @FXML
    private MFXButton btnYearly;

    @FXML
    private MFXComboBox<?> cmbxMonth;

    @FXML
    private MFXComboBox<?> cmbxYear;

    @FXML
    private MFXDatePicker dateField;

    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoType.ORDER);
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    public void initialize() {
        btnOrderRpt.setOnAction(actionEvent -> {
            try {
                generateOrderReport();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        btnOrderByCustomer.setOnAction(actionEvent -> {
            try {
                generateOrderByCustomerReport();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        btnAllCustomerRpt.setOnAction(actionEvent -> generateAllCustomerReport());
    }

    private void generateAllCustomerReport() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logoPath", JasperReportUtil.class.getResourceAsStream("/invoice_logo.png"));

        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/all_customer.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateOrderByCustomerReport() throws SQLException, ClassNotFoundException {
        if (!txtCustomerContact.getText().isEmpty()) {
            CustomerDto customer = customerBo.getCustomerByContact(txtCustomerContact.getText());
            Map<String,Object> customerMap = new HashMap<>();
            customerMap.put("customer_id",customer.getCustomerId());
            customerMap.put("first_name",customer.getFirstName());
            customerMap.put("last_name",customer.getLastName());
            customerMap.put("contact",customer.getContactNo());
            customerMap.put("email",customer.getEmail());


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("customer_details", customerMap);
            parameters.put("logoPath", JasperReportUtil.class.getResourceAsStream("/invoice_logo.png"));
            parameters.put("customer_id",customerMap.get("customer_id"));

            try {
                JasperDesign design = JRXmlLoader.load("src/main/resources/reports/customer_orders.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(design);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void generateOrderReport() throws SQLException, ClassNotFoundException {
        if (!txtOrderId.getText().isEmpty()) {
            OrderDto order = orderBo.getOrderByID(txtOrderId.getText());
            Map<String,Object> orderMap = new HashMap<>();
            orderMap.put("orderId", order.getOrderId());
            orderMap.put("customer", order.getCustomer());
            orderMap.put("description", order.getDescription());
            orderMap.put("status", order.getStatus());
            orderMap.put("staff", order.getStaff());
            orderMap.put("orderDate",order.getOrderDate());

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("order_details", orderMap);
            parameters.put("logoPath", JasperReportUtil.class.getResourceAsStream("/invoice_logo.png"));
            parameters.put("order_id",orderMap.get("orderId"));

            try {
                JasperDesign design = JRXmlLoader.load("src/main/resources/reports/final_bill.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(design);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void initLoggedUser(StaffDto staffDto) {
        lblStaffId.setText(staffDto.getStaffId());
    }
}

