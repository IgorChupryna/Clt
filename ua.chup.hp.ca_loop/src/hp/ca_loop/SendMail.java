package hp.ca_loop;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.*;

/**
 *
 * @author Chupryna-IV
 */
public class SendMail {

    public static void sendMail(String to, String toCopy, String from, String host, Boolean debug, String msgText, String subject ){


        // create some properties and get the default Session
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        if (debug) {
            props.put("mail.debug", debug);
        }

        Session session;
        session = Session.getInstance(props, null);
        session.setDebug(debug);

        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));

            //InternetAddress[] address = {new InternetAddress(to)};
            //InternetAddress[] address2 = {new InternetAddress(toCopy)};

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(toCopy));

            msg.setSubject(subject);
            msg.setSentDate(new Date());
            // If the desired charset is known, you can use
            // setText(text, charset)
            msg.setText(msgText);

            Transport.send(msg);
        } catch (MessagingException mex) {
            System.out.println("\n--Exception handling in msgsendsample.java");
            System.out.println();
            Exception ex = mex;
            do {
                if (ex instanceof SendFailedException) {
                    SendFailedException sfex = (SendFailedException)ex;
                    Address[] invalid = sfex.getInvalidAddresses();
                    if (invalid != null) {
                        System.out.println("    ** Invalid Addresses");
                        for (int i = 0; i < invalid.length; i++) {
                            System.out.println("         " + invalid[i]);
                        }
                    }
                    Address[] validUnsent = sfex.getValidUnsentAddresses();
                    if (validUnsent != null) {
                        System.out.println("    ** ValidUnsent Addresses");
                        for (int i = 0; i < validUnsent.length; i++) {
                            System.out.println("         "+validUnsent[i]);
                        }
                    }
                    Address[] validSent = sfex.getValidSentAddresses();
                    if (validSent != null) {
                        System.out.println("    ** ValidSent Addresses");
                        for (int i = 0; i < validSent.length; i++) {
                            System.out.println("         "+validSent[i]);
                        }
                    }
                }
                System.out.println();
                if (ex instanceof MessagingException) {
                    ex = ((MessagingException)ex).getNextException();
                }
                else {
                    ex = null;
                }
            } while (ex != null);
        }
    }
    public static void usage() {
        System.out.println("usage: java msgsendsample <to> <from> <smtp> true|false");


    }
}
