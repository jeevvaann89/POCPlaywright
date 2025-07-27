package com.hooks;

import com.microsoft.playwright.*;
import com.cucumberconstants.*;
import org.apache.logging.log4j.Logger;

/**
 * Manages Playwright instances for TestNG tests.
 * Handles browser setup/teardown, context creation, video recording,
 * and screenshot attachments for Allure reporting.
 * Uses ThreadLocal for parallel test execution.
 */
public class PlaywrightHooks {
	

	    // ThreadLocal to manage Playwright instances per thread for parallel execution
	    private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
	    private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
	    private static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
	    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();

	    private static Logger log = LoggerUtil.getLogger(PlaywrightHooks.class);

	    /**
	     * Initializes Playwright, Browser, BrowserContext, and Page based on properties from config.properties.
	     * Supported browsers: "chromium", "firefox", "webkit".
	     * Headless mode can be configured.
	     */
	    public static void initDriver() {
	        log.info("Initializing Playwright driver...");
	        String browserName = ConfigurationReader.getProperty("browser");
	        boolean headless = Boolean.parseBoolean(ConfigurationReader.getProperty("headless"));
			log.info("Initializing Playwright environment..."+ConfigurationReader.getProperty("environment"));

	        tlPlaywright.set(Playwright.create());
	        BrowserType browserType;

	        switch (browserName.toLowerCase()) {
	            case "chromium":
	                browserType = getPlaywright().chromium();
	                break;
	            case "firefox":
	                browserType = getPlaywright().firefox();
	                break;
	            case "webkit":
	                browserType = getPlaywright().webkit();
	                break;
	            default:
	                log.warn("Unsupported browser specified: {}. Defaulting to Chromium.", browserName);
	                browserType = getPlaywright().chromium();
	                break;
	        }

	        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);
	        tlBrowser.set(browserType.launch(launchOptions));
	        tlBrowserContext.set(getBrowser().newContext());
	        tlPage.set(getBrowserContext().newPage());

	        log.info("Playwright driver initialized successfully for browser: {} (headless: {})", browserName, headless);
	    }

	    /**
	     * Returns the Playwright instance for the current thread.
	     * @return Playwright instance
	     */
	    public static Playwright getPlaywright() {
	        return tlPlaywright.get();
	    }

	    /**
	     * Returns the Browser instance for the current thread.
	     * @return Browser instance
	     */
	    public static Browser getBrowser() {
	        return tlBrowser.get();
	    }

	    /**
	     * Returns the BrowserContext instance for the current thread.
	     * @return BrowserContext instance
	     */
	    public static BrowserContext getBrowserContext() {
	        return tlBrowserContext.get();
	    }

	    /**
	     * Returns the Page instance for the current thread. This is the primary object
	     * for interacting with the web page.
	     * @return Page instance
	     */
	    public static Page getPage() {
	        return tlPage.get();
	    }

	    /**
	     * Closes the Playwright instances for the current thread.
	     * This should be called after each test scenario or feature.
	     */
	    public static void closeDriver() {
	        log.info("Closing Playwright driver...");
	        if (getPage() != null) {
	            getPage().close();
	            tlPage.remove();
	        }
	        if (getBrowserContext() != null) {
	            getBrowserContext().close();
	            tlBrowserContext.remove();
	        }
	        if (getBrowser() != null) {
	            getBrowser().close();
	            tlBrowser.remove();
	        }
	        if (getPlaywright() != null) {
	            getPlaywright().close();
	            tlPlaywright.remove();
	        }
	        log.info("Playwright driver closed.");
	    }
	}