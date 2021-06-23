package ua.startit.pageobjects;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class SignUpPage {

    private By name = By.id("ap_customer_name");
    private By email = By.id("ap_email");
    private By password = By.id("ap_password");
    private By passwordCheck = By.id("ap_password_check");
    private By signUp = By.id("continue");

    public SignUpPage setName(String username) {
        $(name).setValue(username);
        return this;
    }

    public SignUpPage setEmail(String userEmail) {
        $(email).setValue(userEmail);
        return this;
    }

    public SignUpPage setPassword(String userPassword) {
        $(password).setValue(userPassword);
        return this;
    }

    public SignUpPage setPasswordCheck(String userPasswordCheck) {
        $(passwordCheck).setValue(userPasswordCheck);
        return this;
    }

    public VerifyEmailAddressPage clickToCreateNewAccount() {
        $(signUp).click();
        return new VerifyEmailAddressPage();
    }
}