package ru.netology.sql_deadline.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.sql_deadline.data.DataHelper;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static ru.netology.sql_deadline.data.DataHelper.generateRandomPassword;

public class LoginPage {

    private final SelenideElement loginField = $("[data-test-id='login'] input");
    private final SelenideElement passwordField = $("[data-test-id='password'] input");

    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification] .notification__content");


    public SelenideElement getLoginField() {
        return loginField;
    }

    public SelenideElement getPasswordField() {
        return passwordField;
    }

    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public VerificationPage validUser(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public VerificationPage validLoginAndRandomPassword(DataHelper.AuthInfoFakePass info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(generateRandomPassword());
        loginButton.click();
        return new VerificationPage();
    }

}
