package ua.startit.support.mail;

import java.util.List;

public class Runner {

    public static void main(String[] args) {
        EmailService emailService = new EmailService()
                .connectToService("verifaev@gmail.com", "101202storm");

        List<Email> emails = emailService.fetch();
        for (Email e : emails) {
            System.out.println(e.getFrom());
            System.out.println(e.getSubject());
            System.out.println(e.getContent().get(0));
            System.out.println();
        }

        emailService.closeConnection();
    }
}