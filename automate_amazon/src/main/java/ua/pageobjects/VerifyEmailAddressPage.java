package ua.startit.pageobjects;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class VerifyEmailAddressPage {

    private By codeInput = By.cssSelector("input[name=\"code\"]");

    public HomePage setCode(String code) {
        $(codeInput)
                .setValue(code)
                .submit();

        return new HomePage();
    }
}