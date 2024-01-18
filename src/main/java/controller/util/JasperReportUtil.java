package controller.util;

import db.DBConnection;
import net.sf.jasperreports.engine.*;

import java.util.HashMap;
import java.util.Map;

public class JasperReportUtil {
    public static void generatePDFReport(Map<String,Object> orderParams, String outputPath) {
        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport("src/main/resources/reports/place_order_report.jrxml");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("order_details", orderParams);
            parameters.put("logoPath", JasperReportUtil.class.getResourceAsStream("/invoice_logo.png"));
            parameters.put("order_id",orderParams.get("orderId"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DBConnection.getInstance().getConnection());
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

            System.out.println("PDF report generated successfully at: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
