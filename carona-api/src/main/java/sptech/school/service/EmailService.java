package sptech.school.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.Key;
import java.util.Properties;
import java.util.Random;


@Service
public class EmailService {

    public static void enviarEmail(String destinatario) {
        final String remetente = "caronasofwaresptech@outlook.com";
        final String senha = "460820Ab@";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.port",  "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(remetente, senha);
                    }
                });

        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));




            message.setSubject("Carona - Confirmação de Cadastro");
            message.setText("Olá, você foi cadastrado com sucesso no sistema de caronas da escola. Aproveite para compartilhar caronas com seus colegas.");
            Transport.send(message);
            System.out.println("Email enviado com sucesso");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void enviarEmailComAnexo(String destinatario, File anexo) {
        final String remetente = "caronasofwaresptech@outlook.com";
        final String senha = "460820Ab@";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(remetente, senha);
                    }
                });

        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject("Arquivo CSV");

            MimeBodyPart attachment = new MimeBodyPart();
            DataSource source = new FileDataSource(anexo);
            attachment.setDataHandler(new DataHandler(source));
            attachment.setFileName(anexo.getName());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachment);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email com arquivo CSV enviado com sucesso");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String enviarTokenRecuperacao(String destinatario) {
        final String remetente = "caronasofwaresptech@outlook.com";
        final String senha = "460820Ab@";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.port",  "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(remetente, senha);
                    }
                });

        session.setDebug(true);


        String codigo = gerarCodigoAleatorio();
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject("Recuperação de senha");
            message.setText("Seu código de recuperação de senha é: " + codigo);
            Transport.send(message);
            System.out.println("Email enviado com sucesso");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return codigo;
    }

    public static String gerarCodigoAleatorio() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder();
        Random rnd = new Random();
        while (codigo.length() < 8) {
            int index = (int) (rnd.nextFloat() * caracteres.length());
            codigo.append(caracteres.charAt(index));
        }
        return codigo.toString();
    }
}
