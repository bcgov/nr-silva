Feature: Silviculture Search

  Scenario: Performing a search for TFL47
    Given I visit "silviculture-search"    
    Then I can see the page is called "Silviculture Search - Silva"
    And I can see the title "Silviculture Search"
    When I search for "TFL47"
    Then I can see "at least" "1" result on the table "Opening search table"
