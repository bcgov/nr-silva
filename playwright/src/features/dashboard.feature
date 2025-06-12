Feature: Dashboard

  Scenario: Visiting the dashboard
    Given I visit "/dashboard"
    Then I can see the page is called "Silva"
    Then I can read "Dashboard"
