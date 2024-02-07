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
import java.time.LocalDate;
import java.util.Arrays;
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
    private MFXComboBox<String> cmbxMonth;

    @FXML
    private MFXTextField txtYear;

    @FXML
    private MFXDatePicker dateField;

    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoType.ORDER);
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    public void initialize() {
        String[] month = {"Jan","Feb","Mar","April","May","June","July","Aug","Sep","Oct","Nov","Dec"};
        cmbxMonth.getItems().addAll(Arrays.asList(month));

        btnOrderRpt.setOnAction(actionEvent -> {
            try {
                generateOrderReport();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        btnOrderByCustomer.setOnAction(actionEvent -> {
            try {
                generateOrderByCustomerReport();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        btnAllCustomerRpt.setOnAction(actionEvent -> generateAllCustomerReport());

        btnDaily.setOnAction(actionEvent -> generateDailyReport());

        btnMonthly.setOnAction(actionEvent -> generateMonthlyReport());

        btnYearly.setOnAction(actionEvent -> generateYearlyReport());
    }

    private void generateYearlyReport() {
        if(!txtYear.getText().isEmpty()){
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("logoPath", JasperReportUtil.class.getResourceAsStream("/invoice_logo.png"));
            parameters.put("date",txtYear.getText()+"%");

            try {
                JasperDesign design = JRXmlLoader.load("src/main/resources/reports/yearly_report.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(design);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void generateMonthlyReport() {
        if(dateField.getCurrentDate()!=null){
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("logoPath", JasperReportUtil.class.getResourceAsStream("/invoice_logo.png"));
            parameters.put("date", LocalDate.now().getYear()+"-0"+getMonth(cmbxMonth.getText())+"%");

            try {
                JasperDesign design = JRXmlLoader.load("src/main/resources/reports/monthly_report.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(design);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void generateDailyReport() {
        if(dateField.getCurrentDate()!=null){
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("logoPath", JasperReportUtil.class.getResourceAsStream("/invoice_logo.png"));
            parameters.put("date",dateField.getValue().toString());

            try {
                JasperDesign design = JRXmlLoader.load("src/main/resources/reports/daily_report.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(design);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        }
    }

    Integer getMonth(String month){
        switch (month){
            case "Jan" : return 1;
            case "Feb" : return 2;
            case "Mar" : return 3;
            case "April" : return 4;
            case "May" : return 5;
            case "June" : return 6;
            case "July" : return 7;
            case "Aug" : return 8;
            case "Sep" : return 9;
            case "Oct" : return 10;
            case "Nov" : return 11;
            case "Dec" : return 12;
        }
        return -1;
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

