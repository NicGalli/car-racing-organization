Feature: Circuits List Page
  Circuits List Page Specifications

  Background: Initial State of the Page
    Given The database contains a few circuits
    And The Circuits Page is requested
    And The Circuits Page is shown
    And The list contains a few circuits

  Scenario: Add a Circuit
    Given The user clicks the new circuit button
    And The user is redirected to the edit circuit page
    When The user fills the circuit form
    And The user clicks the confirm button of the edit circuit page
    Then The Circuits Page is shown
    And The list contains a few circuits and the new circuit

  Scenario: Update a Circuit
    Given The user clicks the edit circuit button
    And The user is redirected to the edit circuit page
    When The user fills the circuit form
    And The user clicks the confirm button of the edit circuit page
    Then The Circuits Page is shown
    And The list contains a few circuits and the updated circuit

  Scenario: Delete a Circuit
    Given The user clicks the edit circuit button
    And The user is redirected to the edit circuit page
    When The user clicks the delete circuit button
    Then The Circuits Page is shown
    And The circuit is not shown in the list
    And The circuit is not present in the database
