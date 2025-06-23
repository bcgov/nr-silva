Feature: Dashboard

  Scenario: Visiting the dashboard
    Given I visit "dashboard"
    Then I can see the page is called "Silva"
    Then I can read "Dashboard"

  Scenario: Navigate from Dashboard to Search
    Given I visit "dashboard"
    Then I can see the page is called "Silva"
    And I can see the button "Silviculture search"
    Then I click on the "Silviculture search" button
    And I can see the page is called "Silviculture Search - Silva"
    And I can see the page URL is "silviculture-search"
    And I can see the title "Silviculture Search"