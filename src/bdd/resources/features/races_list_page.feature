Feature: Races List Page
  Races List Page Specifications

  Background: Initial State of the Page
    Given The database contains a few races
    And The Races Page is requested
    And The Races Page is shown
    And The list contains a few races

  Scenario: View a Race
    When The user clicks the view race button
    Then The user is redirected to the view race page

  Scenario: Add a Race
    Given The user clicks the new race button
    And The user is redirected to the edit race page
    When The user fills the race form
    And The user clicks the confirm button of the edit race page
    And The view race Page is shown
    Then The Races Page is requested
    And The list contains a few races and the new race

  Scenario: Update a Race
    Given The user clicks the view race button
    And The user is redirected to the view race page
    And The user clicks the edit race button
    And The user is redirected to the edit race page
    And The user updates the race form
    When The user clicks the confirm button of the edit race page
    And The view race Page is shown
    And The race is updated
    Then The Races Page is requested
    And The Races Page is shown
    And The list contains a few races and the updated race

  Scenario: Delete a Race
    Given The user clicks the view race button
    And The user is redirected to the view race page
    When The user clicks the delete race button
    Then The Races Page is shown
    And The race is not shown in the list
    And The race is not present in the database
