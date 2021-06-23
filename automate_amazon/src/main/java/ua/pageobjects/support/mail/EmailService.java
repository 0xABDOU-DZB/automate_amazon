package ua.startit.support.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static ua.startit.support.mail.EmailHeader.*;
import static ua.startit.support.mail.Protocol.IMAP;


public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    private static final String INBOX = "inbox";

    private Protocol protocol;
    private String host;
    private Store store;

    public EmailService(Protocol protocol) {
        this.protocol = protocol;
        this.host = protocol.getHost();
    }

    public EmailService() {
        this.protocol = IMAP;
        this.host = protocol.getHost();
    }

    public EmailService connectToService(String user, String password) {
        try {
            this.store = openConnection(user, password, this.protocol);
        } catch (MessagingException e) {
            e.printStackTrace();
            LOG.error("There is a problem with setting store! Check your credentials");
        }
        return this;
    }

    public List<Email> fetch() {
        Folder folder = null;
        List<Email> emails = new ArrayList<>();
        try {
            folder = getFolder(store, INBOX, Flag.READ_ONLY);
            Message[] messages = getMessages(folder, Flags.Flag.SEEN);
            emails = getContent(messages);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

        return emails;
    }

    private Store openConnection(final String user, final String password, Protocol protocol) throws MessagingException {
        String protocolName = protocol.getProtocol();

        Store store;

        // create properties field
        Properties properties = new Properties();

        properties.put("mail.store.protocol", protocolName);
        properties.put("mail." + protocolName + ".host", host);
        properties.put("mail." + protocolName + ".starttls.enable", "true");
        properties.put("mail." + protocolName + ".port", "993");
        properties.put("mail." + protocolName + ".ssl.trust", "*");

//            Session emailSession = Session.getDefaultInstance(properties);
        Session emailSession = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        // emailSession.setDebug(true);

        // create the store object and connect with the server
        store = emailSession.getStore(protocolName + "s");
        LOG.info("Connection opened.");
        store.connect(host, user, password);

        return store;
    }

    public boolean closeConnection() {
        try {
            this.store.close();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Message[] getMessages(Folder folder, Flags.Flag flag) throws MessagingException {
        Message messages[];
        // search for all "unseen" messages
        Flags seen = new Flags(flag);
        FlagTerm unseen = new FlagTerm(seen, false);
        messages = folder.search(unseen);
        return messages;
    }

    private Folder getFolder(Store store, String folderName, Flag flag) throws MessagingException {
        Folder folder = store.getFolder(folderName);
        switch (flag) {
            case READ_ONLY:
                folder.open(Folder.READ_ONLY);
                break;
            case READ_WRITE:
                folder.open(Folder.READ_WRITE);
                break;
        }
        return folder;
    }

    private List<Email> getContent(Message[] messages) throws IOException, MessagingException {
        List<Email> emails = new ArrayList<>();
        LOG.info("Unseen emails found: " + messages.length);
        if (messages.length == 0) {
            return null;
        }
        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];

            emails.add(writePart(message));
        }
        return emails;
    }

    public void markAllEmailsAsRead() {
        Folder folder;
        try {
            folder = getFolder(store, INBOX, Flag.READ_WRITE);
            Message[] messages = getMessages(folder, Flags.Flag.SEEN);
            for (Message message : messages) {
                message.setFlag(Flags.Flag.SEEN, true);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        LOG.info("All emails marked as read.");
    }

    public String getLastEmail() {
        // Tue Mar 15 14:58:41 CET 2016
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        List<String> dates = fetch()
                .stream()
                .map(Email::getReceivedDate).sorted((o1, o2) -> {
                    try {
                        return simpleDateFormat.parse(o1).compareTo(simpleDateFormat.parse(o2));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                })
                .collect(Collectors.toList());
        return dates.get(dates.size() - 1);

    }

    private Email writePart(Part p) throws IOException, MessagingException {
        Email email = new Email();

        if (p instanceof Message) {
            email = writeEnvelope((Message) p);
        }

        email.setContentType(p.getContentType());

        //check if the content is plain text
        if (p.isMimeType("text/plain")) {
            email.setContentType(CONTENT_PLAIN);
            email.setContent(p.getContent().toString());
        } else if (p.isMimeType("text/html")) {
            email.setContentType(CONTENT_HTML);
            email.setContent(p.getContent().toString());
        }

        //check if the content has attachment
        else if (p.isMimeType("multipart/*")) {
            email.setContentType(CONTENT_MULTIPART);
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                email.setContent(mp.getBodyPart(i).getContent().toString());

            }
        }
        return email;
    }


    private Email writeEnvelope(Message m) throws MessagingException {
        Email email = new Email();
        Address[] a;


        // FROM
        if ((a = m.getFrom()) != null) {
            String[] addresses = new String[a.length];
            for (int i = 0; i < a.length; i++) {
                addresses[i] = a[i].toString();
            }
            email.setFrom(addresses);
        }

        // TO
        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
            String[] addresses = new String[a.length];
            for (int i = 0; i < a.length; i++) {
                addresses[i] = a[i].toString();
            }
            email.setTo(addresses);
        }

        // SUBJECT
        if (m.getSubject() != null) {
            email.setSubject(m.getSubject());
        }

        // SENT_DATE
        if (m.getSentDate() != null) {
            email.setSentDate(m.getSentDate().toString());
        }

        // RECEIVED_DATE
        if (m.getReceivedDate() != null) {
            email.setReceivedDate(m.getReceivedDate().toString());
        }
        return email;
    }

}