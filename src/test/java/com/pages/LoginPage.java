package com.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState; // Import LoadState for ElementHandler
import com.hooks.PlaywrightHooks; // Assuming PlaywrightHooks provides the Page instance
import com.cucumberconstants.LoggerUtil;
import com.cucumberconstants.ElementHandler; // Import the ElementHandler
import org.apache.logging.log4j.Logger;

public class LoginPage {

    private Page page;
    private ElementHandler elementHandler; // Declare an instance of ElementHandler
    private static Logger log = LoggerUtil.getLogger(LoginPage.class);

    // Locators
    private final String USERNAME_INPUT = "#user-name";
    private final String PASSWORD_INPUT = "#password";
    private final String LOGIN_BUTTON = "#login-button";
    private final String ERROR_MESSAGE = "[data-test='error']"; // Updated example error message locator
    private final String HOME_PAGE_ELEMENT = "#shopping_cart_container"; // Example element on a successful login page (e.g., for Sauce Demo)


    public LoginPage() {
        this.page = PlaywrightHooks.getPage();
        this.elementHandler = new ElementHandler(this.page); // Initialize ElementHandler with the current page
        log.info("LoginPage initialized with current Playwright Page and ElementHandler.");
    }

    /**
     * Navigates to the login page URL.
     * @param url The URL of the login page.
     */
    public void navigateToLoginPage(String url) {
        log.info("Navigating to login page: {}", url);
        page.navigate(url);
        // Use ElementHandler's waitForPageToLoad for more robust page load waiting
        elementHandler.waitForPageToLoad(LoadState.NETWORKIDLE);
        log.info("Navigated to login page successfully.");
    }

    /**
     * Enters the username into the username input field using ElementHandler.
     * @param username The username to enter.
     */
    public void enterUsername(String username) {
        log.info("Entering username: {}", username);
        elementHandler.enterText(USERNAME_INPUT, username);
    }

    /**
     * Enters the password into the password input field using ElementHandler.
     * @param password The password to enter.
     */
    public void enterPassword(String password) {
        log.info("Entering password: {}", password);
        elementHandler.enterText(PASSWORD_INPUT, password);
    }

    /**
     * Clicks the login button using ElementHandler.
     */
    public void clickLoginButton() {
        log.info("Clicking login button.");
        elementHandler.clickElement(LOGIN_BUTTON);
    }

    /**
     * Performs a login operation with the given credentials.
     * @param username The username.
     * @param password The password.
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        log.info("Attempted login with username: {}", username);
    }

    /**
     * Checks if the login error message is displayed using ElementHandler.
     * @return true if the error message is visible, false otherwise.
     */
    public boolean isErrorMessageDisplayed() {
        // Use ElementHandler's isSelectorPresent for checking visibility
        boolean isVisible = elementHandler.isSelectorPresent(ERROR_MESSAGE);
        log.info("Is error message displayed? {}", isVisible);
        return isVisible;
    }

    /**
     * Gets the text of the error message.
     * @return The text of the error message, or null if not found.
     */
    public String getErrorMessageText() {
        // Direct Playwright call as ElementHandler doesn't have a getText method
        // You could add one to ElementHandler if this is a common need.
        String text = page.locator(ERROR_MESSAGE).textContent();
        log.info("Error message text: {}", text);
        return text;
    }

    /**
     * Checks if the user is redirected to the home page (or a specific element on it)
     * using ElementHandler's assertion.
     * @return true if an element indicating successful login is visible, false otherwise.
     */
    public boolean isUserLoggedIn() {
        // Use ElementHandler's assertLocatorIsVisible for robust checking
        // This will throw an AssertionError if the element is not visible,
        // which is often desired for login success verification.
        try {
            elementHandler.assertLocatorIsVisible(page.locator(HOME_PAGE_ELEMENT));
            log.info("User is logged in. Home page element '{}' is visible.", HOME_PAGE_ELEMENT);
            return true;
        } catch (AssertionError e) {
            log.warn("User is NOT logged in. Home page element '{}' is not visible. Error: {}", HOME_PAGE_ELEMENT, e.getMessage());
            return false;
        }
    }
}
