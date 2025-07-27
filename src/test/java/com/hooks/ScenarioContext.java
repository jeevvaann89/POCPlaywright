package com.hooks;

import com.microsoft.playwright.Page;
import com.cucumberconstants.*;
import org.apache.logging.log4j.Logger;

/**
 * A shared context class to hold the Playwright Page instance
 * and other scenario-specific data, making it accessible across
 * hooks and step definitions.
 */
public class ScenarioContext {

    private Page page;
    private static Logger log = LoggerUtil.getLogger(ScenarioContext.class);

    public ScenarioContext() {
        log.info("ScenarioContext initialized.");
    }

    /**
     * Sets the Playwright Page instance in the context.
     * @param page The Playwright Page instance.
     */
    public void setPage(Page page) {
        this.page = page;
        log.debug("Playwright Page set in ScenarioContext.");
    }

    /**
     * Retrieves the Playwright Page instance from the context.
     * @return The Playwright Page instance.
     * @throws IllegalStateException if the Page has not been set.
     */
    public Page getPage() {
        if (page == null) {
            log.error("Attempted to retrieve null Playwright Page from ScenarioContext. Ensure driver is initialized.");
            throw new IllegalStateException("Playwright Page is not initialized in ScenarioContext. " +
                                            "Ensure PlaywrightDriver.initDriver() is called before accessing the Page.");
        }
        log.debug("Playwright Page retrieved from ScenarioContext.");
        return page;
    }
}

