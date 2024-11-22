// $(window).on("resize", function () {
//     drawGame(document.getElementById("gameDropDown").value);
// });

function loadDefaultData() {
    populateGameDropDown();
    populateCommandChoices([]);
}

function getGameUrlPrefix() {
    const gameDropDown = document.getElementById("gameDropDown");
    if (!gameDropDown.value) {
        gameDropDown.value = gameDropDown.options[gameDropDown.options.length - 1].value;
    }
    return "api/game/" + gameDropDown.value;
}

function getGame() {
    const url = getGameUrlPrefix();
    const xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const game = JSON.parse(xhr.responseText);
            drawGame(game);
            setStatusMessage(game.statusMessage);
        } else {
            setStatusMessage("Error getting game with at URL " + url);
            populateGameDropDown();
        }
    };
    xhr.send();
}

function setStatusMessage(message) {
    const statusElement = document.getElementById("statusMessage");
    statusElement.textContent = message;
}

function clearStatusMessage() {
    setStatusMessage("");
}

function setGameDropDownValue(gameId) {
    const gameDropDown = document.getElementById("gameDropDown");
    gameDropDown.value = gameId;
}

function populateGameDropDown() {
    const url = "api/games";
    const xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const gameIds = JSON.parse(xhr.responseText);
            const gameDropDown = document.getElementById("gameDropDown");
            gameDropDown.innerHTML = "";
            gameIds.forEach(gameId => {
                const option = document.createElement("option");
                option.value = gameId;
                option.text = gameId;
                gameDropDown.add(option);
            });
            if (gameIds.length > 0) {
                gameDropDown.value = gameIds[gameIds.length - 1];
            }
            getGame();
        }
    };
    xhr.send();
}

function clearCommandChoices() {
    const commandChoice = document.getElementById("commandChoice");
    commandChoice.innerHTML = "";
}

function playTurn() {
    let url = getGameUrlPrefix() + "/playTurn/"

    const commandChoice = document.getElementById("commandChoice");
    if (commandChoice.value) {
        url += commandChoice.value;
    } else {
        url += "NULL";
    }
    const xhr = new XMLHttpRequest();
    xhr.open("PUT", url, true);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const game = JSON.parse(xhr.responseText);
            drawGame(game);
            populateCommandChoices(game.availableCommands);
            setStatusMessage(game.statusMessage);
            if (game.gameOver) {
                window.clearInterval(intervalId);
                clearCommandChoices();
            }
        }
    };
    xhr.send();
}


function populateCommandChoices(choices) {
    const commandChoice = document.getElementById("commandChoice");
    commandChoice.innerHTML = "";
    choices.forEach(choice => {
        const option = document.createElement("option");
        option.value = choice;
        option.text = choice;
        commandChoice.add(option);
    });
    commandChoice.disable = choices.size == 0;
    commandChoice.value = null;
}

function getGameParameters() {
    const name = document.getElementById("name").value;
    const playerName = document.getElementById("playerName").value;
    const numRooms = document.getElementById("numRooms").value;
    const numAdventurers = document.getElementById("numAdventurers").value;
    const numKnights = document.getElementById("numKnights").value;
    const numCowards = document.getElementById("numCowards").value;
    const numGluttons = document.getElementById("numGluttons").value;
    const numCreatures = document.getElementById("numCreatures").value;
    const numDemons = document.getElementById("numDemons").value;
    const numFood = document.getElementById("numFood").value;
    const numArmor = document.getElementById("numArmor").value;
    return {
        name: name,
        playerName: playerName,
        numRooms: numRooms,
        numAdventurers: numAdventurers,
        numKnights: numKnights,
        numCowards: numCowards,
        numGluttons: numGluttons,
        numCreatures: numCreatures,
        numDemons: numDemons,
        numFood: numFood,
        numArmor: numArmor
    };
}

function createGame() {
    const url = "api/game/create";
    const xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 201) {
            const game = JSON.parse(xhr.responseText);
            populateGameDropDown();
            document.getElementById("newGameStatus").textContent = "Game " + game.name + " created";
            setGameDropDownValue(game.name);
            getGame();
        }
    };
    const gameParameters = JSON.stringify(getGameParameters());
    xhr.send(gameParameters);
}

var intervalId = null;

function updateAutomatically() {
    const playAutomatically = document.getElementById("playAutomatically").checked;
    const secondBetweenTurns = document.getElementById("secondBetweenTurns").value;
    if (intervalId !== null) {
        window.clearInterval(intervalId);
    }
    if (playAutomatically) {
        intervalId = window.setInterval(playTurn, secondBetweenTurns * 1000);
    } else {
        if (intervalId !== null) {
            window.clearInterval(intervalId);
            intervalId = null;
        }
    }
}

