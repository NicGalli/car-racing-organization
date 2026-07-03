Feature: Pilots List Page
  Pilots List Page Specifications

  Background: Initial State of the Page
    Given The database contains a few pilots
    And The Pilots Page is requested
    And The Pilots Page is shown
    And The list contains a few pilots

  Scenario: Add a Pilot
    Given The user clicks the new pilot button
    And The user is redirected to the edit pilot page
    When The user fills the pilot form
    And The user clicks the confirm button
    Then The Pilots Page is shown
    And The list contains a few pilots
    And The list contains the new pilot

  Scenario: Update a Pilot
    Given The user clicks the edit pilot button
    And The user is redirected to the edit pilot page
    When The user fills the pilot form
    And The user clicks the confirm button
    Then The Pilots Page is shown
    And The list contains a few pilots and the updated pilot

  Scenario: Delete a Pilot
    Given The pilot is shown in the list
    And The user clicks the edit pilot button
    And The user is redirected to the edit pilot page
    When The user clicks the delete pilot button
    Then The Pilots Page is shown
    And The pilot is not shown in the list
    And The pilot is not present in the database
