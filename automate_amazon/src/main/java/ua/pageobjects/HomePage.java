package ua.startit.pageobjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class HomePage extends BasePage {

    @FindBy(id = "twotabsearchtextbox")
    private SelenideElement searchInputField;

    @FindBy(css = "#nav-link-accountList")
    private SelenideElement signIn;


    @Step("Search for {} text")
    public void search(String text) {
        searchInputField
                .setValue(text)
                .submit();
    }

    @Step("Click on Sign in button")
    public SignInPage clickOnSignIn() {
        signIn
                .should(Condition.enabled)
                .click();

        return page(SignInPage.class);
    }

    @Step("Check if {} is displayed")
    public boolean isNameDisplayed(String firstName) {
        $(By.xpath(String.format("//span[text()='Hello, %s']", firstName))).should(Condition.visible);
        return true;
    }
}