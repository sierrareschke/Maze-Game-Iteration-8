# OOAD Homework 9:
## Turning Polymorphia Into a Service
#### (?? points)

NOTE: Expect some minor edits/updates

Introduction
Team Members:
Java Version: 21
Comments/Assumptions: None

IMPORTANT: You will submit this assignment via a link to your GIT repository. If you are re-using a repository from a previous homework, create a branch called "Homework_6" and commit all changes
for this assignment into that branch. And turn in a link to this branch.

## Grading Rubric:

### Deductions

    NOTE: for this assignment you do NOT need to worry about method coverage and you do NOT need to submit a screenshot of the coverage

* Meaningful names for everything: variables, methods, classes, interfaces, etc. (1% off for each bad name, up to 10% total)
* No "magic" numbers or strings (1% off for each one, up to 10%)
* No System.out.println() calls anywhere in your main code – replace with logging (see below) or eliminate outright. 1% off for each System.out.println statement in src/main/java code.
* 1% deduction for each missing required addition to the README.md (game outputs, screenshots, diagrams)

### Method Construction Possible Deductions (max is listed under Required Capabilities)

Methods should be:
* "short" -- with very few exceptions all methods should fit on a screen using a readable font.
* well named (duh).
* properly denoted as instance methods vs. static methods (static methods don't reference the _this_ pointer).
* limited complexity (level of indentation due to control structures).
* not have comments that could be turned into just as readable code.

All of this can be achieved through functional decomposition of more complicated methods (see lecture on October 2nd).

### Required Capabilities

* Convert game play to go one step at a time.
* A turn ends when all players have executed their turn
* but a turn will **pause** when the game needs human input (if there is a human player in the game).
* Create a new subclass of Adventurer called APIPlayer. This player will have a strategy that will be called to
  determine the next move.
* Refactor the Human Player Strategy to return list of command choices. These will be returned to the UI.
* The Human Strategy should work for the a general Adventurer and for the API Player.
* Modify Polymorphia to check if there is an API Player in the game and handle it accordingly.
* Polymorphia needs to keep track of the players still pending a turn.
* You'll need to create an adaptor class that represents the game state and the game play for the Polymorphia game.
  This class should just consist of simple types and a constructor. You'll return it
  from some of the endpoints and it will automatically be converted to JSON.
* Implement the endpoints list below.

#### /api/games [GET]

Example response:

```json
[
  "Seven-room Maze",
  "API2"
]
```

#### /api/game/create [POST]

This endpoint creates a new game. The request body will be a JSON object with the fields shown below.
You'll need to create a record class to represent this request. Your endpoint declaration will look like this:

```java

@PostMapping("/api/game/create")
public ResponseEntity<?> createGame(@Validated @RequestBody PolymorphiaParameters params) {
}```
```

Example request:

```json
{
  "name": "My Game",
  "playerName": "Professor",
  "numRooms": 7,
  "numAdventurers": 2,
  "numCreatures": 2,
  "numKnights": 1,
  "numCowards": 1,
  "numGluttons": 1,
  "numDemons": 2,
  "numFood": 10,
  "numArmor": 3
}
```

Example response:

```json
{
  "name": "My Game",
  "turn": 0,
  "inMiddleOfTurn": false,
  "gameOver": false,
  "statusMessage": "Turn 0 just ended.",
  "livingAdventurers": [
    "Arwen",
    "Professor",
    "Lady Brienne",
    "Sir Eats-a-lot",
    "Sir Lancelot",
    "Frodo",
    "Sir Robin"
  ],
  "livingCreatures": [
    "Ogre",
    "Beelzebub",
    "Satan",
    "Dragon"
  ],
  "rooms": [
    {
      "name": "Goblin's Fountain",
      "neighbors": [
        "Pool of Lava",
        "Troll Bridge"
      ],
      "contents": [
        "Lady Brienne(health: 8.00)",
        "Dragon(health: 3.00)",
        "eggs(1.9)",
        "plate-Armor"
      ]
    },
    {
      "name": "Pit of Despair",
      "neighbors": [
        "Stalactite Cave",
        "Troll Bridge"
      ],
      "contents": [
        "Frodo(health: 5.00)",
        "Sir Robin(health: 5.00)",
        "fries(2.0)",
        "pizza(1.4)"
      ]
    },
    {
      "name": "Pool of Lava",
      "neighbors": [
        "Goblin's Fountain",
        "Dungeon",
        "Stalactite Cave"
      ],
      "contents": [
        "Ogre(health: 3.00)",
        "Beelzebub(health: 15.00)"
      ]
    },
    {
      "name": "Dungeon",
      "neighbors": [
        "Pool of Lava",
        "Dragon's Den"
      ],
      "contents": [
        "cupcake(1.7)",
        "banana(1.7)"
      ]
    },
    {
      "name": "Dragon's Den",
      "neighbors": [
        "Dungeon",
        "Stalactite Cave"
      ],
      "contents": [
        "Sir Lancelot(health: 8.00)",
        "Sir Eats-a-lot(health: 5.00)",
        "salad(1.6)"
      ]
    },
    {
      "name": "Stalactite Cave",
      "neighbors": [
        "Pit of Despair",
        "Pool of Lava",
        "Dragon's Den"
      ],
      "contents": [
        "Arwen(health: 5.00)",
        "Satan(health: 15.00)",
        "Professor(health: 8.00)",
        "burger(1.4)",
        "chainmail-Armor"
      ]
    },
    {
      "name": "Troll Bridge",
      "neighbors": [
        "Goblin's Fountain",
        "Pit of Despair"
      ],
      "contents": [
        "apple(1.6)",
        "steak(1.8)",
        "bacon(1.2)",
        "leather-Armor"
      ]
    }
  ],
  "availableCommands": []
}
```

#### /api/game/{gameId}/playTurn [PUT]

This is the request to just play a turn. Notice that NULL the final word.

Example request: /api/game/My%20Game/playTurn/NULL

A request that indicates which command the human player has chosen looks like this:

    /api/game/My%20Game/playTurn/MOVE
    /api/game/My%20Game/playTurn/EAT
    /api/game/My%20Game/playTurn/WEAR%20ARMOR
    /api/game/My%20Game/playTurn/FIGHT
    /api/game/My%20Game/playTurn/DO%20NOTHING

There is no payload for this command. An example response is the new status of
the game:

```json
{
  "name": "Four Room",
  "turn": 2,
  "inMiddleOfTurn": false,
  "gameOver": false,
  "statusMessage": "Turn 2 just ended.",
  "livingAdventurers": [
    "Frodo",
    "chainmail-Armored Lady Brienne",
    "Arwen",
    "Professor",
    "Sir Lancelot"
  ],
  "livingCreatures": [
    "Dragon",
    "Satan"
  ],
  "rooms": [
    {
      "name": "Crystal Palace",
      "neighbors": [
        "Pool of Lava",
        "Swamp",
        "Stalactite Cave"
      ],
      "contents": [
        "leather-Armor"
      ]
    },
    {
      "name": "Pool of Lava",
      "neighbors": [
        "Crystal Palace",
        "Swamp",
        "Stalactite Cave"
      ],
      "contents": [
        "Arwen(health: 6.05)",
        "Professor(health: 7.50)"
      ]
    },
    {
      "name": "Swamp",
      "neighbors": [
        "Crystal Palace",
        "Pool of Lava"
      ],
      "contents": [
        "Frodo(health: 6.66)",
        "chainmail-Armored Lady Brienne(health: 8.00)",
        "cupcake(1.7)",
        "apple(1.3)",
        "burger(1.5)"
      ]
    },
    {
      "name": "Stalactite Cave",
      "neighbors": [
        "Crystal Palace",
        "Pool of Lava"
      ],
      "contents": [
        "Sir Lancelot(health: 2.00)",
        "Dragon(health: 3.00)",
        "Satan(health: 14.00)",
        "salad(1.7)",
        "fries(1.3)"
      ]
    }
  ],
  "availableCommands": []
}
```

If the game pauses to get input from the API player, the response will populate the availableCommands array with the
possible commands
and look like this:

```json
{
  "name": "Four Room",
  "turn": 3,
  "inMiddleOfTurn": true,
  "gameOver": false,
  "statusMessage": "In the middle of turn 3 Waiting for Professor to make a move.",
  "livingAdventurers": [
    "Arwen",
    "Frodo",
    "chainmail-Armored Lady Brienne",
    "Professor"
  ],
  "livingCreatures": [
    "Dragon",
    "Satan"
  ],
  "rooms": [
    {
      "name": "Crystal Palace",
      "neighbors": [
        "Pool of Lava",
        "Swamp",
        "Stalactite Cave"
      ],
      "contents": [
        "leather-Armor"
      ]
    },
    {
      "name": "Pool of Lava",
      "neighbors": [
        "Crystal Palace",
        "Swamp",
        "Stalactite Cave"
      ],
      "contents": [
        "Professor(health: 7.50)"
      ]
    },
    {
      "name": "Swamp",
      "neighbors": [
        "Crystal Palace",
        "Pool of Lava"
      ],
      "contents": [
        "Frodo(health: 8.35)",
        "chainmail-Armored Lady Brienne(health: 9.47)",
        "Arwen(health: 5.80)",
        "apple(1.3)"
      ]
    },
    {
      "name": "Stalactite Cave",
      "neighbors": [
        "Crystal Palace",
        "Pool of Lava"
      ],
      "contents": [
        "Dragon(health: 3.00)",
        "Satan(health: 13.50)",
        "salad(1.7)",
        "fries(1.3)"
      ]
    }
  ],
  "availableCommands": [
    "DO NOTHING",
    "MOVE"
  ]
}
```

## Getting Started

First, you should start up the service and confirm that it comes up and you can hit the base URL
and see the UI. Nothing will work at this point.

Next, get the UI working for a default game that does not contain an API player.

Some tests are provided for you. You should run these tests and see that they fail. Then you should
implement the endpoints and the game logic to make the tests pass.

## Changes will be made to these classes:

* CharacterFactory
* Maze
* Polymorphia
* PolymorphiaParameters
* PolymorphiaControllerTest
* PolymorphiaController
* ArmoredCharacter
* Character
*

## New classes:

* APIPlayer



