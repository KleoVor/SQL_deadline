package ru.netology.sql_deadline.test;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.sql_deadline.data.DataHelper;
import ru.netology.sql_deadline.data.SQLHelper;
import ru.netology.sql_deadline.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.sql_deadline.data.SQLHelper.cleanAuthCodes;
import static ru.netology.sql_deadline.data.SQLHelper.cleanDatabase;

public class BankLoginTest {

    LoginPage loginPage;

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    public void clearLogin() {
        SelenideElement loginField = loginPage.getLoginField(); // получаем loginField через метод
        loginField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE); // используем loginField
    }

    public void clearPass() {
        SelenideElement passwordField = loginPage.getPasswordField(); // получаем loginField через метод
        passwordField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE); // используем loginField
    }


    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validUser(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldGetErrorUserDoesNotExist() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validUser(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");

    }

    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void shouldGetErrorValidUserInvalidVerificationCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validUser(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");

    }


    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void shouldErrorValidLoginInvalidPassword() {

        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            var AuthInfoFakePass = DataHelper.getValidLoginAndRandomPassword();
            var verificationPage = loginPage.validLoginAndRandomPassword(AuthInfoFakePass);

            if (attempt == maxAttempts) {
                verificationPage.verifyErrorNotificationBlockedPass("Превышено число попыток ввода пароля. Логин заблокирован. Обратитесь в банк");
            } else {
                verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
                clearLogin();
                clearPass();
            }
        }
    }


    @Test
    @DisplayName("A notification about blocking the user should appear if the user is valid and the code is entered three times invalid")
    void shouldErrorValidUserInvalidVerificationCodeThrice() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validUser(authInfo);
        verificationPage.verifyVerificationPageVisibility();

        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            var verificationCode = DataHelper.generateRandomVerificationCode();
            verificationPage.verify(verificationCode.getCode());

            if (attempt == maxAttempts) {
                verificationPage.verifyErrorNotificationBlocked("Превышено число попыток ввода кода. Логин заблокирован. Обратитесь в банк");
            } else {
                verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
            }
        }
    }

}

