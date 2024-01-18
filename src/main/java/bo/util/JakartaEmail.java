package bo.util;

import controller.util.StatusType;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;

public class JakartaEmail {
    //provide username
    private static final String USERNAME = "dassanayakanadeesha@gmail.com";
    //provide password
    private static final String PASSWORD = "msfpdsrtxvvlikif";
    //provide host address
    private static final String HOST = "smtp.gmail.com";
    private JakartaEmail() {
    }

    //configure SMTP server details
    private static Properties getprops() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "587");

        return props;
    }


    public static boolean sendEmail(String subject, String msg, String sendTo, MailType mailType) {
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        };
        Session session = Session.getInstance(getprops(), authenticator);

        File file = new File("src/main/resources/reports/report.pdf");


        try {
            //create a MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
            message.setSubject(subject);

            switch (mailType) {
                case TEXT_ONLY:
                    message.setText(msg);
                    break;
                case HTML_CONTENT:
                    message.setContent(msg, "text/html");
                    break;
                case ATTACHMENT:
                    message.setContent(getAttachmentBody(file.getAbsolutePath(),msg));
            }

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Multipart getAttachmentBody(String filePath,String msg) throws MessagingException {
        BodyPart messageBodyPart = new MimeBodyPart();
        //set the actual message
        messageBodyPart.setText(msg);
        //create an instance of multipart object
        Multipart multipart = new MimeMultipart();
        //set the first text message part
        multipart.addBodyPart(messageBodyPart);
        //set the second part, which is the attachment
        messageBodyPart = new MimeBodyPart();

        DataSource source = new FileDataSource(filePath);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("Your Order Receipt.pdf");
        multipart.addBodyPart(messageBodyPart);

        return multipart;
    }
}
