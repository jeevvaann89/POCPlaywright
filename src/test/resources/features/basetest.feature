Feature: User Login Functionality
   @smoke
  Scenario: Successful login with valid credentials
    Given I am on the login page
    When I enter username "standard_user" and password "secret_sauce"
    And I click the login button
    Then I should be logged in successfully

  Scenario Outline: Unsuccessful login with invalid credentials
    Given I am on the login page
    When I enter username "<username>" and password "<password>"
    And I click the login button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"

     Examples:
    |username|password|
    |    invaliduser    |   wrongpass     |
