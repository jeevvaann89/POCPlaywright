package com.stepdefinitions;

//import com.example.automation.config.ConfigurationReader;
import com.pages.LoginPage;
import com.cucumberconstants.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class LoginSteps {

    private LoginPage loginPage;
    private static Logger log = LoggerUtil.getLogger(LoginSteps.class);

    public LoginSteps() {
        this.loginPage = new LoginPage();
        log.info("LoginSteps initialized.");
    }

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        String url = ConfigurationReader.getEnvProperty("base.url");
        log.info("Navigating to application URL: {}", url);
        loginPage.navigateToLoginPage(url);
    }

    @When("I enter username {string} and password {string}")
    public void i_enter_username_and_password(String username, String password) {
        log.info("Entering credentials - Username: {}, Password: {}", username, password);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("I click the login button")
    public void i_click_the_login_button() {
        log.info("Clicking login button.");
        loginPage.clickLoginButton();
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        log.info("Verifying successful login.");
        Assert.assertTrue(loginPage.isUserLoggedIn(), "User was not logged in successfully!");
        log.info("Login successful.");
    }

    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedErrorMessage) {
        log.info("Verifying error message: {}", expectedErrorMessage);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed!");
        Assert.assertEquals(loginPage.getErrorMessageText(), expectedErrorMessage, "Error message text mismatch!");
        log.info("Error message verified: {}", expectedErrorMessage);
    }
}
