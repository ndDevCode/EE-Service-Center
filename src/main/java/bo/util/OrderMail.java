package bo.util;

public class OrderMail {
    private OrderMail() {
    }

    public static boolean sendPlaceOrderMail(String mail) {
        final String SUBJECT = "Order Placed Successfully!";

        final String ORDER_PLACE_MSG = "Dear Madam/Sir,\n" +
                "Thank You for Trusting us. Please Find the receipt for your Order.\n" +
                "Best Regards\n" +
                "E & E Service Center";

        return JakartaEmail.sendEmail(SUBJECT, ORDER_PLACE_MSG, mail, MailType.ATTACHMENT);
    }

    public static boolean sendOrderCompleted(String mail) {
        final String SUBJECT = "Order has been Successfully Completed!";

        final String ORDER_PLACE_MSG = "Dear Madam/Sir,\n\n" +
                "Thank You for Trusting us. Your Order has been Completed!. Please Pickup the items\n\n" +
                "Best Regards\n" +
                "E & E Service Center";

        return JakartaEmail.sendEmail(SUBJECT, ORDER_PLACE_MSG, mail, MailType.TEXT_ONLY);
    }

    public static boolean sendOrderClosedMail(String mail) {
        final String SUBJECT = "Order Completed!";
        final String ORDER_PLACE_MSG = "Dear Madam/Sir,\n\n" +
                "Thank You for your payment. Please Find the final receipt for your Order.\n\n" +
                "Best Regards\n" +
                "E & E Service Center";
        return JakartaEmail.sendEmail(SUBJECT, ORDER_PLACE_MSG, mail, MailType.ATTACHMENT);
    }
}
