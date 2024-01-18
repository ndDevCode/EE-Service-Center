package bo.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

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
            }

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
