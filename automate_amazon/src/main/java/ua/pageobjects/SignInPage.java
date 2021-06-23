package ua.startit.pageobjects;

import org.openqa.selenium.By;
import ua.startit.LoginStrategy;

import static com.codeborne.selenide.Selenide.$;

public class SignInPage {

    private By signUpButton = By.id("auth-create-account-link");

    public HomePage login(String username, String password, LoginStrategy strategy) {
        strategy.login(username, password);
        return new HomePage();
    }

    public SignUpPage signUp() {
        $(signUpButton).click();
        return new SignUpPage();
    }
}