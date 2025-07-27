package com.runner;


import com.cucumberconstants.ConfigurationReader;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * This class serves as the TestNG runner for Cucumber features.
 * It specifies where to find feature files and step definitions,
 * and configures reporting.
 */
@CucumberOptions(
        features = "src/test/resources/features", // Path to your feature files
        glue = {"com.stepdefinitions", "com.hooks"}, // Package where step definitions and hooks are located
        plugin = {
        		"pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", // Prints Cucumber output to console
                "html:target/cucumber-reports/cucumber-html-report.html", // Generates HTML report
                "json:target/cucumber-reports/cucumber.json", // Generates JSON report
                "junit:target/cucumber-reports/cucumber.xml", // Generates JUnit XML report
                "rerun:target/cucumber-reports/rerun.txt" // Generates a file with failed scenarios for re-running
        },
        monochrome = true, // Makes console output more readable
        publish = true // Publishes a report to Cucumber Reports service (optional)
//         tags = "@smoke" // Use tags to run specific scenarios or features, e.g., "@smoke" or "not @wip"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * This method provides scenarios to TestNG for parallel execution.
     * It's crucial for running tests in parallel using TestNG's data provider feature.
     * @return A 2D array of scenarios.
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @Parameters({"environment"})
    @BeforeSuite
    public void setUp(@Optional("qa") String environment) {
        ConfigurationReader.setEnvironment(environment);
    }
}
