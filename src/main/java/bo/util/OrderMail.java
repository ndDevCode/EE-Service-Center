package bo.util;

import dto.ItemDto;
import dto.OrderDto;

public class OrderMail {
    private OrderMail() {
    }

    public static boolean sendPlaceOrderMail(OrderDto orderDto, String mail) {
        final String SUBJECT = "Order Placed Successfully!";

        String orderItems = new String();
        double total = 0;
        for (Object dto : orderDto.getItems()) {
            ItemDto item = (ItemDto) dto;
            orderItems +=
                    "        <tr>\n" +
                    "            <td>"+item.getItemId()+"</td>\n" +
                    "            <td>"+item.getName()+"</td>\n" +
                    "            <td>"+item.getRepairPrice()+"</td>\n" +
                    "        </tr>\n";
            total+= item.getRepairPrice();
        }


        final String ORDER_PLACE_MSG = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Order Note</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "        }\n" +
                "        .order-details {\n" +
                "            margin-top: 20px;\n" +
                "            border-collapse: collapse;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "        .order-details, .order-details th, .order-details td {\n" +
                "            border: 1px solid #ddd;\n" +
                "        }\n" +
                "        .order-details th, .order-details td {\n" +
                "            padding: 8px;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "        .order-details th {\n" +
                "            background-color: #f2f2f2;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h2>Your Order has been Successfully Placed!</h2>\n" +
                "<h3>Order Note</h3>\n" +
                "<hr>" +
                "\n" +
                "<pre><strong>Order ID       :</strong> " + orderDto.getOrderId() + "</pre>\n" +
                "<pre><strong>Date           :</strong> " + orderDto.getOrderDate() + "</pre>\n" +
                "<pre><strong>Customer       :</strong> " + orderDto.getCustomer() + "</pre>\n" +
                "<pre><strong>Description    :</strong> " + orderDto.getDescription() + "</pre>\n" +
                "<pre><strong>Order Status   :</strong> " + orderDto.getStatus() + "</pre>\n" +
                "<pre><strong>Order Placed By:</strong> " + orderDto.getStaff() + "</pre>\n" +
                "\n" +
                "<h3>Order Details</h3>\n" +
                "<hr>" +
                "\n" +
                "<table class=\"order-details\">\n" +
                "    <thead>\n" +
                "        <tr>\n" +
                "            <th>Item ID</th>\n" +
                "            <th>Item Name</th>\n" +
                "            <th>Price</th>\n" +
                "        </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                orderItems +
                "        <tr>"+
                "           <td colspan=\"2\"><strong>Subtotal</strong></td>" +
                "           <td>"+total+"</td>" +
                "        </tr> + " +
                "        <tr>" +
                "           <td colspan=\"2\"><strong>Tax</strong></td>" +
                "           <td>0%</td>" +
                "        </tr>" +
                "        <tr>" +
                "           <td colspan=\"2\"><strong>Total</strong></td>" +
                "           <td>"+total+"</td>" +
                "        </tr>" +
                "    </tbody>\n" +
                "</table>\n" +
                "\n" +
                "\n" +
                "<pre style=\"color:red;\">***Please Note that the Prices May change with any addition parts that needed for your service!</pre>"+
                "<p><strong>Thank you for your order!</strong></p>\n" +
                "<p><strong>E & E Service Center</strong></p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";

        return JakartaEmail.sendEmail(SUBJECT, ORDER_PLACE_MSG, mail, MailType.HTML_CONTENT);
    }
}
