package com.cucumberconstants;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

// Removed java.util.logging.Level as it's not compatible with Log4j
// import java.util.logging.Level; // This line is removed

import com.cucumberconstants.LoggerUtil;
import org.apache.logging.log4j.Logger; // Ensure this import is correct for Log4j

/**
 * ElementHandler provides a set of utility methods for interacting with web elements
 * using Playwright in Java. It includes methods for clicking, entering text,
 * waiting for elements/pages to load, checking element presence, and performing assertions,
 * all with integrated logging and exception handling.
 */
public class ElementHandler {

    private final Page page;

    private static Logger log = LoggerUtil.getLogger(ElementHandler.class);

    /**
     * Constructor for ElementHandler.
     *
     * @param page The Playwright Page object to interact with.
     */
    public ElementHandler(Page page) {
        this.page = page;
        log.info("ElementHandler initialized with a new Page instance.");
    }

    /**
     * Clicks on an element identified by the given Locator object.
     * It ensures the element is ready (visible) before attempting to click.
     *
     * @param locator The Playwright Locator object for the element to click.
     * @throws RuntimeException if the click operation fails due to Playwright or unexpected errors.
     */
    public void clickElement(Locator locator) {
        try {

            log.info("Attempting to click element with locator: " + locator.toString());
            // Ensure the locator is loaded and visible before clicking
            waitForLocatorToLoad(locator);
            locator.click();
            log.info("Successfully clicked element: " + locator.toString());
        } catch (PlaywrightException e) {

            log.error("PlaywrightException while clicking element " + locator.toString() + ": " + e.getMessage(), e);
            throw new RuntimeException("Failed to click element: " + locator.toString(), e);
        } catch (Exception e) {

            log.error("An unexpected error occurred while clicking element " + locator.toString() + ": " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while clicking element: " + locator.toString(), e);
        }
    }

    /**
     * Clicks on an element identified by the given CSS selector string.
     * This is an overloaded method for convenience.
     *
     * @param selector The CSS selector string for the element to click.
     * @throws RuntimeException if the click operation fails.
     */
    public void clickElement(String selector) {
        clickElement(page.locator(selector));
    }

    /**
     * Enters text into an input field identified by the given Locator object.
     * It ensures the element is ready (visible and editable) before filling text.
     *
     * @param locator The Playwright Locator object for the input field.
     * @param text    The text string to enter into the field.
     * @throws RuntimeException if the text entry operation fails.
     */
    public void enterText(Locator locator, String text) {
        try {

            log.info("Attempting to enter text '" + text + "' into element with locator: " + locator.toString());
            // Ensure the locator is loaded and editable before filling
            waitForLocatorToLoad(locator);
            locator.fill(text);
            log.info("Successfully entered text '" + text + "' into element: " + locator.toString());
        } catch (PlaywrightException e) {

            log.error("PlaywrightException while entering text into " + locator.toString() + ": " + e.getMessage(), e);
            throw new RuntimeException("Failed to enter text into element: " + locator.toString(), e);
        } catch (Exception e) {

            log.error("An unexpected error occurred while entering text into " + locator.toString() + ": " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while entering text into element: " + locator.toString(), e);
        }
    }

    /**
     * Enters text into an input field identified by the given CSS selector string.
     * This is an overloaded method for convenience.
     *
     * @param selector The CSS selector string for the input field.
     * @param text     The text string to enter.
     * @throws RuntimeException if the text entry operation fails.
     */
    public void enterText(String selector, String text) {
        enterText(page.locator(selector), text);
    }

    /**
     * Waits for a specific locator to be in a loaded state, specifically visible.
     * Playwright's default actions often implicitly wait, but this provides an
     * explicit wait for robustness.
     *
     * @param locator The Playwright Locator object to wait for.
     * @throws RuntimeException if the locator does not become visible within the default timeout.
     */
    public void waitForLocatorToLoad(Locator locator) {
        try {

            log.info("Waiting for locator to load and become visible: " + locator.toString());
            locator.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
            log.info("Locator is loaded and visible: " + locator.toString());
        } catch (PlaywrightException e) {
            log.error("PlaywrightException while waiting for locator " + locator.toString() + " to load: " + e.getMessage(), e);
            throw new RuntimeException("Failed to wait for locator to load: " + locator.toString(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while waiting for locator " + locator.toString() + " to load: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while waiting for locator to load: " + locator.toString(), e);
        }
    }

    /**
     * Waits for a specific CSS selector to be in a loaded state (visible).
     * This is an overloaded method for convenience.
     *
     * @param selector The CSS selector string to wait for.
     * @throws RuntimeException if the selector does not become visible.
     */
    public void waitForLocatorToLoad(String selector) {
        waitForLocatorToLoad(page.locator(selector));
    }

    /**
     * Waits for the entire page to reach a specific load state.
     * Recommended states:
     * - `LoadState.LOAD`: Fired when the whole page has loaded, including all dependent resources such as stylesheets and images.
     * - `LoadState.DOMCONTENTLOADED`: Fired when the initial HTML document has been completely loaded and parsed.
     * - `LoadState.NETWORKIDLE`: No network connections for at least 500 ms. Often the most reliable for full page load.
     *
     * @param loadState The desired LoadState enum value.
     * @throws RuntimeException if the page does not reach the specified load state within the default timeout.
     */
    public void waitForPageToLoad(LoadState loadState) {
        try {
            log.info("Waiting for page to load with state: " + loadState.toString());
            page.waitForLoadState(loadState);
            log.info("Page loaded successfully with state: " + loadState.toString());
        } catch (PlaywrightException e) {

            log.error("PlaywrightException while waiting for page to load with state " + loadState.toString() + ": " + e.getMessage(), e);
            throw new RuntimeException("Failed to wait for page to load with state: " + loadState.toString(), e);
        } catch (Exception e) {

            log.error("An unexpected error occurred while waiting for page to load with state " + loadState.toString() + ": " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while waiting for page to load with state: " + loadState.toString(), e);
        }
    }

    /**
     * Checks if a CSS selector is present and currently visible on the page.
     *
     * @param selector The CSS selector string to check for presence and visibility.
     * @return true if the selector is present and visible, false otherwise.
     * @throws RuntimeException if an unexpected error occurs during the check.
     */
    public boolean isSelectorPresent(String selector) {
        try {
            log.info("Checking if selector is present and visible: " + selector);
            // .isVisible() checks if the element is attached to DOM and visible.
            boolean isVisible = page.locator(selector).isVisible();
            if (isVisible) {
                log.info("Selector '" + selector + "' is present and visible.");
            } else {
                log.info("Selector '" + selector + "' is NOT present or visible.");
            }
            return isVisible;
        } catch (PlaywrightException e) {

            log.warn("PlaywrightException while checking selector presence " + selector + ": " + e.getMessage());
            return false;
        } catch (Exception e) {

            log.error("An unexpected error occurred while checking selector presence " + selector + ": " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while checking selector presence: " + selector, e);
        }
    }

    /**
     * Performs an assertion to check if a given Playwright Locator is visible on the page.
     * Uses Playwright's built-in `assertThat` for robust assertions.
     *
     * @param locator The Playwright Locator object to assert visibility for.
     * @throws AssertionError if the locator is not visible.
     * @throws RuntimeException if an unexpected error occurs during the assertion.
     */
    public void assertLocatorIsVisible(Locator locator) {
        try {

            log.info("Asserting that locator is visible: " + locator.toString());
            assertThat(locator).isVisible();
            log.info("Assertion passed: Locator '" + locator.toString() + "' is visible.");
        } catch (AssertionError e) {

            log.error("Assertion failed: Locator '" + locator.toString() + "' is not visible. " + e.getMessage(), e);
            throw e; // Re-throw the assertion error to indicate test failure
        } catch (Exception e) {

            log.error("An unexpected error occurred during assertion for locator " + locator.toString() + ": " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred during assertion for locator: " + locator.toString(), e);
        }
    }

    /**
     * Performs an assertion to check if a given Playwright Locator has specific text content.
     * Uses Playwright's built-in `assertThat` for robust assertions.
     *
     * @param locator The Playwright Locator object to assert text content for.
     * @param expectedText The exact text string that the locator is expected to have.
     * @throws AssertionError if the locator's text does not match the expected text.
     * @throws RuntimeException if an unexpected error occurs during the assertion.
     */
    public void assertLocatorHasText(Locator locator, String expectedText) {
        try {

            log.info("Asserting that locator '" + locator.toString() + "' has text: '" + expectedText + "'");
            assertThat(locator).hasText(expectedText);
            log.info("Assertion passed: Locator '" + locator.toString() + "' has text '" + expectedText + "'.");
        } catch (AssertionError e) {

            log.error("Assertion failed: Locator '" + locator.toString() + "' does not have text '" + expectedText + "'. Actual text might differ. " + e.getMessage(), e);
            throw e; // Re-throw the assertion error to indicate test failure
        } catch (Exception e) {

            log.error("An unexpected error occurred during assertion for locator " + locator.toString() + ": " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred during assertion for locator: " + locator.toString(), e);
        }
    }
}
