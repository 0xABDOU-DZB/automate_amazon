package ua.startit.support.mail;

public enum Protocol {
    POP3, IMAP;

    public String getHost() {
        String host;
        switch (this) {
            case POP3:
                host = "pop.gmail.com";
                break;
            case IMAP:
                host = "imap.gmail.com";
                break;
            default:
                host = "imap.gmail.com";
        }
        return host;
    }

    public String getProtocol() {
        String mailProtocol;
        switch (this) {
            case POP3:
                mailProtocol = "pop3";
                break;
            case IMAP:
                mailProtocol = "imap";
                break;
            default:
                mailProtocol = "imap";
        }
        return mailProtocol;
    }
}