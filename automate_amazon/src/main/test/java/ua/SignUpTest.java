package ua.startit;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.startit.pageobjects.HomePage;
import ua.startit.pageobjects.SignInPage;
import ua.startit.pageobjects.SignUpPage;
import ua.startit.pageobjects.VerifyEmailAddressPage;
import ua.startit.support.exceptions.MailIsNotReceivedException;
import ua.startit.support.mail.Email;
import ua.startit.support.mail.EmailService;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Epic("Login and Registration")
@Feature("Sign up")
@Severity(SeverityLevel.CRITICAL)
public class SignUpTest extends BaseTest {

    static final String FIRST_NAME = "Some";
    private static final String LAST_NAME = "name";
    private static final String USERNAME = FIRST_NAME + " " + LAST_NAME;
    static final String PASSWORD = "qwerty";
    static final String EMAIL_ADDRESS = "verifaev+%s@gmail.com";

    static String formattedEmailAddress;

    private EmailService emailService;

    @BeforeMethod(alwaysRun = true)
    public void markEmailsAsRead() {
        emailService = new EmailService()
                .connectToService("verifaev@gmail.com", "101202storm");
        emailService.markAllEmailsAsRead();
    }

    @Test
    public void testName() throws MailIsNotReceivedException, InterruptedException {
        HomePage homePage = new HomePage();
        homePage.clickOnSignIn();

        SignInPage signInPage = new SignInPage();
        SignUpPage signUpPage = signInPage.signUp();

        signUpPage.setName(USERNAME);
        signUpPage.setPassword(PASSWORD);
        signUpPage.setPasswordCheck(PASSWORD);
        String uuid = UUID.randomUUID().toString();
        signUpPage.setEmail(formattedEmailAddress = String.format(EMAIL_ADDRESS, uuid));

        VerifyEmailAddressPage verifyPage = signUpPage.clickToCreateNewAccount();
        String code = getCode(uuid);
        HomePage homePageAfterSignUp = verifyPage.setCode(code);

        Assert.assertTrue(homePageAfterSignUp.isNameDisplayed(FIRST_NAME),
                "Blah-blah");
    }

    private String getCode(String uuid) throws MailIsNotReceivedException, InterruptedException {
        Thread.sleep(5000);
        List<Email> emailsList = emailService.fetch();
        for (Email email : emailsList) {
            List<String> emailTo = email.getTo();
            for (String to : emailTo) {
                if (to.contains(uuid)) {
                    String content = email.getContent().get(0);
                    Pattern compile = Pattern.compile(".*>([0-9]{6})<");
                    Matcher matcher = compile.matcher(content);
                    if (matcher.find()) {
                        return matcher.group(1);
                    }
                }
            }

        }

        throw new MailIsNotReceivedException("Mail was not received!");
    }


}