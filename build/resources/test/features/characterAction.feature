Feature: Details of Character play
  As a character designer
  I want to ensure that different characters are playing with the correct strategy
  So that I can ensure the game is correct


  Scenario: Coward will fight a Creature if he cannot run away
    Given a maze with the following room:
      | Only Room |
    Given a Coward "Sir Run Away"
    And a Creature "Ogre"
    And all characters are in room "Only Room"

    When "Sir Run Away" executes his turn

    Then a fight did take place
    And "Ogre" lost some health


  Scenario: Coward will run away if it can
    Given a maze with the following rooms:
      | Starting Room |
      | Other Room    |
    Given a Coward "Sir Run Away"
    And a Creature "Ogre"
    And all characters are in room "Starting Room"

    When "Sir Run Away" executes his turn

    Then a fight did not take place
    And "Ogre" did not lose some health
    And "Sir Run Away" lost some health
    And "Sir Run Away" is in room "Other Room"


  Scenario: Knight will fight a Creature even if he can run away
    Given a maze with the following rooms:
      | Starting Room |
      | Other Room    |
    And a Knight "Sir Galahad"
    And a Creature "Ogre"
    And all characters are in room "Starting Room"

    When "Sir Galahad" executes his turn

    Then a fight did take place
    And "Ogre" lost some health


  Scenario: Glutton will eat food if it is available over fighting or running
    Given a maze with the following rooms:
      | Starting Room |
      | Other Room    |
    And a Glutton "Eats-a-lot"
    And a Creature "Ogre"
    And food "apple" is placed in room "Starting Room"
    And all characters are in room "Starting Room"

    When "Eats-a-lot" executes his turn

    Then a fight did not take place
    And "Eats-a-lot" is in room "Starting Room"
    And "Eats-a-lot" gained some health
    And room "Starting Room" has 0 food items


  Scenario: Glutton must fight Demon even when food and neighboring rooms are available
    Given a maze with the following rooms:
      | Starting Room |
      | Other Room    |
    And a Glutton "Eats-a-lot"
    And a Demon "Demon"
    And food "apple" is placed in room "Starting Room"
    And all characters are in room "Starting Room"

    When "Eats-a-lot" executes his turn

    Then a fight did take place
    And "Eats-a-lot" is in room "Starting Room"
    And "Eats-a-lot" lost some health
    And "Demon" lost some health
