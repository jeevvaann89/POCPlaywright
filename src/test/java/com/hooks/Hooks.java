package com.hooks;

import com.hooks.PlaywrightHooks;
import com.cucumberconstants.*;
import com.hooks.ScenarioContext; // Import the new ScenarioContext
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;


import java.io.ByteArrayInputStream;
import java.nio.file.Paths;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.Logger;

public class Hooks {

    private static Logger log = LoggerUtil.getLogger(Hooks.class);
    private ScenarioContext scenarioContext; // Inject ScenarioContext

    // Cucumber will automatically inject ScenarioContext if it's defined as a glue
    public Hooks(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        log.info("Hooks initialized with ScenarioContext.");
    }
//    public Hooks() {}

    /**
     * This method runs before each Cucumber scenario.
     * It initializes the Playwright driver and sets the Page in ScenarioContext.
     */
    @Before
    public void setup(Scenario scenario) {
        log.info("Starting scenario: {}", scenario.getName());
        PlaywrightHooks.initDriver();
        // Set the initialized Page into the ScenarioContext
        scenarioContext.setPage(PlaywrightHooks.getPage());
        log.info("Playwright driver initialized and Page set in ScenarioContext for scenario: {}", scenario.getName());
    }
    
    
    @AfterStep
    public void afterStep(Scenario scenario) {
//        if (scenario.isFailed()) {
            log.error("Step failed in scenario: {}. Taking screenshot...", scenario.getName());
            // Ensure page is not null before taking screenshot
            if (scenarioContext.getPage() != null) {
                byte[] screenshot = scenarioContext.getPage().screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
                    .setPath(Paths.get("target/screenshots/" + scenario.getName().replaceAll(" ", "_") + "_step_failure.png"))
                    .setFullPage(true));
//                scenario.attach(screenshot, "image/png", "step screenshot");
                Allure.addAttachment("Screenshot","image/png", new ByteArrayInputStream(screenshot),"png");
                log.info("Screenshot taken for failed step in scenario: {}", scenario.getName());
            } else {
                log.warn("Could not take screenshot for failed step in scenario: {} as Playwright Page was null.", scenario.getName());
            }
//        }
    }

    /**
     * This method runs after each Cucumber scenario.
     * It closes the Playwright driver and takes a screenshot if the scenario fails.
     */
    @After
    public void tearDown(Scenario scenario) {
        log.info("Ending scenario: {}", scenario.getName());
        if (scenario.isFailed()) {
            log.error("Scenario failed: {}. Taking screenshot...", scenario.getName());
            // Ensure page is not null before taking screenshot
            if (scenarioContext.getPage() != null) {
                byte[] screenshot = scenarioContext.getPage().screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
                    .setPath(Paths.get("target/screenshots/" + scenario.getName().replaceAll(" ", "_") + "_failure.png"))
                    .setFullPage(true));
                scenario.attach(screenshot, "image/png", "screenshot");
                log.info("Screenshot taken for failed scenario: {}", scenario.getName());
            } else {
                log.warn("Could not take screenshot for failed scenario: {} as Playwright Page was null.", scenario.getName());
            }
        }
        PlaywrightHooks.closeDriver();
        log.info("Playwright driver closed for scenario: {}", scenario.getName());
    }
}
