Feature: Details of game play
  As a game player
  I want to play a game with a variety of characters
  So that I can see how they interact with each other


  Scenario: Play the simplest game
    Given a maze with the following attributes:
      | number of rooms       | 1 |
      | number of adventurers | 1 |
      | number of creatures   | 1 |
      | number of food items  | 1 |
    When the game is played in the created maze
    Then I should be told that either all the adventurers or all of the creatures have died


  Scenario: Play a complicated game
    Given a maze with the following attributes:
      | number of rooms       | 7 |
      | number of adventurers | 1 |
      | number of knights     | 2 |
      | number of cowards     | 2 |
      | number of gluttons    | 2 |
      | number of creatures   | 5 |
      | number of demons      | 2 |
      | number of food items  | 9 |
    When the game is played in the created maze
    Then I should be told that either all the adventurers or all of the creatures have died
    And the game should be over
