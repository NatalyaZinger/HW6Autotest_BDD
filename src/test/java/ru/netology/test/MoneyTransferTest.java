package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    @BeforeEach
    void login() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        //var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.login(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.verify(verificationCode);
    }

    @Test
    void TransferFirstToSecond() {
        int value = 3000;
        String cardNumber = String.valueOf(DataHelper.getFirstCardNumber());
        var dashboardPage = new DashboardPage();
        var firstCardBalance = dashboardPage.getFirstCardBalance();
        var secondCardBalance = dashboardPage.getSecondCardBalance();
        dashboardPage.transferButtonFirstToSecond();
        var transferPage = new TransferPage();
        transferPage.transferData(value, cardNumber);
        var firstCardBalanceAfterTransfer = dashboardPage.getFirstCardBalance();
        var secondCardBalanceAfterTransfer = dashboardPage.getSecondCardBalance();
        Assertions.assertEquals(firstCardBalance - value, firstCardBalanceAfterTransfer);
        Assertions.assertEquals(secondCardBalance + value, secondCardBalanceAfterTransfer);
    }

    @Test
    void TransferSecondToFirst() {
        int value = 7000;
        String cardNumber = String.valueOf(DataHelper.getSecondCardNumber());
        var dashboardPage = new DashboardPage();
        var firstCardBalance = dashboardPage.getFirstCardBalance();
        var secondCardBalance = dashboardPage.getSecondCardBalance();
        dashboardPage.transferButtonSecondToFirst();
        var transferPage = new TransferPage();
        transferPage.transferData(value, cardNumber);
        var firstCardBalanceAfterTransfer = dashboardPage.getFirstCardBalance();
        var secondCardBalanceAfterTransfer = dashboardPage.getSecondCardBalance();
        Assertions.assertEquals(secondCardBalance - value, secondCardBalanceAfterTransfer);
        Assertions.assertEquals(firstCardBalance + value, firstCardBalanceAfterTransfer);
    }

    @Test
    void maxValueTransferFirstToSecond() {
        String cardNumber = String.valueOf(DataHelper.getFirstCardNumber());
        var dashboardPage = new DashboardPage();
        var firstCardBalance = dashboardPage.getFirstCardBalance();
        var secondCardBalance = dashboardPage.getSecondCardBalance();
        dashboardPage.transferButtonFirstToSecond();
        var transferPage = new TransferPage();
        transferPage.transferData(firstCardBalance, cardNumber);
        var finalBalance = dashboardPage.getFirstCardBalance() + dashboardPage.getSecondCardBalance();
        Assertions.assertEquals(firstCardBalance + secondCardBalance, finalBalance);
    }

    @Test
    void maxValueTransferSecondToFirst() {
        String cardNumber = String.valueOf(DataHelper.getSecondCardNumber());
        var dashboardPage = new DashboardPage();
        var firstCardBalance = dashboardPage.getFirstCardBalance();
        var secondCardBalance = dashboardPage.getSecondCardBalance();
        dashboardPage.transferButtonSecondToFirst();
        var transferPage = new TransferPage();
        transferPage.transferData(secondCardBalance, cardNumber);
        var finalBalance = dashboardPage.getFirstCardBalance() + dashboardPage.getSecondCardBalance();
        Assertions.assertEquals(firstCardBalance + secondCardBalance, finalBalance);
    }

    @Test
    void TransferAboveLimit() {
        int value = 15000;
        String cardNumber = String.valueOf(DataHelper.getSecondCardNumber());
        var dashboardPage = new DashboardPage();
        var secondCardBalance = dashboardPage.getSecondCardBalance();
        dashboardPage.transferButtonSecondToFirst();
        var transferPage = new TransferPage();
        transferPage.transferData(value, cardNumber);
        transferPage.getNotification();
    }
}