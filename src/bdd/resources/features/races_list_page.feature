Feature: Races List Page
  Races List Page Specifications

  Background: Initial State of the Page
    Given The database contains a few races
    When The Races Page is requested
    Then The Races Page is shown
    And The list contains a few races

  Scenario: View a Race
    When The user clicks the view race button
    Then The user is redirected to the view race page
    And The Race contains all the information

  Scenario: Add a Race
    Given The user clicks the new race button
    And The user is redirected to the view race page
    When The user fills the race form
    And The user clicks the confirm button of the edit race page
    Then The view race Page is shown
    And The Race contains the information of the new race
    Then The Races Page is requested
    And The list contains a few races and the new race
    And The new race is present in the database

  Scenario: Add and Remove a Pilot
    Given The user clicks the view race button
    And The user is redirected to the view race page
    When The user selects a pilot and clicks the add pilot button
    And The view race Page is shown 
    And The race contains the old pilots and the new pilot
    Then The user clicks the remove pilot button
    And The view race Page is shown 
    And The race contains all the pilots except the removed one

  Scenario: Update a Race
    Given The user clicks the view race button
    And The user is redirected to the view race page
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
