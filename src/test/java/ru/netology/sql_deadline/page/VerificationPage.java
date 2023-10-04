package ru.netology.sql_deadline.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;


public class VerificationPage {


    private final SelenideElement codeField = $("[data-test-id='code'] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");
    private final SelenideElement errorNotificationBlocked = $("[data-test-id='error-blocked'] .blocked__content");

    private final SelenideElement errorNotificationBlockedPass = $("[data-test-id='error-blockedpass'] .blocked__content");

    public void verifyVerificationPageVisibility() {
        codeField.shouldBe(visible);
    }

    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void verifyErrorNotificationBlocked(String expectedText) {
        errorNotificationBlocked.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void verifyErrorNotificationBlockedPass(String expectedText) {
        errorNotificationBlockedPass.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public DashboardPage validVerify(String verificationCode) {
        verify(verificationCode);
        return new DashboardPage();
    }

    public void verify(String verificationCode) {
        codeField.setValue(verificationCode);
        verifyButton.click();

    }
}
