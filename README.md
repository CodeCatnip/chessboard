# Chessboard Simulation

# Overview
This application simulates a chessboard battle game where units can perform various commands such as moving and shooting. The game includes different types of units, each with specific capabilities and movement patterns. The application supports saving the current state of the game, command history, and the position of units for both the current game and any previously created games.

# Features
* Configurable Board: The chessboard's dimensions can be defined in the configuration.

* Two Opposing Sides: The game includes units for two sides: white and black.

* Unit Types:

  * Archer:
        Movement: Can move one square in any direction (up, down, left, right).
  Shooting: Can shoot n squares in one direction (left, right, up, or down).
  * Transport:
  Movement: Can move 1, 2, or 3 squares in any direction (up, down, left, or right).
  * Cannon:
  Shooting: Can shoot n squares horizontally (left or right) and m squares vertically (up or down), including diagonally.
* Command Execution: Executes commands to move or shoot units, with different constraints based on unit type.

* Command History: Tracks all commands executed during the game.

* Unit History: Maintains a record of unit positions and statuses over time.

# Setup
1. Dependencies: Ensure you have the required dependencies in your pom.xml (or equivalent configuration file).
2. Database Configuration: Configure your H2 database or any other supported database in application.properties.
3. Run the Application: Use mvn spring-boot:run or your preferred method to start the application.


# REST API Endpoints
# Start a New Game
* Endpoint: POST /new-game
* Description: Initializes a new game with default units and configuration.

# Execute Command
* Endpoint: POST /command
* Parameters:
   * side (String): The color of the side executing the command.
   * unitId (Long): The ID of the unit to execute the command.
   * type (CommandType): The type of command (e.g., MOVE, SHOOT).
   * details (String): The details of the command (e.g., direction and distance).

# Execute Random Command
* Endpoint: POST /random-command
* Parameters:
  * side (String): The color of the side to execute a random command.
  * unitId (Long): The ID of the unit to execute a random command.

# Get All Units
* Endpoint: GET /units
* Description: Retrieves a list of all units currently in the game.


# Example Commands
* Move a Transport:
  {
  "side": "white",
  "unitId": 29,
  "type": "MOVE",
  "details": "RIGHT 3"
  }
* Shoot with a Cannon:
  {
  "side": "black",
  "unitId": 27,
  "type": "SHOOT",
  "details": "2 2"
  }