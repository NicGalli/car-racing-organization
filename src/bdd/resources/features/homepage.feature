Feature: Homepage
  Homepage Specifications

  Background: Initial State of the Page
    Given The database contains a few races, pilots, and circuits
    Then The homepage is requested
    And The homepage is shown, and it contains three links

  Scenario: Visit Races List
    When The races page link is clicked
    Then The races list page is shown and it contains a few races

  Scenario: Visit Pilots List
    When The pilots page link is clicked
    Then The pilots list page is shown and it contains a few pilots

  Scenario: Visit Circuits List
    When The circuits page link is clicked
    Then The circuits list page is shown and it contains a few circuits
