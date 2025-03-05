
package utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "yassminemegbli44@gmail.com";
    private static final String SENDER_PASSWORD = "qldp zwpq kqai xurn"; // Remplacez par le mot de passe d'application

    public static boolean sendEmail(String to, String subject, String content) {
        // Activer TLS 1.2 et TLS 1.3
        System.setProperty("mail.smtp.ssl.protocols", "TLSv1.2,TLSv1.3");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.connectiontimeout", "5000"); // Timeout de connexion
        props.put("mail.smtp.timeout", "5000"); // Timeout général

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("✅ Email envoyé avec succès !");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
            return false;
        }
    }
}
