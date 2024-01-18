package controller.util;

import db.DBConnection;
import net.sf.jasperreports.engine.*;

import java.util.HashMap;
import java.util.Map;

public class JasperReportUtil {
    public static void generatePDFReport(Map<String,Object> orderParams, String outputPath, StatusType type) {
        try {
            JasperReport reportModel = null;

            if(type == StatusType.CLOSED){
                reportModel = JasperCompileManager.compileReport("src/main/resources/reports/final_bill.jrxml");
            }else if(type == StatusType.PENDING){
                reportModel = JasperCompileManager.compileReport("src/main/resources/reports/place_order_report.jrxml");
            }

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("order_details", orderParams);
            parameters.put("logoPath", JasperReportUtil.class.getResourceAsStream("/invoice_logo.png"));
            parameters.put("order_id",orderParams.get("orderId"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportModel, parameters, DBConnection.getInstance().getConnection());
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

            System.out.println("PDF report generated successfully at: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
