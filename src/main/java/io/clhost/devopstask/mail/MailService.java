package io.clhost.devopstask.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailService {
    private String username;
    private String password;
    private Properties mailProps = new Properties();
    private List<String> receivers = new ArrayList<>();

    private boolean isConfigured = false;

    public void configure() {
        Properties properties = new Properties();

        try {
            properties.load(new InputStreamReader(getClass().getResourceAsStream("/app.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        username = properties.getProperty("mail.username");
        password = properties.getProperty("mail.password");

        mailProps.setProperty("mail.smtp.host", properties.getProperty("mail.smtp.host"));
        mailProps.setProperty("mail.smtp.port", properties.getProperty("mail.smtp.port"));
        mailProps.setProperty("mail.smtp.auth", properties.getProperty("mail.smtp.auth"));
        mailProps.setProperty("mail.smtp.ssl.trust", properties.getProperty("mail.smtp.ssl.trust"));
        mailProps.setProperty("mail.smtp.starttls.enable", properties.getProperty("mail.smtp.starttls.enable"));

        isConfigured = true;
    }

    public void addReceivers() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/receivers.txt")));

        String str;
        while ((str = reader.readLine()) != null) {
            receivers.add(str);
        }
    }

    public void addReceiver(String receiver) {
        receivers.add(receiver);
    }

    public void send(Mail mail) {
        if (!isConfigured) {
            throw new IllegalStateException("Service is not configured.");
        }

        System.out.println("Sending messages..");
        for (String to : receivers) {
            send(mail, to);
        }
        System.out.println("All messages sent!");
    }

    private void send(Mail mail, String to) {
        Session session = Session.getInstance(mailProps,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(mail.getSubject());
            message.setText(mail.getText());

            Transport.send(message);

            System.out.println("To: " + to + ", Sent: " + mail.getSubject());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
