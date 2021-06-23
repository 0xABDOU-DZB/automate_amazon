package ua.startit;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ua.startit.pageobjects.HomePage;

import static com.codeborne.selenide.Selenide.open;
import static ua.startit.SignUpTest.*;

@Epic("Login and Registration")
@Feature("Sign in")
@io.qameta.allure.Severity(io.qameta.allure.SeverityLevel.CRITICAL)
public class SignInTest extends BaseTest {

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"Smoke", "Regression"})
    public void signTest() {
        homePage
                .clickOnSignIn()
                .login(EMAIL_ADDRESS, PASSWORD, new UiLoginStrategy());

        Assert.assertTrue(homePage.isNameDisplayed(FIRST_NAME),
                "Blah-blah");
    }

}