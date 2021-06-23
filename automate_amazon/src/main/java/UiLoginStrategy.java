package ua.startit;

import org.openqa.selenium.By;
import ua.startit.pageobjects.HomePage;
import ua.startit.pageobjects.SignInPage;

import static com.codeborne.selenide.Selenide.$;

public class UiLoginStrategy implements LoginStrategy {

    private By username = By.id("ap_email");
    private By password = By.id("ap_password");
    private By submit = By.id("signInSubmit");

    public void setUsername(String username) {
        $(this.username).setValue(username);
    }

    public void setPassword(String password) {
        $(this.password).setValue(password);
    }

    public void submit() {
        $(submit).click();
    }

    @Override
    public void login(String username, String password) {
        // implement via UI
        setUsername(username);
        setPassword(password);
        submit();
    }
}